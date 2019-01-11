package com.udacity.ramanujam.popularmovies;

import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.udacity.ramanujam.popularmovies.data.MovieContract;
import com.udacity.ramanujam.popularmovies.data.PopularMoviesAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class PopularMoviesFragment extends Fragment {
    private static final String LOG_TAG = PopularMoviesFragment.class.getSimpleName();
    private ArrayList<MovieItem> popularMoviesDataGrid;
    private GridView popularMoviesGridView;
    private String sortPreference;
    private SharedPreferences sharedPreferences;

    private PopularMoviesAdapter popularMoviesAdapter;
    private RecyclerView popularMoviesRecyclerView;

    public PopularMoviesFragment() {
    }


    public static PopularMoviesFragment newInstance() {
        PopularMoviesFragment fragment = new PopularMoviesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sortPreference = sharedPreferences.getString(getString(R.string.pref_sortby_key),
                getString(R.string.pref_sortby_value));

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.popular_movies_list , container, false);

        popularMoviesRecyclerView = rootView.findViewById(R.id.recyclerview_popular_movies_list);
        popularMoviesRecyclerView.setHasFixedSize(true);
        popularMoviesDataGrid = new ArrayList<>();
        popularMoviesAdapter = new PopularMoviesAdapter(getActivity(), R.layout.popular_movie, popularMoviesDataGrid);

        popularMoviesRecyclerView.setAdapter(popularMoviesAdapter);

        updateMovies();
        return rootView;
    }

    public class FetchPopularMovies extends AsyncTask<String, Void, Integer> {

        final String LOG_TAG = FetchPopularMovies.class.getSimpleName();
        StringBuilder stringBuilder;

        @Override
        protected Integer doInBackground(String... strings) {
            Integer result = 0;
            if(strings.length < 1) {
                return null;
            }

            final String BASE_URL_POPULAR = "http://api.themoviedb.org/3/movie/popular";
            final String BASE_URL_TOP_RATING = "http://api.themoviedb.org/3/movie/top_rated";
            final String API_KEY_PARAM = "api_key";
            final String API_KEY = "";
            HttpURLConnection httpURLConnection = null;

            BufferedReader reader = null;

            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String preferredUrl = sharedPreferences.getString(getString(R.string.pref_sortby_key),
                    getString(R.string.pref_sortby_value));

            if(preferredUrl.equals(getString(R.string.pref_sort_myfavorites))) {
                ContentResolver contentResolver;
                contentResolver = getActivity().getContentResolver();

                if(contentResolver != null) {
                    Cursor cursor = contentResolver.query(MovieContract.MovieEntry.CONTENT_URI, null, null, null, null);
                    if(cursor != null) {
                        popularMoviesDataGrid = populateMovieDataFromDatabase(cursor);
                        result = 1;
                    }
                }

            } else {
                try {
                    Uri uri = Uri.parse((preferredUrl.equals(getString(R.string.pref_sortby_value)) ? BASE_URL_POPULAR : BASE_URL_TOP_RATING))
                            .buildUpon()
                            .appendQueryParameter(API_KEY_PARAM, API_KEY)
                            .build();
                    URL url = new URL(uri.toString());
                    httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.connect();

                    InputStream inputStream = httpURLConnection.getInputStream();
                    stringBuilder = new StringBuilder();

                    if(inputStream == null) {
                        return null;
                    }

                    reader = new BufferedReader(new InputStreamReader(inputStream));

                    String line;
                    while((line = reader.readLine()) != null) {
                        stringBuilder.append(line).append('\n');
                    }

                    if(stringBuilder.length() == 0) {
                        return null;
                    }
                    populateMovieDataFromJson(stringBuilder.toString());

                    reader.close();

                    popularMoviesDataGrid = populateMovieDataFromJson(stringBuilder.toString());
                    result = 1;
                } catch(Exception e) {
                    Log.e(LOG_TAG, "Error: ", e);
                } finally {
                    if(httpURLConnection != null) {
                        httpURLConnection.disconnect();
                    }
                    if(reader != null) {
                        try {
                            reader.close();
                        } catch(Exception e) {
                            Log.e(LOG_TAG, "Error closing stream", e);
                        }

                    }
                }
            }

            return result;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(Integer result) {
            if(popularMoviesDataGrid.size() > 0) {
                popularMoviesAdapter = new PopularMoviesAdapter(getActivity(), R.layout.popular_movie, popularMoviesDataGrid);
                popularMoviesRecyclerView.setAdapter(popularMoviesAdapter);
                popularMoviesAdapter.notifyDataSetChanged();
            }
        }
    }

    private ArrayList<MovieItem> populateMovieDataFromDatabase(Cursor cursor) {
        ArrayList<MovieItem> moviesDataGrid = new ArrayList<>();

        if(cursor != null) {
            if(cursor.moveToFirst()) {
             do {
                 MovieItem movieItem = new MovieItem(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE)));
                 movieItem.setMovieId(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID)));
                 movieItem.setReleaseDate(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE)));
                 movieItem.setSynopsis(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_SYNOPSIS)));
                 movieItem.setUserRating(cursor.getDouble(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_RATING)));
                 movieItem.setMovieBlob(cursor.getBlob(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_IMAGE)));
                 moviesDataGrid.add(movieItem);
             } while (cursor.moveToNext());
            }
        }
        return moviesDataGrid;
    }

    private ArrayList<MovieItem> populateMovieDataFromJson(String movieJsonString) throws JSONException {
        final String MOVIE_LIST = "results";
        final String MOVIE_ID = "id";
        final String MOVIE_POSTER_PATH = "poster_path";
        final String MOVIE_TITLE = "original_title";
        final String MOVIE_POSTER_BASE_URL = "http://image.tmdb.org/t/p/";
        final String MOVIE_SYNOPSIS = "overview";
        final String MOVIE_RELEASE_DATE = "release_date";
        final String MOVIE_USER_RATING = "vote_average";
        final String MOVIE_POSTER_SIZE = "w185";

        JSONObject movieJSONObject = new JSONObject(movieJsonString);
        JSONArray moviesArray = movieJSONObject.getJSONArray(MOVIE_LIST);

        ArrayList<MovieItem> moviesDataGrid = new ArrayList<>();

        for(int i = 0; i < moviesArray.length(); i++) {
            JSONObject movie = moviesArray.getJSONObject(i);
            MovieItem movieItem = new MovieItem(
                    MOVIE_POSTER_BASE_URL + MOVIE_POSTER_SIZE + movie.getString(MOVIE_POSTER_PATH),
                    movie.getString(MOVIE_TITLE)
            );
            movieItem.setMovieId(movie.getString(MOVIE_ID));
            Log.i(LOG_TAG, movie.getString(MOVIE_TITLE) + ": " + movie.getString(MOVIE_ID));
            movieItem.setReleaseDate(movie.getString(MOVIE_RELEASE_DATE));
            movieItem.setSynopsis(movie.getString(MOVIE_SYNOPSIS));
            movieItem.setUserRating(movie.getDouble(MOVIE_USER_RATING));
            moviesDataGrid.add(movieItem);

        }

        return moviesDataGrid;
    }

    private void updateMovies() {
        new FetchPopularMovies().execute("test");
    }

    @Override
    public void onStart() {
        super.onStart();
        String newSortPreference = sharedPreferences.getString(getString(R.string.pref_sortby_key), getString(R.string.pref_sortby_value));
        if(!sortPreference.equals(newSortPreference)) {
            sortPreference = newSortPreference;
            updateMovies();
        }
    }

}
