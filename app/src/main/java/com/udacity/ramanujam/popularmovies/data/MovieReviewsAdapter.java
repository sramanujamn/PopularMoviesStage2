package com.udacity.ramanujam.popularmovies.data;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.udacity.ramanujam.popularmovies.R;

import java.util.ArrayList;

public class MovieReviewsAdapter extends RecyclerView.Adapter<MovieReviewsAdapter.MovieReviewsAdapterViewHolder> {

    private int listItemLayout;
    private ArrayList<MovieReview> movieReviewArrayList;
    private int columnWidth = -1;
    GridLayoutManager gridLayoutManager;

    public MovieReviewsAdapter(int layoutId, ArrayList<MovieReview> arrayList) {
        listItemLayout = layoutId;
        this.movieReviewArrayList = arrayList;
    }

    @NonNull
    @Override
    public MovieReviewsAdapter.MovieReviewsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(listItemLayout, parent, false);
        return new MovieReviewsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MovieReviewsAdapter.MovieReviewsAdapterViewHolder holder, final int position) {

        TextView reviewAuthorTV = holder.reviewAuthorTextView;
        TextView reviewCommentTV = holder.reviewCommentTextView;
        reviewAuthorTV.setText(movieReviewArrayList.get(position).getReviewAuthor());
        reviewCommentTV.setText(movieReviewArrayList.get(position).getReviewComment());

        if(position == 0) {
            holder.dividerView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return movieReviewArrayList == null ? 0 : movieReviewArrayList.size();
    }

    public class MovieReviewsAdapterViewHolder extends RecyclerView.ViewHolder {

        final TextView reviewAuthorTextView;
        final TextView reviewCommentTextView;
        final View dividerView;

        public MovieReviewsAdapterViewHolder(View itemView) {
            super(itemView);
            reviewAuthorTextView = (TextView)itemView.findViewById(R.id.reviewAuthor);
            reviewCommentTextView = (TextView)itemView.findViewById(R.id.reviewContents);
            dividerView = (View)itemView.findViewById(R.id.reviewDivider);
        }
    }

}
