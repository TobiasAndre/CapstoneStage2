package com.tobiasandre.goestetica.ui.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tobiasandre.goestetica.R;
import com.tobiasandre.goestetica.database.GoEsteticaContract;

import java.text.DecimalFormat;

/**
 * Created by TobiasAndre on 30/06/2017.
 */

public class TreatmentAdapter extends RecyclerView.Adapter<TreatmentAdapter.TreatmentAdapterViewHolder> {

    private final static String TAG = TreatmentAdapter.class.getSimpleName();

    private final Context mContext;
    private Cursor mCursor;


    private final Callbacks mCallbacks;

    public interface Callbacks {
        void open(int position);
    }

    public TreatmentAdapter(@NonNull Context context,Callbacks callbacks) {
        mContext = context;
        this.mCallbacks = callbacks;
    }


    @Override
    public TreatmentAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_treatment, viewGroup, false);


        view.setFocusable(true);

        return new TreatmentAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TreatmentAdapterViewHolder treatmentAdapterViewHolder, int position) {
        mCursor.moveToPosition(position);

        treatmentAdapterViewHolder.id = mCursor.getInt(mCursor.getColumnIndex(GoEsteticaContract.TreatmentEntry._ID));
        treatmentAdapterViewHolder.name.setText(mCursor.getString(mCursor.getColumnIndex(GoEsteticaContract.TreatmentEntry.COLUMN_TREATMENT_NAME)));
        treatmentAdapterViewHolder.description.setText(mCursor.getString(mCursor.getColumnIndex(GoEsteticaContract.TreatmentEntry.COLUMN_TREATMENT_DESCRIPTION)));
        Double vlprice = mCursor.getDouble(mCursor.getColumnIndex(GoEsteticaContract.TreatmentEntry.COLUMN_TREATMENT_PRICE));
        DecimalFormat format = new DecimalFormat(mContext.getString(R.string.default_value_format));
        treatmentAdapterViewHolder.price.setText(format.format(vlprice));
    }

    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }


    class TreatmentAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        int id = 0;
        final TextView name,description,price;


        TreatmentAdapterViewHolder(View view) {
            super(view);
            id = 0;
            name = (TextView)view.findViewById(R.id.tv_treatment_name);
            description = (TextView)view.findViewById(R.id.tv_treatment_description);
            price = (TextView)view.findViewById(R.id.tv_treatment_price);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            int id = mCursor.getInt(mCursor.getColumnIndex(GoEsteticaContract.CustomerEntry._ID));
            mCallbacks.open(id);
        }
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }
}
