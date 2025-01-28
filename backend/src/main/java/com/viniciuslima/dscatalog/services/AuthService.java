package com.viniciuslima.dscatalog.services;

import com.viniciuslima.dscatalog.dto.EmailUserDTO;
import com.viniciuslima.dscatalog.dto.NewPasswordDTO;
import com.viniciuslima.dscatalog.entities.PasswordRecover;
import com.viniciuslima.dscatalog.entities.User;
import com.viniciuslima.dscatalog.repositories.PasswordRecoverRepository;
import com.viniciuslima.dscatalog.repositories.UserRepository;
import com.viniciuslima.dscatalog.services.exceptions.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordRecoverRepository repository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${email.password-recover.token.minutes}")
    private Long tokenMinutes;

    @Value("${email.password-recover.uri}")
    private String recoverURI;


    @Transactional
    public void createRecoverToken(EmailUserDTO body) {

        User user = userRepository.findByEmail(body.getEmail());
        if (user == null) {
            throw new ResourceNotFoundException("Email not found");
        }

        String token = UUID.randomUUID().toString();

        String stringFormat = "Olá, " + user.getFirstName() + ".\n\n" + "Você solicitou a recuperação de senha para a sua conta de email " + user.getEmail() + " Clique no link abaixo para alterar a sua senha: \n\n" + recoverURI + token + " Seu token expirará em " + tokenMinutes + " Minutos" + "\n\n" + " Se você não solicitou a recuperação de senha, por favor, ignore este e-mail.\n\n";

        PasswordRecover entity = new PasswordRecover();
        entity.setEmail(user.getEmail());
        entity.setToken(token);
        entity.setExpiration(Instant.now().plusSeconds(tokenMinutes * 60));
        entity = repository.save(entity);

        emailService.sendEmail(entity.getEmail(), "Recuperação de senha", stringFormat);

    }

    @Transactional
    public void saveNewPassword(NewPasswordDTO body) {
        List<PasswordRecover> result = repository.searchValidTokens(body.getToken(), Instant.now());
        if (result.isEmpty()) {
            throw new ResourceNotFoundException("Token inválido ou expirado");
        }
        User user = userRepository.findByEmail(result.get(0).getEmail());
        user.setPassword(passwordEncoder.encode(body.getPassword()));
        user = userRepository.save(user);
    }

    protected User authenticated() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Jwt jwtPrincipal = (Jwt) authentication.getPrincipal();
            String username = jwtPrincipal.getClaim("username");
            return userRepository.findByEmail(username);
        } catch (Exception e) {
            throw new UsernameNotFoundException("Invalid user");
        }
    }

}
