package com.senai.baseapp.controller;

import com.senai.baseapp.domain.usuario.*;
import com.senai.baseapp.security.KeycloakAdminClient;
import com.senai.baseapp.security.KeycloakFeign;
import com.senai.baseapp.security.KeycloakTokenClient;
import com.senai.baseapp.security.KeycloakMasterTokenClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.admin-username}")
    private String adminUsername;

    @Value("${keycloak.admin-password}")
    private String adminPassword;

    private final KeycloakFeign keycloakFeign;
    private final KeycloakAdminClient keycloakAdminClient;
    private final KeycloakTokenClient keycloakTokenClient;
    private final KeycloakMasterTokenClient keycloakMasterTokenClient;

    public AuthController(KeycloakFeign keycloakFeign, KeycloakAdminClient keycloakAdminClient, KeycloakTokenClient keycloakTokenClient, KeycloakMasterTokenClient keycloakMasterTokenClient) {
        this.keycloakFeign = keycloakFeign;
        this.keycloakAdminClient = keycloakAdminClient;
        this.keycloakTokenClient = keycloakTokenClient;
        this.keycloakMasterTokenClient = keycloakMasterTokenClient;
    }

    @PostMapping("/login")
    public ResponseEntity<?> auth(@RequestBody LoginDTO login) {
        Map<String, String> form = new HashMap<>();
        form.put("client_id", clientId);
        form.put("client_secret", clientSecret);
        form.put("username", login.getUsername());
        form.put("password", login.getSenha());
        form.put("grant_type", "password");

        TokenDTO token = keycloakFeign.login(form);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UsuarioRegisterDto registerDto) {
        try {
            // Obter token de admin do Keycloak
            MultiValueMap<String, String> adminTokenForm = new LinkedMultiValueMap<>();
            adminTokenForm.add("client_id", "admin-cli");
            adminTokenForm.add("username", adminUsername);
            adminTokenForm.add("password", adminPassword);
            adminTokenForm.add("grant_type", "password");

            KeycloakTokenResponseDTO adminToken = keycloakMasterTokenClient.getToken(adminTokenForm);
            String bearerToken = "Bearer " + adminToken.getAccessToken();

            // Criar usuário no Keycloak
            KeycloakUserRequestDTO keycloakUser = new KeycloakUserRequestDTO(
                registerDto.getEmail(), 
                registerDto.getNomeCompleto(), 
                registerDto.getSenha()
            );
            
            keycloakAdminClient.createUser(bearerToken, keycloakUser);
            
            return ResponseEntity.ok("Usuário criado com sucesso no Keycloak");
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao criar usuário: " + e.getMessage());
        }
    }
}
