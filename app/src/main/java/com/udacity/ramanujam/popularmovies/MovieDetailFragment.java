package com.udacity.ramanujam.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.udacity.ramanujam.popularmovies.data.MovieContract;
import com.udacity.ramanujam.popularmovies.data.MovieReview;
import com.udacity.ramanujam.popularmovies.data.MovieReviewsAdapter;
import com.udacity.ramanujam.popularmovies.data.MovieTrailer;
import com.udacity.ramanujam.popularmovies.data.MovieTrailersAdapter;
import com.udacity.ramanujam.popularmovies.databinding.FragmentMovieDetailBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class MovieDetailFragment extends Fragment {

    private static final String LOG_TAG = MovieDetailFragment.class.getSimpleName();

    private ArrayList<MovieTrailer> movieTrailerArrayList;
    private ArrayList<MovieReview> movieReviewArrayList;
    private RecyclerView trailerRecyclerView;
    private RecyclerView reviewRecyclerView;

    public MovieDetailFragment() {
        // Required empty public constructor
    }

    public static MovieDetailFragment newInstance() {
        MovieDetailFragment fragment = new MovieDetailFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null) {
            movieTrailerArrayList = savedInstanceState.getParcelableArrayList("MOVIE_TRAILERS");
        }

        if(movieTrailerArrayList != null) {
            //movieTrailersAdapter = new MovieTrailersAdapter(R.layout.movie_trailer, movieTrailerArrayList);
            //trailerRecyclerView.setAdapter(movieTrailersAdapter);
            //movieTrailersAdapter.notifyDataSetChanged();
        }
    }

    public class FetchMovieTrailers extends AsyncTask<String, Void, String> {

        final String LOG_TAG = MovieDetailFragment.FetchMovieTrailers.class.getSimpleName();
        StringBuilder stringBuilder;

        @Override
        protected String doInBackground(String... strings) {
            Integer result = 0;
            if(strings.length < 1) {
                return null;
            }

            final String BASE_URL = "http://api.themoviedb.org/3/movie/";
            final String URL_TRAILERS = "/videos";
            final String API_KEY_PARAM = "api_key";
            //final String API_KEY = "";
            final String API_KEY = "4d4b117e167918afecdc77c072c91eac";
            HttpURLConnection httpURLConnection = null;

            BufferedReader reader = null;

            String movieId = strings[0];

            Log.i(LOG_TAG, "Movie ID: " + movieId);

            try {
                Uri uri = Uri.parse(BASE_URL + movieId + URL_TRAILERS)
                        .buildUpon()
                        .appendQueryParameter(API_KEY_PARAM, API_KEY)
                        .build();
                URL url = new URL(uri.toString());
                Log.i(LOG_TAG, "URL: " + uri.toString());
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
                //populateMovieTrailerDetailsFromJson(stringBuilder.toString());

                reader.close();

                //popularMoviesDataGrid = populateMovieDataFromJson(stringBuilder.toString());
                movieTrailerArrayList = populateMovieTrailerDetailsFromJson(stringBuilder.toString());
                Log.i(LOG_TAG, "Trailers Array Size: " + movieTrailerArrayList.size());
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
                return null;
            }
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String result) {

                if((movieTrailerArrayList != null) && (movieTrailerArrayList.size() > 0)) {

                    MovieTrailersAdapter movieTrailersAdapter = new MovieTrailersAdapter(R.layout.movie_trailer, movieTrailerArrayList);
                    trailerRecyclerView.setAdapter(movieTrailersAdapter);
                    movieTrailersAdapter.notifyDataSetChanged();
                }
        }
    }

    public class FetchMovieReviews extends AsyncTask<String, Void, String> {

        final String LOG_TAG = MovieDetailFragment.FetchMovieReviews.class.getSimpleName();
        StringBuilder stringBuilder;

        @Override
        protected String doInBackground(String... strings) {
            Integer result = 0;
            if(strings.length < 1) {
                return null;
            }

            final String BASE_URL = "http://api.themoviedb.org/3/movie/";
            final String URL_REVIEWS = "/reviews";
            final String API_KEY_PARAM = "api_key";
            //final String API_KEY = "";
            final String API_KEY = "4d4b117e167918afecdc77c072c91eac";
            HttpURLConnection httpURLConnection = null;

            BufferedReader reader = null;

            String movieId = strings[0];

            Log.i(LOG_TAG, "Movie ID: " + movieId);

            try {
                Uri uri = Uri.parse(BASE_URL + movieId + URL_REVIEWS)
                        .buildUpon()
                        .appendQueryParameter(API_KEY_PARAM, API_KEY)
                        .build();
                URL url = new URL(uri.toString());
                Log.i(LOG_TAG, "URL: " + uri.toString());
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

                reader.close();

                movieReviewArrayList = populateMovieReviewDetailsFromJson(stringBuilder.toString());
                Log.i(LOG_TAG, "Reviews Array Size: " + movieReviewArrayList.size());
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
                return null;
            }
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String result) {

            if((movieReviewArrayList != null) && (movieReviewArrayList.size() > 0)) {

                MovieReviewsAdapter movieReviewsAdapter = new MovieReviewsAdapter(R.layout.movie_review, movieReviewArrayList);
                reviewRecyclerView.setAdapter(movieReviewsAdapter);
                movieReviewsAdapter.notifyDataSetChanged();
            }
        }
    }

    private ArrayList<MovieTrailer> populateMovieTrailerDetailsFromJson(String jsonData) throws JSONException {

        final String TRAILER_LIST = "results";
        final String TRAILER_ID = "id";
        final String TRAILER_KEY = "key";
        final String TRAILER_NAME = "name";

        JSONObject trailerJSONObject = new JSONObject(jsonData);
        JSONArray trailersArray = trailerJSONObject.getJSONArray(TRAILER_LIST);

        ArrayList<MovieTrailer> movieTrailerArrayList = new ArrayList<>();

        for(int i = 0; i < trailersArray.length(); i++) {
            JSONObject trailer = trailersArray.getJSONObject(i);
            MovieTrailer movieTrailer = new MovieTrailer();
            movieTrailer.setId(trailer.getString(TRAILER_ID));
            movieTrailer.setKey(trailer.getString(TRAILER_KEY));
            movieTrailer.setName(trailer.getString(TRAILER_NAME));
            movieTrailerArrayList.add(movieTrailer);
        }

        return movieTrailerArrayList;

    }

    private ArrayList<MovieReview> populateMovieReviewDetailsFromJson(String jsonData) throws JSONException {

        final String REVIEW_LIST = "results";
        final String REVIEW_AUTHOR = "author";
        final String REVIEW_CONTENT = "content";
        //final String REVIEW_URL = "url";

        JSONObject reviewJSONObject = new JSONObject(jsonData);
        JSONArray reviewsArray = reviewJSONObject.getJSONArray(REVIEW_LIST);

        ArrayList<MovieReview> movieReviewArrayList = new ArrayList<>();

        for(int i = 0; i < reviewsArray.length(); i++) {
            JSONObject review = reviewsArray.getJSONObject(i);
            MovieReview movieReview = new MovieReview();
            movieReview.setReviewAuthor(review.getString(REVIEW_AUTHOR));
            movieReview.setReviewComment(review.getString(REVIEW_CONTENT));
            //movieReview.setReviewUrl(review.getString(REVIEW_URL));
            movieReviewArrayList.add(movieReview);
        }

        return movieReviewArrayList;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        Intent intent = getActivity().getIntent();
        //final MovieItem movieItemParcel = null;

        LinearLayoutManager trailersLayoutManager = new LinearLayoutManager(rootView.getContext(), LinearLayoutManager.VERTICAL, false);
        LinearLayoutManager reviewsLayoutManager = new LinearLayoutManager(rootView.getContext(), LinearLayoutManager.VERTICAL, false);

        trailerRecyclerView = (RecyclerView)rootView.findViewById(R.id.movie_trailer_list);
        reviewRecyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerview_movie_review_list);

        trailerRecyclerView.setLayoutManager(trailersLayoutManager);
        reviewRecyclerView.setLayoutManager(reviewsLayoutManager);

            if(intent != null) {
                final MovieItem movieItemParcel = (MovieItem) intent.getParcelableExtra("movie");
                Cursor cursor = getActivity().getContentResolver().query(MovieContract.MovieEntry.buildMoviesUri(movieItemParcel.getMovieId()), null, null, null, null);

                FragmentMovieDetailBinding binding = DataBindingUtil.bind(rootView);
                binding.movieTitle.setText(movieItemParcel.getTitle());
                binding.movieRating.setText(new Double(movieItemParcel.getUserRating()).toString() + "/10");
                binding.movieReleaseDate.setText(movieItemParcel.getReleaseDate());
                binding.movieSynopsis.setText(movieItemParcel.getSynopsis());
                final ImageView movieImage = binding.movieImage;
                byte[] movieBlob = movieItemParcel.getMovieBlob();
                if((movieBlob != null) && (movieBlob.length > 0)) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(movieBlob, 0, movieBlob.length);
                    binding.movieImage.setImageBitmap(bitmap);
                } else {
                    Picasso.with(getContext()).load(movieItemParcel.getImageUrl()).into(movieImage);
                }
                new FetchMovieTrailers().execute(movieItemParcel.getMovieId());
                new FetchMovieReviews().execute(movieItemParcel.getMovieId());

                if(cursor.getCount() == 0) {
                    final Button favoriteButton = (Button)rootView.findViewById(R.id.saveFavoriteButton);
                    favoriteButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Uri uri;

                            ContentValues movieValues = new ContentValues();

                            movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movieItemParcel.getMovieId());
                            movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, movieItemParcel.getTitle());
                            movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE, movieItemParcel.getReleaseDate());
                            movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_RATING, movieItemParcel.getUserRating());
                            movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_SYNOPSIS, movieItemParcel.getSynopsis());

                            Bitmap bitmap = ((BitmapDrawable)movieImage.getDrawable()).getBitmap();
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                            byte[] image = byteArrayOutputStream.toByteArray();
                            movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_IMAGE, image);

                            uri = getActivity().getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, movieValues);
                            if(Integer.parseInt(uri.getLastPathSegment()) > 0) {
                                favoriteButton.setVisibility(View.INVISIBLE);
                                ImageButton favoriteIconButton = (ImageButton)rootView.findViewById(R.id.favouriteIconButton);
                                favoriteIconButton.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                } else {
                    final Button button = (Button)rootView.findViewById(R.id.saveFavoriteButton);
                    button.setVisibility(View.INVISIBLE);
                    final ImageButton favouriteIconButton = (ImageButton) rootView.findViewById(R.id.favouriteIconButton);
                    favouriteIconButton.setVisibility(View.VISIBLE);
                    favouriteIconButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Uri uri = MovieContract.MovieEntry.buildMoviesUri(movieItemParcel.getMovieId());
                            int result;

                            result = getActivity().getContentResolver().delete(uri, null, new String[] {movieItemParcel.getMovieId()});

                            Log.i(LOG_TAG, "Result after deletion: " + uri + ", " + result);

                            if(result != 0) {
                                favouriteIconButton.setVisibility(View.INVISIBLE);
                                button.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                }
            }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

        outState.putParcelableArrayList("MOVIE_TRAILERS", movieTrailerArrayList);

        super.onSaveInstanceState(outState);

    }
}
