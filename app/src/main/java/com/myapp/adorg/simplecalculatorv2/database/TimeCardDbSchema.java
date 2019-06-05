package com.myapp.adorg.simplecalculatorv2.database;

public class TimeCardDbSchema {
    public static final class TimeCardTable {
        public static final String NAME = "history_menu";

        public static final class Cols {

            public static final String UUID = "UUID";
            public static final String DATE = "date";
            public static final String TREATMENT_TIME = "treatment_time";
            public static final String END_HOUR = "end_hour";
            public static final String END_MINUTE = "end_minute";
            public static final String START_TIME = "start_time";
            public static final String END_TIME = "end_time";
            public static final String PRODUCTIVITY = "productivity";
            public static final String PAID_TIME = "paid_time";
            public static final String UNPAID_BREAK = "unpaid_break";
            public static final String MEETING_TRAVEL = "meeting_travel";
            public static final String TIME_CARD_DATE = "time_card_date";
            public static final String PAID_TIME_INT = "paid_time_int";
            public static final String PRODUCTIVITY_INT = "productivity_int";
            public static final String START_HOUR = "start_hour";
            public static final String START_MINUTE = "start_minute";
            public static final String IS_24_HOUR = "is_24_hour";
        }
    }
}