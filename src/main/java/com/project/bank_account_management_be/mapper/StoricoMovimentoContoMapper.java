package com.project.bank_account_management_be.mapper;

import com.project.bank_account_management_be.dto.StoricoMovimentoContoDTO;
import com.project.bank_account_management_be.entity.StoricoMovimentoConto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface StoricoMovimentoContoMapper {

    StoricoMovimentoContoMapper INSTANCE = Mappers.getMapper(StoricoMovimentoContoMapper.class);

    @Mapping(source = "movimentoId", target = "id")
    @Mapping(source = "tipologiaMovimento.descrizione", target = "tipoMovimento")
    @Mapping(source = "tipologiaMovimento.direzione", target = "direzioneMovimento")
    StoricoMovimentoContoDTO toDTO(StoricoMovimentoConto movimento);
}