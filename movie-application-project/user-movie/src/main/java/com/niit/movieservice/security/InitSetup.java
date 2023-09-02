package com.niit.movieservice.security;


import com.niit.movieservice.model.User;
import com.niit.movieservice.repository.UserMovieRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;



@Component
@RequiredArgsConstructor
public class InitSetup {

    private boolean alreadySetup = false;

    private final UserMovieRepository userMovieRepository;
    @EventListener
    public void onApplicationReadyEvent(ApplicationReadyEvent event) {
        if(alreadySetup)
            return;

        if(userMovieRepository.findById("test@test.com").isEmpty()) {
            User user = new User();
            user.setEmail("test@test.com");
            this.userMovieRepository.save(user);
        }
        alreadySetup = true;
    }

}
