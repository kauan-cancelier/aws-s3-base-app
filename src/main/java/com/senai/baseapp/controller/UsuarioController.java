package com.senai.baseapp.controller;

import com.senai.baseapp.aws.S3Service;
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
    private final S3Service s3Service;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UsuarioDto> criarConta(
            @RequestPart("nomeCompleto") String nomeCompleto,
            @RequestPart("email") String email,
            @RequestPart("senha") String senha,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        
        log.info("Creating new user account for email: {}", email);
        
        ContaInicialDto contaInicialDto = ContaInicialDto.builder()
                .nomeCompleto(nomeCompleto)
                .email(email)
                .senha(senha)
                .build();

        var usuario = usuarioService.salvar(ContaInicialMapper.contaInicialParaUsuario(contaInicialDto));

        if (file != null && !file.isEmpty()) {
            String photoKey = s3Service.uploadFile(file, usuario.getId().toString());
            usuario.setFotoKey(photoKey);
            usuario = usuarioService.salvar(usuario);
            log.info("Profile photo uploaded for user: {}", usuario.getId());
        }

        UsuarioDto usuarioDto = usuarioMapper.toDTO(usuario);
        setPhotoKeyIfExists(usuario, usuarioDto);
        
        log.info("User account created successfully for email: {}", email);
        return ResponseEntity.ok(usuarioDto);
    }


    @PostMapping("/{id}/photo")
    public ResponseEntity<String> uploadPhoto(@PathVariable String id, @RequestParam("file") MultipartFile file) {
        String key = s3Service.uploadFile(file, id);
        return ResponseEntity.ok(key);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDto> getUsuario(@PathVariable String id) {
        var usuario = usuarioService.findById(UUID.fromString(id));
        UsuarioDto usuarioDto = usuarioMapper.toDTO(usuario);
        setPhotoKeyIfExists(usuario, usuarioDto);
        return ResponseEntity.ok(usuarioDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDto> updateUsuario(
            @PathVariable String id, 
            @Valid @RequestBody ContaInicialDto contaInicialDto) {
        
        log.info("Updating user account for id: {}", id);
        
        var usuario = usuarioService.findById(UUID.fromString(id));
        usuario.setNomeCompleto(contaInicialDto.getNomeCompleto());
        usuario.setEmail(contaInicialDto.getEmail());
        
        if (contaInicialDto.getSenha() != null && !contaInicialDto.getSenha().isBlank()) {
            usuario.setSenha(contaInicialDto.getSenha());
        }
        
        var updatedUsuario = usuarioService.salvar(usuario);
        UsuarioDto usuarioDto = usuarioMapper.toDTO(updatedUsuario);
        setPhotoKeyIfExists(updatedUsuario, usuarioDto);
        
        log.info("User account updated successfully for id: {}", id);
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

    private void setPhotoKeyIfExists(Usuario usuario, UsuarioDto usuarioDto) {
        if (usuario.getFotoKey() != null && !usuario.getFotoKey().isBlank()) {
            usuarioDto.setFotoKey(usuario.getFotoKey());
        }
    }

}
