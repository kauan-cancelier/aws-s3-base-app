package com.senai.baseapp.domain.usuario;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class KeycloakUserRequestDTO {

    private String username;
    private Boolean enabled;
    private String email;
    private String firstName;
    private Boolean emailVerified;
    private List<CredentialRepresentation> credentials;

    public KeycloakUserRequestDTO(String email, String nome, String senha) {
        this.username = email;
        this.enabled = true;
        this.email = email;
        this.firstName = nome;
        this.emailVerified = true;
        this.credentials = List.of(new CredentialRepresentation("password", senha, false));
    }
}
