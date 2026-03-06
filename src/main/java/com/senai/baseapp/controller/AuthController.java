package com.senai.baseapp.controller;

import com.senai.baseapp.controller.dto.AuthResponse;
import com.senai.baseapp.controller.dto.LoginRequest;
import com.senai.baseapp.domain.usuario.Usuario;
import com.senai.baseapp.domain.usuario.UsuarioRepository;
import com.senai.baseapp.security.JwtTokenProvider;
import com.senai.baseapp.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UsuarioRepository usuarioRepository;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("Authentication attempt for email: {}", loginRequest.getEmail());
        
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getSenha()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = tokenProvider.generateToken(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            Usuario usuario = usuarioRepository.findById(userDetails.getId())
                    .orElseThrow(() -> new RuntimeException("User not found after authentication"));

            AuthResponse response = new AuthResponse(jwt, usuario.getId(), usuario.getEmail());
            log.info("Authentication successful for email: {}", loginRequest.getEmail());
            
            return ResponseEntity.ok(response);
            
        } catch (BadCredentialsException ex) {
            log.warn("Invalid credentials for email: {}", loginRequest.getEmail());
            throw new BadCredentialsException("Email ou senha inválidos");
        } catch (AuthenticationException ex) {
            log.error("Authentication failed for email: {}", loginRequest.getEmail(), ex);
            throw new BadCredentialsException("Erro na autenticação");
        }
    }

}
