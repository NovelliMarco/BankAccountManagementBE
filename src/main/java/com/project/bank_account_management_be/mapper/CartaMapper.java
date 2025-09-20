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
    @Mapping(source = "utente.utenteId", target = "utenteId")
    @Mapping(source = "utente.nome", target = "nomeUtente")
    @Mapping(source = "utente.cognome", target = "cognomeUtente")
    @Mapping(source = "utente.codiceFiscale", target = "codiceFiscaleUtente")
    CartaDTO toDTO(Carta carta);

    @Mapping(source = "id", target = "cartaId")
    @Mapping(target = "utente", ignore = true)
    @Mapping(target = "transazioni", ignore = true)
    @Mapping(target = "dataCreazione", ignore = true)
    Carta toEntity(CartaDTO dto);
}
