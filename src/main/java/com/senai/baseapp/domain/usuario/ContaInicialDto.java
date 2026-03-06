package com.senai.baseapp.domain.usuario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContaInicialDto {
    private String nomeCompleto;
    private String email;
    private String senha;
}
