package com.project.bank_account_management_be.mapper;

import com.project.bank_account_management_be.dto.TransazioneContoDTO;
import com.project.bank_account_management_be.entity.TransazioneConto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TransazioneContoMapper {

    TransazioneContoMapper INSTANCE = Mappers.getMapper(TransazioneContoMapper.class);

    @Mapping(source = "transazioneId", target = "id")
    @Mapping(source = "tipologiaTransazione.descrizione", target = "tipoTransazione")
    TransazioneContoDTO toDTO(TransazioneConto transazione);
}