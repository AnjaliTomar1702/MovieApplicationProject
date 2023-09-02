package com.niit.recommended.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.niit.recommended.domain.*;
import com.niit.recommended.exception.ContentNotFoundException;
import com.niit.recommended.exception.GenreNotFoundException;
import com.niit.recommended.repository.MovieRecommendedRepository;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.AbstractMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@EnableScheduling
@EnableRetry
public class ThirdPartyApiServiceImpl implements ThirdPartyApiService{

    @Autowired
    private MovieRecommendedRepository movieRecommendedRepository;
    private static final String BASE_URL = "https://api.themoviedb.org";
    private static final String API_KEY = "dd4d819639705d332d531217b4f7c6b6";
    private final List<String> recommendedCardGenres = List.of("Action", "Animation", "Comedy", "Documentary", "Drama", "Family", "Fantasy", "Horror", "Romance", "Science Fiction", "Western");

    @Override
    public List<Movie> searchMovieListByMovieName(String movieName) throws ContentNotFoundException{
        String url = BASE_URL + "/3/search/movie?api_key=" + API_KEY + "&query="+movieName;
        RestTemplate restTemplate = new RestTemplate();
        String resultString = restTemplate.getForObject(url, String.class);
        if(resultString == null)
            throw  new ContentNotFoundException();
        JsonArray result = JsonParser.parseString(resultString).getAsJsonObject().getAsJsonArray("results");
        if(result.isEmpty())
            throw  new ContentNotFoundException();
        return result.asList()
                .stream()
                .map(this::mapJsonElementToMovie)
                .toList();
    }

    @Override
    public Movie getMovieById(Integer id) throws ContentNotFoundException{
        String url = BASE_URL + "/3/movie/" + id + "?api_key=" + API_KEY + "&language=en-US";
        RestTemplate restTemplate = new RestTemplate();
        String resultString = restTemplate.getForObject(url, String.class);
        if(resultString == null)
            throw  new ContentNotFoundException();
        JsonElement result = JsonParser.parseString(resultString);
        return this.mapJsonElementToMovie(result);
    }

    @Override
    public List<Movie> searchMovieListByMovieNameOne(String movieName) throws ContentNotFoundException{
        String url = BASE_URL + "/3/search/movie?api_key=" + API_KEY + "&query="+movieName;
        RestTemplate restTemplate = new RestTemplate();
        String resultString = restTemplate.getForObject(url, String.class);
        if(resultString == null)
            throw  new ContentNotFoundException();
        JsonArray result = JsonParser.parseString(resultString).getAsJsonObject().getAsJsonArray("results");
        if(result.isEmpty())
            throw  new ContentNotFoundException();
        return result.asList()
                .stream()
                .map(this::mapJsonElementToMovie)
                .limit(1)
                .toList();

    }

    @Override
    public List<Movie> searchMoviesListByYear(String year) throws ContentNotFoundException{
        String url = BASE_URL + "/3/discover/movie?api_key=" + API_KEY + "&primary_release_date.gte="+year+"-01-01&primary_release_date.lte="+year+"-12-31";
        RestTemplate restTemplate = new RestTemplate();
        String resultString = restTemplate.getForObject(url, String.class);
        if(resultString == null)
            throw  new ContentNotFoundException();
        JsonArray result = JsonParser.parseString(resultString).getAsJsonObject().getAsJsonArray("results");
        if(result.isEmpty())
            throw  new ContentNotFoundException();
        return result.asList()
                .stream()
                .map(this::mapJsonElementToMovie)
                .limit(6)
                .toList();
    }

    @Override
    public String getMovieIdByMovieName(String name ,List<Movie> movies) {
        String id = "";
        id = String.valueOf(movies.stream().filter(i -> i.getOriginalTitle().equals(name)).map(g -> g.getMovieId()).findAny().get());
        return id;
    }

    @Override
    public List<Cast> getMovieCredits(String movieId) {
        String url = BASE_URL + "/3/movie/"+movieId+"/credits?api_key=" + API_KEY;
        RestTemplate restTemplate = new RestTemplate();
        JSONObject result = restTemplate.getForObject(url, JSONObject.class);
        System.out.println(result.get("cast"));
        List<Cast> casts = ((List<Object>)result.get("cast"))
                .stream()
                .map(i -> (LinkedHashMap)i)
                .map(i -> new Cast(
                        i.get("name").toString()
                        ,i.get("known_for_department").toString()))
                .limit(5)
                .toList();
        return casts;
    }

    @Override
    public List<Movie> movieListforDifferentGenre(String id) {
        String d = id;
        String url = BASE_URL + "/3/genre/"+id+"/movies?api_key=" + API_KEY;
        RestTemplate restTemplate = new RestTemplate();
        String resultString = restTemplate.getForObject(url, String.class);
        if(resultString == null)
            throw  new ContentNotFoundException();
        JsonArray result = JsonParser.parseString(resultString).getAsJsonObject().getAsJsonArray("results");
        if(result.isEmpty())
            throw  new ContentNotFoundException();
        return result.asList()
                .stream()
                .map(this::mapJsonElementToMovie)
                .limit(6)
                .toList();
    }

    public Object getMovieInfoData(String movieName) {
        List<Movie> movieList = this.searchMovieListByMovieNameOne(movieName);
        String id= this.getMovieIdByMovieName(movieName,movieList);
        List<Cast> castList = this.getMovieCastByMovieId(id);
        List<Trailer> trailerList = this.getMovieTrailerByMovieId(id);
        MovieInfo movieInfo = new MovieInfo(movieList,castList,trailerList);
        return  movieInfo;
    }

    public List<Movie> searchOneMovieDetail(String movieName) {
        List<Movie> movieList = this.searchMovieListByMovieNameOne(movieName);
        return  movieList;
    }

    public List<Cast> getCastForMovies(String name) {
        List<Movie> movies = this.searchMovieListByMovieName(name);
        String id = this.getMovieIdByMovieName(name , movies);
        List<Cast> casts = this.getMovieCastByMovieId(id);
        return casts;
    }

    public List<Trailer> getMovieTrailer(String movieName) {
        List<Movie> movies = this.searchMovieListByMovieName(movieName);
        String id = this.getMovieIdByMovieName(movieName , movies);
        List<Trailer> trailer = this.getMovieTrailerByMovieId(id);
        return trailer;
    }

    public List<Movie> getMoviesByYear(String year) {
        List<Movie> movieListByYear = this.searchMoviesListByYear(year);
        return movieListByYear;
    }

    @Override
    public List<Cast> getMovieCastByMovieId(String movieId) throws ContentNotFoundException{
        String url = BASE_URL + "/3/movie/"+movieId+"/credits?api_key=" + API_KEY;
        RestTemplate restTemplate = new RestTemplate();
        String resultString = restTemplate.getForObject(url,String.class);
        if(resultString == null)
            throw  new ContentNotFoundException();
        JsonArray result = JsonParser.parseString(resultString).getAsJsonObject().getAsJsonArray("cast");
        if(result.isEmpty())
            throw  new ContentNotFoundException();
        return result.asList()
                .stream()
                .map(i -> new Cast(returnJsonElementAsNullableString(i.getAsJsonObject().get("name"))
                        ,returnJsonElementAsNullableString(i.getAsJsonObject().get("known_for_department"))
                ))
                .limit(5)
                .toList();
    }

    @Override
    public List<Trailer> getMovieTrailerByMovieId(String movieId) throws ContentNotFoundException{
        String url = BASE_URL + "/3/movie/" + movieId + "/videos?api_key=" + API_KEY;
        RestTemplate restTemplate = new RestTemplate();
        String resultString = restTemplate.getForObject(url,String.class);
        if(resultString == null)
            throw  new ContentNotFoundException();
        JsonArray result = JsonParser.parseString(resultString).getAsJsonObject().getAsJsonArray("results");
        if(result.isEmpty())
            throw  new ContentNotFoundException();
        return result.asList()
                .stream()
                .map(i -> new Trailer(returnJsonElementAsNullableString(i.getAsJsonObject().get("key"))
                        ,returnJsonElementAsNullableString(i.getAsJsonObject().get("site"))
                        ,returnJsonElementAsNullableString(i.getAsJsonObject().get("id"))
                ))
                .limit(5)
                .toList();
    }

    @Override
    public List<Movie> getMoviesByGenreName(String genreName) throws GenreNotFoundException {
        Integer genreId = this.movieRecommendedRepository.getGenresByNames(List.of(genreName))
                .stream()
                .findFirst()
                .orElseThrow(GenreNotFoundException::new)
                .getId();
        List<Movie> movies = this.getMoviesByGenreIds(genreId.toString());
        if(movies.isEmpty())
            throw new ContentNotFoundException();
        return movies;
    }

    @Override
    public List<Review> getMovieReviewsByMovieId(Integer movieId) {
        String url = BASE_URL + "/3/movie/" + movieId + "/reviews?api_key=" + API_KEY + "&language=en-US";
        RestTemplate restTemplate = new RestTemplate();
        String resultString = restTemplate.getForObject(url, String.class);
        if(resultString == null)
            throw  new ContentNotFoundException();
        JsonArray result = JsonParser.parseString(resultString).getAsJsonObject().getAsJsonArray("results");
        if(result.isEmpty())
            throw  new ContentNotFoundException();
        return result.asList()
                .stream()
                .map(i -> new Review(returnJsonElementAsNullableString(i.getAsJsonObject().get("id"))
                        ,returnJsonElementAsNullableString(i.getAsJsonObject().get("author"))
                        , returnJsonElementAsNullableString(i.getAsJsonObject().get("content"))
                        , returnJsonElementAsNullableString(i.getAsJsonObject().get("created_at"))))
                .toList();
    }


//    public List<Movie> getMovieListActionGenre()
//    {
//        String actionId = "28";
//        List<Movie> movies = this.get.movieListforDifferentGenre(actionId);
//        return movies;
//    }
//    public List<Movie> getMovieListComedyGenre()
//    {
//        String comedyId = "35";
//        List<Movie> movies = thirdPartyApiServiceImpl.movieListforDifferentGenre(comedyId); return movies;
//    }
//    public List<Movie> getMovieListCrimeGenre()
//    {
//        String crimeId = "80";
//        List<Movie> movies = thirdPartyApiServiceImpl.movieListforDifferentGenre(crimeId); return movies;
//    }
//
//    public List<Movie> getMovieListFamilyGenre()
//    {
//        String familyId = "10751";
//        List<Movie> movies = thirdPartyApiServiceImpl.movieListforDifferentGenre(familyId); return  movies;
//    }

    @Override
    public List<Movie> getMoviesByGenreIds(String genreIdQuery) throws ContentNotFoundException{
        String url = BASE_URL + "/3/discover/movie?api_key=" + API_KEY + "&include_adult=false&include_video=false&language=en-US&page=1&sort_by=popularity.desc&with_genres=" + genreIdQuery;
        RestTemplate restTemplate = new RestTemplate();
        String resultString = restTemplate.getForObject(url, String.class);
        if(resultString == null)
            throw  new ContentNotFoundException();
        JsonArray result = JsonParser.parseString(resultString).getAsJsonObject().getAsJsonArray("results");
        if(result.isEmpty())
            throw  new ContentNotFoundException();
        return result.asList()
                .stream()
                .map(this::mapJsonElementToMovie)
                .toList();
    }

    @Scheduled(initialDelay = 10000L, fixedDelay = 3600000L)
    @Retryable(maxAttempts = 10, backoff = @Backoff(delay = 10000L))
    public void refreshGenreList() throws Exception{
        String uri = BASE_URL + "/3/genre/movie/list?api_key=" + API_KEY + "&language=en-US";
        RestTemplate restTemplate = new RestTemplate();
        String resultString = restTemplate.getForObject(uri, String.class);
        if(resultString == null)
            throw new ContentNotFoundException();
        JsonArray result = JsonParser.parseString(resultString).getAsJsonObject().getAsJsonArray("genres");
        if(result.isEmpty())
            throw  new ContentNotFoundException();
        this.movieRecommendedRepository.clearGenreList();
        this.movieRecommendedRepository.populateGenreList(result.asList()
                .stream()
                .map(i -> new Genre(i.getAsJsonObject().get("id").getAsInt(), i.getAsJsonObject().get("name").getAsString()))
                .toList());
    }


    @Scheduled(initialDelay = 20000L, fixedDelay = 3600000L)
    @Retryable(maxAttempts = 10, backoff = @Backoff(delay = 10000L))
    public void refreshPopularMovieList() throws Exception{
        String url1 = BASE_URL + "/3/movie/popular?api_key=" + API_KEY + "&page=1&language=en-US&region=US";
        RestTemplate restTemplate = new RestTemplate();
        String resultString = restTemplate.getForObject(url1, String.class);
        if(resultString == null)
            throw new ContentNotFoundException();
        JsonArray result = JsonParser.parseString(resultString).getAsJsonObject().getAsJsonArray("results");
        if(result.isEmpty())
            throw  new ContentNotFoundException();
        this.movieRecommendedRepository.clearPopularMovieList();
        this.movieRecommendedRepository.populatePopularMovieList(result.asList()
                .stream()
                .map(this::mapJsonElementToMovie)
                .filter(i -> i.getPosterPath() != null)
                .toList());
    }

    @Scheduled(initialDelay = 20000L, fixedDelay = 3600000L)
    @Retryable(maxAttempts = 10, backoff = @Backoff(delay = 10000L))
    public void refreshUpcomingMovieList() throws Exception{
        String url = BASE_URL + "/3/movie/upcoming?api_key=" + API_KEY + "&page=1&language=en-US&region=US";
        RestTemplate restTemplate = new RestTemplate();
        String resultString = restTemplate.getForObject(url, String.class);
        if(resultString == null)
            throw new ContentNotFoundException();
        JsonArray result = JsonParser.parseString(resultString).getAsJsonObject().getAsJsonArray("results");
        if(result.isEmpty())
            throw  new ContentNotFoundException();
        this.movieRecommendedRepository.clearUpcomingMovieList();
        this.movieRecommendedRepository.populateUpcomingMovieList(result.asList()
                .stream()
                .map(this::mapJsonElementToMovie)
                .filter(i -> i.getBackdropPath() != null)
                .toList());
    }

    @Scheduled(initialDelay = 20000L, fixedDelay = 3600000L)
    @Retryable(maxAttempts = 10, backoff = @Backoff(delay = 10000L))
    public void refreshRecommendedCardsMap() throws Exception{
        this.movieRecommendedRepository.clearRecommendedCardsMap();
        this.movieRecommendedRepository.populateRecommendedCardsMap(this.recommendedCardGenres
                .stream()
                .map(i -> new AbstractMap.SimpleEntry<String, List<Movie>>(i, this.getMoviesByGenreName(i)))
                .collect(Collectors.toMap(i -> i.getKey(), j -> j.getValue())));
    }

    private String returnJsonElementAsNullableString(JsonElement jsonElement){
        if(jsonElement.isJsonNull())
            return null;
        return jsonElement.getAsString();
    }

    private Movie mapJsonElementToMovie(JsonElement jsonElement){
        List<Genre> genreList = null;
        if(jsonElement.getAsJsonObject().has("genre_ids"))
            genreList = this.movieRecommendedRepository.getGenresById(jsonElement.getAsJsonObject().get("genre_ids")
                    .getAsJsonArray()
                    .asList()
                    .stream()
                    .map(JsonElement::getAsInt)
                    .toList());
        else if(jsonElement.getAsJsonObject().has("genres"))
            genreList = jsonElement.getAsJsonObject().getAsJsonArray("genres")
                    .asList()
                    .stream()
                    .map(i -> new Genre(i.getAsJsonObject().get("id").getAsInt()
                                , i.getAsJsonObject().get("name").getAsString()))
                    .toList();
        return new Movie(returnJsonElementAsNullableString(jsonElement.getAsJsonObject().get("id"))
                , returnJsonElementAsNullableString(jsonElement.getAsJsonObject().get("original_title"))
                , returnJsonElementAsNullableString(jsonElement.getAsJsonObject().get("overview"))
                , returnJsonElementAsNullableString(jsonElement.getAsJsonObject().get("poster_path"))
                , returnJsonElementAsNullableString(jsonElement.getAsJsonObject().get("vote_average"))
                , returnJsonElementAsNullableString(jsonElement.getAsJsonObject().get("release_date"))
                , returnJsonElementAsNullableString(jsonElement.getAsJsonObject().get("backdrop_path"))
                , genreList);
    }
}



