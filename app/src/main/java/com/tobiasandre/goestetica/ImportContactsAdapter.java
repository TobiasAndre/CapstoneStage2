package com.tobiasandre.goestetica;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.tobiasandre.goestetica.database.model.Contact;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TobiasAndre on 24/06/2017.
 */

public class ImportContactsAdapter extends RecyclerView.Adapter<ImportContactsAdapter.ViewHolder> {

    private final static String TAG = ImportContactsAdapter.class.getSimpleName();

    private List<Contact> mContacts;
    private final Callbacks mCallbacks;
    private Context mContext;

    public interface Callbacks {
        void open(Contact contact, int position);
    }

    public ImportContactsAdapter(ArrayList<Contact> contacts, Callbacks callbacks) {
        mContacts = contacts;
        this.mCallbacks = callbacks;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_contact, parent, false);
        final Context context = view.getContext();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Contact contact = mContacts.get(position);
        final Context context = holder.mView.getContext();

        holder.mContact = contact;
        holder.mName.setText(contact.getName().trim());
        holder.mPhone.setText(contact.getNrPhone().trim());
        holder.mEmail.setText(contact.getEmail().trim());
        holder.mFoto.setImageBitmap(contact.getFoto());
        holder.mContact.setImported(holder.mContact.getImported()==true);
        if(!holder.mContact.getImported()){
            holder.mChecked.setVisibility(View.GONE);
        }
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallbacks.open(contact, holder.getAdapterPosition());
                holder.mContact.setImported(!holder.mContact.getImported()? true :false);
                if(!holder.mContact.getImported()){
                    holder.mChecked.setVisibility(View.GONE);
                }else{
                    holder.mChecked.setVisibility(View.VISIBLE);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        TextView mName,mPhone,mEmail;
        ImageView mFoto,mChecked;

        public Contact mContact;

        public ViewHolder(View view) {
            super(view);
            mName = (TextView)view.findViewById(R.id.tv_display_name);
            mPhone = (TextView)view.findViewById(R.id.tv_phone_number);
            mEmail = (TextView)view.findViewById(R.id.tv_email_address);
            mFoto = (ImageView)view.findViewById(R.id.img_contact);
            mChecked = (ImageView)view.findViewById(R.id.img_checked);
            mView = view;
        }

    }

    public void add(List<Contact> contacts) {
        mContacts.clear();
        mContacts.addAll(contacts);
        notifyDataSetChanged();
    }
}
