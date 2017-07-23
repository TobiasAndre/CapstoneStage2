package com.tobiasandre.goestetica.sync;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.v7.preference.PreferenceManager;

import com.tobiasandre.goestetica.R;
import com.tobiasandre.goestetica.database.GoEsteticaContract;
import com.tobiasandre.goestetica.database.model.Treatment;
import com.tobiasandre.goestetica.database.model.TreatmentType;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by TobiasAndre on 22/07/2017.
 */

public class SyncService extends IntentService {

    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;
    private static final String SERVER_ADDRESS = "SERVER_ADDRESS";
    private static final String SERVER_PORT = "SERVER_PORT";


    public SyncService() {
        super(SyncService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final String serverAddress = sharedPreferences.getString(SERVER_ADDRESS,"http://192.168.0.102");
        final String serverPort = sharedPreferences.getString(SERVER_PORT,"18080");

        final ResultReceiver receiver = intent.getParcelableExtra("receiver");

        Bundle bundle = new Bundle();
        bundle.putString(Intent.EXTRA_TEXT, getString(R.string.init_sync));
        receiver.send(STATUS_RUNNING, bundle);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(serverAddress+":"+String.valueOf(serverPort))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        InfoService service = retrofit.create(InfoService.class);
        try {

            bundle.putString(Intent.EXTRA_TEXT, getString(R.string.init_sync));
            receiver.send(STATUS_RUNNING, bundle);


            bundle.putString(Intent.EXTRA_TEXT, getString(R.string.sync_types));
            receiver.send(STATUS_RUNNING, bundle);

            Call<List<TreatmentType>> getTypes = service.discoverTreatmentTypes();
            List<TreatmentType> treatmentTypes = getTypes.execute().body();

            bundle.putString(Intent.EXTRA_TEXT, getString(R.string.sync_treatments));
            receiver.send(STATUS_RUNNING, bundle);

            Call<List<Treatment>> getTreatments = service.discoverTreatments();
            List<Treatment> treatments = getTreatments.execute().body();

            ContentResolver contentResolver = getContentResolver();

            ContentValues[] treatmentTypeValues = new ContentValues[treatmentTypes.size()];
            ContentValues value;

            for(TreatmentType tp: treatmentTypes){
                value = new ContentValues();
                value.put(GoEsteticaContract.TreatmentTypeEntry.COLUMN_TREATMENT_TYPE_NAME,tp.getName());
                treatmentTypeValues[0] = value;

                contentResolver.bulkInsert(
                        GoEsteticaContract.TreatmentTypeEntry.CONTENT_URI,
                        treatmentTypeValues);
            }


            ContentValues[] treatmentValues = new ContentValues[treatments.size()];
            for(Treatment t :treatments){
                value = new ContentValues();
                value.put(GoEsteticaContract.TreatmentEntry.COLUMN_TREATMENT_NAME,t.getName());
                value.put(GoEsteticaContract.TreatmentEntry.COLUMN_TREATMENT_DESCRIPTION,t.getDescription());
                value.put(GoEsteticaContract.TreatmentEntry.COLUMN_TREATMENT_SESSIONS,t.getSessions());
                value.put(GoEsteticaContract.TreatmentEntry.COLUMN_TREATMENT_PRICE,t.getPrice());
                value.put(GoEsteticaContract.TreatmentEntry.COLUMN_TREATMENT_DURATION,t.getDuration());
                value.put(GoEsteticaContract.TreatmentEntry.COLUMN_TREATMENT_TYPE,t.getType());
                treatmentValues[0] = value;

                contentResolver.bulkInsert(
                    GoEsteticaContract.TreatmentEntry.CONTENT_URI,
                    treatmentValues);
            }

            bundle.putString(Intent.EXTRA_TEXT, getString(R.string.sync_finish));
            receiver.send(STATUS_FINISHED, bundle);

        }catch (Exception error){
            bundle.putString(Intent.EXTRA_TEXT,"Error: "+error.getMessage());
            receiver.send(STATUS_ERROR,bundle);
        }
    }
}
