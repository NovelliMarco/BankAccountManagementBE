package com.project.bank_account_management_be.mapper;

import com.project.bank_account_management_be.dto.LoginDTO;
import com.project.bank_account_management_be.entity.Login;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface LoginMapper {

    LoginMapper INSTANCE = Mappers.getMapper(LoginMapper.class);

    @Mapping(source = "loginId", target = "id")
    @Mapping(source = "passwordHash", target = "password")
    LoginDTO toDTO(Login login);

    @Mapping(source = "id", target = "loginId")
    @Mapping(source = "password", target = "passwordHash")
    @Mapping(target = "utente", ignore = true)
    @Mapping(target = "ultimoAccesso", ignore = true)
    Login toEntity(LoginDTO dto);
}