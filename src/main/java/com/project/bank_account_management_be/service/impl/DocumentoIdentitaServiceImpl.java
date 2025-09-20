package com.project.bank_account_management_be.service.impl;

import com.project.bank_account_management_be.dto.DocumentoIdentitaDTO;
import com.project.bank_account_management_be.entity.DocumentoIdentita;
import com.project.bank_account_management_be.entity.Utente;
import com.project.bank_account_management_be.mapper.DocumentoIdentitaMapper;
import com.project.bank_account_management_be.repository.DocumentoIdentitaRepository;
import com.project.bank_account_management_be.repository.UtenteRepository;
import com.project.bank_account_management_be.service.DocumentoIdentitaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DocumentoIdentitaServiceImpl implements DocumentoIdentitaService {

    private final DocumentoIdentitaRepository documentoRepository;
    private final UtenteRepository utenteRepository;
    private final DocumentoIdentitaMapper documentoMapper;

    @Override
    @Transactional(readOnly = true)
    public List<DocumentoIdentitaDTO> getAllDocumenti() {
        return documentoRepository.findAll().stream()
                .map(documentoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DocumentoIdentitaDTO getDocumentoById(Integer id) {
        return documentoRepository.findById(id)
                .map(documentoMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Documento non trovato con ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocumentoIdentitaDTO> getDocumentiByUtente(Integer utenteId) {
        return documentoRepository.findAll().stream()
                .filter(doc -> doc.getUtente().getUtenteId().equals(utenteId))
                .map(documentoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocumentoIdentitaDTO> getDocumentiByCodiceFiscale(String codiceFiscale) {
        return documentoRepository.findAll().stream()
                .filter(doc -> doc.getUtente().getCodiceFiscale().equals(codiceFiscale))
                .map(documentoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DocumentoIdentitaDTO createDocumento(DocumentoIdentitaDTO dto) {
        // Verifica che l'utente esista
        Utente utente = utenteRepository.findById(dto.getUtenteId())
                .orElseThrow(() -> new RuntimeException("Utente non trovato con ID: " + dto.getUtenteId()));

        // Verifica che non esista già un documento dello stesso tipo per lo stesso utente
        boolean existsDocumento = documentoRepository.findAll().stream()
                .anyMatch(doc -> doc.getUtente().getUtenteId().equals(dto.getUtenteId()) &&
                        doc.getTipoDocumento().equals(dto.getTipoDocumento()) &&
                        doc.getNumeroDocumento().equals(dto.getNumeroDocumento()));

        if (existsDocumento) {
            throw new RuntimeException("Documento già esistente per questo utente: " +
                    dto.getTipoDocumento() + " - " + dto.getNumeroDocumento());
        }

        DocumentoIdentita documento = DocumentoIdentita.builder()
                .utente(utente)
                .tipoDocumento(dto.getTipoDocumento())
                .numeroDocumento(dto.getNumeroDocumento())
                .dataRilascio(dto.getDataRilascio())
                .dataScadenza(dto.getDataScadenza())
                .rilasciatoDa(dto.getRilasciatoDa())
                .dataCreazione(LocalDateTime.now())
                .build();

        documento = documentoRepository.save(documento);
        log.info("Creato documento {} per utente: {} - {}",
                documento.getTipoDocumento(),
                utente.getCodiceFiscale(),
                utente.getNome() + " " + utente.getCognome());

        return documentoMapper.toDTO(documento);
    }

    @Override
    public DocumentoIdentitaDTO updateDocumento(Integer id, DocumentoIdentitaDTO dto) {
        DocumentoIdentita documento = documentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Documento non trovato con ID: " + id));

        // Aggiorna i campi modificabili
        documento.setTipoDocumento(dto.getTipoDocumento());
        documento.setNumeroDocumento(dto.getNumeroDocumento());
        documento.setDataRilascio(dto.getDataRilascio());
        documento.setDataScadenza(dto.getDataScadenza());
        documento.setRilasciatoDa(dto.getRilasciatoDa());

        documento = documentoRepository.save(documento);
        log.info("Aggiornato documento ID: {} - Tipo: {}", id, documento.getTipoDocumento());

        return documentoMapper.toDTO(documento);
    }

    @Override
    public void deleteDocumento(Integer id) {
        DocumentoIdentita documento = documentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Documento non trovato con ID: " + id));

        documentoRepository.deleteById(id);
        log.info("Eliminato documento ID: {} - Tipo: {} per utente: {}",
                id,
                documento.getTipoDocumento(),
                documento.getUtente().getCodiceFiscale());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocumentoIdentitaDTO> getDocumentiInScadenza() {
        LocalDate oggi = LocalDate.now();
        LocalDate limiteScadenza = oggi.plusDays(30); // Prossimi 30 giorni

        return documentoRepository.findAll().stream()
                .filter(doc -> doc.getDataScadenza() != null &&
                        doc.getDataScadenza().isAfter(oggi) &&
                        doc.getDataScadenza().isBefore(limiteScadenza))
                .map(documentoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocumentoIdentitaDTO> getDocumentiScaduti() {
        LocalDate oggi = LocalDate.now();

        return documentoRepository.findAll().stream()
                .filter(doc -> doc.getDataScadenza() != null &&
                        doc.getDataScadenza().isBefore(oggi))
                .map(documentoMapper::toDTO)
                .collect(Collectors.toList());
    }
}