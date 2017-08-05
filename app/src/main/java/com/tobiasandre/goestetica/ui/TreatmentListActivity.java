package com.tobiasandre.goestetica.ui;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.ProgressBar;

import com.tobiasandre.goestetica.R;
import com.tobiasandre.goestetica.database.GoEsteticaContract;
import com.tobiasandre.goestetica.ui.Adapter.TreatmentAdapter;

public class TreatmentListActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>,
        TreatmentAdapter.Callbacks {

    public static final String TAG = TreatmentListActivity.class.getSimpleName();
    private static final int ID_TREATMENT_LOADER = 44;
    private int mPosition = RecyclerView.NO_POSITION;
    private final Uri contentUri = GoEsteticaContract.TreatmentEntry.CONTENT_URI;
    private TreatmentAdapter mTreatmentAdapter;
    private RecyclerView mRecyclerView;
    private ProgressBar mLoadingIndicator;

    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treatment_list);

        FloatingActionButton fbAddTreatment = (FloatingActionButton)findViewById(R.id.fab_add_treatment);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_treatments);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        mTreatmentAdapter = new TreatmentAdapter(this, this);

        mRecyclerView.setAdapter(mTreatmentAdapter);

        showLoading();

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView=(SearchView) findViewById(R.id.search_treatment);
        searchView.setFocusable(true);// searchView is null
        searchView.setFocusableInTouchMode(true);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                restartLoader(newText);
                return false;
            }
        });

        fbAddTreatment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getSupportLoaderManager().initLoader(ID_TREATMENT_LOADER, null, this);
    }

    private void restartLoader(String vWhere){

        Bundle bundle = new Bundle();
        bundle.putString("filter",vWhere);

        getSupportLoaderManager().restartLoader(ID_TREATMENT_LOADER,bundle,this);
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
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        String filter = "";
        if(bundle!=null){
            filter = bundle.getString("filter");
        }
        filter = GoEsteticaContract.TreatmentEntry.COLUMN_TREATMENT_NAME + " like '%"+filter+"%' ";
        return new CursorLoader(TreatmentListActivity.this,contentUri,null,filter,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(mTreatmentAdapter!=null && data!=null) {
            mTreatmentAdapter.swapCursor(data);

            if (mPosition == RecyclerView.NO_POSITION){
                mPosition = 0;
            }
            mRecyclerView.smoothScrollToPosition(mPosition);
            if (data.getCount() != 0) {
                showCustomerDataView();
            }else{
                showCustomerDataView();
                finish();
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if(mTreatmentAdapter!=null)
            mTreatmentAdapter.swapCursor(null);
    }

    @Override
    public void open(int position) {
        Intent data = new Intent();
        data.putExtra("id",position);
        if (getParent() == null) {
            setResult(Activity.RESULT_OK, data);
        } else {
            getParent().setResult(Activity.RESULT_OK, data);
        }
        finish();
    }
}
