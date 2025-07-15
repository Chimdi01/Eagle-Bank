package org.banking.service.auth;

import org.banking.service.model.LoginRequest;
import org.banking.service.model.LoginResponse;
import org.banking.service.model.BadRequestErrorResponse;
import org.banking.service.util.JwtUtil;
import org.banking.service.util.ValidationUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.banking.service.user.UserService;
import org.banking.service.model.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * REST controller for authentication endpoints.
 * Provides endpoints for user login and test JWT generation.
 */
@RestController
@RequestMapping("/v1/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    /**
     * Authenticates a user and issues a JWT if the user exists.
     * @param request the login request containing userId
     * @return the login response with JWT or error
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        String userId = request.getUserId();
        try {
            ValidationUtil.validateUserId(userId);
        } catch (IllegalArgumentException ex) {
            BadRequestErrorResponse error = new BadRequestErrorResponse();
            error.setMessage(ex.getMessage());
            BadRequestErrorResponse.Detail detail = new BadRequestErrorResponse.Detail();
            detail.setField("userId");
            detail.setMessage(ex.getMessage());
            detail.setType("pattern");
            error.setDetails(java.util.Collections.singletonList(detail));
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        org.banking.service.model.UserResponse user = userService.fetchUser(userId);
        if (user == null) {
            ErrorResponse error = new ErrorResponse();
            error.setMessage("User not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        String token = JwtUtil.generateToken(userId);
        return ResponseEntity.ok(new LoginResponse(token));
    }

    /**
     * Issues a JWT for any syntactically valid userId (for testing purposes).
     * @param request the login request containing userId
     * @return the login response with JWT or error
     */
    @PostMapping("/test-jwt")
    public ResponseEntity<?> generateTestJwt(@RequestBody LoginRequest request) {
        String userId = request.getUserId();
        try {
            ValidationUtil.validateUserId(userId);
        } catch (IllegalArgumentException ex) {
            BadRequestErrorResponse error = new BadRequestErrorResponse();
            error.setMessage(ex.getMessage());
            BadRequestErrorResponse.Detail detail = new BadRequestErrorResponse.Detail();
            detail.setField("userId");
            detail.setMessage(ex.getMessage());
            detail.setType("pattern");
            error.setDetails(java.util.Collections.singletonList(detail));
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        String token = JwtUtil.generateToken(userId);
        return ResponseEntity.ok(new LoginResponse(token));
    }
} 