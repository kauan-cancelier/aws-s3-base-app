package com.senai.baseapp.domain.usuario;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ContaInicialDto {
    private String nomeCompleto;
    private String email;
    private String senha;
}
