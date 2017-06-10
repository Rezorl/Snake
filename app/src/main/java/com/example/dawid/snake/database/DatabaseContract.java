package com.example.dawid.snake.database;

import android.provider.BaseColumns;

/**
 * Created by Dawid on 16.05.2017.
 */

public final class DatabaseContract {
    private DatabaseContract()
    {

    }
    public static class DB implements BaseColumns
    {
        public static final String TABLE_PERSONS = "persons";
        public static final String COLUMN_NICK = "nick";
        public static final String COLUMN_POINTS = "points";
        public static final String COLUMN_LEVEL = "level";
    }
}
