package com.tobiasandre.goestetica.ui;

import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.tobiasandre.goestetica.R;
import com.tobiasandre.goestetica.database.GoEsteticaContract;
import com.tobiasandre.goestetica.ui.Adapter.TreatmentAdapter;

public class TreatmentListActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>,
        TreatmentAdapter.TreatmentAdapterOnClickHandler {

    public static final String TAG = TreatmentListActivity.class.getSimpleName();
    public static final int ID_TREATMENT_LOADER = 44;
    private int mPosition = RecyclerView.NO_POSITION;

    private TreatmentAdapter mTreatmentAdapter;
    private RecyclerView mRecyclerView;
    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treatment_list);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_treatments);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        mTreatmentAdapter = new TreatmentAdapter(this, this);

        mRecyclerView.setAdapter(mTreatmentAdapter);

        showLoading();

        getSupportLoaderManager().initLoader(ID_TREATMENT_LOADER, null, this);
    }

    @Override
    public void onClick(long id) {

    }

    private void showLoading() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    private void showCustomerDataView() {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri contentUri = GoEsteticaContract.TreatmentEntry.CONTENT_URI;
        return new CursorLoader(TreatmentListActivity.this,contentUri,null,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(mTreatmentAdapter!=null && data!=null) {
            mTreatmentAdapter.swapCursor(data);

            if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
            mRecyclerView.smoothScrollToPosition(mPosition);
            if (data.getCount() != 0) showCustomerDataView();
        }else{
            Log.v(TAG,"OnLoadFinished: mAdapter is null");
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if(mTreatmentAdapter!=null)
            mTreatmentAdapter.swapCursor(null);
        else
            Log.v(TAG,"OnLoadFinished: mAdapter is null");
    }
}
