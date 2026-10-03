// Serviço responsável pelo registro da linha do tempo e eventos de produção.

package com.improel.samp.service;

import com.improel.samp.domain.PauseReason;
import com.improel.samp.entity.ProductionLog;
import com.improel.samp.entity.ProductionOrder;
import com.improel.samp.entity.User;
import com.improel.samp.entity.Workstation;
import com.improel.samp.repository.ProductionLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductionLogService {

    private final ProductionLogRepository productionLogRepository;

    @Autowired
    public ProductionLogService(ProductionLogRepository productionLogRepository) {
        this.productionLogRepository = productionLogRepository;
    }

    // Lista todos os registros de produção.
    @Transactional(readOnly = true)
    public List<ProductionLog> findAll() {
        return productionLogRepository.findAll();
    }

    // Busca um log específico pelo ID.
    @Transactional(readOnly = true)
    public Optional<ProductionLog> findById(Long id) {
        return productionLogRepository.findById(id);
    }

    // Lista o histórico de logs de uma Ordem de Produção em ordem cronológica.
    @Transactional(readOnly = true)
    public List<ProductionLog> findByProductionOrder(ProductionOrder productionOrder) {
        return productionLogRepository.findByProductionOrderOrderByStartTimestampAsc(productionOrder);
    }

    // Lista os logs registrados de uma bancada de montagem específica.
    @Transactional(readOnly = true)
    public List<ProductionLog> findByWorkstation(Workstation workstation) {
        return productionLogRepository.findByWorkstation(workstation);
    }

    // Lista os apontamento efetuados por um determinado operador.
    @Transactional(readOnly = true)
    public List<ProductionLog> findByOperator(User operator) {
        return productionLogRepository.findByOperator(operator);
    }

    // Busca o último evento realizado em uma bancada específica.
    @Transactional(readOnly = true)
    public Optional<ProductionLog> findLatestByWorkstation(Workstation workstation) {
        return productionLogRepository.findFirstByWorkstationOrderByStartTimestampDesc(workstation);
    }

    // Busca o último evento registrado em uma Ordem de Produção específica.
    @Transactional(readOnly = true)
    public Optional<ProductionLog> findLatestByProductionOrder(ProductionOrder productionOrder) {
        return productionLogRepository.findFirstByProductionOrderOrderByStartTimestampDesc(productionOrder);
    }

    // Salva ou atualiza um registro de log de produção.
    @Transactional
    public ProductionLog save(ProductionLog productionLog) {
        return productionLogRepository.save(productionLog);
    }
}
