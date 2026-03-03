package com.senai.baseapp.domain.usuario;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDto {
    private UUID id;
    private String nomeCompleto;
    private String email;
}
