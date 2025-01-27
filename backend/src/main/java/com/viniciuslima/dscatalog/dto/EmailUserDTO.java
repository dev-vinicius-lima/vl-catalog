package com.viniciuslima.dscatalog.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class EmailUserDTO {

    @NotBlank(message = "O campo e-mail é obrigatório")
    @Email(message = "Email informado não é válido")
    private String email;

    public EmailUserDTO() {
    }

    public EmailUserDTO(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

}
