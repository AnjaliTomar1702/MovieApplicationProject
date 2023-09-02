package com.niit.recommended.domain;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieInfo {
    private List<Movie> movieList;
   // private List<Genre> genreList;
    private List<Cast> castList;
    private List<Trailer> trailerList;

}
