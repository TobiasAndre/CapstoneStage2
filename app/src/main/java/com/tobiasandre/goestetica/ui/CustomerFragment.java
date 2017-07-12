package com.tobiasandre.goestetica.ui;


import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.tobiasandre.goestetica.R;
import com.tobiasandre.goestetica.database.GoEsteticaContract;
import com.tobiasandre.goestetica.utils.TYPE_SNACKBAR;
import com.tobiasandre.goestetica.utils.Util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;


/**
 * Created by TobiasAndre on 22/06/2017.
 */

public class CustomerFragment extends Fragment {

    private static String TAG = CustomerFragment.class.getSimpleName();
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 101;

    public  static final int RequestPermissionCode  = 1 ;
    private int idCustomer=-1;
    static final int REQUEST_TAKE_PHOTO = 1;
    View rootView;
    EditText edCustomerName,edCustomerPhone,edCustomerCellPhone,edCustomerEmail,edCustomerAddress;
    ImageButton saveButton,findCustomerButton;
    Spinner spnDefaultPaymentType;
    ImageView customerPhoto;
    FloatingActionButton btnAddPhoto;
    Uri uri;
    Intent GalIntent, CropIntent ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_customer, container, false);
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_CONTACTS},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        }

        if(ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED){

            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.CAMERA)){

            }

            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CAMERA},
                    MY_CAMERA_REQUEST_CODE);
        }

        edCustomerName = (EditText)rootView.findViewById(R.id.ed_customer_name);
        edCustomerPhone = (EditText)rootView.findViewById(R.id.ed_customer_phone);
        edCustomerCellPhone = (EditText)rootView.findViewById(R.id.ed_customer_cellphone);
        edCustomerEmail = (EditText)rootView.findViewById(R.id.ed_customer_email);
        edCustomerAddress = (EditText)rootView.findViewById(R.id.ed_customer_address);
        findCustomerButton = (ImageButton)rootView.findViewById(R.id.btn_find_customer);
        saveButton = (ImageButton)rootView.findViewById(R.id.btn_save_customer);
        spnDefaultPaymentType = (Spinner)rootView.findViewById(R.id.spnPaymentType);
        customerPhoto = (ImageView)rootView.findViewById(R.id.photo);
        btnAddPhoto = (FloatingActionButton)rootView.findViewById(R.id.fab_add_photo);

        if(findCustomerButton!=null){
            findCustomerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentListCustomer = new Intent(rootView.getContext(),CustomerListActivity.class);
                    startActivityForResult(intentListCustomer,3);
                }
            });
        }

        if(saveButton!=null){
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(validateRequired()) {
                        saveValues();
                    }
                }
            });
        }
        if(btnAddPhoto!=null){
            btnAddPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    customDialog();
                }
            });
        }

        EnableRuntimePermission();

        if(findCustomerButton!=null){

        }
        Intent intentListCustomer = new Intent(rootView.getContext(),CustomerListActivity.class);
        startActivityForResult(intentListCustomer,3);

        return rootView;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            ImageCropFunction();
        }
        else if(requestCode ==2){
            uri = data.getData();
            ImageCropFunction();
        }
        else if(requestCode ==3){
            if(data!=null) {
                Bundle res = data.getExtras();
                if (res.containsKey("id")) {
                    idCustomer = res.getInt("id");
                    loadData(idCustomer);
                }
            }
        }
        else if (requestCode == 1) {

            if (data != null) {

                Bundle bundle = data.getExtras();

                Bitmap bitmap = bundle.getParcelable("data");

                customerPhoto.setImageBitmap(bitmap);
                customerPhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);

            }
        }
    }

    private void loadData(int id){
        Uri contentUri = GoEsteticaContract.CustomerEntry.CONTENT_URI;
        String selection = GoEsteticaContract.CustomerEntry._ID + " = ?";
        String[] selectionArguments = new String[]{String.valueOf(id)};

        Cursor c = getContext().getContentResolver().query(contentUri, null, selection, selectionArguments, null);
        if (c != null) {
            while (c.moveToNext()) {

                String imageUrl = c.getString(c.getColumnIndexOrThrow(GoEsteticaContract.CustomerEntry.COLUMN_CUSTOMER_PHOTO));
                Bitmap myBitmap = BitmapFactory.decodeFile(imageUrl);
                customerPhoto.setImageBitmap(myBitmap);

                edCustomerName.setText(c.getString(c.getColumnIndexOrThrow(GoEsteticaContract.CustomerEntry.COLUMN_CUSTOMER_NAME)));
                edCustomerPhone.setText(c.getString(c.getColumnIndexOrThrow(GoEsteticaContract.CustomerEntry.COLUMN_CUSTOMER_FONE)));
                edCustomerCellPhone.setText(c.getString(c.getColumnIndexOrThrow(GoEsteticaContract.CustomerEntry.COLUMN_CUSTOMER_CELLPHONE)));
                edCustomerEmail.setText(c.getString(c.getColumnIndexOrThrow(GoEsteticaContract.CustomerEntry.COLUMN_CUSTOMER_EMAIL)));
                selectValue(spnDefaultPaymentType, c.getString(c.getColumnIndexOrThrow(GoEsteticaContract.CustomerEntry.COLUMN_CUSTOMER_DEFAULT_PAYMENT_TYPE)));
                edCustomerAddress.setText(c.getString(c.getColumnIndexOrThrow(GoEsteticaContract.CustomerEntry.COLUMN_CUSTOMER_ADDRESS)));
            }
            c.close();
        }
    }

    private void selectValue(Spinner spinner, String value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).equals(value)) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    private Boolean validateRequired(){
        if(edCustomerName.getText().toString().isEmpty()){
            Util.makeSnackbar(rootView,getString(R.string.required_field).concat("-").concat(getString(R.string.customer_name)),Snackbar.LENGTH_LONG,TYPE_SNACKBAR.ERROR).show();
            return false;
        }
        if(edCustomerCellPhone.getText().toString().isEmpty()){
            Util.makeSnackbar(rootView,getString(R.string.required_field).concat("-").concat(getString(R.string.customer_cellphone)),Snackbar.LENGTH_LONG,TYPE_SNACKBAR.ERROR).show();
            return false;
        }
        return true;
    }

    private void saveValues(){
        try {

            customerPhoto.buildDrawingCache();

            Bitmap bitmap = customerPhoto.getDrawingCache();

            String imagePath = savePhoto(bitmap);

            ContentValues[] customerValues = new ContentValues[1];
            ContentValues value = new ContentValues();

            value.put(GoEsteticaContract.CustomerEntry.COLUMN_CUSTOMER_NAME, edCustomerName.getText().toString());
            value.put(GoEsteticaContract.CustomerEntry.COLUMN_CUSTOMER_CELLPHONE, edCustomerCellPhone.getText().toString());
            value.put(GoEsteticaContract.CustomerEntry.COLUMN_CUSTOMER_FONE, edCustomerPhone.getText().toString());
            value.put(GoEsteticaContract.CustomerEntry.COLUMN_CUSTOMER_ADDRESS, edCustomerAddress.getText().toString());
            value.put(GoEsteticaContract.CustomerEntry.COLUMN_CUSTOMER_EMAIL, edCustomerEmail.getText().toString());
            value.put(GoEsteticaContract.CustomerEntry.COLUMN_CUSTOMER_PHOTO, imagePath);
            value.put(GoEsteticaContract.CustomerEntry.COLUMN_CUSTOMER_DEFAULT_PAYMENT_TYPE, spnDefaultPaymentType.getSelectedItem().toString());

            customerValues[0] = value;

            ContentResolver customerContentResolver = this.getContext().getContentResolver();

            if(idCustomer > 0) {

                String selectionClause = GoEsteticaContract.CustomerEntry._ID + " = ?";
                String[] selectionArgs = new String[]{String.valueOf(idCustomer)};
                customerContentResolver.update(
                        GoEsteticaContract.CustomerEntry.CONTENT_URI,
                        customerValues[0],selectionClause,selectionArgs);

            }else{
                customerContentResolver.bulkInsert(
                        GoEsteticaContract.CustomerEntry.CONTENT_URI,
                        customerValues);
            }


            Util.makeSnackbar(rootView,getString(R.string.save_sucess), Snackbar.LENGTH_LONG, TYPE_SNACKBAR.SUCCESS).show();

            ClearFields();

        }catch (Exception error){
            Util.makeSnackbar(rootView,TAG.concat(":").concat(getString(R.string.general_error)).concat(" - ").concat(error.getMessage()),Snackbar.LENGTH_LONG,TYPE_SNACKBAR.ERROR).show();
        }
    }

    private String savePhoto(Bitmap bm){
        String fileName = "";
        try {
            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root + "/GoEstetica");
            myDir.mkdirs();

            String fname = System.currentTimeMillis()+".jpg";
            File file = new File (myDir, fname);
            if (file.exists ()) file.delete ();
            file.createNewFile();

            fileName = file.getAbsolutePath();
            try {
                FileOutputStream out = new FileOutputStream(file);
                bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
                out.flush();
                out.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileName;
    }

    private void ClearFields(){
        Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.blank_profile_picture);
        customerPhoto.setImageBitmap(bitmap);
        edCustomerName.setText("");
        edCustomerCellPhone.setText("");
        edCustomerAddress.setText("");
        edCustomerEmail.setText("");
        edCustomerPhone.setText("");
        edCustomerName.requestFocus();
        idCustomer = -1;
    }

    public void EnableRuntimePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.CAMERA))
        {

            Toast.makeText(getActivity(),"CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(getActivity(),new String[]{
                    Manifest.permission.CAMERA}, RequestPermissionCode);

        }
    }

    public void GetImageFromGallery(){

        GalIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(Intent.createChooser(GalIntent, "Select Image From Gallery"), 2);

    }

    public void ClickImageFromCamera() {
        dispatchTakePictureIntent();
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String mCurrentPhotoPath;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = null;
                try {
                    photoURI = FileProvider.getUriForFile(getContext(),
                            "com.example.android.fileprovider",
                            photoFile);
                }catch (Exception erro){
                    System.out.println(erro.getMessage());
                }
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    public void ImageCropFunction() {

        // Image Crop Code
        try {
            CropIntent = new Intent("com.android.camera.action.CROP");

            CropIntent.setDataAndType(uri, "image/*");
            CropIntent.putExtra("crop", "true");
            CropIntent.putExtra("outputX", 256);
            CropIntent.putExtra("outputY", 256);
            CropIntent.putExtra("aspectX", 1);
            CropIntent.putExtra("aspectY", 1);
            CropIntent.putExtra("scaleUpIfNeeded", true);
            CropIntent.putExtra("return-data", true);

            startActivityForResult(CropIntent, 1);

        } catch (ActivityNotFoundException e) {

        }
    }

    public void customDialog(){
        // custom dialog
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.custom_image_dialog);
        dialog.setTitle("Photo");

        TextView text = (TextView) dialog.findViewById(R.id.tv_select_image);
        TextView text2= (TextView) dialog.findViewById(R.id.tv_take_picture);

        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                GetImageFromGallery();
            }
        });

        text2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ClickImageFromCamera();
            }
        });

        dialog.show();

    }


}
