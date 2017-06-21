package com.tobiasandre.goestetica;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tobiasandre.goestetica.ui.LoginActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intentLogin = new Intent(this,LoginActivity.class);
        startActivity(intentLogin);
    }
}
