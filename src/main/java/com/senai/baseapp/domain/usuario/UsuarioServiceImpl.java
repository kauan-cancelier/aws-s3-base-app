package com.senai.baseapp.domain.usuario;

import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository repo;

    public UsuarioServiceImpl(UsuarioRepository repo) {
        this.repo = repo;
    }

    @Override
    public Usuario salvar(Usuario usuario) {
        return repo.save(usuario);
    }

}
