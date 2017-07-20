package com.tobiasandre.goestetica.ui;

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
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.tobiasandre.goestetica.R;
import com.tobiasandre.goestetica.database.GoEsteticaContract;
import com.tobiasandre.goestetica.ui.Adapter.ReportAdapter;
import com.tobiasandre.goestetica.ui.Adapter.ScheduleAdapter;

import java.util.Date;

/**
 * Created by TobiasAndre on 22/06/2017.
 */

public class ReportFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>,
        ReportAdapter.Callbacks{

    private static String TAG = ReportFragment.class.getSimpleName();
    public static final int ID_REPORT_LOADER = 44;
    private int mPosition = RecyclerView.NO_POSITION;
    Uri contentUri = GoEsteticaContract.ScheduleEntry.CONTENT_URI;
    View rootView;
    int idCustomer,idTreatment;
    TextView tvDtInit,tvDtFin;
    Spinner spnReportType,spnResultType;
    ImageButton btnGenerate,btnFindTreatment,btnFindCustomer;
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
        tvDtInit = (TextView)rootView.findViewById(R.id.tv_dt_ini);
        tvDtFin = (TextView)rootView.findViewById(R.id.tv_dt_fin);
        spnReportType = (Spinner)rootView.findViewById(R.id.spnReportType);
        spnResultType = (Spinner)rootView.findViewById(R.id.spnResultType);
        btnGenerate = (ImageButton)rootView.findViewById(R.id.btnRunReport);
        btnFindCustomer = (ImageButton)rootView.findViewById(R.id.btn_find_customer);
        btnFindTreatment = (ImageButton)rootView.findViewById(R.id.btn_find_treatment);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        mAdapter = new ReportAdapter(getContext(), (ReportAdapter.Callbacks) getContext());

        mRecyclerView.setAdapter(mAdapter);

        tvDtInit.setText(new Date().toString());
        tvDtFin.setText(new Date().toString());

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

        getActivity().getSupportLoaderManager().initLoader(ID_REPORT_LOADER, null, this);

        return rootView;
    }

    private void generateReport() {


        getActivity().getSupportLoaderManager().initLoader(ID_REPORT_LOADER, null, this);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==2){
            if(data!=null) {
                Bundle res = data.getExtras();
                if (res.containsKey("id")) {
                    idTreatment = res.getInt("id");
                }
            }
        }else if (requestCode == 3){
            if(data!=null) {
                Bundle res = data.getExtras();
                if (res.containsKey("id")) {
                    idCustomer = res.getInt("id");
                }
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        String filter = "";
        if(bundle!=null){
            filter = bundle.getString("filter");
        }
        filter = GoEsteticaContract.CustomerEntry.COLUMN_CUSTOMER_NAME + " like '%"+filter+"%' ";
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

        }else{
            Log.v(TAG,"OnLoadFinished: mAdapter is null");
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if(mAdapter!=null)
            mAdapter.swapCursor(null);
        else {
            Log.v(TAG, "OnLoadFinished: mAdapter is null");
        }
    }

    @Override
    public void open(int position) {

    }
}
