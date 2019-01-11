package com.udacity.ramanujam.popularmovies.data;

import android.os.Parcel;
import android.os.Parcelable;

public class MovieReview implements Parcelable {

    private String reviewAuthor;
    private String reviewComment;

    public MovieReview(Parcel in) {
        reviewAuthor = in.readString();
        reviewComment = in.readString();
    }

    public static final Creator<MovieReview> CREATOR = new Creator<MovieReview>() {
        @Override
        public MovieReview createFromParcel(Parcel in) {
            return new MovieReview(in);
        }

        @Override
        public MovieReview[] newArray(int size) {
            return new MovieReview[size];
        }
    };

    public MovieReview() {

    }

    public String getReviewAuthor() {
        return reviewAuthor;
    }

    public void setReviewAuthor(String reviewAuthor) {
        this.reviewAuthor = reviewAuthor;
    }

    public String getReviewComment() {
        return reviewComment;
    }

    public void setReviewComment(String reviewComment) {
        this.reviewComment = reviewComment;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(reviewAuthor);
        parcel.writeString(reviewComment);
    }
}
