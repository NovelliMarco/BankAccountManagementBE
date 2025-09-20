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
    @Mapping(source = "utente.utenteId", target = "utenteId")
    @Mapping(source = "utente.nome", target = "nomeUtente")
    @Mapping(source = "utente.cognome", target = "cognomeUtente")
    @Mapping(source = "utente.codiceFiscale", target = "codiceFiscaleUtente")
    ContoDTO toDTO(Conto conto);

    @Mapping(source = "id", target = "contoId")
    @Mapping(target = "utente", ignore = true)
    @Mapping(target = "transazioni", ignore = true)
    @Mapping(target = "movimenti", ignore = true)
    @Mapping(target = "dataApertura", ignore = true)
    Conto toEntity(ContoDTO dto);
}
