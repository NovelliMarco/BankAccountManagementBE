package com.project.bank_account_management_be.mapper;

import com.project.bank_account_management_be.dto.DocumentoIdentitaDTO;
import com.project.bank_account_management_be.entity.DocumentoIdentita;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring")
public interface DocumentoIdentitaMapper {

    DocumentoIdentitaMapper INSTANCE = Mappers.getMapper(DocumentoIdentitaMapper.class);

    @Mapping(source = "documentoId", target = "id")
    @Mapping(source = "utente.utenteId", target = "utenteId")
    DocumentoIdentitaDTO toDTO(DocumentoIdentita documento);

    @Mapping(source = "id", target = "documentoId")
    @Mapping(target = "utente", ignore = true)
    @Mapping(target = "dataCreazione", ignore = true)
    DocumentoIdentita toEntity(DocumentoIdentitaDTO dto);
}