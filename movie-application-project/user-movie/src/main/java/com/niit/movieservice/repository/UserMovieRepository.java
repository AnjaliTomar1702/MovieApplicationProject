package com.niit.movieservice.repository;

import com.niit.movieservice.model.Movie;
import com.niit.movieservice.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserMovieRepository extends MongoRepository<User,String> {
}
