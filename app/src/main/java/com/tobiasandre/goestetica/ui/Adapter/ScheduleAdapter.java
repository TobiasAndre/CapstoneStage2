package com.tobiasandre.goestetica.ui.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.tobiasandre.goestetica.R;
import com.tobiasandre.goestetica.database.GoEsteticaContract;

import java.text.DecimalFormat;


/**
 * Created by TobiasAndre on 13/07/2017.
 */

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleAdapterViewHolder>  {

    private final static String TAG = ScheduleAdapter.class.getSimpleName();
    private final Context mContext;
    private Cursor mCursor;

    private final Callbacks mCallbacks;


    public interface Callbacks {
        void open(int position);
    }

    public ScheduleAdapter(@NonNull Context context, Callbacks callbacks) {
        mContext = context;
        this.mCallbacks = callbacks;
    }

    @Override
    public ScheduleAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_schedule_task, viewGroup, false);
        view.setFocusable(true);

        return new ScheduleAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ScheduleAdapterViewHolder scheduleAdapterViewHolder, int position) {
        mCursor.moveToPosition(position);

        int idCustomer = mCursor.getInt(mCursor.getColumnIndexOrThrow(GoEsteticaContract.ScheduleEntry.COLUMN_SCHEDULE_CUSTOMER_ID));
        Uri contentUri = GoEsteticaContract.CustomerEntry.CONTENT_URI;
        String selection = GoEsteticaContract.CustomerEntry._ID + " = ?";
        String[] selectionArguments = new String[]{String.valueOf(idCustomer)};
        Cursor c = mContext.getContentResolver().query(contentUri, null, selection, selectionArguments, null);
        String dsPhotoPath = "";
        if (c != null) {
            while (c.moveToNext()) {
                scheduleAdapterViewHolder.name_customer.setText(c.getString(c.getColumnIndexOrThrow(GoEsteticaContract.CustomerEntry.COLUMN_CUSTOMER_NAME)));
                dsPhotoPath = c.getString(c.getColumnIndexOrThrow(GoEsteticaContract.CustomerEntry.COLUMN_CUSTOMER_PHOTO));

            }
            c.close();
        }
        if(!dsPhotoPath.isEmpty()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(dsPhotoPath);
            scheduleAdapterViewHolder.imgCustomer.setImageBitmap(myBitmap);
        }

        int idTreatment = mCursor.getInt(mCursor.getColumnIndexOrThrow(GoEsteticaContract.ScheduleEntry.COLUMN_SCHEDULE_TREATMENT_ID));
        contentUri = GoEsteticaContract.TreatmentEntry.CONTENT_URI;
        selection = GoEsteticaContract.TreatmentEntry._ID + " = ?";
        selectionArguments = new String[]{String.valueOf(idTreatment)};
        c = mContext.getContentResolver().query(contentUri, null, selection, selectionArguments, null);
        if (c != null) {
            int idTreatmentType = 0;
            while (c.moveToNext()) {
                scheduleAdapterViewHolder.name_treatment.setText(c.getString(c.getColumnIndexOrThrow(GoEsteticaContract.TreatmentEntry.COLUMN_TREATMENT_NAME)));
                idTreatmentType = c.getInt(c.getColumnIndexOrThrow(GoEsteticaContract.TreatmentEntry.COLUMN_TREATMENT_TYPE));
                scheduleAdapterViewHolder.tm_treatment.setText(String.format(mContext.getString(R.string.duration),c.getString(c.getColumnIndexOrThrow(GoEsteticaContract.TreatmentEntry.COLUMN_TREATMENT_DURATION))));

            }
            c.close();
            if(idTreatmentType>0) {
                contentUri = GoEsteticaContract.TreatmentTypeEntry.CONTENT_URI;
                selection = GoEsteticaContract.TreatmentTypeEntry._ID +" = ? ";
                selectionArguments = new String[]{String.valueOf(idTreatmentType)};
                c = mContext.getContentResolver().query(contentUri,null,selection,selectionArguments,null);
                if(c!=null) {
                    while(c.moveToNext()) {
                        scheduleAdapterViewHolder.tp_treatment.setText(c.getString(c.getColumnIndexOrThrow(GoEsteticaContract.TreatmentTypeEntry.COLUMN_TREATMENT_TYPE_NAME)));
                    }
                    c.close();
                }
            }
        }
        DecimalFormat formate = new DecimalFormat("0.00");
        Double vlSession = Double.valueOf(mCursor.getString(mCursor.getColumnIndexOrThrow(GoEsteticaContract.ScheduleEntry.COLUMN_SCHEDULE_PRICE)));
        scheduleAdapterViewHolder.tv_vl_session.setText(String.format(mContext.getString(R.string.schedule_vl_session),formate.format(vlSession)));
        scheduleAdapterViewHolder.hr_schedule.setText(mCursor.getString(mCursor.getColumnIndexOrThrow(GoEsteticaContract.ScheduleEntry.COLUMN_SCHEDULE_START_HOUR)));
        scheduleAdapterViewHolder.tv_qt_sessions.setText(String.format(mContext.getString(R.string.schedule_qt_session),mCursor.getString(mCursor.getColumnIndexOrThrow(GoEsteticaContract.ScheduleEntry.COLUMN_SCHEDULE_SESSIONS))));
        if(mCursor.getString(mCursor.getColumnIndexOrThrow(GoEsteticaContract.ScheduleEntry.COLUMN_SCHEDULE_CONFIRMED)).equals("false")){
            scheduleAdapterViewHolder.tv_confirmation.setText("Não Confirmado");
            scheduleAdapterViewHolder.tv_confirmation.setTextColor(Color.RED);
        }else{
            scheduleAdapterViewHolder.tv_confirmation.setText("Confirmado");
        }


    }

    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    class ScheduleAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        int id = 0;
        final ImageView imgCustomer;
        final TextView name_customer,hr_schedule,tp_treatment,name_treatment,
                tv_qt_sessions,tv_vl_session,tm_treatment,tv_confirmation;


        ScheduleAdapterViewHolder(View view) {
            super(view);
            id = 0;
            imgCustomer = (ImageView)view.findViewById(R.id.img_customer);

            name_customer = (TextView)view.findViewById(R.id.tv_nm_customer);
            hr_schedule = (TextView)view.findViewById(R.id.tv_hr_schedule);
            tp_treatment = (TextView)view.findViewById(R.id.tv_tp_treatment);
            name_treatment = (TextView)view.findViewById(R.id.tv_nm_treatment);
            tv_qt_sessions = (TextView)view.findViewById(R.id.tv_qt_session);
            tv_vl_session = (TextView)view.findViewById(R.id.tv_vl_session);
            tm_treatment = (TextView)view.findViewById(R.id.tv_time_treatment);
            tv_confirmation = (TextView)view.findViewById(R.id.tv_confirmation);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            int id = mCursor.getInt(mCursor.getColumnIndex(GoEsteticaContract.ScheduleEntry._ID));
            mCallbacks.open(id);
        }
    }



}
