package com.project.bank_account_management_be.mapper;

import com.project.bank_account_management_be.dto.TipologiaMovimentoDTO;
import com.project.bank_account_management_be.entity.TipologiaMovimento;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TipologiaMovimentoMapper {

    TipologiaMovimentoMapper INSTANCE = Mappers.getMapper(TipologiaMovimentoMapper.class);

    @Mapping(source = "tipoMovimentoId", target = "id")
    TipologiaMovimentoDTO toDTO(TipologiaMovimento tipologia);

    @Mapping(source = "id", target = "tipoMovimentoId")
    @Mapping(target = "movimenti", ignore = true)
    @Mapping(target = "dataCreazione", ignore = true)
    TipologiaMovimento toEntity(TipologiaMovimentoDTO dto);
}