package com.niit.movieservice.service;

import com.niit.movieservice.dto.NotificationDTO;
import com.niit.movieservice.dto.NotificationDeliveredDTO;
import com.niit.movieservice.exception.*;
import com.niit.movieservice.model.Movie;
import com.niit.movieservice.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface UserMovieService {
    User registerUser(String email, String password, MultipartFile file) throws UserAlreadyExistsException, ImageTooLargeException, IOException, InternalServerError, ExecutionException, InterruptedException;
    User addMovieToFavourites(Movie movie, String userId) throws UserNotFoundException, MovieAlreadyExistsException;
    List<Movie> getAllFavouriteMovies(String email);
    boolean removeMovieFromFavourites(int movieId, String emailId) throws MovieNotFoundException;
    boolean pushNotification(Movie movie,String email) throws UserNotFoundException;
    List<Movie> getNotification(String email);
    void deleteNotification(NotificationDeliveredDTO notificationDeliveredDTO) throws Exception,UserNotFoundException;
    }
