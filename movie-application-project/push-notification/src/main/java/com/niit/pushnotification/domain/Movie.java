package com.niit.pushnotification.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class Movie {
    private int movieId;
    private String originalTitle;
    private String overview;

    private String posterPath;
    private String voteAverage;

    private String releaseDate;
}
