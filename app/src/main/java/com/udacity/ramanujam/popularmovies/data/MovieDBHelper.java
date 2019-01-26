package com.udacity.ramanujam.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MovieDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "popularmovies.db";

    public static final int DATABASE_VERSION = 2;

    private static final String DATABASE_ALTER_SQL_1 = "ALTER TABLE "
            + MovieContract.MovieEntry.TABLE_NAME
            + " ADD COLUMN " + MovieContract.MovieEntry.COLUMN_MOVIE_IMAGE + " BLOB;";

    public MovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MOVIES_TABLE = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME
                + "("
                + MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MovieContract.MovieEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, "
                + MovieContract.MovieEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, "
                + MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE + " TEXT NOT NULL, "
                + MovieContract.MovieEntry.COLUMN_MOVIE_RATING + " TEXT NOT NULL, "
                + MovieContract.MovieEntry.COLUMN_MOVIE_IMAGE + " BLOB, "
                + MovieContract.MovieEntry.COLUMN_MOVIE_SYNOPSIS + " TEXT NOT NULL, "
                + " UNIQUE (" + MovieContract.MovieEntry.COLUMN_MOVIE_ID + ") "
                + " ON CONFLICT REPLACE"
                + ")";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if(oldVersion < 2) {
            sqLiteDatabase.execSQL(DATABASE_ALTER_SQL_1);
        }
    }
}
