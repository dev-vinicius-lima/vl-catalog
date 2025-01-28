package com.viniciuslima.dscatalog.resources;

import com.viniciuslima.dscatalog.dto.EmailUserDTO;
import com.viniciuslima.dscatalog.dto.NewPasswordDTO;
import com.viniciuslima.dscatalog.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthResource {

    @Autowired
    private AuthService authService;

    @PostMapping("/recover-token")
    public ResponseEntity<Void> createRecoverToken(@Valid @RequestBody EmailUserDTO body) {
        authService.createRecoverToken(body);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/new-password")
    public ResponseEntity<Void> saveNewPassword(@Valid @RequestBody NewPasswordDTO body) {
        authService.saveNewPassword(body);
        return ResponseEntity.noContent().build();
    }


}
