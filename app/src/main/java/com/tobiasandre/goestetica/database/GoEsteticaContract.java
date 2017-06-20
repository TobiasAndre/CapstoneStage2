package com.tobiasandre.goestetica.database;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by TobiasAndre on 19/06/2017.
 */

public class GoEsteticaContract {

    public static final String CONTENT_AUTHORITY = "com.tobiasandre.goestetica";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_CUSTOMER = "customer";
    public static final String PATH_TREATMENT = "treatment";
    public static final String PATH_SCHEDULE = "schedule";

    public static final class CustomerEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_CUSTOMER)
                .build();

        public static final String TABLE_NAME = "customer";

        public static final String COLUMN_CUSTOMER_ID = "customer_id";
        public static final String COLUMN_CUSTOMER_NAME = "name";
        public static final String COLUMN_CUSTOMER_FONE = "fone";
        public static final String COLUMN_CUSTOMER_CELLPHONE = "cellphone";

    }

    public static final class TreatmentEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_TREATMENT)
                .build();


        public static final String TABLE_NAME = "treatment";



    }

    public static final class ScheduleEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_SCHEDULE)
                .build();


        public static final String TABLE_NAME = "schedule";


    }

}
