package com.senai.baseapp.controller;

import com.senai.baseapp.domain.usuario.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioMapper usuarioMapper;


    public UsuarioController(UsuarioService usuarioService,
                             UsuarioMapper usuarioMapper) {
        this.usuarioService = usuarioService;
        this.usuarioMapper = usuarioMapper;
    }

    @PostMapping
    public ResponseEntity<UsuarioDto> criarConta(@RequestBody ContaInicialDto contaInicialDto) {
        var usuario = usuarioService.salvar(ContaInicialMapper.contaInicialParaUsuario(contaInicialDto));
        return ResponseEntity.ok(usuarioMapper.toDTO(usuario));
    }

}
