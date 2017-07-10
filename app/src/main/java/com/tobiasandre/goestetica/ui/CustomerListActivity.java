package com.tobiasandre.goestetica.ui;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.tobiasandre.goestetica.R;
import com.tobiasandre.goestetica.database.GoEsteticaContract;
import com.tobiasandre.goestetica.ui.Adapter.CustomerAdapter;

public class CustomerListActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>,
        CustomerAdapter.Callbacks {

    public static final String TAG = CustomerListActivity.class.getSimpleName();
    public static final int ID_CUSTOMER_LOADER = 44;
    private int mPosition = RecyclerView.NO_POSITION;
    Uri contentUri = GoEsteticaContract.CustomerEntry.CONTENT_URI;
    SearchView searchView;

    private CustomerAdapter mCustomerAdapter;
    private RecyclerView mRecyclerView;
    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_list);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_customers);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        mCustomerAdapter = new CustomerAdapter(this, this);

        mRecyclerView.setAdapter(mCustomerAdapter);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView=(SearchView) findViewById(R.id.search_customers);
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

        showLoading();

        getSupportLoaderManager().initLoader(ID_CUSTOMER_LOADER, null, this);

    }

    private void restartLoader(String vWhere){

        Bundle bundle = new Bundle();
        bundle.putString("filter",vWhere);

        getSupportLoaderManager().restartLoader(ID_CUSTOMER_LOADER,bundle,this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {
        String filter = "";
        if(bundle!=null){
            filter = bundle.getString("filter");
        }
        filter = GoEsteticaContract.CustomerEntry.COLUMN_CUSTOMER_NAME + " like '%"+filter+"%' ";
        return new CursorLoader(CustomerListActivity.this,contentUri,null,filter,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(mCustomerAdapter!=null && data!=null) {
            mCustomerAdapter.swapCursor(data);

            if (mPosition == RecyclerView.NO_POSITION)
                mPosition = 0;

            mRecyclerView.smoothScrollToPosition(mPosition);
            if (data.getCount() != 0)
                showCustomerDataView();

        }else{
            Log.v(TAG,"OnLoadFinished: mAdapter is null");
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if(mCustomerAdapter!=null)
            mCustomerAdapter.swapCursor(null);
        else
            Log.v(TAG,"OnLoadFinished: mAdapter is null");
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
