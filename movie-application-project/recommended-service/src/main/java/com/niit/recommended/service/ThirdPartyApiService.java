package com.niit.recommended.service;

import com.niit.recommended.domain.Cast;
import com.niit.recommended.domain.Review;
import com.niit.recommended.domain.Trailer;
import com.niit.recommended.domain.Movie;
import com.niit.recommended.exception.ContentNotFoundException;
import com.niit.recommended.exception.GenreNotFoundException;

import java.util.List;

public interface ThirdPartyApiService {
    List<Movie> searchMovieListByMovieName(String movieName) throws ContentNotFoundException;
    Movie getMovieById(Integer id) throws ContentNotFoundException;
    List<Movie> searchMovieListByMovieNameOne(String movieName) throws ContentNotFoundException;
    List<Movie> searchMoviesListByYear(String year) throws ContentNotFoundException;
    String getMovieIdByMovieName(String name ,List<Movie> movies) throws ContentNotFoundException;
    List<Cast> getMovieCredits(String movieId) throws ContentNotFoundException;
    List<Movie> movieListforDifferentGenre(String id);
    List<Trailer> getMovieTrailer(String movieId);
    List<Movie> getMoviesByGenreIds(String genreIdQuery) throws ContentNotFoundException;
    Object getMovieInfoData(String movieName);
    List<Movie> searchOneMovieDetail(String movieName);
    List<Cast> getCastForMovies(String name);
//    List<Movie> getMovieListActionGenre();
//    List<Movie> getMovieListComedyGenre();
//    List<Movie> getMovieListCrimeGenre();
//    List<Movie> getMovieListFamilyGenre();
    List<Movie> getMoviesByYear(String year) throws ContentNotFoundException;
    List <Movie> getMoviesByGenreName(String genreName) throws GenreNotFoundException, ContentNotFoundException;
    List<Review> getMovieReviewsByMovieId(Integer movieId);
    List<Cast> getMovieCastByMovieId(String movieId) throws ContentNotFoundException;
    List<Trailer> getMovieTrailerByMovieId(String movieId) throws ContentNotFoundException;
}
