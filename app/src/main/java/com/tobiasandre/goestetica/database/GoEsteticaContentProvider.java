package com.tobiasandre.goestetica.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by TobiasAndre on 19/06/2017.
 */

public class GoEsteticaContentProvider extends ContentProvider {

    public static final int CODE_CUSTOMER = 100;
    public static final int CODE_TREATMENT = 101;
    public static final int CODE_SCHEDULE = 102;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private GoEsteticaDBHelper mOpenHelper;

    public static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        final String authority = GoEsteticaContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, GoEsteticaContract.PATH_CUSTOMER, CODE_CUSTOMER);
        matcher.addURI(authority, GoEsteticaContract.PATH_TREATMENT, CODE_TREATMENT);
        return matcher;
    }

    @Override
    public boolean onCreate(){

        mOpenHelper = new GoEsteticaDBHelper(getContext());
        return false;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri,@NonNull ContentValues[] values) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int rowsInserted = 0;

        switch (sUriMatcher.match(uri)) {
            case CODE_CUSTOMER:
                db.beginTransaction();
                try{
                    for(ContentValues value : values){
                        long _id = db.insert(GoEsteticaContract.CustomerEntry.TABLE_NAME,null,value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                }finally {
                    db.endTransaction();
                }

                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsInserted;

            case CODE_TREATMENT:
                db.beginTransaction();
                try{
                    for(ContentValues value : values){
                        long _id = db.insert(GoEsteticaContract.TreatmentEntry.TABLE_NAME,null,value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                }finally {
                    db.endTransaction();
                }
                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
            case CODE_SCHEDULE:
                db.beginTransaction();
                try{
                    for(ContentValues value : values){
                        long _id = db.insert(GoEsteticaContract.ScheduleEntry.TABLE_NAME,null,value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                }finally {
                    db.endTransaction();
                }
                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
            default:
                return super.bulkInsert(uri,values);
        }
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;

        switch (sUriMatcher.match(uri)) {

            case CODE_CUSTOMER: {

                cursor = mOpenHelper.getReadableDatabase().query(
                        GoEsteticaContract.CustomerEntry.TABLE_NAME,
                        projection,
                        selection ,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }

            case CODE_TREATMENT: {

                cursor = mOpenHelper.getReadableDatabase().query(
                        GoEsteticaContract.TreatmentEntry.TABLE_NAME,
                        projection,
                        GoEsteticaContract.TreatmentEntry._ID + " = ? " ,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }

            case CODE_SCHEDULE: {

                cursor = mOpenHelper.getReadableDatabase().query(
                        GoEsteticaContract.ScheduleEntry.TABLE_NAME,
                        projection,
                        GoEsteticaContract.ScheduleEntry._ID + " = ? " ,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new RuntimeException("We are not implementing getType in GoEstetica.");
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values){
        throw new RuntimeException(
                "We are not implementing insert in GoEstetica. Use bulkInsert instead");
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int numRowsDeleted;

        switch (sUriMatcher.match(uri)) {

            case CODE_CUSTOMER:
                numRowsDeleted = mOpenHelper.getWritableDatabase().delete(
                        GoEsteticaContract.CustomerEntry.TABLE_NAME,
                        selection,
                        selectionArgs);

                break;

            case CODE_TREATMENT:
                numRowsDeleted = mOpenHelper.getWritableDatabase().delete(
                        GoEsteticaContract.TreatmentEntry.TABLE_NAME,
                        selection,
                        selectionArgs);

                break;

            case CODE_SCHEDULE:

                numRowsDeleted = mOpenHelper.getWritableDatabase().delete(
                        GoEsteticaContract.ScheduleEntry.TABLE_NAME,
                        selection,
                        selectionArgs);

                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (numRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numRowsDeleted;

    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int numRowsUpdated = 0;

        switch (sUriMatcher.match(uri)){
            case CODE_CUSTOMER:
                numRowsUpdated = mOpenHelper.getWritableDatabase().update(
                        GoEsteticaContract.CustomerEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);

                break;
            case CODE_TREATMENT:

                numRowsUpdated = mOpenHelper.getWritableDatabase().update(
                        GoEsteticaContract.TreatmentEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            case CODE_SCHEDULE:

                numRowsUpdated = mOpenHelper.getWritableDatabase().update(
                        GoEsteticaContract.ScheduleEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: "+uri);
        }

        if(numRowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri,null);
        }

        return numRowsUpdated;
    }
}
