package com.tobiasandre.goestetica.ui;


import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.tobiasandre.goestetica.R;
import com.tobiasandre.goestetica.database.GoEsteticaContract;
import com.tobiasandre.goestetica.utils.TYPE_SNACKBAR;
import com.tobiasandre.goestetica.utils.Util;

import java.sql.Time;
import java.util.Date;

/**
 * Created by TobiasAndre on 22/06/2017.
 */

public class ScheduleFragment extends Fragment {

    View rootView;
    EditText edDtSchedule,edHrSchedule,edNameCustomer,edNameTreatment,edVlPrice,edQtSessions;
    ImageButton btFindCustomer,btFindTreatment,btSave;
    Integer idCustomer,idTreatment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.schedule_fragment, container, false);

        edDtSchedule = (EditText)rootView.findViewById(R.id.dt_schedule);
        edHrSchedule = (EditText)rootView.findViewById(R.id.hr_schedule);
        edNameCustomer = (EditText)rootView.findViewById(R.id.nm_customer);
        edNameTreatment = (EditText)rootView.findViewById(R.id.nm_treatment);
        edVlPrice = (EditText)rootView.findViewById(R.id.vl_price);
        edQtSessions = (EditText)rootView.findViewById(R.id.nr_sessions);
        btFindCustomer = (ImageButton)rootView.findViewById(R.id.btn_find_customer);
        btFindTreatment = (ImageButton)rootView.findViewById(R.id.btn_find_treatment);
        btSave = (ImageButton)rootView.findViewById(R.id.btn_save_schedule);

        if(btFindCustomer!=null){
            btFindCustomer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentListCustomer = new Intent(rootView.getContext(),CustomerListActivity.class);
                    startActivityForResult(intentListCustomer,1);
                }
            });
        }

        if(btFindTreatment!=null){
            btFindTreatment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentListTreatment = new Intent(getContext(),TreatmentListActivity.class);
                    startActivityForResult(intentListTreatment,2);
                }
            });
        }

        if(btSave!=null){
            btSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveValues();
                }
            });
        }

        return rootView;
    }

    private void saveValues(){
        if(verifyRequired()){

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1){
            if(data!=null) {
                Bundle res = data.getExtras();
                if (res.containsKey("id")) {
                    idCustomer = res.getInt("id");
                    loadCustomerData(idCustomer);
                }
            }
        }
        if(requestCode==2){
            if(data!=null) {
                Bundle res = data.getExtras();
                if (res.containsKey("id")) {
                    idTreatment = res.getInt("id");
                    loadTreatmentData(idTreatment);
                }
            }
        }
    }

    private void loadCustomerData(int id){
        if(id > 0){
            Uri contentUri = GoEsteticaContract.CustomerEntry.CONTENT_URI;
            String selection = GoEsteticaContract.CustomerEntry._ID + " = ?";
            String[] selectionArguments = new String[]{String.valueOf(id)};

            Cursor c = getContext().getContentResolver().query(contentUri, null, selection, selectionArguments, null);
            if (c != null) {
                while (c.moveToNext()) {
                    edNameCustomer.setText(c.getString(c.getColumnIndexOrThrow(GoEsteticaContract.CustomerEntry.COLUMN_CUSTOMER_NAME)));
                }
                c.close();
            }
        }
    }

    private void loadTreatmentData(int id){
        if(id > 0){
            Uri contentUri = GoEsteticaContract.TreatmentEntry.CONTENT_URI;
            String selection = GoEsteticaContract.TreatmentEntry._ID+" = ?";
            String[] selectionArguments = new String[]{String.valueOf(id)};
            Cursor c = getContext().getContentResolver().query(contentUri, null, selection, selectionArguments, null);
            if (c != null) {
                while (c.moveToNext()) {
                    edNameTreatment.setText(c.getString(c.getColumnIndexOrThrow(GoEsteticaContract.TreatmentEntry.COLUMN_TREATMENT_NAME)));
                    edVlPrice.setText(c.getString(c.getColumnIndexOrThrow(GoEsteticaContract.TreatmentEntry.COLUMN_TREATMENT_PRICE)));
                    edQtSessions.setText(c.getString(c.getColumnIndexOrThrow(GoEsteticaContract.TreatmentEntry.COLUMN_TREATMENT_SESSIONS)));
                }
                c.close();
            }
        }
    }

    private Boolean verifyRequired(){
        if(edDtSchedule.getText().toString().isEmpty()){
            edDtSchedule.setText(String.valueOf(new Date()));
        }
        if(edHrSchedule.getText().toString().isEmpty()){
            edHrSchedule.setText(String.valueOf(new Date().getTime()));
        }
        if(edQtSessions.getText().toString().isEmpty()){
            edQtSessions.setText("1");
        }
        if(edVlPrice.getText().toString().isEmpty()){
            edVlPrice.setText("0,00");
        }
        if(edNameTreatment.getText().toString().isEmpty()){
            Util.makeSnackbar(rootView,getString(R.string.treatment_name_required), Snackbar.LENGTH_LONG, TYPE_SNACKBAR.ERROR).show();
            edNameTreatment.requestFocus();
            return false;
        }
        if(edNameCustomer.getText().toString().isEmpty()){
            Util.makeSnackbar(rootView,getString(R.string.customer_required), Snackbar.LENGTH_LONG, TYPE_SNACKBAR.ERROR).show();
            edNameCustomer.requestFocus();
            return false;
        }
        return true;
    }

}
