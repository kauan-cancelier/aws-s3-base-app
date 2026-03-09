package com.senai.baseapp.component;

import com.senai.baseapp.aws.S3Service;
import com.senai.baseapp.domain.usuario.Usuario;
import com.senai.baseapp.domain.usuario.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class FotoUploadComponent {

    private final S3Service s3Service;
    private final UsuarioService usuarioService;

    public void atualizarFotoPerfil(MultipartFile arquivo, String idUsuario) {
        String chaveFoto = realizarUpload(arquivo, idUsuario);
        
        Usuario usuario = usuarioService.findById(UUID.fromString(idUsuario));
        usuario.setFotoKey(chaveFoto);
        usuarioService.salvar(usuario);
    }

    public String fazerUploadFoto(MultipartFile arquivo, String idUsuario) {
        return realizarUpload(arquivo, idUsuario);
    }

    private String realizarUpload(MultipartFile arquivo, String idUsuario) {
        validarArquivo(arquivo);
        return s3Service.uploadFile(arquivo, idUsuario);
    }

    private void validarArquivo(MultipartFile arquivo) {
        if (arquivo == null || arquivo.isEmpty()) {
            throw new IllegalArgumentException("Arquivo não pode ser nulo ou vazio");
        }
    }
}
