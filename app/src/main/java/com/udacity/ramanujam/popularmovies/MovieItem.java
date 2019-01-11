package com.udacity.ramanujam.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;


public class MovieItem implements Parcelable {

    private String imageUrl;
    private String title;
    private String synopsis;
    private String releaseDate;
    private double userRating;
    private String movieId;
    private byte[] movieBlob;

    public byte[] getMovieBlob() {
        return movieBlob;
    }

    public void setMovieBlob(byte[] movieBlob) {
        this.movieBlob = movieBlob;
    }

    protected MovieItem(Parcel in) {
        imageUrl = in.readString();
        title = in.readString();
        synopsis = in.readString();
        releaseDate = in.readString();
        userRating = in.readDouble();
        movieId = in.readString();
        movieBlob = in.createByteArray();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageUrl);
        dest.writeString(title);
        dest.writeString(synopsis);
        dest.writeString(releaseDate);
        dest.writeDouble(userRating);
        dest.writeString(movieId);
        dest.writeByteArray(movieBlob);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MovieItem> CREATOR = new Creator<MovieItem>() {
        @Override
        public MovieItem createFromParcel(Parcel in) {
            return new MovieItem(in);
        }

        @Override
        public MovieItem[] newArray(int size) {
            return new MovieItem[size];
        }
    };

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public double getUserRating() {
        return userRating;
    }

    public void setUserRating(double userRating) {
        this.userRating = userRating;
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public MovieItem(String imageUrl, String title) {
        super();
        this.imageUrl = imageUrl;
        this.title = title;
    }

    public MovieItem(String title) {
        super();
        this.title = title;
    }


}
