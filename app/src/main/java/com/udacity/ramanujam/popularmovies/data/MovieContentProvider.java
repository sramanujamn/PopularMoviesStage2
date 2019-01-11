package com.udacity.ramanujam.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public class MovieContentProvider extends ContentProvider {

    public static final String TAG = MovieContentProvider.class.getSimpleName();

    public static final int CODE_ALL_MOVIES = 700;
    public static final int CODE_MOVIE_WITH_SEARCHSTRING = 710;

    public static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDBHelper movieDBHelper;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MovieContract.PATH_MOVIES, CODE_ALL_MOVIES);
        matcher.addURI(authority, MovieContract.PATH_MOVIES + "/*", CODE_MOVIE_WITH_SEARCHSTRING);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        movieDBHelper = new MovieDBHelper(getContext());
        Log.v(TAG, "Inside OnCreate");
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectorArgs, @Nullable String sortOrder) {
        Cursor cursor;
        int match = sUriMatcher.match(uri);
        String resultsLimit = "0, 20";
        String[] selectionArguments;
        String selectionCriteria;

        switch (match) {
            case CODE_MOVIE_WITH_SEARCHSTRING:
                String movieSearchString = uri.getLastPathSegment();
                selectionArguments = new String[] {movieSearchString};
                selectionCriteria = MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ? ";
                Log.v(TAG, selectionCriteria);

                cursor = movieDBHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        selectionCriteria,
                        selectionArguments,
                        null,
                        null,
                        sortOrder
                );
                break;

            case CODE_ALL_MOVIES:
                cursor = movieDBHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectorArgs,
                        null,
                        null,
                        sortOrder, resultsLimit
                );
                break;

                default:
                    throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        Uri returnUri;

        switch(sUriMatcher.match(uri)) {
            case CODE_ALL_MOVIES:
                long _id = movieDBHelper.getWritableDatabase().insert(
                        MovieContract.MovieEntry.TABLE_NAME,
                        null,
                        contentValues
                );
                if(_id > 0) {
                    returnUri = MovieContract.MovieEntry.buildMoviesUri(_id);
                } else {
                    throw new SQLException("Failed to insert row: " + uri);
                }
                break;

                default:
                   throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int numRowsDeleted;

        /*if(selection == null) {
            selection = "1";
        }*/

        switch (sUriMatcher.match(uri)) {
            case CODE_MOVIE_WITH_SEARCHSTRING:
                String movieSearchString = uri.getLastPathSegment();
                String[] selectionArguments = new String[] {movieSearchString};

                numRowsDeleted = movieDBHelper.getWritableDatabase().delete(
                        MovieContract.MovieEntry.TABLE_NAME,
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",
                        selectionArgs
                );
                break;

                default:
                    throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if(numRowsDeleted != 0) {

            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
