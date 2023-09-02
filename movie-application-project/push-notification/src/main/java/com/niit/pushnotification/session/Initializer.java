package com.niit.pushnotification.session;

import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;
import org.springframework.stereotype.Component;

@Component
public class Initializer extends AbstractHttpSessionApplicationInitializer {
    public Initializer(){
        super(Config.class);
    }
}


