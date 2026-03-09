package com.senai.baseapp.component;

import com.senai.baseapp.aws.S3Service;
import com.senai.baseapp.domain.usuario.Usuario;
import com.senai.baseapp.domain.usuario.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
@Slf4j
public class PhotoUploadComponent {

    private final S3Service s3Service;
    private final UsuarioService usuarioService;

    public void uploadPhotoAndUpdateUser(MultipartFile file, String userId) {
        String photoKey = s3Service.uploadFile(file, userId);
        
        Usuario usuario = usuarioService.findById(java.util.UUID.fromString(userId));
        usuario.setFotoKey(photoKey);
        usuarioService.salvar(usuario);
        
        log.info("Profile photo uploaded for user: {}", userId);
    }

    public String uploadPhotoOnly(MultipartFile file, String userId) {
        return s3Service.uploadFile(file, userId);
    }
}
