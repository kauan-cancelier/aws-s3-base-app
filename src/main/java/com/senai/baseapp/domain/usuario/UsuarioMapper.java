package com.senai.baseapp.domain.usuario;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    UsuarioDto toDTO(Usuario usuario);

    Usuario toEntity(UsuarioDto dto);

}