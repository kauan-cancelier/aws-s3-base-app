package com.senai.baseapp.component;

import com.senai.baseapp.domain.usuario.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class UsuarioComponent {

    private final UsuarioService usuarioService;
    private final UsuarioMapper usuarioMapper;
    private final PhotoUploadComponent photoUploadComponent;

    public UsuarioDto criarUsuarioComFoto(UsuarioDto usuarioDto) {
        ContaInicialDto contaInicialDto = ContaInicialDto.builder()
                .nomeCompleto(usuarioDto.getNomeCompleto())
                .email(usuarioDto.getEmail())
                .senha(usuarioDto.getSenha())
                .build();

        var usuario = usuarioService.salvar(ContaInicialMapper.contaInicialParaUsuario(contaInicialDto));

        if (usuarioDto.getFile() != null && !usuarioDto.getFile().isEmpty()) {
            photoUploadComponent.uploadPhotoAndUpdateUser(usuarioDto.getFile(), usuario.getId().toString());
        }

        UsuarioDto result = usuarioMapper.toDTO(usuario);
        setPhotoKeyIfExists(usuario, result);
        
        log.info("User account created successfully for email: {}", usuarioDto.getEmail());
        return result;
    }

    public String uploadFotoUsuario(String id, org.springframework.web.multipart.MultipartFile file) {
        return photoUploadComponent.uploadPhotoOnly(file, id);
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
