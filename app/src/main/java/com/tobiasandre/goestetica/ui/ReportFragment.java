package com.tobiasandre.goestetica.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.tobiasandre.goestetica.R;
import com.tobiasandre.goestetica.database.GoEsteticaContract;
import com.tobiasandre.goestetica.ui.Adapter.ReportAdapter;
import com.tobiasandre.goestetica.ui.Adapter.ScheduleAdapter;
import com.tobiasandre.goestetica.utils.Util;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by TobiasAndre on 22/06/2017.
 */

public class ReportFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>,
        ReportAdapter.Callbacks{

    private static String TAG = ReportFragment.class.getSimpleName();
    public static final int ID_SCHEDULE_LOADER = 102;
    private int mPosition = RecyclerView.NO_POSITION;
    Uri contentUri = GoEsteticaContract.ScheduleEntry.CONTENT_URI;
    View rootView;
    int idCustomer,idTreatment;
    TextView tvDtInit,tvDtFin;
    Spinner spnReportType,spnResultType;
    ImageButton btnGenerate,btnFindTreatment,btnFindCustomer,btnSetDateIni,btnSetDateFin;
    EditText edCustomerName,edTreatmentName;
    private RecyclerView mRecyclerView;
    private ReportAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_report, container, false);

        idCustomer = 0;
        idTreatment = 0;
        edCustomerName = (EditText)rootView.findViewById(R.id.ed_customer_name);
        edTreatmentName = (EditText)rootView.findViewById(R.id.ed_treatment_name);
        tvDtInit = (TextView)rootView.findViewById(R.id.tv_dt_ini);
        tvDtFin = (TextView)rootView.findViewById(R.id.tv_dt_fin);
        spnReportType = (Spinner)rootView.findViewById(R.id.spnReportType);
        spnResultType = (Spinner)rootView.findViewById(R.id.spnResultType);
        btnGenerate = (ImageButton)rootView.findViewById(R.id.btnRunReport);
        btnFindCustomer = (ImageButton)rootView.findViewById(R.id.btn_find_customer);
        btnFindTreatment = (ImageButton)rootView.findViewById(R.id.btn_find_treatment);
        btnSetDateIni = (ImageButton)rootView.findViewById(R.id.btnSetDateIni);
        btnSetDateFin = (ImageButton)rootView.findViewById(R.id.btnSetDateFin);
        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.recycler_report);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        mAdapter = new ReportAdapter(getContext(), this);

        mRecyclerView.setAdapter(mAdapter);

        if(btnSetDateIni!=null){
            btnSetDateIni.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Calendar c = Calendar.getInstance();
                    int mYear = c.get(Calendar.YEAR);
                    int mMonth = c.get(Calendar.MONTH);
                    int mDay = c.get(Calendar.DAY_OF_MONTH);
                    System.out.println("the selected " + mDay);
                    DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                            new mDateIniSetListener(), mYear, mMonth, mDay);
                    dialog.show();
                }
            });
        }

        if(btnSetDateFin!=null){
            btnSetDateFin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Calendar c = Calendar.getInstance();
                    int mYear = c.get(Calendar.YEAR);
                    int mMonth = c.get(Calendar.MONTH);
                    int mDay = c.get(Calendar.DAY_OF_MONTH);
                    System.out.println("the selected " + mDay);
                    DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                            new mDateFinSetListener(), mYear, mMonth, mDay);
                    dialog.show();
                }
            });
        }

        tvDtInit.setText(Util.getStringDate().replace("-","/"));
        tvDtFin.setText(Util.getStringDate().replace("-","/"));



        if(btnFindTreatment!=null){
            btnFindTreatment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentListTreatment = new Intent(getContext(),TreatmentListActivity.class);
                    startActivityForResult(intentListTreatment,2);
                }
            });
        }

        if(btnFindCustomer!=null){
            btnFindCustomer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentListCustomer = new Intent(rootView.getContext(),CustomerListActivity.class);
                    startActivityForResult(intentListCustomer,3);
                }
            });
        }

        if(btnGenerate!=null){
            btnGenerate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    generateReport();
                }
            });
        }

        getActivity().getSupportLoaderManager().initLoader(ID_SCHEDULE_LOADER, null, this);

        return rootView;
    }

    private void generateReport() {

        mAdapter.swapCursor(null);

        String filter = " date between '"+tvDtInit.getText().toString()+"' and '"+tvDtFin.getText().toString()+"'";
        if(idCustomer>0){
            filter += " and customer_id = "+idCustomer;
        }
        if(idTreatment>0){
            filter += " and treatment_id = "+idTreatment;
        }

        Bundle bundle = new Bundle();
        bundle.putString("filter",filter);

        getActivity().getSupportLoaderManager().restartLoader(ID_SCHEDULE_LOADER, bundle, this);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==2){
            if(data!=null) {
                Bundle res = data.getExtras();
                if (res.containsKey("id")) {
                    loadTreatment(res.getInt("id"));
                }
            }
        }else if (requestCode == 3){
            if(data!=null) {
                Bundle res = data.getExtras();
                if (res.containsKey("id")) {
                    loadCustomer(res.getInt("id"));
                }
            }
        }
    }

    private void loadCustomer(int id){
        idCustomer = id;
        Uri contentUri = GoEsteticaContract.CustomerEntry.CONTENT_URI;
        String selection = GoEsteticaContract.CustomerEntry._ID + " = ?";
        String[] selectionArguments = new String[]{String.valueOf(idCustomer)};

        Cursor c = getContext().getContentResolver().query(contentUri, null, selection, selectionArguments, null);
        if (c != null) {
            while (c.moveToNext()) {
                edCustomerName.setText(c.getString(c.getColumnIndexOrThrow(GoEsteticaContract.CustomerEntry.COLUMN_CUSTOMER_NAME)));
            }
            c.close();
        }
    }

    private void loadTreatment(int id){
        idTreatment = id;
        Uri contentUri = GoEsteticaContract.TreatmentEntry.CONTENT_URI;
        String selection = GoEsteticaContract.TreatmentEntry._ID + " = ?";
        String[] selectionArguments = new String[]{String.valueOf(idTreatment)};
        Cursor c = getContext().getContentResolver().query(contentUri, null, selection, selectionArguments, null);
        if (c != null) {
            while (c.moveToNext()) {
                edTreatmentName.setText(c.getString(c.getColumnIndexOrThrow(GoEsteticaContract.TreatmentEntry.COLUMN_TREATMENT_NAME)));
            }
            c.close();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        String filter = " date between '"+tvDtInit.getText().toString()+"' and '"+tvDtFin.getText().toString()+"'";
        if(idCustomer>0){
            filter += " and customer_id = "+idCustomer;
        }
        if(idTreatment>0){
            filter += " and treatment_id = "+idTreatment;
        }
        if(bundle!=null){
            filter = bundle.getString("filter");
        }
        return new CursorLoader(getActivity(),contentUri,null,filter,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(mAdapter!=null && data!=null) {
            mAdapter.swapCursor(data);

            if (mPosition == RecyclerView.NO_POSITION){
                mPosition = 0;
            }

            mRecyclerView.smoothScrollToPosition(mPosition);

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if(mAdapter!=null)
            mAdapter.swapCursor(null);
    }

    @Override
    public void open(int position) {

    }

    private class mDateIniSetListener implements DatePickerDialog.OnDateSetListener{

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            DecimalFormat format = new DecimalFormat("00");
            int mYear = year;
            int mMonth = monthOfYear;
            int mDay = dayOfMonth;
            tvDtInit.setText(new StringBuilder()
                    // Month is 0 based so add 1
                    .append(format.format(mDay)).append("/").append(format.format(mMonth + 1)).append("/")
                    .append(mYear).append(" "));

            System.out.println(tvDtInit.getText().toString());

        }
    }

    private class mDateFinSetListener implements DatePickerDialog.OnDateSetListener{

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            DecimalFormat format = new DecimalFormat("00");
            int mYear = year;
            int mMonth = monthOfYear;
            int mDay = dayOfMonth;
            tvDtFin.setText(new StringBuilder()
                    // Month is 0 based so add 1
                    .append(format.format(mDay)).append("/").append(format.format(mMonth + 1)).append("/")
                    .append(mYear).append(" "));

            System.out.println(tvDtFin.getText().toString());

        }
    }
}
