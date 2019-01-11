package com.udacity.ramanujam.popularmovies.data;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.udacity.ramanujam.popularmovies.R;

import java.util.ArrayList;

public class MovieTrailersAdapter extends RecyclerView.Adapter<MovieTrailersAdapter.MovieTrailersAdapterViewHolder> {

    private int listItemLayout;
    private ArrayList<MovieTrailer> movieTrailerArrayList;

    public MovieTrailersAdapter(int layoutId, ArrayList<MovieTrailer> arrayList) {
        listItemLayout = layoutId;
        this.movieTrailerArrayList = arrayList;
    }

    @NonNull
    @Override
    public MovieTrailersAdapter.MovieTrailersAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(listItemLayout, parent, false);
        return new MovieTrailersAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MovieTrailersAdapter.MovieTrailersAdapterViewHolder holder, final int position) {

        TextView trailerNameTV = holder.trailerNameTextView;
        trailerNameTV.setText(movieTrailerArrayList.get(position).getName());
        ImageButton trailerPlayButton = holder.trailerPlayButton;
        trailerPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MovieTrailer trailer = movieTrailerArrayList.get(position);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + trailer.getKey()));
                view.getContext().startActivity(intent);
            }
        });

        if(position == 0) {
            holder.dividerView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return movieTrailerArrayList == null ? 0 : movieTrailerArrayList.size();
    }

    public class MovieTrailersAdapterViewHolder extends RecyclerView.ViewHolder {

        public final TextView trailerNameTextView;
        public final ImageButton trailerPlayButton;
        private final View dividerView;
        private String key;
        private String id;

        public MovieTrailersAdapterViewHolder(View itemView) {
            super(itemView);
            trailerNameTextView = (TextView)itemView.findViewById(R.id.trailerName);
            trailerPlayButton = (ImageButton)itemView.findViewById(R.id.trailerPlay);
            dividerView = (View)itemView.findViewById(R.id.trailerDivider);
        }
    }
}
