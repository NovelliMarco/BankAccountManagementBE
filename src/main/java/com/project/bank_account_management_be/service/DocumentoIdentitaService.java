package com.project.bank_account_management_be.service;

import com.project.bank_account_management_be.dto.DocumentoIdentitaDTO;

import java.util.List;

public interface DocumentoIdentitaService {

    List<DocumentoIdentitaDTO> getAllDocumenti();

    DocumentoIdentitaDTO getDocumentoById(Integer id);

    List<DocumentoIdentitaDTO> getDocumentiByUtente(Integer utenteId);

    List<DocumentoIdentitaDTO> getDocumentiByCodiceFiscale(String codiceFiscale);

    DocumentoIdentitaDTO createDocumento(DocumentoIdentitaDTO dto);

    DocumentoIdentitaDTO updateDocumento(Integer id, DocumentoIdentitaDTO dto);

    void deleteDocumento(Integer id);

    List<DocumentoIdentitaDTO> getDocumentiInScadenza();

    List<DocumentoIdentitaDTO> getDocumentiScaduti();
}