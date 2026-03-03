package com.senai.baseapp.security;

import com.senai.baseapp.domain.usuario.KeycloakClientResponseDTO;
import com.senai.baseapp.domain.usuario.KeycloakRoleResponseDTO;
import com.senai.baseapp.domain.usuario.KeycloakUserRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name = "keycloak-admin", url = "${keycloak.admin-server-url}")
public interface KeycloakAdminClient {

    @PostMapping("/users")
    ResponseEntity<Void> createUser(@RequestHeader("Authorization") String token, @RequestBody KeycloakUserRequestDTO user);

    @GetMapping("/clients")
    List<KeycloakClientResponseDTO> getClients(@RequestHeader("Authorization") String token, @RequestParam("clientId") String clientId);

    @GetMapping("/clients/{clientUuid}/roles")
    List<KeycloakRoleResponseDTO> getClientRoles(@RequestHeader("Authorization") String token, @PathVariable String clientUuid);

    @PostMapping("/users/{userId}/role-mappings/clients/{clientUuid}")
    ResponseEntity<Void> assignClientRoles(@RequestHeader("Authorization") String token, @PathVariable String userId, @PathVariable String clientUuid, @RequestBody List<Map<String, String>> roles);

}
