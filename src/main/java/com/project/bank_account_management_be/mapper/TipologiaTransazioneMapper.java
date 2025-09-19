package com.project.bank_account_management_be.mapper;

import com.project.bank_account_management_be.dto.TipologiaTransazioneDTO;
import com.project.bank_account_management_be.entity.TipologiaTransazione;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TipologiaTransazioneMapper {

    TipologiaTransazioneMapper INSTANCE = Mappers.getMapper(TipologiaTransazioneMapper.class);

    @Mapping(source = "tipoTransazioneId", target = "id")
    TipologiaTransazioneDTO toDTO(TipologiaTransazione tipologia);

    @Mapping(source = "id", target = "tipoTransazioneId")
    @Mapping(target = "transazioniConto", ignore = true)
    @Mapping(target = "transazioniCarta", ignore = true)
    @Mapping(target = "dataCreazione", ignore = true)
    TipologiaTransazione toEntity(TipologiaTransazioneDTO dto);
}