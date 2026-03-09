package com.senai.baseapp.component;

import com.senai.baseapp.aws.S3Service;
import com.senai.baseapp.domain.usuario.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class UsuarioComponent {

    private final UsuarioService usuarioService;
    private final UsuarioMapper usuarioMapper;
    private final S3Service s3Service;

    public UsuarioDto criarUsuarioComFoto(String nomeCompleto, String email, String senha, MultipartFile file) {
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
        return usuarioDto;
    }

    public String uploadFotoUsuario(String id, MultipartFile file) {
        return s3Service.uploadFile(file, id);
    }

    public UsuarioDto obterUsuarioComFoto(String id) {
        var usuario = usuarioService.findById(UUID.fromString(id));
        UsuarioDto usuarioDto = usuarioMapper.toDTO(usuario);
        setPhotoKeyIfExists(usuario, usuarioDto);
        return usuarioDto;
    }

    public UsuarioDto atualizarUsuarioComFoto(String id, ContaInicialDto contaInicialDto) {
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
        return usuarioDto;
    }

    private void setPhotoKeyIfExists(Usuario usuario, UsuarioDto usuarioDto) {
        if (usuario.getFotoKey() != null && !usuario.getFotoKey().isBlank()) {
            usuarioDto.setFotoKey(usuario.getFotoKey());
        }
    }
}
