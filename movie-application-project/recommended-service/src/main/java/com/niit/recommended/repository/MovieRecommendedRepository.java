package com.niit.recommended.repository;

import com.niit.recommended.domain.Genre;

import com.niit.recommended.domain.Movie;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.function.Predicate;


@Repository
public class MovieRecommendedRepository {

    private List<Movie> popularMovieList = new ArrayList<>();
    private List<Movie> movieMovieList = new ArrayList<>();
    private List<Genre> genreList = new ArrayList<>();
    private Map<String, List<Movie>> recommendedCardsMap = new HashMap<>();

    public List<Movie> getPopularMovieList(Predicate<Movie> predicate) {
        return this.popularMovieList.stream().filter(predicate).toList();
    }

    public List<Movie> getUpcomingMovieList(Predicate<Movie> predicate) {
        return this.movieMovieList.stream().filter(predicate).toList();
    }

    public List<Genre> getGenreList()
    {
        return Collections.unmodifiableList(this.genreList);
    }

    public Map<String, List<Movie>> getRecommendedCardsMap() {
        return Collections.unmodifiableMap(this.recommendedCardsMap);
    }

    public boolean clearPopularMovieList(){
        this.popularMovieList.clear();
        return true;
    }

    public boolean clearUpcomingMovieList(){
        this.movieMovieList.clear();
        return true;
    }

    public boolean clearGenreList(){
        this.genreList.clear();
        return true;
    }

    public boolean clearRecommendedCardsMap(){
        this.recommendedCardsMap.clear();
        return true;
    }

    public boolean populatePopularMovieList(List<Movie> movies) {
        this.popularMovieList.addAll(movies);
        return true;
    }

    public boolean populateUpcomingMovieList(List<Movie> movies) {
        this.movieMovieList.addAll(movies);
        return true;
    }

    public  boolean populateGenreList(List<Genre> genres) {
        this.genreList.addAll(genres);
        return true;
    }

    public  boolean populateRecommendedCardsMap(Map<String, List<Movie>> recommendedCardsMap) {
        this.recommendedCardsMap.putAll(recommendedCardsMap);
        return true;
    }

    public List<Genre> getGenresById(List<Integer> genreIds){
        return this.genreList.stream()
                .filter(i -> genreIds.stream().anyMatch(j -> j.equals(i.getId())))
                .toList();
    }

    public List<Genre> getGenresByNames(List<String> genreNames){
        return this.genreList.stream()
                .filter(i -> genreNames.stream().anyMatch(j -> j.equals(i.getName())))
                .toList();
    }

}
