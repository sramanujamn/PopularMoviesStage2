package com.udacity.ramanujam.popularmovies.data;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.ramanujam.popularmovies.MovieItem;
import com.udacity.ramanujam.popularmovies.R;

import java.util.ArrayList;

public class PopularMoviesAdapter extends RecyclerView.Adapter<PopularMoviesAdapter.PopularMoviesAdapterViewHolder> {

    private Context context;
    private int resourceLayoutId;
    private ArrayList<MovieItem> popularMoviesArrayList = new ArrayList<>();
    private static final String LOG_TAG = PopularMoviesAdapter.class.getSimpleName();

    public PopularMoviesAdapter(int resourceLayoutId, ArrayList<MovieItem> movieItemArrayList) {
        this.resourceLayoutId = resourceLayoutId;
        this.popularMoviesArrayList = movieItemArrayList;
    }

    public PopularMoviesAdapter(Context context, int resourceLayoutId, ArrayList<MovieItem> movieItemArrayList) {
        this.context = context;
        this.resourceLayoutId = resourceLayoutId;
        this.popularMoviesArrayList = movieItemArrayList;
    }

    @NonNull
    @Override
    public PopularMoviesAdapter.PopularMoviesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resourceLayoutId, parent, false);
        return new PopularMoviesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularMoviesAdapter.PopularMoviesAdapterViewHolder holder, int position) {

        ImageView movieImage = holder.movieImage;
        TextView movieTitle = holder.movieTitle;

        final MovieItem movieItem = popularMoviesArrayList.get(position);
        holder.movieTitle.setText(movieItem.getTitle());
        if((movieItem.getMovieBlob() != null) && (movieItem.getMovieBlob().length > 0)) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(movieItem.getMovieBlob(), 0, movieItem.getMovieBlob().length);
            holder.movieImage.setImageBitmap(bitmap);
        } else {
            Picasso.with(context).load(movieItem.getImageUrl()).into(holder.movieImage);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), com.udacity.ramanujam.popularmovies.MovieDetailActivity.class)
                        .putExtra("movie", movieItem);
                view.getContext().startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return popularMoviesArrayList == null ? 0 : popularMoviesArrayList.size();
    }

    public class PopularMoviesAdapterViewHolder extends RecyclerView.ViewHolder {

        final ImageView movieImage;
        final TextView movieTitle;

        public PopularMoviesAdapterViewHolder(View itemView) {
            super(itemView);
            movieImage = (ImageView)itemView.findViewById(R.id.movieGridItemImage);
            movieTitle = (TextView)itemView.findViewById(R.id.movieGridItemTitle);
        }
    }
}
