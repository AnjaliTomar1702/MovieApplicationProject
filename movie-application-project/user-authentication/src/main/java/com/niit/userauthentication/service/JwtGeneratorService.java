package com.niit.userauthentication.service;


import com.niit.userauthentication.domain.DatabaseUser;
import com.niit.userauthentication.dto.MessageDTO;
import com.niit.userauthentication.exception.UserNotEnabledException;
import com.niit.userauthentication.security.AdditionalSecurityConfig;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtGeneratorService implements SecurityTokenGenerator{

    @Override
    public MessageDTO generateToken(DatabaseUser databaseUser) throws UserNotEnabledException {
        String jwtToken = null;
        Map<String, Object> claims = new HashMap<>();
        if(!databaseUser.isEnabled())
            throw new UserNotEnabledException();
        claims.put("roles"
                , AdditionalSecurityConfig.getAuthorities(databaseUser.getRoles())
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .reduce("", (i, t) -> t + "," + i)
                        .substring(0));
        jwtToken = Jwts.builder().setSubject(databaseUser.getEmail())
                .setExpiration(Date.from(Instant.now().plus(1, ChronoUnit.DAYS)))
                .addClaims(claims)
                .signWith(SignatureAlgorithm.HS256,"sfrswfes832zd&54cd55ddshfvTrsf$se6d67ds66s66sfd5f5").compact();
        return new MessageDTO("Token " + jwtToken);
    }
}
