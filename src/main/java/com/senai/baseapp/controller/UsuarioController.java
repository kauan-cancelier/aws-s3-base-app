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

    @PostMapping
    public ResponseEntity<UsuarioDto> criarConta(@RequestBody UsuarioDto dadosCriacao) {
        UsuarioDto resultado = usuarioComponent.criarUsuarioComFoto(dadosCriacao);
        return ResponseEntity.ok(resultado);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UsuarioDto> criarContaComFoto(@RequestPart("nomeCompleto") String nomeCompleto, @RequestPart("email") String email, @RequestPart("senha") String senha, @RequestPart(value = "file", required = false) MultipartFile arquivo) {
        UsuarioDto usuarioDto = construirUsuarioDtoComFoto(nomeCompleto, email, senha, arquivo);
        UsuarioDto resultado = usuarioComponent.criarUsuarioComFoto(usuarioDto);
        return ResponseEntity.ok(resultado);
    }


    @PostMapping("/{id}/foto")
    public ResponseEntity<String> fazerUploadFoto(
            @PathVariable String id, 
            @RequestParam("file") MultipartFile arquivo) {
        
        String chaveFoto = usuarioComponent.fazerUploadFotoUsuario(id, arquivo);
        return ResponseEntity.ok(chaveFoto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDto> buscarUsuarioPorId(@PathVariable String id) {
        UsuarioDto usuarioDto = usuarioComponent.buscarUsuarioPorId(id);
        return ResponseEntity.ok(usuarioDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDto> atualizarUsuario(
            @PathVariable String id, 
            @Valid @RequestBody ContaInicialDto dadosAtualizados) {
        
        UsuarioDto usuarioDto = usuarioComponent.atualizarDadosUsuario(id, dadosAtualizados);
        return ResponseEntity.ok(usuarioDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirUsuario(@PathVariable String id) {
        usuarioService.deleteById(UUID.fromString(id));
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<UsuarioDto>> listarUsuarios(@PageableDefault Pageable paginacao) {
        Page<Usuario> usuarios = usuarioService.listarPagina(paginacao);
        Page<UsuarioDto> usuariosDto = usuarios.map(usuarioMapper::toDTO);
        return ResponseEntity.ok(usuariosDto);
    }

    private UsuarioDto construirUsuarioDtoComFoto(String nomeCompleto, String email, String senha, MultipartFile arquivo) {
        return UsuarioDto.builder()
                .nomeCompleto(nomeCompleto)
                .email(email)
                .senha(senha)
                .file(arquivo)
                .build();
    }
}
