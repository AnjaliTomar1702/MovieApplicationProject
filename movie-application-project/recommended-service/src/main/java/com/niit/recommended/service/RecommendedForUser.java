package com.niit.recommended.service;

import com.niit.recommended.domain.*;

import java.util.List;
import java.util.Map;

public interface RecommendedForUser {

    List<Movie> getPopularList();
    List<Genre> getGenreList();
    List<Movie> getUpcomingMovieList();
    Map<String, List<Movie>> getRecommendedCardsMap();
}
