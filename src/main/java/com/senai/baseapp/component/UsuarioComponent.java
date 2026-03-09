package com.senai.baseapp.component;

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
    private final FotoUploadComponent fotoUploadComponent;

    public UsuarioDto criarUsuarioComFoto(UsuarioDto usuarioDto) {
        ContaInicialDto dadosConta = converterParaContaInicial(usuarioDto);
        Usuario usuario = salvarNovoUsuario(dadosConta);
        
        if (possuiFoto(usuarioDto)) {
            fotoUploadComponent.atualizarFotoPerfil(usuarioDto.getFile(), usuario.getId().toString());
        }

        return converterParaDtoComFoto(usuario);
    }

    public String fazerUploadFotoUsuario(String idUsuario, MultipartFile arquivo) {
        return fotoUploadComponent.fazerUploadFoto(arquivo, idUsuario);
    }

    public UsuarioDto buscarUsuarioPorId(String idUsuario) {
        Usuario usuario = usuarioService.findById(UUID.fromString(idUsuario));
        return converterParaDtoComFoto(usuario);
    }

    public UsuarioDto atualizarDadosUsuario(String idUsuario, ContaInicialDto dadosAtualizados) {
        log.info("Atualizando dados do usuário: {}", idUsuario);
        
        Usuario usuario = usuarioService.findById(UUID.fromString(idUsuario));
        atualizarCamposUsuario(usuario, dadosAtualizados);
        
        Usuario usuarioAtualizado = usuarioService.salvar(usuario);
        return converterParaDtoComFoto(usuarioAtualizado);
    }

    private ContaInicialDto converterParaContaInicial(UsuarioDto usuarioDto) {
        return ContaInicialDto.builder()
                .nomeCompleto(usuarioDto.getNomeCompleto())
                .email(usuarioDto.getEmail())
                .senha(usuarioDto.getSenha())
                .build();
    }

    private Usuario salvarNovoUsuario(ContaInicialDto dadosConta) {
        return usuarioService.salvar(ContaInicialMapper.contaInicialParaUsuario(dadosConta));
    }

    private boolean possuiFoto(UsuarioDto usuarioDto) {
        return usuarioDto.getFile() != null && !usuarioDto.getFile().isEmpty();
    }

    private UsuarioDto converterParaDtoComFoto(Usuario usuario) {
        UsuarioDto usuarioDto = usuarioMapper.toDTO(usuario);
        adicionarChaveFotoSeExistir(usuario, usuarioDto);
        return usuarioDto;
    }

    private void atualizarCamposUsuario(Usuario usuario, ContaInicialDto dados) {
        usuario.setNomeCompleto(dados.getNomeCompleto());
        usuario.setEmail(dados.getEmail());
        
        if (possuiSenha(dados)) {
            usuario.setSenha(dados.getSenha());
        }
    }

    private boolean possuiSenha(ContaInicialDto dados) {
        return dados.getSenha() != null && !dados.getSenha().isBlank();
    }

    private void adicionarChaveFotoSeExistir(Usuario usuario, UsuarioDto usuarioDto) {
        if (usuario.getFotoKey() != null && !usuario.getFotoKey().isBlank()) {
            usuarioDto.setFotoKey(usuario.getFotoKey());
        }
    }
}
