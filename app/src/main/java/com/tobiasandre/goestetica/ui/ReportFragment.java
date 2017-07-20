package com.tobiasandre.goestetica.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.tobiasandre.goestetica.R;
import com.tobiasandre.goestetica.ui.Adapter.ReportAdapter;

/**
 * Created by TobiasAndre on 22/06/2017.
 */

public class ReportFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>,
        ReportAdapter.Callbacks{

    View rootView;
    int idCustomer,idTreatment;
    TextView tvDtInit,tvDtFin;
    Spinner spnReportType,spnResultType;
    ImageButton btnGenerate;

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

        if(btnGenerate!=null){
            btnGenerate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    generateReport();
                }
            });
        }

        return rootView;
    }

    private void generateReport() {


    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void open(int position) {

    }
}
