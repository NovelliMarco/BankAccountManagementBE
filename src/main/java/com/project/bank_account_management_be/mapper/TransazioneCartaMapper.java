package com.project.bank_account_management_be.mapper;

import com.project.bank_account_management_be.dto.TransazioneCartaDTO;
import com.project.bank_account_management_be.entity.TransazioneCarta;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TransazioneCartaMapper {

    TransazioneCartaMapper INSTANCE = Mappers.getMapper(TransazioneCartaMapper.class);

    @Mapping(source = "transazioneId", target = "id")
    @Mapping(source = "tipologiaTransazione.tipoTransazioneId", target = "tipoTransazione")
    TransazioneCartaDTO toDTO(TransazioneCarta transazione);
}
