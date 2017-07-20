package com.tobiasandre.goestetica.ui.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tobiasandre.goestetica.R;
import com.tobiasandre.goestetica.database.GoEsteticaContract;


/**
 * Created by TobiasAndre on 19/07/2017.
 */

public class ReportAdapter  extends RecyclerView.Adapter<ReportAdapter.ReportAdapterViewHolder>  {

    private final static String TAG = CustomerAdapter.class.getSimpleName();

    private final Context mContext;
    private Cursor mCursor;

    private final Callbacks mCallbacks;

    public interface Callbacks {
        void open(int position);
    }

    public ReportAdapter(@NonNull Context context, Callbacks callbacks) {
        mContext = context;
        this.mCallbacks = callbacks;
    }

    @Override
    public ReportAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_report_analytic, viewGroup, false);
        view.setFocusable(true);

        return new ReportAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReportAdapterViewHolder reportAdapterViewHolder, int position) {
        mCursor.moveToPosition(position);
        reportAdapterViewHolder.tvDtSchedule.setText(mCursor.getString(mCursor.getColumnIndexOrThrow(GoEsteticaContract.ScheduleEntry.COLUMN_SCHEDULE_DATE)));
        int idCustomer = mCursor.getInt(mCursor.getColumnIndexOrThrow(GoEsteticaContract.ScheduleEntry.COLUMN_SCHEDULE_CUSTOMER_ID));
        if(idCustomer>0) {
            Uri contentUri = GoEsteticaContract.CustomerEntry.CONTENT_URI;
            String selection = GoEsteticaContract.CustomerEntry._ID + " = ?";
            String[] selectionArguments = new String[]{String.valueOf(idCustomer)};
            Cursor c = mContext.getContentResolver().query(contentUri, null, selection, selectionArguments, null);
            if (c != null) {
                while (c.moveToNext()) {
                    reportAdapterViewHolder.tvNmCustomer.setText(c.getString(c.getColumnIndexOrThrow(GoEsteticaContract.CustomerEntry.COLUMN_CUSTOMER_NAME)));
                }
                c.close();
            }
        }
        int idTreatment = mCursor.getInt(mCursor.getColumnIndexOrThrow(GoEsteticaContract.ScheduleEntry.COLUMN_SCHEDULE_TREATMENT_ID));
        if(idTreatment>0){
            Uri contentUri = GoEsteticaContract.TreatmentEntry.CONTENT_URI;
            String selection = GoEsteticaContract.TreatmentEntry._ID + " = ?";
            String[] selectionArguments = new String[]{String.valueOf(idTreatment)};
            Cursor c = mContext.getContentResolver().query(contentUri, null, selection, selectionArguments, null);
            if (c != null) {
                while (c.moveToNext()) {
                    reportAdapterViewHolder.tvNmTreatment.setText(c.getString(c.getColumnIndexOrThrow(GoEsteticaContract.TreatmentEntry.COLUMN_TREATMENT_NAME)));
                }
                c.close();
            }
        }
        reportAdapterViewHolder.tvVlSession.setText(mCursor.getString(mCursor.getColumnIndexOrThrow(GoEsteticaContract.ScheduleEntry.COLUMN_SCHEDULE_PRICE)));
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

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    class ReportAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView tvDtSchedule,tvNmCustomer,tvNmTreatment,tvVlSession;

        ReportAdapterViewHolder(View view) {
            super(view);
            tvDtSchedule = (TextView)view.findViewById(R.id.tv_dt_schedule);
            tvNmCustomer = (TextView)view.findViewById(R.id.tv_nm_customer);
            tvNmTreatment = (TextView)view.findViewById(R.id.tv_nm_treatment);
            tvVlSession = (TextView)view.findViewById(R.id.tv_vl_session);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
        }
    }
}
