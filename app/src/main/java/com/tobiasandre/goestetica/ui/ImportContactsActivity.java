package com.tobiasandre.goestetica.ui;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.tobiasandre.goestetica.ImportContactsAdapter;
import com.tobiasandre.goestetica.R;
import com.tobiasandre.goestetica.database.model.Contact;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class ImportContactsActivity extends AppCompatActivity implements ImportContactsAdapter.Callbacks{

    private static final String TAG = ImportContactsActivity.class.getSimpleName();
    ArrayList<Contact> mContacts;
    private ProgressDialog pDialog;
    Cursor cursor;
    int counter;
    RecyclerView mRecyclerView;
    ImportContactsAdapter mAdapter;
    private Handler updateBarHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_contacts);

        mContacts = new ArrayList<>();
        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_contacts);

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        mAdapter = new ImportContactsAdapter(new ArrayList<Contact>(),this);
        mRecyclerView.setAdapter(mAdapter);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getString(R.string.read_contacts));
        pDialog.setCancelable(false);
        pDialog.show();


        new Thread(new Runnable() {

            @Override
            public void run() {
                getContacts();
            }
        }).start();


        mAdapter.add(mContacts);
    }


    public void getContacts() {

        try {
            String phoneNumber = null;
            String email = null;

            Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
            String _ID = ContactsContract.Contacts._ID;
            String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
            String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

            String Photo_URI = ContactsContract.Contacts.PHOTO_THUMBNAIL_URI;


            Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
            String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
            String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

            Uri EmailCONTENT_URI = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
            String EmailCONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
            String DATA = ContactsContract.CommonDataKinds.Email.DATA;

            ContentResolver contentResolver = getContentResolver();

            cursor = contentResolver.query(CONTENT_URI, null, null, null, null);

            // Iterate every contact in the phone
            if (cursor.getCount() > 0) {

                counter = 0;
                while (cursor.moveToNext()) {
                    Contact contact = new Contact();

                    String contact_id = cursor.getString(cursor.getColumnIndex(_ID));
                    String name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
                    long contactId = Long.valueOf(contact_id);
                    contact.setName(name);
                    contact.setNrPhone("");
                    contact.setEmail("");
                    contact.setImported(true);

                    int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(HAS_PHONE_NUMBER)));
                    if (hasPhoneNumber > 0) {

                        contact.setName(name);

                        //This is to read multiple phone numbers associated with the same contact
                        Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[]{contact_id}, null);

                        while (phoneCursor.moveToNext()) {
                            phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                            contact.setNrPhone(phoneNumber);
                        }

                        phoneCursor.close();

                        // Read every email id associated with the contact
                        Cursor emailCursor = contentResolver.query(EmailCONTENT_URI, null, EmailCONTACT_ID + " = ?", new String[]{contact_id}, null);
                        while (emailCursor.moveToNext()) {

                            email = emailCursor.getString(emailCursor.getColumnIndex(DATA));
                            contact.setEmail(email);
                        }
                        emailCursor.close();

                        try {

                            InputStream streamPhoto = openPhoto(contactId);
                            Bitmap photoContact = BitmapFactory.decodeStream(streamPhoto);

                            if (photoContact == null) {
                                photoContact = BitmapFactory.decodeResource(getResources(), R.drawable.blank_profile_picture);
                            }

                            contact.setFoto(photoContact);
                        } catch (Exception erro) {
                            Log.e(TAG,erro.getMessage());
                        }
                        mContacts.add(contact);
                    }
                }


                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            mAdapter.add(mContacts);
                        }catch (Exception e){
                            Log.e(TAG,e.getMessage());
                        }
                    }
                });


                // Dismiss the progressbar after 500 millisecondds
                updateBarHandler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        pDialog.cancel();
                    }
                }, 500);


            }
        }catch (Exception erro){
            Log.e(TAG,erro.getMessage());
            pDialog.cancel();
        }
    }

    @Override
    public void open(Contact contact, int position) {

    }

    public InputStream openPhoto(long contactId) {
        Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
        Uri photoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
        Cursor cursor = getContentResolver().query(photoUri,
                new String[] {ContactsContract.Contacts.Photo.PHOTO}, null, null, null);
        if (cursor == null) {
            return null;
        }
        try {
            if (cursor.moveToFirst()) {
                byte[] data = cursor.getBlob(0);
                if (data != null) {
                    return new ByteArrayInputStream(data);
                }
            }
        } finally {
            cursor.close();
        }
        return null;
    }



}
