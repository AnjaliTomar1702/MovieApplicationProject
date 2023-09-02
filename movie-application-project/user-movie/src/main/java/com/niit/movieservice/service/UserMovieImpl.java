package com.niit.movieservice.service;

import com.niit.movieservice.dto.NotificationDTO;
import com.niit.movieservice.dto.NotificationDeliveredDTO;
import com.niit.movieservice.exception.*;
import com.niit.movieservice.model.Movie;
import com.niit.movieservice.model.User;
import com.niit.movieservice.proxy.UserProxy;
import com.niit.movieservice.repository.UserMovieRepository;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RequiredArgsConstructor
@Service
public class UserMovieImpl implements UserMovieService{

   private final UserMovieRepository userMovieRepository;
    private final UserProxy userProxy;
    private final RabbitTemplate rabbitTemplate;
    private final DirectExchange directExchange;


    @Override
    public User registerUser(String email, String password, MultipartFile file) throws UserAlreadyExistsException, ImageTooLargeException, IOException, InternalServerError, ExecutionException, InterruptedException {
        User user = new User();
        user.setEmail(email);
        if(userMovieRepository.findById(user.getEmail()).isPresent())
        {
            throw new UserAlreadyExistsException();
        }

        if(userProxy.save(email,password,file).getStatusCode().equals(HttpStatus.CREATED)) {
            return userMovieRepository.save(user);
        }

throw new InternalServerError();
    }

    @Override
    public boolean pushNotification(Movie movie,String email) throws UserNotFoundException {
        User user = userMovieRepository.findById(email).orElseThrow(UserNotFoundException::new);
        LocalDateTime localDateTime = LocalDate.parse(movie.getReleaseDate()).atStartOfDay();
        //dont send json object send notificationDTO with delivery-date attribute set as release date, please change release date format to LocalDate.toString() format
        this.rabbitTemplate.convertAndSend(this.directExchange.getName(), "notification-consume-routing", new NotificationDTO(email,movie), message -> {
            message.getMessageProperties().setHeader("delivery-date", localDateTime.toString());
            return message;
        });
        List<Movie> addNotification = user.getNotifications();
                addNotification.add(movie);
        user.setNotifications(addNotification);
        userMovieRepository.save(user);

        return true ;
    }
  @Override
    public List<Movie> getNotification(String email){
       return userMovieRepository.findById(email).get().getNotifications();

  }

    @Override
    @RabbitListener(queues = "notification-delivered-queue", ackMode = "AUTO")
    public void deleteNotification(NotificationDeliveredDTO notificationDeliveredDTO) throws Exception, UserNotFoundException {
        boolean movieIdIsPresent = false;
            User userDetails = userMovieRepository.findById(notificationDeliveredDTO.getEmailId()).orElseThrow(UserNotFoundException::new);
            List<Movie> delMovies = userDetails.getNotifications();
            movieIdIsPresent = delMovies.removeIf(x -> x.getMovieId()==notificationDeliveredDTO.getMovie().getMovieId());

            System.err.print(delMovies);
            userDetails.setNotifications(delMovies);
            userMovieRepository.save(userDetails);
        }

    @Override
    public User addMovieToFavourites(Movie movie, String email) throws UserNotFoundException, MovieAlreadyExistsException {
        if (userMovieRepository.findById(email).isPresent()) {
            User user = userMovieRepository.findById(email).get();
            List<Movie> getAllMovies = user.getFavourites();
            if(getAllMovies.stream().anyMatch(i -> i.getMovieId() == movie.getMovieId()))
                throw new MovieAlreadyExistsException();
            getAllMovies.add(movie);

            user.setFavourites(getAllMovies);
            return userMovieRepository.save(user);

        }
        else {
            throw new UserNotFoundException();
        }
    }
//    @Override
//    public User addMovieToFavourites(Movie movie,String email) throws UserNotFoundException, MovieAlreadyExistsException {
//       User user = userMovieRepository.findById(email).orElseThrow(UserNotFoundException::new);
//        List<Movie> getAllMovies = user.getFavourites();
//
//        if (getAllMovies.stream().anyMatch(x -> x.getOriginalTitle().equals(movie.getOriginalTitle())))
//            throw new MovieAlreadyExistsException();
//        else {
//            getAllMovies.add(movie);
//            user.setFavourites(getAllMovies);
//
////            user.getFavourites(getAllMovies).add(movie);
//            return userMovieRepository.save(user);
//        }
//    }
    @Override
    public List<Movie> getAllFavouriteMovies(String email) {

        return userMovieRepository.findById(email).get().getFavourites();

    }

    @Override
    public boolean removeMovieFromFavourites(int movieId, String emailId) throws MovieNotFoundException {
        boolean movieIdIsPresent = false;
        if (userMovieRepository.findById(emailId).isPresent()) {
            User userDetails = userMovieRepository.findById(emailId).get();
            List<Movie> favMovies = userDetails.getFavourites();
            movieIdIsPresent = favMovies.removeIf(x -> x.getMovieId()==(movieId));
            if (!movieIdIsPresent) {
                throw new MovieNotFoundException();
            }
            userDetails.setFavourites(favMovies);
            userMovieRepository.save(userDetails);
            return true;
        } else
        {
            return false;
        }
    }
}
