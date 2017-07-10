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

    private final Callbacks mCallbacks;

    public interface Callbacks {
        void open(int position);
    }

    public CustomerAdapter(@NonNull Context context,Callbacks callbacks) {
        mContext = context;
        this.mCallbacks = callbacks;
    }

    @Override
    public CustomerAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_contact, viewGroup, false);

        view.setFocusable(true);

        return new CustomerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomerAdapterViewHolder customerAdapterViewHolder, int position) {
        mCursor.moveToPosition(position);
        customerAdapterViewHolder.idCadastro = mCursor.getInt(mCursor.getColumnIndex(GoEsteticaContract.CustomerEntry._ID));
        String photoWay = mCursor.getString(mCursor.getColumnIndex(GoEsteticaContract.CustomerEntry.COLUMN_CUSTOMER_PHOTO));
        try {
            if (!photoWay.isEmpty()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(photoWay);
                customerAdapterViewHolder.ContactImg.setImageBitmap(myBitmap);
            } else {
                customerAdapterViewHolder.ContactImg.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.blank_profile_picture));
            }

        }catch (Exception error){
            error.printStackTrace();
        }
        customerAdapterViewHolder.name.setText(mCursor.getString(mCursor.getColumnIndex(GoEsteticaContract.CustomerEntry.COLUMN_CUSTOMER_NAME)));
        customerAdapterViewHolder.email.setText(mCursor.getString(mCursor.getColumnIndex(GoEsteticaContract.CustomerEntry.COLUMN_CUSTOMER_EMAIL)));
        customerAdapterViewHolder.phone.setText(mCursor.getString(mCursor.getColumnIndex(GoEsteticaContract.CustomerEntry.COLUMN_CUSTOMER_CELLPHONE)));
        customerAdapterViewHolder.imgCheck.setVisibility(View.INVISIBLE);
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

    public void filterRecyclerView(String charText){
        charText = charText.toLowerCase();


        notifyDataSetChanged();
    }

    class CustomerAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        int idCadastro = 0;
        final ImageView ContactImg,imgCheck;
        final TextView name,email,phone;


        CustomerAdapterViewHolder(View view) {
            super(view);
            idCadastro = 0;
            ContactImg = (ImageView)view.findViewById(R.id.img_contact);
            imgCheck = (ImageView)view.findViewById(R.id.img_checked);
            name = (TextView)view.findViewById(R.id.tv_display_name);
            email = (TextView)view.findViewById(R.id.tv_email_address);
            phone = (TextView)view.findViewById(R.id.tv_phone_number);

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
}