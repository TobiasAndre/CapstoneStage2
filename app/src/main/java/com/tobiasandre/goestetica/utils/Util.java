package com.tobiasandre.goestetica.utils;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by TobiasAndre on 22/06/2017.
 */

public class Util {

    @NonNull
    public static Snackbar makeSnackbar(@NonNull View layout, @NonNull CharSequence  text, int duration,TYPE_SNACKBAR type){
        Snackbar snackBarView = Snackbar.make(layout, text, duration);
        snackBarView.getView().setBackgroundColor(Color.BLACK);
        TextView tv = (TextView) snackBarView.getView().findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);

        if(type == TYPE_SNACKBAR.ERROR) {
            snackBarView.getView().setBackgroundColor(Color.RED);
            tv.setTextColor(Color.WHITE);
        }else if (type==TYPE_SNACKBAR.ALERT){
            snackBarView.getView().setBackgroundColor(Color.YELLOW);
            tv.setTextColor(Color.BLACK);
        }else if(type==TYPE_SNACKBAR.MESSAGE){
            snackBarView.getView().setBackgroundColor(Color.BLUE);
            tv.setTextColor(Color.WHITE);
        }else if(type==TYPE_SNACKBAR.SUCCESS){
            snackBarView.getView().setBackgroundColor(Color.GREEN);
            tv.setTextColor(Color.BLACK);
        }

        //snackBarView.setActionTextColor(actionTextColor);

        return snackBarView;
    }

    public static void setSpinText(Spinner spin, String text)
    {
        for(int i= 0; i < spin.getAdapter().getCount(); i++)
        {
            if(spin.getAdapter().getItem(i).toString().contains(text))
            {
                spin.setSelection(i);
            }
        }
    }

    public static String getStringDate(){
        String mStrDate = "";

        DecimalFormat format = new DecimalFormat("00");
        int mDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int mYear = Calendar.getInstance().get(Calendar.YEAR);
        int mMonth = Calendar.getInstance().get(Calendar.MONTH)+1;

        mStrDate = format.format(mDay)+"-"+format.format(mMonth)+"-"+mYear;

        return mStrDate;
    }


}
