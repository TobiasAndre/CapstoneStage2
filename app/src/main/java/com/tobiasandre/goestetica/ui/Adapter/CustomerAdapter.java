package com.tobiasandre.goestetica.ui.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tobiasandre.goestetica.R;
import com.tobiasandre.goestetica.database.GoEsteticaContract;

/**
 * Created by TobiasAndre on 28/06/2017.
 */

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerAdapterViewHolder> {

    private final static String TAG = CustomerAdapter.class.getSimpleName();

    private final Context mContext;
    private Cursor mCursor;

    final private CustomerAdapterOnClickHandler mClickHandler;

    public interface CustomerAdapterOnClickHandler {
        void onClick(long id);
    }

    public CustomerAdapter(@NonNull Context context, CustomerAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    @Override
    public CustomerAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_customer_list, viewGroup, false);

        view.setFocusable(true);

        return new CustomerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomerAdapterViewHolder customerAdapterViewHolder, int position) {
        mCursor.moveToPosition(position);

        Bitmap customerPhoto = null;
        String photoWay = mCursor.getString(mCursor.getColumnIndex(GoEsteticaContract.CustomerEntry.COLUMN_CUSTOMER_PHOTO));
        Bitmap bitmap = BitmapFactory.decodeFile(photoWay);

        customerAdapterViewHolder.ContactImg.setImageBitmap(customerPhoto);
        customerAdapterViewHolder.name.setText(mCursor.getString(mCursor.getColumnIndex(GoEsteticaContract.CustomerEntry.COLUMN_CUSTOMER_NAME)));
        customerAdapterViewHolder.email.setText(mCursor.getString(mCursor.getColumnIndex(GoEsteticaContract.CustomerEntry.COLUMN_CUSTOMER_EMAIL)));
        customerAdapterViewHolder.phone.setText(mCursor.getString(mCursor.getColumnIndex(GoEsteticaContract.CustomerEntry.COLUMN_CUSTOMER_CELLPHONE)));
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

    class CustomerAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ImageView ContactImg;
        final TextView name,email,phone;


        CustomerAdapterViewHolder(View view) {
            super(view);
            ContactImg = (ImageView)view.findViewById(R.id.img_contact);
            name = (TextView)view.findViewById(R.id.tv_display_name);
            email = (TextView)view.findViewById(R.id.tv_email_address);
            phone = (TextView)view.findViewById(R.id.tv_phone_number);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
        }
    }
}