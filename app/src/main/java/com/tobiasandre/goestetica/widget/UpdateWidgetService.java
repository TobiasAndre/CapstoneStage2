package com.tobiasandre.goestetica.widget;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

import com.tobiasandre.goestetica.R;
import com.tobiasandre.goestetica.database.GoEsteticaContract;
import com.tobiasandre.goestetica.utils.Util;

import java.text.DecimalFormat;

/**
 * Created by TobiasAndre on 24/07/2017.
 */

public class UpdateWidgetService extends Service {

    private int mQtScheduling = 0;
    private int mQtConfirmations = 0;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        DecimalFormat formato = new DecimalFormat("00");

        RemoteViews view = new RemoteViews(getPackageName(), R.layout.widget_layout);
        Uri contentUri = GoEsteticaContract.ScheduleEntry.CONTENT_URI;
        String selection = GoEsteticaContract.ScheduleEntry._ID + " = ?";
        String[] selectionArguments = new String[]{String.valueOf("")};
        Cursor c = getBaseContext().getContentResolver().query(contentUri, null, selection, selectionArguments, null);
        if (c != null) {
            while (c.moveToNext()) {
                mQtScheduling++;
            }
            c.close();
        }

        view.setTextViewText(R.id.id_qt_schedule, formato.format(mQtScheduling));
        view.setTextViewText(R.id.id_qt_confirmations,formato.format(mQtConfirmations));

        ComponentName theWidget = new ComponentName(this, GoEsteticaWidgetProvider.class);

        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        manager.updateAppWidget(theWidget, view);


        return super.onStartCommand(intent, flags, startId);
    }
}
