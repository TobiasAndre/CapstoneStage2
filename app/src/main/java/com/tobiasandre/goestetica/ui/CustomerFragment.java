package com.tobiasandre.goestetica.ui;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import com.tobiasandre.goestetica.R;
import com.tobiasandre.goestetica.database.GoEsteticaContentProvider;
import com.tobiasandre.goestetica.database.GoEsteticaContract;
import com.tobiasandre.goestetica.database.GoEsteticaDBHelper;
import com.tobiasandre.goestetica.utils.Util;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;


/**
 * Created by TobiasAndre on 22/06/2017.
 */

public class CustomerFragment extends Fragment {

    private static String TAG = CustomerFragment.class.getSimpleName();

    View rootView;
    EditText edCustomerName,edCustomerPhone,edCustomerCellPhone,edCustomerEmail,edCustomerAddress;
    ImageButton saveButton,findCustomerButton;
    Spinner spnDefaultPaymentType;
    ImageView customerPhoto;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_customer, container, false);

        edCustomerName = (EditText)rootView.findViewById(R.id.ed_customer_name);
        edCustomerPhone = (EditText)rootView.findViewById(R.id.ed_customer_phone);
        edCustomerCellPhone = (EditText)rootView.findViewById(R.id.ed_customer_cellphone);
        edCustomerEmail = (EditText)rootView.findViewById(R.id.ed_customer_email);
        edCustomerAddress = (EditText)rootView.findViewById(R.id.ed_customer_address);
        findCustomerButton = (ImageButton)rootView.findViewById(R.id.btn_find_customer);
        saveButton = (ImageButton)rootView.findViewById(R.id.btn_save_customer);
        spnDefaultPaymentType = (Spinner)rootView.findViewById(R.id.spnPaymentType);
        customerPhoto = (ImageView)rootView.findViewById(R.id.photo);

        if(saveButton!=null){
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    validateRequired();

                    saveValues();
                }
            });
        }

        if(findCustomerButton!=null){

        }


        Intent intentListCustomer = new Intent(rootView.getContext(),CustomerListActivity.class);
        startActivity(intentListCustomer);


        return rootView;
    }



    private void validateRequired(){
        if(edCustomerName.getText().toString().isEmpty()){
            Util.showLongSnackBar(rootView,getString(R.string.required_field).concat("-").concat(getString(R.string.customer_name)));
            return;
        }
        if(edCustomerCellPhone.getText().toString().isEmpty()){
            Util.showLongSnackBar(rootView,getString(R.string.required_field).concat("-").concat(getString(R.string.customer_cellphone)));
            return;
        }
    }

    private void saveValues(){
        try {
            customerPhoto.buildDrawingCache();
            Bitmap bitmap = customerPhoto.getDrawingCache();
            long idCustomerPhoto = System.currentTimeMillis();

            savePhoto(bitmap, idCustomerPhoto);

            ContentValues[] customerValues = new ContentValues[1];
            ContentValues value = new ContentValues();
            value.put(GoEsteticaContract.CustomerEntry.COLUMN_CUSTOMER_NAME, edCustomerName.getText().toString());
            value.put(GoEsteticaContract.CustomerEntry.COLUMN_CUSTOMER_CELLPHONE, edCustomerCellPhone.getText().toString());
            value.put(GoEsteticaContract.CustomerEntry.COLUMN_CUSTOMER_FONE, edCustomerPhone.getText().toString());
            value.put(GoEsteticaContract.CustomerEntry.COLUMN_CUSTOMER_ADDRESS, edCustomerAddress.getText().toString());
            value.put(GoEsteticaContract.CustomerEntry.COLUMN_CUSTOMER_EMAIL, edCustomerEmail.getText().toString());
            value.put(GoEsteticaContract.CustomerEntry.COLUMN_CUSTOMER_PHOTO, String.valueOf(idCustomerPhoto).concat(".jpg"));
            value.put(GoEsteticaContract.CustomerEntry.COLUMN_CUSTOMER_DEFAULT_PAYMENT_TYPE, spnDefaultPaymentType.getSelectedItem().toString());

            customerValues[0] = value;

            ContentResolver customerContentResolver = this.getContext().getContentResolver();

            customerContentResolver.bulkInsert(
                    GoEsteticaContract.CustomerEntry.CONTENT_URI,
                    customerValues);

        }catch (Exception error){
            Util.showLongSnackBar(rootView,TAG.concat(":").concat(getString(R.string.general_error)).concat(" - ").concat(error.getMessage()));
        }
    }

    private void savePhoto(Bitmap bm,long idCustomerPhoto){
        ByteArrayOutputStream outputBuffer = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, outputBuffer);
        byte[] byteImage1=outputBuffer.toByteArray();

        //save file to internal storage
        FileOutputStream outputStream;

        try {
            outputStream = this.getContext().openFileOutput(String.valueOf(idCustomerPhoto).concat(".jpg"), Context.MODE_PRIVATE);
            outputStream.write(byteImage1);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            Util.showLongSnackBar(rootView,TAG.concat(":").concat(getString(R.string.general_error)).concat(" - ").concat(e.getMessage()));
        }
    }

}
