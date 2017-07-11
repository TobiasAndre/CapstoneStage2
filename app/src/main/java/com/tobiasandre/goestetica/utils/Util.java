package com.tobiasandre.goestetica.utils;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

/**
 * Created by TobiasAndre on 22/06/2017.
 */

public class Util {



    public static void showLongSnackBar(View mView, String mText,TYPE_SNACKBAR type){
        Snackbar snackbar = Snackbar.make(mView, mText, Snackbar.LENGTH_LONG);
        if(type == TYPE_SNACKBAR.ERROR) {
            snackbar.getView().setBackgroundColor(Color.RED);
        }else if (type==TYPE_SNACKBAR.ALERT){
            snackbar.getView().setBackgroundColor(Color.YELLOW);
        }else if(type==TYPE_SNACKBAR.MESSAGE){
            snackbar.getView().setBackgroundColor(Color.BLUE);
        }else if(type==TYPE_SNACKBAR.SUCCESS){
            snackbar.getView().setBackgroundColor(Color.GREEN);
        }
        snackbar.show();
    }


    @NonNull
    public static Snackbar makeSnackbar(@NonNull View layout, @NonNull CharSequence  text, int duration, int backgroundColor, int textColor/*, int actionTextColor*/){
        Snackbar snackBarView = Snackbar.make(layout, text, duration);
        snackBarView.getView().setBackgroundColor(backgroundColor);
        //snackBarView.setActionTextColor(actionTextColor);
        TextView tv = (TextView) snackBarView.getView().findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(textColor);
        return snackBarView;
    }

}
