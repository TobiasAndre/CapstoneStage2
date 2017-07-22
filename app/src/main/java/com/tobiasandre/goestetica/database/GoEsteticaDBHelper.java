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
        final String SQL_CREATE_CUSTOMER_TABLE =
                " CREATE TABLE "+ GoEsteticaContract.CustomerEntry.TABLE_NAME + " ( " +
                        GoEsteticaContract.CustomerEntry._ID               + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        GoEsteticaContract.CustomerEntry.COLUMN_CUSTOMER_NAME + " STRING ,"+
                        GoEsteticaContract.CustomerEntry.COLUMN_CUSTOMER_FONE + " STRING ,"+
                        GoEsteticaContract.CustomerEntry.COLUMN_CUSTOMER_CELLPHONE + " STRING ,"+
                        GoEsteticaContract.CustomerEntry.COLUMN_CUSTOMER_DEFAULT_PAYMENT_TYPE + " STRING ,"+
                        GoEsteticaContract.CustomerEntry.COLUMN_CUSTOMER_EMAIL+" STRING ,"+
                        GoEsteticaContract.CustomerEntry.COLUMN_CUSTOMER_ADDRESS+" STRING,"+
                        GoEsteticaContract.CustomerEntry.COLUMN_CUSTOMER_PHOTO + " STRING,"+
                " UNIQUE (" + GoEsteticaContract.CustomerEntry._ID + ") ON CONFLICT REPLACE);";

        db.execSQL(SQL_CREATE_CUSTOMER_TABLE);

        final String SQL_CREATE_TREATMENT_TYPE_TABLE =
                " CREATE TABLE "+GoEsteticaContract.TreatmentTypeEntry.TABLE_NAME + " ( "+
                        GoEsteticaContract.TreatmentTypeEntry._ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                        GoEsteticaContract.TreatmentTypeEntry.COLUMN_TREATMENT_TYPE_NAME+" STRING NOT NULL,"+
                " UNIQUE("+GoEsteticaContract.TreatmentTypeEntry._ID+")ON CONFLICT REPLACE);";

        db.execSQL(SQL_CREATE_TREATMENT_TYPE_TABLE);

        final String SQL_CREATE_TREATMENT_TABLE =
                " CREATE TABLE "+ GoEsteticaContract.TreatmentEntry.TABLE_NAME + " ( "+
                    GoEsteticaContract.TreatmentEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    GoEsteticaContract.TreatmentEntry.COLUMN_TREATMENT_NAME + " STRING NOT NULL, "+
                    GoEsteticaContract.TreatmentEntry.COLUMN_TREATMENT_DESCRIPTION + " STRING, "+
                    GoEsteticaContract.TreatmentEntry.COLUMN_TREATMENT_PRICE + " NUMERIC, "+
                    GoEsteticaContract.TreatmentEntry.COLUMN_TREATMENT_SESSIONS + " INTEGER, "+
                    GoEsteticaContract.TreatmentEntry.COLUMN_TREATMENT_DURATION + " STRING,"+
                    GoEsteticaContract.TreatmentEntry.COLUMN_TREATMENT_TYPE + " INTEGER NOT NULL,"+
                " UNIQUE (" + GoEsteticaContract.TreatmentEntry._ID + ") ON CONFLICT REPLACE);";
        db.execSQL(SQL_CREATE_TREATMENT_TABLE);

        final String SQL_CREATE_SCHEDULE_TABLE =
                " CREATE TABLE "+GoEsteticaContract.ScheduleEntry.TABLE_NAME + " ( "+
                        GoEsteticaContract.ScheduleEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                        GoEsteticaContract.ScheduleEntry.COLUMN_SCHEDULE_CUSTOMER_ID + " INTEGER NOT NULL,"+
                        GoEsteticaContract.ScheduleEntry.COLUMN_SCHEDULE_TREATMENT_ID + " INTEGER NOT NULL,"+
                        GoEsteticaContract.ScheduleEntry.COLUMN_SCHEDULE_DATE + " DATE NOT NULL,"+
                        GoEsteticaContract.ScheduleEntry.COLUMN_SCHEDULE_SESSIONS + " INTEGER NOT NULL,"+
                        GoEsteticaContract.ScheduleEntry.COLUMN_SCHEDULE_PRICE + " NUMERIC NOT NULL, "+
                        GoEsteticaContract.ScheduleEntry.COLUMN_SCHEDULE_START_HOUR + " STRING NOT NULL,"+
                        GoEsteticaContract.ScheduleEntry.COLUMN_SCHEDULE_CONFIRMED + " STRING NOT NULL,"+
                        GoEsteticaContract.ScheduleEntry.COLUMN_SCHEDULE_SESSION_MINUTES + " INTEGER NOT NULL,"+
                " UNIQUE (" + GoEsteticaContract.ScheduleEntry._ID + ") ON CONFLICT REPLACE);";
        db.execSQL(SQL_CREATE_SCHEDULE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ GoEsteticaContract.CustomerEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ GoEsteticaContract.TreatmentTypeEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ GoEsteticaContract.TreatmentEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ GoEsteticaContract.ScheduleEntry.TABLE_NAME);
        onCreate(db);
    }
}
