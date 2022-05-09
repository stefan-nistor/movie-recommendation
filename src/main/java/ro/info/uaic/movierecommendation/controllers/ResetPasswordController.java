package ro.info.uaic.movierecommendation.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.info.uaic.movierecommendation.dtoresponses.PasswordDTO;
import ro.info.uaic.movierecommendation.exceptions.InvalidPasswordException;
import ro.info.uaic.movierecommendation.exceptions.UserNotFoundException;
import ro.info.uaic.movierecommendation.services.ResetPasswordService;

@RestController
public class ResetPasswordController {

    @Autowired
    ResetPasswordService resetPasswordService;

    @PostMapping("/api/v1/reset-password/{email}")
    public ResponseEntity<?> sendResetToken(@PathVariable String email) throws UserNotFoundException {
        resetPasswordService.sendTokenToEmail(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/v1/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordDTO passwordDTO) throws InvalidPasswordException {
        resetPasswordService.resetPassword(passwordDTO);
        return ResponseEntity.ok().build();
    }

}
