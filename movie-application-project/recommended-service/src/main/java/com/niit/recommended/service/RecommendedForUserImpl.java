package com.niit.recommended.service;

import com.niit.recommended.domain.*;
import com.niit.recommended.repository.MovieRecommendedRepository;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RecommendedForUserImpl implements RecommendedForUser{

    private final MovieRecommendedRepository movieRecommendedRepository;

    @Override
    public List<Movie> getPopularList() {
        return this.movieRecommendedRepository.getPopularMovieList(i -> true);
    }

    @Override
    public List<Genre> getGenreList() {
        return this.movieRecommendedRepository.getGenreList();
    }

    @Override
    public List<Movie> getUpcomingMovieList() {
        return this.movieRecommendedRepository.getUpcomingMovieList(i -> true);
    }

    @Override
    public Map<String, List<Movie>> getRecommendedCardsMap() {
        return this.movieRecommendedRepository.getRecommendedCardsMap();
    }

}
