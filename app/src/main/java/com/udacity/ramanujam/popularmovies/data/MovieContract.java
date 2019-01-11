package com.udacity.ramanujam.popularmovies.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.udacity.ramanujam.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIES = "movies";

    public final static class MovieEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIES)
                .build();

        public static final String TABLE_NAME = "movies";

        public static final String COLUMN_MOVIE_ID = "movieid";
        public static final String COLUMN_MOVIE_TITLE = "title";
        public static final String COLUMN_MOVIE_RELEASE_DATE = "releasedate";
        public static final String COLUMN_MOVIE_RATING = "rating";
        public static final String COLUMN_MOVIE_IMAGE = "image";
        public static final String COLUMN_MOVIE_SYNOPSIS = "synopsis";

        public static Uri buildMoviesUri(String movie) {
            return CONTENT_URI.buildUpon()
                    .appendPath(movie)
                    .build();
        }

        public static Uri buildMoviesUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }
}
