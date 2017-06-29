package com.tobiasandre.goestetica.ui;

import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.tobiasandre.goestetica.R;
import com.tobiasandre.goestetica.database.GoEsteticaContract;
import com.tobiasandre.goestetica.ui.Adapter.CustomerAdapter;

public class CustomerListActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>,
        CustomerAdapter.CustomerAdapterOnClickHandler {

    public static final String TAG = CustomerListActivity.class.getSimpleName();
    public static final int ID_CUSTOMER_LOADER = 44;
    private int mPosition = RecyclerView.NO_POSITION;

    public static final String[] MAIN_CUSTOMER_PROJECTION = {
            GoEsteticaContract.CustomerEntry.COLUMN_CUSTOMER_NAME,
            GoEsteticaContract.CustomerEntry.COLUMN_CUSTOMER_CELLPHONE,
            GoEsteticaContract.CustomerEntry.COLUMN_CUSTOMER_EMAIL,
            GoEsteticaContract.CustomerEntry.COLUMN_CUSTOMER_ADDRESS,
            GoEsteticaContract.CustomerEntry.COLUMN_CUSTOMER_PHOTO,
            GoEsteticaContract.CustomerEntry.COLUMN_CUSTOMER_FONE,
            GoEsteticaContract.CustomerEntry.COLUMN_CUSTOMER_DEFAULT_PAYMENT_TYPE
    };

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

        showLoading();

        getSupportLoaderManager().initLoader(ID_CUSTOMER_LOADER, null, this);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {

        Uri contentUri = GoEsteticaContract.CustomerEntry.CONTENT_URI;
        return new CursorLoader(CustomerListActivity.this,contentUri,MAIN_CUSTOMER_PROJECTION,null,null,null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(mCustomerAdapter!=null && data!=null) {
            mCustomerAdapter.swapCursor(data);

            if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
            mRecyclerView.smoothScrollToPosition(mPosition);
            if (data.getCount() != 0) showCustomerDataView();
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
}
