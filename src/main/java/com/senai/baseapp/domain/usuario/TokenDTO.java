package com.senai.baseapp.domain.usuario;

import lombok.Data;

@Data
public class TokenDTO {

    private String access_token;
    private String refresh_token;
    private String token_type;
    private int expires_in;
    private int refresh_expires_in;
    private String scope;
}
