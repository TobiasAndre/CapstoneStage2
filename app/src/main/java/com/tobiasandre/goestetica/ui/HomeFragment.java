package com.tobiasandre.goestetica.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tobiasandre.goestetica.MainActivity;
import com.tobiasandre.goestetica.R;

/**
 * Created by TobiasAndre on 22/06/2017.
 */

public class HomeFragment extends Fragment {

    FloatingActionButton btnAdd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_init, container, false);

        btnAdd = (FloatingActionButton)rootView.findViewById(R.id.fab_add_scheduling);

        if(btnAdd!=null) {
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ImportContactsActivity.class);
                    startActivity(intent);
                }
            });
        }

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}