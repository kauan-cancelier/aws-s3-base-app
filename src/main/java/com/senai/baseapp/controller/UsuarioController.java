package com.senai.baseapp.controller;

import com.senai.baseapp.component.UsuarioComponent;
import com.senai.baseapp.domain.usuario.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
@Slf4j
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioMapper usuarioMapper;
    private final UsuarioComponent usuarioComponent;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UsuarioDto> criarConta(@RequestPart("nomeCompleto") String nomeCompleto, @RequestPart("email") String email, @RequestPart("senha") String senha, @RequestPart(value = "file", required = false) MultipartFile file) {
        UsuarioDto usuarioDto = usuarioComponent.criarUsuarioComFoto(nomeCompleto, email, senha, file);
        return ResponseEntity.ok(usuarioDto);
    }


    @PostMapping("/{id}/photo")
    public ResponseEntity<String> uploadPhoto(@PathVariable String id, @RequestParam("file") MultipartFile file) {
        String key = usuarioComponent.uploadFotoUsuario(id, file);
        return ResponseEntity.ok(key);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDto> getUsuario(@PathVariable String id) {
        UsuarioDto usuarioDto = usuarioComponent.obterUsuarioComFoto(id);
        return ResponseEntity.ok(usuarioDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDto> updateUsuario(
            @PathVariable String id, 
            @Valid @RequestBody ContaInicialDto contaInicialDto) {
        
        UsuarioDto usuarioDto = usuarioComponent.atualizarUsuarioComFoto(id, contaInicialDto);
        return ResponseEntity.ok(usuarioDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable String id) {
        usuarioService.deleteById(UUID.fromString(id));
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<UsuarioDto>> listarPagina(@PageableDefault Pageable pageable) {
        Page<Usuario> usuarios = usuarioService.listarPagina(pageable);
        Page<UsuarioDto> usuariosDto = usuarios.map(usuarioMapper::toDTO);
        return ResponseEntity.ok(usuariosDto);
    }

}
