package com.senai.baseapp.domain.usuario;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KeycloakTokenRequestDTO {

    private String clientId;
    private String clientSecret;
    private String grantType;
}
