// Serviço responsável pelo gerenciamento das bancadas de montagem.

package com.improel.samp.service;

import com.improel.samp.domain.WorkstationStatus;
import com.improel.samp.dto.request.CreateWorkstationRequestDTO;
import com.improel.samp.entity.Workstation;
import com.improel.samp.repository.WorkstationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkstationService {

    private final WorkstationRepository workstationRepository;

    // Lista de todas as bancadas cadastradas.
    @Transactional(readOnly = true)
    public List<Workstation> findAll() {
        return workstationRepository.findAll();
    }

    // Busca uma bancada pelo seu ID.
    @Transactional(readOnly = true)
    public Optional<Workstation> findById(Long id) {
        return workstationRepository.findById(id);
    }

    // Cadastra ou atualiza uma bancada de montagem.
    @Transactional
    public Workstation create(CreateWorkstationRequestDTO request) {
        if (workstationRepository.existsByName(request.name())) {
            throw new IllegalArgumentException("Já existe uma bancada cadastrada com este nome.");
        }
        Workstation workstation = new Workstation(
                null,
                request.name(),
                request.status(),
                request.tabletUuid()
        );
        return workstationRepository.save(workstation);
    }

    // Altera o status de uma bancada de montagem.
    @Transactional
    public Workstation updateStatus(Long id, WorkstationStatus newStatus) {
        Workstation workstation = workstationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Bancada não encontrada para o ID informado: " + id));

        workstation.setStatus(newStatus);
        return workstationRepository.save(workstation);
    }
}
