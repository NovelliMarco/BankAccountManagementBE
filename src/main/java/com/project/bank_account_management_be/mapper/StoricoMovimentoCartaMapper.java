package com.project.bank_account_management_be.mapper;

import com.project.bank_account_management_be.dto.StoricoMovimentoCartaDTO;
import com.project.bank_account_management_be.entity.StoricoMovimentoCarta;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface StoricoMovimentoCartaMapper {

    StoricoMovimentoCartaMapper INSTANCE = Mappers.getMapper(StoricoMovimentoCartaMapper.class);

    @Mapping(source = "movimentoID", target = "id")
    @Mapping(source = "carta.cartaId", target = "cartaId")
    @Mapping(source = "tipoMovimento.descrizione", target = "tipoMovimento")
    StoricoMovimentoCartaDTO toDTO(StoricoMovimentoCarta movimento);

    @Mapping(source = "id", target = "movimentoID")
    @Mapping(target = "carta", ignore = true)
    @Mapping(target = "tipoMovimento", ignore = true)
    @Mapping(target = "dataMovimento", ignore = true)
    @Mapping(target = "movimenti", ignore = true)
    StoricoMovimentoCarta toEntity(StoricoMovimentoCartaDTO dto);
}