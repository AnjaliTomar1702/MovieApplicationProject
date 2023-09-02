package com.niit.recommended.controller;

import com.niit.recommended.domain.*;
import com.niit.recommended.service.RecommendedForUser;
import com.niit.recommended.service.ThirdPartyApiService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.CompletableFuture;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/")
@Tag(name = "Recommended Service")
public class RecommendedController {

    private final RecommendedForUser recommendedForUser;
    private final ThirdPartyApiService thirdPartyApiService;

    @ApiResponse(description = "Get(), gets genre list")
    @TimeLimiter(name = "TimeoutIn15Seconds")
    @CircuitBreaker(name = "WindowOf10")
    @GetMapping(value = "/recommended/movie/genre")
    public CompletableFuture<ResponseEntity<List<Genre>>> movieListGenre() {
        return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(this.recommendedForUser.getGenreList(), HttpStatus.OK));
    }

    @ApiResponse(description = "Get(), gets recommended movie list")
    @TimeLimiter(name = "TimeoutIn15Seconds")
    @CircuitBreaker(name = "WindowOf10")
    @GetMapping(value = "/recommended/movie/popular")
    public CompletableFuture<ResponseEntity<List<Movie>>> getRecommendedMovieList() {
        return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(this.recommendedForUser.getPopularList(), HttpStatus.OK));
    }

    @ApiResponse(description = "Get(), gets upcoming movies list")
    @TimeLimiter(name = "TimeoutIn15Seconds")
    @CircuitBreaker(name = "WindowOf10")
    @GetMapping(value = "/recommended/movie/upcoming")
    public CompletableFuture<ResponseEntity<List<Movie>>> getUpcomingMovieList() {
        return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(this.recommendedForUser.getUpcomingMovieList(), HttpStatus.OK));
    }

    @ApiResponse(description = "Get(), gets recommended cards map")
    @TimeLimiter(name = "TimeoutIn15Seconds")
    @CircuitBreaker(name = "WindowOf10")
    @GetMapping(value = "/recommended/movie/cards")
    public CompletableFuture<ResponseEntity<Map<String, List<Movie>>>> getRecommendedCardsMap() {
        return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(this.recommendedForUser.getRecommendedCardsMap(), HttpStatus.OK));
    }

    @ApiResponse(description = "Get(genreName), gets movies by genre name")
    @TimeLimiter(name = "TimeoutIn15Seconds")
    @CircuitBreaker(name = "WindowOf10")
    @GetMapping(value = "/recommended/movie/searchByGenreName/{name}")
    public CompletableFuture<ResponseEntity<List<Movie>>> searchMovieByGenreName(@PathVariable String name) {
       return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(this.thirdPartyApiService.getMoviesByGenreName(name), HttpStatus.OK));
    }

    @ApiResponse(description = "Get(year), gets movies by year")
    @TimeLimiter(name = "TimeoutIn15Seconds")
    @CircuitBreaker(name = "WindowOf10")
    @GetMapping(value = "/recommended/movie/searchByYear/{year}")
    public CompletableFuture<ResponseEntity<List<Movie>>> getMoviesByYear(@PathVariable String year) {
        return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(this.thirdPartyApiService.getMoviesByYear(year), HttpStatus.OK));
    }

    @ApiResponse(description = "Get(searchName), returns list of movie matching search name")
    @TimeLimiter(name = "TimeoutIn15Seconds")
    @CircuitBreaker(name = "WindowOf10")
    @GetMapping(value = "/recommended/movie/searchByName/{name}" )  //Search Function by giving MovieName
    public CompletableFuture<ResponseEntity<List<Movie>>> searchMovieListByMovieName(@PathVariable String name) {
        return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(this.thirdPartyApiService.searchMovieListByMovieName(name), HttpStatus.OK));
    }

    @ApiResponse(description = "Get(genreIdQuery), gets movies by genreIdQuery")
    @TimeLimiter(name = "TimeoutIn15Seconds")
    @CircuitBreaker(name = "WindowOf10")
    @GetMapping(value = "/recommended/movie/searchByGenreIdQuery/{genreIdQuery}")
    public CompletableFuture<ResponseEntity<List<Movie>>> getMoviesByGenreIdQuery(@PathVariable String genreIdQuery) {
        return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(this.thirdPartyApiService.getMoviesByGenreIds(genreIdQuery), HttpStatus.OK));
    }

    @ApiResponse(description = "Get(id), gets movie by id")
    @TimeLimiter(name = "TimeoutIn15Seconds")
    @CircuitBreaker(name = "WindowOf10")
    @GetMapping(value = "/recommended/movie/searchById/{id}")
    public CompletableFuture<ResponseEntity<Movie>> getMovieById(@PathVariable Integer id) {
        return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(this.thirdPartyApiService.getMovieById(id), HttpStatus.OK));
    }
    @ApiResponse(description = "Get(movieName), gets cast of movie")
    @TimeLimiter(name = "TimeoutIn15Seconds")
    @CircuitBreaker(name = "WindowOf10")
    @GetMapping(value = "/recommended/movie/searchByName/{name}/cast")
    public CompletableFuture<ResponseEntity<List<Cast>>> getCastForMovies(@PathVariable String name) {
        return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(this.thirdPartyApiService.getCastForMovies(name), HttpStatus.OK));
    }

    @ApiResponse(description = "Get(movieName), gets specific movie info")
    @TimeLimiter(name = "TimeoutIn15Seconds")
    @CircuitBreaker(name = "WindowOf10")
    @GetMapping(value = "/recommended/movie/searchByName/{name}/info")
    public CompletableFuture<ResponseEntity<Object>> getMovieInfoData(String name) {
        return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(this.thirdPartyApiService.getMovieInfoData(name), HttpStatus.OK));
    }

    @ApiResponse(description = "Get(movieName), gets movie trailer")
    @TimeLimiter(name = "TimeoutIn15Seconds")
    @CircuitBreaker(name = "WindowOf10")
    @GetMapping(value = "/recommended/movie/searchByName/{name}/trailer")
    public CompletableFuture<ResponseEntity<List<Trailer>>> getMovieTrailerByName(@PathVariable String name) {
        return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(this.thirdPartyApiService.getMovieTrailer(name), HttpStatus.OK));
    }

    @ApiResponse(description = "Get(movieName), gets movie trailer By MovieId")
    @TimeLimiter(name = "TimeoutIn15Seconds")
    @CircuitBreaker(name = "WindowOf10")
    @GetMapping(value = "/recommended/movie/searchById/{id}/trailer")
    public CompletableFuture<ResponseEntity<List<Trailer>>> getMovieTrailerByMovieId(@PathVariable String id) {
        return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(this.thirdPartyApiService.getMovieTrailerByMovieId(id), HttpStatus.OK));
    }

    @ApiResponse(description = "Get(movieName), gets cast of movie by movieId")
    @TimeLimiter(name = "TimeoutIn15Seconds")
    @CircuitBreaker(name = "WindowOf10")
    @GetMapping(value = "/recommended/movie/searchById/{id}/cast")
    public CompletableFuture<ResponseEntity<List<Cast>>> getCastForMoviesByMovieId(@PathVariable String id) {
        return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(this.thirdPartyApiService.getMovieCastByMovieId(id), HttpStatus.OK));
    }

    @ApiResponse(description = "Get(movieName), gets movie review")
    @TimeLimiter(name = "TimeoutIn15Seconds")
    @CircuitBreaker(name = "WindowOf10")
    @GetMapping(value = "/recommended/movie/searchById/{id}/review")
    public CompletableFuture<ResponseEntity<List<Review>>> getMovieReview(@PathVariable Integer id) {
        return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(this.thirdPartyApiService.getMovieReviewsByMovieId(id), HttpStatus.OK));
    }

//    @ApiResponse(description = "Get(), gets movie list belonging to action genre")
//    @TimeLimiter(name = "TimeoutIn15Seconds", fallbackMethod = "fallback")
//    @CircuitBreaker(name = "WindowOf10", fallbackMethod = "fallback")
//    @GetMapping(value = "/thirdParty/Action")
//    public CompletableFuture<ResponseEntity<List<Movie>>> getMovieListActionGenre() {
//        return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(this.thirdPartyApiService.getMovieListActionGenre(), HttpStatus.OK));
//    }
//
//    @ApiResponse(description = "Get(), gets movie list belonging to comedy genre")
//    @TimeLimiter(name = "TimeoutIn15Seconds", fallbackMethod = "fallback")
//    @CircuitBreaker(name = "WindowOf10", fallbackMethod = "fallback")
//    @GetMapping(value = "/thirdParty/Comedy")
//    public CompletableFuture<ResponseEntity<List<Movie>>> getMovieListComedyGenre() {
//        return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(this.recommendedForUser.getMovieListComedyGenre(), HttpStatus.OK));
//    }
//
//    @ApiResponse(description = "Get(), gets movie list belonging to crime genre")
//    @TimeLimiter(name = "TimeoutIn15Seconds", fallbackMethod = "fallback")
//    @CircuitBreaker(name = "WindowOf10", fallbackMethod = "fallback")
//    @GetMapping(value = "/thirdParty/Crime")
//    public CompletableFuture<ResponseEntity<List<Movie>>> getMovieListCrimeGenre() {
//        return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(this.recommendedForUser.getMovieListCrimeGenre(), HttpStatus.OK));
//    }
//
//    @ApiResponse(description = "Get(), gets movie list belonging to family genre")
//    @TimeLimiter(name = "TimeoutIn15Seconds", fallbackMethod = "fallback")
//    @CircuitBreaker(name = "WindowOf10", fallbackMethod = "fallback")
//    @GetMapping(value = "/thirdParty/Family")
//    public CompletableFuture<ResponseEntity<List<Movie>>> getMovieListFamilyGenre() {
//        return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(this.recommendedForUser.getMovieListFamilyGenre(), HttpStatus.OK));
//    }


//    @GetMapping(value = "/thirdParty")
//    public List<Movie> movieListforDifferentGenre()
//    {
//
//        String id="28";
//        String url1 = "https://api.themoviedb.org/3/genre/"+id+"/movies?api_key=dd4d819639705d332d531217b4f7c6b6";
//
//
//        RestTemplate restTemplate = new RestTemplate();
//        JSONObject result2 = restTemplate.getForObject(url1, JSONObject.class);
//
//        List<Movie> movie = ((List<LinkedHashMap>)result2.get("results"))
//                .stream()
//                .map(i -> new Movie(i.get("id").toString()
//                        , i.get("original_title").toString()
//                        ,i.get("overview").toString()
//                        ,i.get("poster_path").toString()
//                        ,(i.get("vote_average").toString())
//                        ,i.get("release_date").toString()))
//                .limit(10)
//                .toList();
//
//        return movie;
//    }






//    @GetMapping(value = "/clientHello1")
//    private List<Movie> getMovieDetails() {
//
//        String uri = "https://api.themoviedb.org/3/movie/popular?api_key=dd4d819639705d332d531217b4f7c6b6&page=1&language=en-US&region=US";
//        RestTemplate restTemplate = new RestTemplate();
//        JSONObject result = restTemplate.getForObject(uri, JSONObject.class);
// .map(i -> new Movie(i.get("backdrop_path").toString(), (List<Genre>) i.get("genre_ids"),Integer.parseInt(i.get("id").toString()),i.get("original_title").toString(),i.get("overview").toString(),i.get("poster_path").toString(),Float.parseFloat(i.get("vote_average").toString())))
//
//        List<Movie> movie = ((List<Object>)result.get("results"))
//                .stream()
//                .map(i -> (LinkedHashMap)i)
//                .map(i -> new Movie(i.get("backdrop_path").toString(), (List<Genre>) i.get("genre_ids"),Integer.parseInt(i.get("id").toString()),i.get("original_title").toString(),i.get("overview").toString(),i.get("poster_path").toString(),Float.parseFloat(i.get("vote_average").toString())))
//                        .toList();


//        return movie;
 //   }



//    @GetMapping(value="/clientHello3")
//     public Movie getMovieByGenreId()
//    {
//            String url = "https://api.themoviedb.org/3/movie/popular?api_key=dd4d819639705d332d531217b4f7c6b6&page=1&language=en-US&region=US";
//            RestTemplate restTemplate = new RestTemplate();
//            Movie movies = restTemplate.getForObject(url , Movie.class);
//
//        System.out.println(movies);
//
//        return new Movie(movies.getGenreList(),movies.getOriginal_title(),movies.getOverview());
//    }





//    @GetMapping(value = "/clientHello3")
//    private List<Movie> getMovieDetailsByGenre() {
//
//
//
//        String uri = "https://api.themoviedb.org/3/movie/popular?api_key=dd4d819639705d332d531217b4f7c6b6&page=1&language=en-US&region=US";
//        RestTemplate restTemplate = new RestTemplate();
//        JSONObject result = restTemplate.getForObject(uri, JSONObject.class);



//        List<Movie> movie = ((List<Object>)result.get("results"))
//                .stream()
//                .map(i -> (LinkedHashMap)i)
//                .map(i -> new Movie(i.get("backdrop_path").toString(), (int[]) i.get("genre_ids"), Integer.parseInt(i.get("id").toString()),i.get("original_title").toString(),i.get("overview").toString(),i.get("poster_path").toString(),Float.parseFloat(i.get("vote_average").toString())))
//               // .map(i -> new Movie(i.get("backdrop_path").toString(), (List<Genre>) i.get("genre_ids"),Integer.parseInt(i.get("id").toString()),i.get("original_title").toString(),i.get("overview").toString(),i.get("poster_path").toString(),Float.parseFloat(i.get("vote_average").toString())))
//                .toList();

        // List<Movie> moviesGenreId=movie.stream().map(i -> i).toList();
      // List  <List<Integer>> movieIds = moviesGenreId.stream().map(i -> i.getGenreList()).
     //  System.out.println(moviesGenreId);
      // System.out.println(moviesGenreId.get(0).getGenreList());

//       if(moviesGenreId.get(0).getOriginal_title().equals("The Super Mario Bros. Movie"))
//       {
//           System.out.println("Success");
//       }

      //  System.out.println(moviesGenreId.get(0).getGenre_ids());
//        int id = 53;
//        for(int i=0 ; i<moviesGenreId.size() ; i++) {
//            System.out.println("moviesGenreId "+i);
//            System.out.println(moviesGenreId.get(i));
//
//
//            for (int j = 0; j < moviesGenreId; j++) {
//                System.out.println(moviesGenreId.get(i).get(j));
//
//
//
//                if(moviesGenreId.get(i).get(j) == id)
//                {
//                    System.out.println("correct "+i);
//                }
//
//
//
//            }
//
//        }

      //  System.out.println(moviesGenreId);





      //  return movie;
   // }

}
