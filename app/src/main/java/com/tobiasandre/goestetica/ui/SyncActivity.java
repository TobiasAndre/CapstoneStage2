package com.tobiasandre.goestetica.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tobiasandre.goestetica.R;
import com.tobiasandre.goestetica.sync.SyncResultReceiver;
import com.tobiasandre.goestetica.sync.SyncService;

public class SyncActivity extends AppCompatActivity implements SyncResultReceiver.Receiver {

    private SyncResultReceiver mReceiver;
    ProgressBar mProgressao;
    TextView mTextoProgressao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_sync);

        mProgressao = (ProgressBar)findViewById(R.id.progressync);

        mTextoProgressao = (TextView)findViewById(R.id.txtProgressao);

        ImageView imgloading = (ImageView)findViewById(R.id.imgloading);

        Glide.with(this)
                .load(R.drawable.loading)
                .asGif()
                .into(imgloading);

        mReceiver = new SyncResultReceiver(new Handler());
        mReceiver.setReceiver(this);


        Intent intent = new Intent(Intent.ACTION_SYNC, null, this, SyncService.class);
        intent.putExtra("receiver", mReceiver);
        startService(intent);

    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        String error = "";
        switch (resultCode) {
            case SyncService.STATUS_RUNNING:
                String[] results = resultData.getStringArray("result");
                error = resultData.getString(Intent.EXTRA_TEXT);
                mTextoProgressao.setText(error);

                break;
            case SyncService.STATUS_FINISHED:
                error = resultData.getString(Intent.EXTRA_TEXT);
                mTextoProgressao.setText(error);
                mProgressao.setVisibility(View.GONE);

                Intent returnIntent = new Intent();
                returnIntent.putExtra("result",2);
                setResult(Activity.RESULT_OK,returnIntent);

                finish();

            case SyncService.STATUS_ERROR:
                error = resultData.getString(Intent.EXTRA_TEXT);
                mTextoProgressao.setText(error);
                mProgressao.setVisibility(View.GONE);
                break;
        }
    }
}
