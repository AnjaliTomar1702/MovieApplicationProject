package com.niit.userauthentication.controller;

import com.niit.userauthentication.domain.DatabaseUser;
import com.niit.userauthentication.domain.Image;
import com.niit.userauthentication.domain.Role;
import com.niit.userauthentication.dto.*;
import com.niit.userauthentication.exception.ImageTooLargeException;
import com.niit.userauthentication.exception.InvalidCredentialsException;
import com.niit.userauthentication.exception.TokenExpiredException;
import com.niit.userauthentication.exception.UserNotFoundException;
import com.niit.userauthentication.service.AdminService;
import com.niit.userauthentication.service.SecurityTokenGenerator;
import com.niit.userauthentication.service.DatabaseUserService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
@RequestMapping("")
@Tag(name = "User Authentication")
public class UserAuthenticationController {

    private final DatabaseUserService databaseUserService;
    private final SecurityTokenGenerator securityTokenGenerator;
    private final AdminService adminService;

    @ApiResponse(description = "Login (User), returns jwt token on user login")
    @PostMapping(value = "/api/v1/login")
    @CircuitBreaker(name = "WindowOf10")
    @TimeLimiter(name = "TimeoutIn15Seconds")
    public CompletableFuture<ResponseEntity<MessageDTO>> login(@RequestBody @Valid UserDTO userDTO) throws InvalidCredentialsException {
        MessageDTO messageDTO =this.securityTokenGenerator
                .generateToken(this.databaseUserService.getUserFromEmailAndPassword(userDTO.getEmail(), userDTO.getPassword()));
        return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(messageDTO
                , HttpStatus.OK));
    }

    @ApiResponse(description = "Login (User), returns jwt token on user login")
    @PostMapping(value = "/api/v1/admin/login")
    @CircuitBreaker(name = "WindowOf10")
    @TimeLimiter(name = "TimeoutIn15Seconds")
    public CompletableFuture<ResponseEntity<?>> adminLogin(Authentication authentication) throws InvalidCredentialsException {
        return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(HttpStatus.OK));
    }

    @ApiResponse(description = "Save(User), save users and image and give confirmation response")
    @PostMapping(value = "/api/v1/save", consumes = "multipart/form-data")
    @TimeLimiter(name = "TimeoutIn15Seconds")
    @CircuitBreaker(name = "WindowOf10")
    public CompletableFuture<ResponseEntity<MessageDTO>> save(@RequestParam("email") String email, @RequestParam("password") String password, @RequestParam("file") MultipartFile file) throws ImageTooLargeException, IOException {
        byte[] imageBytes =file.getBytes();
        if(imageBytes.length > 204800) //greater than 200KB
            throw new ImageTooLargeException();
        DatabaseUser databaseUser = new DatabaseUser();
        databaseUser.setImage(new Image(imageBytes));
        databaseUser.setPassword(password);
        databaseUser.setEmail(email);
        return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(this.databaseUserService.saveUser(databaseUser), HttpStatus.CREATED));
    }

    @SecurityRequirement(name = "bearerAuth")
    @ApiResponse(description = "Get(), gets image")
    @GetMapping(value = "/api/v1/image", produces = "image/webp")
    @TimeLimiter(name = "TimeoutIn15Seconds")
    @CircuitBreaker(name = "WindowOf10")
    public CompletableFuture<ResponseEntity<byte[]>> getImage(Principal principal) {
        return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(this.databaseUserService.getImageFromEmail(principal.getName()).getImage(), HttpStatus.OK));
    }

    @SecurityRequirement(name = "bearerAuth")
    @ApiResponse(description = "Post(), post image")
    @PostMapping(value = "/api/v1/image")
    @TimeLimiter(name = "TimeoutIn15Seconds")
    @CircuitBreaker(name = "WindowOf10")
    public CompletableFuture<ResponseEntity<byte[]>> updateImage(@RequestParam("file") MultipartFile file, Principal principal) throws ImageTooLargeException, IOException {
        byte[] imageBytes =file.getBytes();
        if(imageBytes.length > 204800) //greater than 200KB
            throw new ImageTooLargeException();
        if(Arrays.equals(this.databaseUserService.updateImage(new Image(imageBytes), principal.getName()).getImage(), imageBytes))
            return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
        else
            return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @ApiResponse(description = "Get(), gets users")
    @GetMapping(value = "/api/v1/admin/users")
    @TimeLimiter(name = "TimeoutIn15Seconds")
    @CircuitBreaker(name = "WindowOf10")
    public CompletableFuture<ResponseEntity<List<DatabaseUser>>> getUsers(@RequestParam(value = "email", required = false) String email) {
        if(email != null)
            return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(this.adminService.getUsersContainingString(email), HttpStatus.OK));
        else
            return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(this.adminService.getUsers(), HttpStatus.OK));
    }

    @ApiResponse(description = "Block(User), block user")
    @PatchMapping(value = "/api/v1/admin/users/block")
    @TimeLimiter(name = "TimeoutIn15Seconds")
    @CircuitBreaker(name = "WindowOf10")
    public CompletableFuture<ResponseEntity<MessageDTO>> blockUser(@RequestBody @Valid UserEmailDTO userEmailDTO) {
        return CompletableFuture.supplyAsync(() -> this.adminService.blockUser(userEmailDTO.getEmail()) ?
                new ResponseEntity<>(new MessageDTO("User " + userEmailDTO.getEmail() + " blocked"), HttpStatus.OK)
                : new ResponseEntity<>(new MessageDTO("User " + userEmailDTO.getEmail() + " not successfully blocked"), HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @ApiResponse(description = "Unblock(User), unblock user")
    @PatchMapping(value = "/api/v1/admin/users/unblock")
    @TimeLimiter(name = "TimeoutIn15Seconds")
    @CircuitBreaker(name = "WindowOf10")
    public CompletableFuture<ResponseEntity<MessageDTO>> unblockUser(@RequestBody @Valid UserEmailDTO userEmailDTO) {
        return CompletableFuture.supplyAsync(() -> this.adminService.unblockUser(userEmailDTO.getEmail()) ?
                new ResponseEntity<>(new MessageDTO("User " + userEmailDTO.getEmail() + " enabled"), HttpStatus.OK)
                : new ResponseEntity<>(new MessageDTO("User " + userEmailDTO.getEmail() + " not successfully enabled"), HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @ApiResponse(description = "addRole(User, Role), adds role")
    @PatchMapping(value = "/api/v1/admin/users/addrole")
    @TimeLimiter(name = "TimeoutIn15Seconds")
    @CircuitBreaker(name = "WindowOf10")
    public CompletableFuture<ResponseEntity<DatabaseUser>> addRoleToUser(@RequestBody @Valid UserRoleDTO userRoleDTO) {
        return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(this.adminService.addRole(userRoleDTO.getEmail()
                , userRoleDTO.getRoleName()), HttpStatus.OK));
    }

    @ApiResponse(description = "removeRole(User, Role), adds role")
    @PatchMapping(value = "/api/v1/admin/users/removerole")
    @TimeLimiter(name = "TimeoutIn15Seconds")
    @CircuitBreaker(name = "WindowOf10")
    public CompletableFuture<ResponseEntity<DatabaseUser>> removeRoleFromUser(@RequestBody @Valid UserRoleDTO userRoleDTO) {
        return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(this.adminService.removeRole(userRoleDTO.getEmail()
                , userRoleDTO.getRoleName()), HttpStatus.OK));
    }

    @ApiResponse(description = "patchUser(User), updates User")
    @PatchMapping(value = "/api/v1/admin/users")
    @TimeLimiter(name = "TimeoutIn15Seconds")
    @CircuitBreaker(name = "WindowOf10")
    public CompletableFuture<ResponseEntity<DatabaseUser>> updateUser(@RequestBody DatabaseUser databaseUser) {
        return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(this.adminService.updateUser(databaseUser), HttpStatus.OK));
    }


    @ApiResponse(description = "Get(), gets roles")
    @GetMapping(value = "/api/v1/admin/roles")
    @TimeLimiter(name = "TimeoutIn15Seconds")
    @CircuitBreaker(name = "WindowOf10")
    public CompletableFuture<ResponseEntity<List<Role>>> getRoles() {
        return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(this.adminService.getRoles(), HttpStatus.OK));
    }

    @ApiResponse(description = "Post(email), generates forgot password link")
    @PostMapping(value = "/api/v1/forgotpassword")
    @TimeLimiter(name = "TimeoutIn15Seconds")
    @CircuitBreaker(name = "WindowOf10")
    public CompletableFuture<ResponseEntity<MessageDTO>> createForgotPasswordLink(Principal principal, HttpSession httpSession) throws Exception, UserNotFoundException {
        this.databaseUserService.sendEmailPasswordResetMessage(principal.getName(), Base64.getEncoder().encodeToString(httpSession.getId().getBytes(StandardCharsets.UTF_8)));
        return CompletableFuture.completedFuture(new ResponseEntity<>(new MessageDTO("Password reset email sent, link is active for 30 minutes"), HttpStatus.OK));
    }

    @ApiResponse(description = "Post(password), resets password")
    @PostMapping(value = "/api/v1/resetpassword")
    @TimeLimiter(name = "TimeoutIn15Seconds")
    @CircuitBreaker(name = "WindowOf10")
    public CompletableFuture<ResponseEntity<MessageDTO>> resetPasswordLink(@RequestBody PasswordDTO passwordDTO, Principal principal) throws Exception, UserNotFoundException {
        if(this.databaseUserService.resetPassword(principal.getName(), passwordDTO.getPassword()))
            return CompletableFuture.completedFuture(new ResponseEntity<>(new MessageDTO("Password successfully reset"), HttpStatus.OK));
        else
            return CompletableFuture.completedFuture(new ResponseEntity<>(new MessageDTO("Server error, please try in some time"), HttpStatus.INTERNAL_SERVER_ERROR));
    }

}
