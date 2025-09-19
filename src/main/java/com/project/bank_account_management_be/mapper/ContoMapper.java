package com.project.bank_account_management_be.mapper;

import com.project.bank_account_management_be.dto.CartaDTO;
import com.project.bank_account_management_be.dto.ContoDTO;
import com.project.bank_account_management_be.entity.Carta;
import com.project.bank_account_management_be.entity.Conto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ContoMapper {
    ContoMapper INSTANCE = Mappers.getMapper(ContoMapper.class);

    @Mapping(source = "contoId", target = "id")
    ContoDTO toDTO(Conto carta);
}
