// Serviço responsável pelo gerenciamento das bancadas de montagem.

package com.improel.samp.service;

import com.improel.samp.domain.WorkstationStatus;
import com.improel.samp.entity.Workstation;
import com.improel.samp.repository.WorkstationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class WorkstationService {

    private final WorkstationRepository workstationRepository;

    @Autowired
    public WorkstationService(WorkstationRepository workstationRepository) {
        this.workstationRepository = workstationRepository;
    }

    // Lista de todas as bancadas cadastradas.
    @Transactional(readOnly = true)
    public List<Workstation> findAll() {
        return workstationRepository.findAll();
    }

    // Busca uma bancada pelo  seu ID.
    @Transactional(readOnly = true)
    public Optional<Workstation> findById (Long id) {
        return workstationRepository.findById(id);
    }

    // Cadastra ou atualiza uma bancada de montagem.
    @Transactional
    public Workstation save (Workstation workstation) {
        if (workstation.getId() == null && workstation.getName() != null && workstationRepository.existsByName(workstation.getName())) {
            throw new IllegalArgumentException("Já existe uma bancada cadastrada com este nome.");
        }
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
