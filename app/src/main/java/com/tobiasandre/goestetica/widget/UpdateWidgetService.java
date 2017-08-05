package com.tobiasandre.goestetica.widget;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.tobiasandre.goestetica.R;
import com.tobiasandre.goestetica.database.GoEsteticaContract;
import com.tobiasandre.goestetica.utils.Util;

import java.text.DecimalFormat;
import java.util.Calendar;

/**
 * Created by TobiasAndre on 24/07/2017.
 */

public class UpdateWidgetService extends RemoteViewsService {

    private int mQtScheduling = 0;
    private int mQtConfirmations = 0;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StackRemoteViewsFactory(this.getApplicationContext(), intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        DecimalFormat formato = new DecimalFormat("00");

        RemoteViews view = new RemoteViews(getPackageName(), R.layout.widget_layout);
        Uri contentUri = GoEsteticaContract.ScheduleEntry.CONTENT_URI;
        DecimalFormat decimalFormat = new DecimalFormat("00");


        int mDay = Calendar.getInstance().DAY_OF_MONTH-1;
        int mMonth = Calendar.getInstance().MONTH;
        int mYear = Calendar.getInstance().YEAR;


        try {
            String selection = GoEsteticaContract.ScheduleEntry.COLUMN_SCHEDULE_DATE + " between '" + Util.getStringDate().replace("/", "-").trim() + "' and '" + decimalFormat.format(mDay + 1) + "-" + decimalFormat.format(mMonth + 1) + "-" + mYear + "'";
            String[] selectionArguments = new String[]{String.valueOf("")};
            Cursor c = getBaseContext().getContentResolver().query(contentUri, null, selection, selectionArguments, null);
            if (c != null) {
                while (c.moveToNext()) {
                    mQtScheduling++;
                }
                c.close();
            }
        }catch (Exception error){
            System.out.println(error.getMessage());
        }
        view.setTextViewText(R.id.id_qt_schedule, formato.format(mQtScheduling));
        view.setTextViewText(R.id.id_qt_confirmations,formato.format(mQtConfirmations));

        ComponentName theWidget = new ComponentName(this, GoEsteticaWidgetProvider.class);

        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        manager.updateAppWidget(theWidget, view);


        return super.onStartCommand(intent, flags, startId);
    }
}

class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;

    public StackRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_layout);

        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}