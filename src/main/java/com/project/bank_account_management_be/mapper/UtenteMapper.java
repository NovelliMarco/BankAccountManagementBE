package com.project.bank_account_management_be.mapper;


import com.project.bank_account_management_be.dto.UtenteDTO;
import com.project.bank_account_management_be.entity.Utente;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UtenteMapper {

    UtenteMapper INSTANCE = Mappers.getMapper(UtenteMapper.class);

    @Mapping(source = "utenteId", target = "id")
    UtenteDTO toDTO(Utente utente);


    @Mapping(source = "id", target = "utenteId")
    Utente toEntity(UtenteDTO dto);
}
