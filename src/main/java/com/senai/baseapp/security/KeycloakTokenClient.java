package com.senai.baseapp.security;

import com.senai.baseapp.domain.usuario.KeycloakTokenResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "keycloak-token", url = "${keycloak.auth-server-url}/protocol/openid-connect")
public interface KeycloakTokenClient {

    @PostMapping(value = "/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    KeycloakTokenResponseDTO getToken(@RequestBody MultiValueMap<String, String> formParams);
}
