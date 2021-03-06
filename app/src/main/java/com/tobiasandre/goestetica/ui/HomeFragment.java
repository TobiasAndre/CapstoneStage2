package com.tobiasandre.goestetica.ui;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tobiasandre.goestetica.R;
import com.tobiasandre.goestetica.database.GoEsteticaContract;
import com.tobiasandre.goestetica.ui.Adapter.ScheduleAdapter;
import com.tobiasandre.goestetica.utils.Util;

import java.text.DecimalFormat;
import java.util.Calendar;


/**
 * Created by TobiasAndre on 22/06/2017.
 */

public class HomeFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>,
        ScheduleAdapter.Callbacks{


    public static final String TAG = HomeFragment.class.getSimpleName();
    private final Uri contentUri = GoEsteticaContract.ScheduleEntry.CONTENT_URI;
    public static final int ID_SCHEDULE_LOADER = 44;
    private int mPosition = RecyclerView.NO_POSITION;
    private RecyclerView mRecyclerView;
    private ProgressBar mLoadingIndicator;

    private TextView tvTakeSchedule,tvCurrentDate;
    private ScheduleAdapter mAdapter;
    private FloatingActionButton btnAdd;
    private DecimalFormat format;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_init, container, false);
        format = new DecimalFormat("00");

        btnAdd = (FloatingActionButton)rootView.findViewById(R.id.fab_add_scheduling);
        tvTakeSchedule = (TextView)rootView.findViewById(R.id.tv_take_schedule);
        tvCurrentDate = (TextView)rootView.findViewById(R.id.tv_current_date);
        ImageButton btnSetDate = (ImageButton) rootView.findViewById(R.id.btnSetDate);

        tvCurrentDate.setText(Util.getStringDate().replace("-","/"));

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_schedule);

        mLoadingIndicator = (ProgressBar) rootView.findViewById(R.id.pb_loading_indicator);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        mAdapter = new ScheduleAdapter(getContext(), this);

        mRecyclerView.setAdapter(mAdapter);


        if(btnAdd!=null) {
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.content_frame, new ScheduleFragment())
                            .addToBackStack(null)
                            .commit();

                }
            });
        }

        showLoading();

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

        Bundle bundle = new Bundle();
        bundle.putString("filter",GoEsteticaContract.ScheduleEntry.COLUMN_SCHEDULE_DATE + " between '" + tvCurrentDate.getText().toString().replace("/","-").trim() +
                "' and '"+(Integer.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)+1))+"-"+format.format(Integer.valueOf(Calendar.getInstance().get(Calendar.MONTH)+1))+"-"+Calendar.getInstance().get(Calendar.YEAR)+"'");

        getActivity().getSupportLoaderManager().initLoader(ID_SCHEDULE_LOADER, bundle , this);

        return rootView;
    }

    private void showLoading() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.VISIBLE);
        tvTakeSchedule.setVisibility(View.VISIBLE);
    }

    private void showCustomerDataView() {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
        tvTakeSchedule.setVisibility(View.INVISIBLE);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {

        String filter = GoEsteticaContract.ScheduleEntry.COLUMN_SCHEDULE_DATE + " between '" + tvCurrentDate.getText().toString().replace("/","-").trim() +
                "' and '"+(Integer.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)+1))+"-"+format.format(Integer.valueOf(Calendar.getInstance().get(Calendar.MONTH)+1))+"-"+Calendar.getInstance().get(Calendar.YEAR)+"'";
        if(bundle!=null){
            filter = bundle.getString("filter");
        }

        return new CursorLoader(getContext(),contentUri,null,filter,null,null);
    }

    private void restartLoader(String vWhere){

        Bundle bundle = new Bundle();
        bundle.putString("filter",vWhere);

        getActivity().getSupportLoaderManager().restartLoader(ID_SCHEDULE_LOADER,bundle,this);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(mAdapter!=null && data!=null) {
            mAdapter.swapCursor(data);

            if (mPosition == RecyclerView.NO_POSITION){
                mPosition = 0;
            }

            mRecyclerView.smoothScrollToPosition(mPosition);
            if (data.getCount() != 0) {
                showCustomerDataView();
            }else{
                showCustomerDataView();
            }
        }else{
            showCustomerDataView();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if(mAdapter!=null)
            mAdapter.swapCursor(null);
        else {
            showCustomerDataView();
        }
    }

    @Override
    public void open(int position) {

        Bundle bundle = new Bundle();
        bundle.putInt("id", position);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        ScheduleFragment scheduleFragment = new ScheduleFragment();
        scheduleFragment.setArguments(bundle);
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame,scheduleFragment)
                .addToBackStack(null)
                .commit();

    }


    private class mDateSetListener implements DatePickerDialog.OnDateSetListener{

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            DecimalFormat format = new DecimalFormat("00");

            tvCurrentDate.setText(new StringBuilder()
                    // Month is 0 based so add 1
                    .append(format.format(dayOfMonth)).append("/").append(format.format(monthOfYear + 1)).append("/")
                    .append(year).append(" "));

            System.out.println(tvCurrentDate.getText().toString());
            String vWhere = GoEsteticaContract.ScheduleEntry.COLUMN_SCHEDULE_DATE+" between '"+tvCurrentDate.getText().toString().replace("/","-").trim()+"' and '"+format.format(dayOfMonth+1)+"-"+format.format(monthOfYear+1)+"-"+year+"'";

            restartLoader(vWhere);

        }
    }
}