package com.senai.baseapp.domain.usuario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UsuarioService {

    Usuario salvar(Usuario usuario);

    Page<Usuario> listarPagina(Pageable pageable);

    Usuario findById(UUID id);

    void deleteById(UUID id);

}
