package com.senai.baseapp.domain.usuario;

import org.springframework.stereotype.Component;

@Component
public class ContaInicialMapper {

    public static Usuario contaInicialParaUsuario(ContaInicialDto contaInicialDto) {
        return Usuario.builder()
                .email(contaInicialDto.getEmail())
                .senha(contaInicialDto.getSenha())
                .nomeCompleto(contaInicialDto.getNomeCompleto())
                .build();
    }

}
