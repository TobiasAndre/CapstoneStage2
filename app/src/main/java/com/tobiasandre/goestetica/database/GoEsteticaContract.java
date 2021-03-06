package com.tobiasandre.goestetica.database;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by TobiasAndre on 19/06/2017.
 */

public class GoEsteticaContract {

    public static final String CONTENT_AUTHORITY = "com.tobiasandre.goestetica";

    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_CUSTOMER = "customer";
    public static final String PATH_TREATMENT = "treatment";
    public static final String PATH_SCHEDULE = "schedule";
    public static final String PATH_TREATMENT_TYPE = "treatmenttype";

    public static final class CustomerEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_CUSTOMER)
                .build();

        public static final String TABLE_NAME = "customer";
        public static final String COLUMN_CUSTOMER_NAME = "name";
        public static final String COLUMN_CUSTOMER_FONE = "fone";
        public static final String COLUMN_CUSTOMER_CELLPHONE = "cellphone";
        public static final String COLUMN_CUSTOMER_PHOTO = "photo";
        public static final String COLUMN_CUSTOMER_ADDRESS = "address";
        public static final String COLUMN_CUSTOMER_DEFAULT_PAYMENT_TYPE = "payment_type";
        public static final String COLUMN_CUSTOMER_EMAIL = "email";

        public static final String DEFAULT_SORT = COLUMN_CUSTOMER_NAME;

        /** Matches: /items/ */
        public static Uri buildDirUri() {
            return BASE_CONTENT_URI.buildUpon().appendPath("customer").build();
        }

        /** Matches: /items/[_id]/ */
        public static Uri buildItemUri(long _id) {
            return BASE_CONTENT_URI.buildUpon().appendPath("customer").appendPath(Long.toString(_id)).build();
        }


    }

    public static final class TreatmentTypeEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_TREATMENT_TYPE)
                .build();

        public static final String TABLE_NAME="treatmenttype";
        public static final String COLUMN_TREATMENT_TYPE_NAME = "name";

        public static Uri buildDirUri() {
            return BASE_CONTENT_URI.buildUpon().appendPath("treatmenttype").build();
        }

        /** Matches: /items/[_id]/ */
        public static Uri buildItemUri(long _id) {
            return BASE_CONTENT_URI.buildUpon().appendPath("treatmenttype").appendPath(Long.toString(_id)).build();
        }
    }

    public static final class TreatmentEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_TREATMENT)
                .build();


        public static final String TABLE_NAME = "treatment";
        public static final String COLUMN_TREATMENT_NAME = "name";
        public static final String COLUMN_TREATMENT_DESCRIPTION = "description";
        public static final String COLUMN_TREATMENT_PRICE = "price";
        public static final String COLUMN_TREATMENT_SESSIONS = "sessions";
        public static final String COLUMN_TREATMENT_DURATION = "duration";
        public static final String COLUMN_TREATMENT_TYPE = "type";
        /** Matches: /items/ */
        public static Uri buildDirUri() {
            return BASE_CONTENT_URI.buildUpon().appendPath("treatment").build();
        }

        /** Matches: /items/[_id]/ */
        public static Uri buildItemUri(long _id) {
            return BASE_CONTENT_URI.buildUpon().appendPath("treatment").appendPath(Long.toString(_id)).build();
        }

    }

    public static final class ScheduleEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_SCHEDULE)
                .build();

        public static final String TABLE_NAME = "schedule";
        public static final String COLUMN_SCHEDULE_CUSTOMER_ID = "customer_id";
        public static final String COLUMN_SCHEDULE_TREATMENT_ID = "treatment_id";
        public static final String COLUMN_SCHEDULE_SESSIONS = "sessions";
        public static final String COLUMN_SCHEDULE_PRICE = "price";
        public static final String COLUMN_SCHEDULE_DATE = "date";
        public static final String COLUMN_SCHEDULE_START_HOUR = "start_hour";
        public static final String COLUMN_SCHEDULE_CONFIRMED = "confirmed";
        public static final String COLUMN_SCHEDULE_SESSION_MINUTES = "session_minutes";

        public static final String[] SCHEDULE_COLUMNS = {
                COLUMN_SCHEDULE_CUSTOMER_ID,
                COLUMN_SCHEDULE_TREATMENT_ID,
                COLUMN_SCHEDULE_SESSIONS,
                COLUMN_SCHEDULE_PRICE,
                COLUMN_SCHEDULE_DATE,
                COLUMN_SCHEDULE_START_HOUR,
                COLUMN_SCHEDULE_SESSION_MINUTES,
                COLUMN_SCHEDULE_CONFIRMED
        };

    }

}
