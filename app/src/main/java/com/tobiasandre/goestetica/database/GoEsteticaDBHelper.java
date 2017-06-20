package com.tobiasandre.goestetica.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by TobiasAndre on 19/06/2017.
 */

public class GoEsteticaDBHelper extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "goestetica.db";
    private static final int DATABASE_VERSION = 1;

    public GoEsteticaDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_CLIENTE_TABLE ="";

        db.execSQL(SQL_CREATE_CLIENTE_TABLE);

        final String SQL_CREATE_TRATAMENTO_TABLE = "";

        db.execSQL(SQL_CREATE_TRATAMENTO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " );
        onCreate(db);
    }
}
