package com.senai.baseapp.domain.usuario;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CredentialRepresentation {

    private String type;
    private String value;
    private Boolean temporary;

    public CredentialRepresentation(String type, String value, Boolean temporary) {
        this.type = type;
        this.value = value;
        this.temporary = temporary;
    }
}
