package com.project.bank_account_management_be.mapper;


import com.project.bank_account_management_be.dto.CartaDTO;
import com.project.bank_account_management_be.entity.Carta;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CartaMapper {

    CartaMapper INSTANCE = Mappers.getMapper(CartaMapper.class);

    @Mapping(source = "cartaId", target = "id")
    CartaDTO toDTO(Carta carta);
}
