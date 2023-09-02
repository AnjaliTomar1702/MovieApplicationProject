package com.niit.movieservice.proxy;

import com.niit.movieservice.dto.LoginDTO;
import com.niit.movieservice.dto.MessageDTO;
import com.niit.movieservice.exception.ImageTooLargeException;
import com.niit.movieservice.model.User;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@FeignClient("user-authentication")
public interface UserProxy {
    @PostMapping(value = "/api/v1/save", consumes = "multipart/form-data")
    public ResponseEntity<MessageDTO> save(@RequestPart("email") String email,
          @RequestPart("password") String password, @RequestPart("file") MultipartFile file) throws ImageTooLargeException, IOException;

}
