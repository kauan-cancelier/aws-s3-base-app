package com.senai.baseapp.domain.usuario;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository repo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Usuario salvar(Usuario usuario) {
        try {
            if (usuario.getSenha() != null && !usuario.getSenha().startsWith("$2a$")) {
                usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
                log.debug("Password encoded for user: {}", usuario.getEmail());
            }
            
            Usuario savedUsuario = repo.save(usuario);
            log.info("User saved successfully: {}", savedUsuario.getEmail());
            return savedUsuario;
            
        } catch (DataIntegrityViolationException ex) {
            log.error("Error saving user - email already exists: {}", usuario.getEmail(), ex);
            throw new RuntimeException("Email já cadastrado no sistema", ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Usuario> listarPagina(Pageable pageable) {
        log.debug("Fetching users page: {}", pageable);
        return repo.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario findById(UUID id) {
        return repo.findById(id)
                .orElseThrow(() -> {
                    log.error("User not found with id: {}", id);
                    return new RuntimeException("Usuário não encontrado com ID: " + id);
                });
    }

    @Override
    public void deleteById(UUID id) {
        if (!repo.existsById(id)) {
            log.error("Attempted to delete non-existent user with id: {}", id);
            throw new RuntimeException("Usuário não encontrado com ID: " + id);
        }
        
        repo.deleteById(id);
        log.info("User deleted successfully: {}", id);
    }

}
