package com.tobiasandre.goestetica.ui;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;

import com.tobiasandre.goestetica.R;
import com.tobiasandre.goestetica.database.GoEsteticaContract;
import com.tobiasandre.goestetica.utils.TYPE_SNACKBAR;
import com.tobiasandre.goestetica.utils.Util;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by TobiasAndre on 22/06/2017.
 */

public class ScheduleFragment extends Fragment {

    final static String TAG = ScheduleFragment.class.getSimpleName();
    View rootView;
    EditText edDtSchedule,edHrSchedule,edNameCustomer,edNameTreatment,edVlPrice,edQtSessions;
    ImageButton btFindCustomer,btFindTreatment,btSave,btnSetDate,btnSetTime;
    Integer idCustomer,idTreatment;
    CheckBox chkConfirmed;

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
        chkConfirmed = (CheckBox)rootView.findViewById(R.id.checkbox_confirmed);
        btSave = (ImageButton)rootView.findViewById(R.id.btn_save_schedule);
        btnSetDate = (ImageButton)rootView.findViewById(R.id.btnSetDate);
        btnSetTime = (ImageButton)rootView.findViewById(R.id.btnSetTime);

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

        if(btnSetDate!=null){
            btnSetDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Calendar c = Calendar.getInstance();
                    int mYear = c.get(Calendar.YEAR);
                    int mMonth = c.get(Calendar.MONTH);
                    int mDay = c.get(Calendar.DAY_OF_MONTH);
                    System.out.println("the selected " + mDay);
                    DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                            new mDateSetListener(), mYear, mMonth, mDay);
                    dialog.show();
                }
            });
        }
        if(btnSetTime!=null){
            btnSetTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int hour = 0;
                    int minute = 0;
                    TimePickerDialog dialog = new TimePickerDialog(getActivity(),new mTimeSetListener(),hour,minute,true);
                    dialog.show();
                }
            });
        }

        cleanFields();

        return rootView;
    }

    private void saveValues(){
        if(verifyRequired()){
            try {
                ContentValues[] treatmentValues = new ContentValues[1];
                ContentValues value = new ContentValues();
                value.put(GoEsteticaContract.ScheduleEntry.COLUMN_SCHEDULE_CUSTOMER_ID, idCustomer);
                value.put(GoEsteticaContract.ScheduleEntry.COLUMN_SCHEDULE_TREATMENT_ID,idTreatment);
                value.put(GoEsteticaContract.ScheduleEntry.COLUMN_SCHEDULE_DATE,edDtSchedule.getText().toString());
                value.put(GoEsteticaContract.ScheduleEntry.COLUMN_SCHEDULE_START_HOUR,edHrSchedule.getText().toString());
                value.put(GoEsteticaContract.ScheduleEntry.COLUMN_SCHEDULE_PRICE,edVlPrice.getText().toString());
                value.put(GoEsteticaContract.ScheduleEntry.COLUMN_SCHEDULE_SESSIONS,edQtSessions.getText().toString());
                value.put(GoEsteticaContract.ScheduleEntry.COLUMN_SCHEDULE_SESSION_MINUTES,"30");
                if(chkConfirmed.isChecked()){
                    value.put(GoEsteticaContract.ScheduleEntry.COLUMN_SCHEDULE_CONFIRMED, "true");
                }else {
                    value.put(GoEsteticaContract.ScheduleEntry.COLUMN_SCHEDULE_CONFIRMED, "false");
                }
                treatmentValues[0] = value;

                ContentResolver treatmentContentResolver = this.getContext().getContentResolver();

                treatmentContentResolver.bulkInsert(
                        GoEsteticaContract.ScheduleEntry.CONTENT_URI,
                        treatmentValues);

                Util.makeSnackbar(rootView, getString(R.string.save_sucess), Snackbar.LENGTH_LONG, TYPE_SNACKBAR.SUCCESS).show();

                cleanFields();

            }catch (Exception error){
                Util.makeSnackbar(rootView,TAG.concat(":").concat(getString(R.string.general_error)).concat(" - ").concat(error.getMessage()),Snackbar.LENGTH_LONG,TYPE_SNACKBAR.ERROR).show();
            }
        }
    }

    private void cleanFields(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        String sDate= sdf.format(date);
        edDtSchedule.setText(sDate);
        sdf = new SimpleDateFormat("HH:mm");
        edHrSchedule.setText(sdf.format(date));
        edQtSessions.setText("1");
        edVlPrice.setText("0,00");
        edNameCustomer.setText("");
        edNameTreatment.setText("");
        edNameCustomer.requestFocus();
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

    private class mDateSetListener implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            DecimalFormat format = new DecimalFormat("00");
            int mYear = year;
            int mMonth = monthOfYear;
            int mDay = dayOfMonth;
            edDtSchedule.setText(new StringBuilder()
                    // Month is 0 based so add 1
                    .append(format.format(mDay)).append("/").append(format.format(mMonth + 1)).append("/")
                    .append(mYear).append(" "));

            System.out.println(edDtSchedule.getText().toString());
        }
    }

    private class mTimeSetListener implements TimePickerDialog.OnTimeSetListener{

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            DecimalFormat format = new DecimalFormat("00");
            edHrSchedule.setText(format.format(hourOfDay)+":"+format.format(minute));
        }
    }
}
