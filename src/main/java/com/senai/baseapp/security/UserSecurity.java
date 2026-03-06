package com.senai.baseapp.security;

import com.senai.baseapp.domain.usuario.Usuario;
import com.senai.baseapp.domain.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component("userSecurity")
public class UserSecurity {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public boolean isOwner(String userId, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        
        try {
            UUID userUuid = UUID.fromString(userId);
            Usuario usuario = usuarioRepository.findById(userUuid).orElse(null);
            
            return usuario != null && usuario.getEmail().equals(userDetails.getUsername());
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
