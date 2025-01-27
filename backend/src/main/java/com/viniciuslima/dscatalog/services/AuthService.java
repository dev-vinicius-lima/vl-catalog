package com.viniciuslima.dscatalog.services;

import com.viniciuslima.dscatalog.dto.EmailUserDTO;
import com.viniciuslima.dscatalog.entities.PasswordRecover;
import com.viniciuslima.dscatalog.entities.User;
import com.viniciuslima.dscatalog.repositories.PasswordRecoverRepository;
import com.viniciuslima.dscatalog.repositories.UserRepository;
import com.viniciuslima.dscatalog.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordRecoverRepository repository;

    @Autowired
    private EmailService emailService;

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

        String stringFormat = "Olá, " + user.getFirstName() + ".\n\n" + "Você solicitou a recuperação de senha para a sua conta de email " + user.getEmail() + " Clique no link abaixo para alterar a sua senha: \n\n" + recoverURI + token + "Seu token expirará em " + tokenMinutes + " Minutos" + "\n\n" + " Se você não solicitou a recuperação de senha, por favor, ignore este e-mail.\n\n";

        PasswordRecover entity = new PasswordRecover();
        entity.setEmail(user.getEmail());
        entity.setToken(token);
        entity.setExpiration(Instant.now().plusSeconds(tokenMinutes * 60));
        entity = repository.save(entity);

        emailService.sendEmail(entity.getEmail(), "Recuperação de senha", stringFormat);

    }
}
