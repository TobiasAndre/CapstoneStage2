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

public class ScheduleFragment extends Fragment {

    View rootView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.schedule_fragment, container, false);

        return rootView;
    }

}
