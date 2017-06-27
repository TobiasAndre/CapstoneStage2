package com.tobiasandre.goestetica.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tobiasandre.goestetica.R;


/**
 * Created by TobiasAndre on 22/06/2017.
 */

public class CustomerFragment extends Fragment {

    private static String TAG = CustomerFragment.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);


        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_customer, container, false);




        return rootView;
    }

}
