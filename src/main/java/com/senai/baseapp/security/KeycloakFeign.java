package com.senai.baseapp.security;


import com.senai.baseapp.domain.usuario.TokenDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "keycloak", url = "${keycloak.auth-server-url}/protocol/openid-connect")
public interface KeycloakFeign {

    @PostMapping(value = "/token", consumes = "application/x-www-form-urlencoded")
    TokenDTO login(@RequestBody Map<String, ?> formParams);
}
