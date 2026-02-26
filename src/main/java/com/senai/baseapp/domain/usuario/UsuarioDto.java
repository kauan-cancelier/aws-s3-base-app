package com.senai.baseapp.domain.usuario;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class UsuarioDto {
    private UUID id;
    private String nomeCompleto;
    private String email;
}
