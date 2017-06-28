package com.tobiasandre.goestetica.utils;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by TobiasAndre on 22/06/2017.
 */

public class Util {



    public static void showLongSnackBar(View mView, String mText){
        Snackbar snackbar = Snackbar.make(mView, mText, Snackbar.LENGTH_LONG);
        snackbar.show();
    }


}
