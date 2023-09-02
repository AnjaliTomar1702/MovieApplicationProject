package com.niit.movieservice.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Document
public class User {
    @Id
    private String email;
    @Transient
    private String password;
//    private List<String> movies;
    private List<Movie> favourites = new ArrayList<>();
    private List<Movie> notifications = new ArrayList<>();
}
