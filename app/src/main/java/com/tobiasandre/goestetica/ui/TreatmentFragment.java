package com.tobiasandre.goestetica.ui;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.tobiasandre.goestetica.R;
import com.tobiasandre.goestetica.database.GoEsteticaContract;
import com.tobiasandre.goestetica.utils.TypeSnackBar;
import com.tobiasandre.goestetica.utils.Util;

/**
 * Created by TobiasAndre on 22/06/2017.
 */

public class TreatmentFragment extends Fragment {

    private final String TAG = TreatmentFragment.class.getSimpleName();
    View rootView;
    Integer idTreatment=-1;
    Spinner typeTreatment;
    EditText edName,edDescription,edPrice,edSessions,edDuration;
    ImageButton btnSave,btnFindTreatment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_treatment, container, false);

        edName = (EditText)rootView.findViewById(R.id.ed_treatment_name);
        edDescription = (EditText)rootView.findViewById(R.id.ed_treatment_description);
        edPrice = (EditText)rootView.findViewById(R.id.ed_treatment_price);
        edSessions = (EditText)rootView.findViewById(R.id.ed_treatment_sessions);
        edDuration = (EditText)rootView.findViewById(R.id.ed_treatment_duration);
        typeTreatment = (Spinner)rootView.findViewById(R.id.spnType);
        btnSave = (ImageButton)rootView.findViewById(R.id.btn_save_treatment);
        btnFindTreatment = (ImageButton)rootView.findViewById(R.id.btn_find_treatment);

        if(btnSave!=null){
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(verifyRequired()){
                        save();
                    }
                }
            });
        }

        if(btnFindTreatment!=null){
            btnFindTreatment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentListTreatment = new Intent(getContext(),TreatmentListActivity.class);
                    startActivityForResult(intentListTreatment,2);
                }
            });
        }

        Intent intentListTreatment = new Intent(rootView.getContext(),TreatmentListActivity.class);
        startActivityForResult(intentListTreatment,2);



        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2){
            if(data!=null) {
                Bundle res = data.getExtras();
                if (res.containsKey("id")) {
                    idTreatment = res.getInt("id");
                    loadData(idTreatment);
                }
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
    }

    private void loadData(int id){
        Uri contentUri = GoEsteticaContract.TreatmentEntry.CONTENT_URI;
        String selection = GoEsteticaContract.TreatmentEntry._ID + " = ?";
        String[] selectionArguments = new String[]{String.valueOf(id)};
        Cursor c = getContext().getContentResolver().query(contentUri, null, selection, selectionArguments, null);
        if (c != null) {
            while (c.moveToNext()) {
                edName.setText(c.getString(c.getColumnIndexOrThrow(GoEsteticaContract.TreatmentEntry.COLUMN_TREATMENT_NAME)));
                edDescription.setText(c.getString(c.getColumnIndexOrThrow(GoEsteticaContract.TreatmentEntry.COLUMN_TREATMENT_DESCRIPTION)));
                edPrice.setText(c.getString(c.getColumnIndexOrThrow(GoEsteticaContract.TreatmentEntry.COLUMN_TREATMENT_PRICE)));
                edSessions.setText(c.getString(c.getColumnIndexOrThrow(GoEsteticaContract.TreatmentEntry.COLUMN_TREATMENT_SESSIONS)));
                edDuration.setText(c.getString(c.getColumnIndexOrThrow(GoEsteticaContract.TreatmentEntry.COLUMN_TREATMENT_DURATION)));
                Util.setSpinText(typeTreatment,c.getString(c.getColumnIndexOrThrow(GoEsteticaContract.TreatmentEntry.COLUMN_TREATMENT_TYPE)));
            }
            c.close();
        }
    }

    private Boolean verifyRequired(){
        if(edPrice.getText().toString().isEmpty()){
            edPrice.setText("0,00");
        }
        if(edSessions.getText().toString().isEmpty()){
            edSessions.setText("1");
        }
        if(edName.getText().toString().isEmpty()){
            Util.makeSnackbar(rootView,getString(R.string.treatment_name_required), Snackbar.LENGTH_LONG, TypeSnackBar.ERROR).show();
            edName.requestFocus();
            return false;
        }

        return true;
    }

    private void save(){

        try {
            ContentValues[] treatmentValues = new ContentValues[1];
            ContentValues value = new ContentValues();
            value.put(GoEsteticaContract.TreatmentEntry.COLUMN_TREATMENT_NAME, edName.getText().toString());
            value.put(GoEsteticaContract.TreatmentEntry.COLUMN_TREATMENT_DESCRIPTION, edDescription.getText().toString());
            value.put(GoEsteticaContract.TreatmentEntry.COLUMN_TREATMENT_PRICE, Double.valueOf(edPrice.getText().toString().replace(",",".")));
            value.put(GoEsteticaContract.TreatmentEntry.COLUMN_TREATMENT_SESSIONS, Integer.valueOf(edSessions.getText().toString()));
            value.put(GoEsteticaContract.TreatmentEntry.COLUMN_TREATMENT_DURATION, edDuration.getText().toString());
            value.put(GoEsteticaContract.TreatmentEntry.COLUMN_TREATMENT_TYPE,typeTreatment.getSelectedItem().toString());
            treatmentValues[0] = value;

            ContentResolver treatmentContentResolver = this.getContext().getContentResolver();

            if (idTreatment > 0) {
                String selectionClause = GoEsteticaContract.TreatmentEntry._ID + " = ?";
                String[] selectionArgs = new String[]{String.valueOf(idTreatment)};
                treatmentContentResolver.update(GoEsteticaContract.TreatmentEntry.CONTENT_URI,
                        treatmentValues[0], selectionClause, selectionArgs);
            } else {
                treatmentContentResolver.bulkInsert(
                        GoEsteticaContract.TreatmentEntry.CONTENT_URI,
                        treatmentValues);
            }

            Util.makeSnackbar(rootView, getString(R.string.save_sucess), Snackbar.LENGTH_LONG, TypeSnackBar.SUCCESS).show();

            clearFields();

        }catch (Exception error){
            Util.makeSnackbar(rootView,TAG.concat(":").concat(getString(R.string.general_error)).concat(" - ").concat(error.getMessage()),Snackbar.LENGTH_LONG,TypeSnackBar.ERROR).show();
        }
    }

    private void clearFields(){
        edName.setText(getString(R.string.default_empty_string));
        edDescription.setText(getString(R.string.default_empty_string));
        edPrice.setText(getString(R.string.price_default));
        edSessions.setText(getString(R.string.qt_default));
        edDuration.setText(getString(R.string.default_time_session));
        typeTreatment.setSelection(0);
        edName.requestFocus();
    }

}
