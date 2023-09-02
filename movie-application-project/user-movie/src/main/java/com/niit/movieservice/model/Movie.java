package com.niit.movieservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
public class Movie {

    @Id
    private int movieId;
    private String originalTitle;
    private String overview;

    private String posterPath;
    private String voteAverage;

    private String releaseDate;
}
