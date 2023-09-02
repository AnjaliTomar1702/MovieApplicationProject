package com.niit.movieservice.controller;

import com.niit.movieservice.dto.MessageDTO;
import com.niit.movieservice.dto.NotificationDTO;
import com.niit.movieservice.exception.*;
import com.niit.movieservice.model.Movie;
import com.niit.movieservice.model.User;
import com.niit.movieservice.service.UserMovieService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
@Tag(name = "User Movie")
public class UserMovieController {

   private final UserMovieService userMovieService;
    private ResponseEntity<?> responseEntity;

    @ApiResponse(description = "Post(email, password, image), registers new user")
    @TimeLimiter(name = "TimeoutIn15Seconds")
    @CircuitBreaker(name = "WindowOf10")
    @PostMapping("/register")
    public CompletableFuture<ResponseEntity<User>> registerUser(@RequestParam("email") String email, @RequestParam("password") String password, @RequestParam("file") MultipartFile file) throws UserAlreadyExistsException, InternalServerError, IOException, ExecutionException, InterruptedException {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return new ResponseEntity<>(this.userMovieService.registerUser(email,password,file), HttpStatus.OK);
            } catch (IOException | ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
//        try {
//            responseEntity =  new ResponseEntity<>(userMovieService.registerUser(email,password,file), HttpStatus.CREATED);
//        }
//        catch(UserAlreadyExistsException | IOException | ExecutionException | InterruptedException e)
//        {
//            throw new UserAlreadyExistsException();
//        }
//        return responseEntity;
    }

    @SecurityRequirement(name = "bearerAuth")
    @ApiResponse(description = "Post(notification), adds new notification to user")
    @TimeLimiter(name = "TimeoutIn15Seconds")
    @CircuitBreaker(name = "WindowOf10")
    @PostMapping("/user/notification")
    public CompletableFuture<ResponseEntity<Boolean>> addNotification(@RequestBody Movie movie,Principal principal) throws Exception, UserNotFoundException {
        return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(this.userMovieService.pushNotification(movie, principal.getName()), HttpStatus.OK));
    }

    @SecurityRequirement(name = "bearerAuth")
    @ApiResponse(description = "Get(), gets notifications of user")
    @TimeLimiter(name = "TimeoutIn15Seconds")
    @CircuitBreaker(name = "WindowOf10")
    @GetMapping("/user/notification")
    public CompletableFuture<ResponseEntity<List<Movie>>> getNotification(Principal principal){
        return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(this.userMovieService.getNotification(principal.getName()), HttpStatus.OK));
    }

    @SecurityRequirement(name = "bearerAuth")
    @ApiResponse(description = "Post(movie), adds movie to user")
    @TimeLimiter(name = "TimeoutIn15Seconds")
    @CircuitBreaker(name = "WindowOf10")
    @PostMapping("/user/favourite")
    public  CompletableFuture<ResponseEntity<User>> addMovieToFavourites(@RequestBody Movie movie, Principal principal) throws UserNotFoundException{
        return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(this.userMovieService.addMovieToFavourites(movie, principal.getName()), HttpStatus.OK));
    }

    @SecurityRequirement(name = "bearerAuth")
    @ApiResponse(description = "Get(), gets all favourites of user")
    @TimeLimiter(name = "TimeoutIn15Seconds")
    @CircuitBreaker(name = "WindowOf10")
    @GetMapping("/user/favourite")
    public CompletableFuture<ResponseEntity<List<Movie>>> getAllFavourites(Principal principal) {
        return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(this.userMovieService.getAllFavouriteMovies(principal.getName()), HttpStatus.OK));
    }

    @SecurityRequirement(name = "bearerAuth")
    @ApiResponse(description = "Delete(movieId), deletes movie from favorites list of user")
    @TimeLimiter(name = "TimeoutIn15Seconds")
    @CircuitBreaker(name = "WindowOf10")
    @DeleteMapping("/user/favourite/{movieId}")
    public CompletableFuture<ResponseEntity<Boolean>> deleteFavouriteFromList(@PathVariable int movieId,Principal principal) throws MovieNotFoundException {
        return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(this.userMovieService.removeMovieFromFavourites(movieId,principal.getName()), HttpStatus.OK));
    }

    public CompletableFuture<ResponseEntity<MessageDTO>> fallback(Exception e) throws Exception {
        if(e instanceof InvalidCredentialsException)
            throw e;
        if(e instanceof TokenExpiredException)
            throw e;
        if(e instanceof InternalServerError)
            throw e;
        if(e instanceof MovieAlreadyExistsException)
            throw e;
        if(e instanceof MovieNotFoundException)
            throw e;
        if(e instanceof UserAlreadyExistsException)
            throw e;
        if(e instanceof UserNotFoundException)
            throw e;
        e.printStackTrace();
        return CompletableFuture.completedFuture(new ResponseEntity<>(new MessageDTO("Server error, please try in some time"), HttpStatus.INTERNAL_SERVER_ERROR));
    }
}
