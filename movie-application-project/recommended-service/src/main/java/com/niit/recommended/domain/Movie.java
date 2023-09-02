package com.niit.recommended.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Data
public class Movie {
    private String movieId;
    private String originalTitle;
    private String overview;
    private String posterPath;
    private String voteAverage;
    private String releaseDate;
    private String backdropPath;
    private List<Genre> genres = new ArrayList<>();

    public Movie(){}
    public Movie(String movieId, String originalTitle, String overview, String posterPath, String voteAverage, String releaseDate, String backdropPath) {
        this.movieId = movieId;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.posterPath = posterPath;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
        this.backdropPath = backdropPath;
    }

    public Movie(String movieId, String originalTitle, String overview, String posterPath, String voteAverage, String releaseDate, String backdropPath, List<Genre> genres) {
        this.movieId = movieId;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.posterPath = posterPath;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
        this.backdropPath = backdropPath;
        this.genres = genres;
    }
}
