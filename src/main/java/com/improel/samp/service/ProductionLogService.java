// Serviço responsável pelo registro da linha do tempo e eventos de produção.

package com.improel.samp.service;

import com.improel.samp.domain.LogType;
import com.improel.samp.domain.OrderStatus;
import com.improel.samp.domain.PauseReason;
import com.improel.samp.dto.OfflineLogSyncDTO;
import com.improel.samp.entity.ProductionLog;
import com.improel.samp.entity.ProductionOrder;
import com.improel.samp.entity.User;
import com.improel.samp.entity.Workstation;
import com.improel.samp.repository.ProductionLogRepository;
import com.improel.samp.repository.ProductionOrderRepository;
import com.improel.samp.repository.UserRepository;
import com.improel.samp.repository.WorkstationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Comparator;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProductionLogService {

    private final ProductionLogRepository productionLogRepository;
    private final ProductionOrderRepository productionOrderRepository;
    private final WorkstationRepository workstationRepository;
    private final UserRepository userRepository;

    @Autowired
    public ProductionLogService(ProductionLogRepository productionLogRepository,
                                ProductionOrderRepository productionOrderRepository,
                                WorkstationRepository workstationRepository,
                                UserRepository userRepository) {
        this.productionLogRepository = productionLogRepository;
        this.productionOrderRepository = productionOrderRepository;
        this.workstationRepository = workstationRepository;
        this.userRepository = userRepository;
    }

    // ----------------------------------------------------------------------------------------------------------------------------------------------------------------
    // MÉTODOS DE CONSULTA (CRUD BÁSICO).
    // ----------------------------------------------------------------------------------------------------------------------------------------------------------------

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

    // Lista os apontamentos efetuados por um determinado operador.
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

    // ----------------------------------------------------------------------------------------------------------------------------------------------------------------
    // REGRAS DE NEGÓCIO - CICLO DE VIDA DA PRODUÇÃO (TABLET)
    // ----------------------------------------------------------------------------------------------------------------------------------------------------------------

    // 1. Inicia a produção de um lote na bancada.
    @Transactional
    public ProductionLog startProduction(Long orderId, Long workstationId, Long operatorId) {
        ProductionOrder order = productionOrderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("OP não encontrada"));
        Workstation workstation = workstationRepository.findById(workstationId)
                .orElseThrow(() -> new IllegalArgumentException("Bancada não encontrada"));
        User operator = userRepository.findById(operatorId)
                .orElseThrow(() -> new IllegalArgumentException("Operador não encontrado"));

        // Valida se a bancada já possui uma atividade em andamento (Log aberto sem data de fim).
        productionLogRepository.findFirstByWorkstationOrderByStartTimestampDesc(workstation)
                .ifPresent(lastLog -> {
                    if (lastLog.getEndTimestamp() == null) {
                        throw new IllegalStateException("A bancada já possui uma atividade em andamento. Conclua ou pause antes de iniciar outra.");
                    }
                });

        ProductionLog log = new ProductionLog(null, order, workstation, operator, LogType.START, LocalDateTime.now());

        // Se a OP estava pendente, altera para em andamento e marca o início oficial.
        if (order.getStatus() == OrderStatus.PENDING) {
            order.setStatus(OrderStatus.IN_PROGRESS);
            order.setStartTime(LocalDateTime.now());
            productionOrderRepository.save(order);
        }

        return productionLogRepository.save(log);
    }

    // 2. Pausa a produção por motivos operacionais/técnicos.
    @Transactional
    public ProductionLog pauseProduction(Long workstationId, PauseReason reason, String remarks) {
        Workstation workstation = workstationRepository.findById(workstationId)
                .orElseThrow(() -> new IllegalArgumentException("Bancada não encontrada"));

        // Busca o evento que está rodando atualmente na bancada.
        ProductionLog activeLog = productionLogRepository.findFirstByWorkstationOrderByStartTimestampDesc(workstation)
                .filter(log -> log.getEndTimestamp() == null && (log.getLogType() == LogType.START || log.getLogType() == LogType.RESUME))
                .orElseThrow(() -> new IllegalStateException("Nenhuma atividade em andamento nesta bancada para pausar."));

        LocalDateTime now = LocalDateTime.now();

        // Encerra o evento atual e calcula o tempo líquido.
        activeLog.setEndTimestamp(now);
        activeLog.setDurationSeconds(Duration.between(activeLog.getStartTimestamp(), now).getSeconds());
        productionLogRepository.save(activeLog);

        // Cria o evento apontando a Pausa.
        ProductionLog pauseLog = new ProductionLog(null, activeLog.getProductionOrder(), workstation, activeLog.getOperator(), LogType.PAUSE, now);
        pauseLog.setPauseReason(reason);
        pauseLog.setRemarks(remarks);

        // Altera o status da OP para PAUSED.
        ProductionOrder order = activeLog.getProductionOrder();
        order.setStatus(OrderStatus.PAUSED);
        productionOrderRepository.save(order);

        return productionLogRepository.save(pauseLog);
    }

    // 3. Retoma a produção após uma pausa.
    @Transactional
    public ProductionLog resumeProduction(Long workstationId) {
        Workstation workstation = workstationRepository.findById(workstationId)
                .orElseThrow(() -> new IllegalArgumentException("Bancada não encontrada"));

        // Busca a pausa ativa.
        ProductionLog activePause = productionLogRepository.findFirstByWorkstationOrderByStartTimestampDesc(workstation)
                .filter(log -> log.getEndTimestamp() == null && log.getLogType() == LogType.PAUSE)
                .orElseThrow(() -> new IllegalStateException("A bancada não se encontra em estado de pausa."));

        LocalDateTime now = LocalDateTime.now();

        // Encerra a contagem de tempo da pausa.
        activePause.setEndTimestamp(now);
        activePause.setDurationSeconds(Duration.between(activePause.getStartTimestamp(), now).getSeconds());
        productionLogRepository.save(activePause);

        // Cria o novo evento de retomada.
        ProductionLog resumeLog = new ProductionLog(null, activePause.getProductionOrder(), workstation, activePause.getOperator(), LogType.RESUME, now);

        ProductionOrder order = activePause.getProductionOrder();
        order.setStatus(OrderStatus.IN_PROGRESS);
        productionOrderRepository.save(order);

        return productionLogRepository.save(resumeLog);
    }

    // 4. Finaliza a etapa de montagem do lote da bancada (Quantidades sendo geridas posteriormente pelo gestor).
    @Transactional
    public ProductionLog completeProduction(Long workstationId) {
        Workstation workstation =  workstationRepository.findById(workstationId)
                .orElseThrow(() -> new IllegalArgumentException("Bancada não encontrada"));

        ProductionLog activeLog = productionLogRepository.findFirstByWorkstationOrderByStartTimestampDesc(workstation)
                .filter(log -> log.getEndTimestamp() == null && (log.getLogType() == LogType.START || log.getLogType() == LogType.RESUME))
                .orElseThrow(() -> new IllegalStateException("Nehuma atividade em andamento nesta bancada para finalizar."));

        LocalDateTime now = LocalDateTime.now();

        // Encerra o evento de montagem.
        activeLog.setEndTimestamp(now);
        activeLog.setDurationSeconds(Duration.between(activeLog.getStartTimestamp(), now).getSeconds());
        productionLogRepository.save(activeLog);

        // Cria um marco pontual (Start e End iguais) apenas para registrar no histórico que houve a conclusão.
        ProductionLog finishLog = new ProductionLog(null, activeLog.getProductionOrder(), workstation, activeLog.getOperator(), LogType.FINISH_BATCH, now);
        finishLog.setEndTimestamp(now);

        ProductionOrder order = activeLog.getProductionOrder();
        order.setStatus(OrderStatus.COMPLETED);
        order.setEndTime(now);
        productionOrderRepository.save(order);

        return productionLogRepository.save(finishLog);
    }

    // ----------------------------------------------------------------------------------------------------------------------------------------------------------------
    // REGRAS DE NEGÓCIO - HANDOVER / TROCA DE OPERADOR (EXCLUSIVO GESTOR)
    // ----------------------------------------------------------------------------------------------------------------------------------------------------------------

    // 5. Libera a bancada e registra a produção parcial de um operador antes da troca.
    @Transactional
    public ProductionLog manageHandoverRelease(Long workstationId, Integer partialQuantity, String remarks) {
        Workstation workstation = workstationRepository.findById(workstationId)
                .orElseThrow(() -> new IllegalArgumentException("Bancada não encontrada"));

        // Busca a atividade em andamento na bancada.
        ProductionLog activeLog = productionLogRepository.findFirstByWorkstationOrderByStartTimestampDesc(workstation)
                .filter(log -> log.getEndTimestamp() == null)
                .orElseThrow(() -> new IllegalStateException("Nenhuma atividade em andamento nesta bancada para realizar a liberação."));

        LocalDateTime now = LocalDateTime.now();

        // Encerra atividade atual do operador 1.
        activeLog.setEndTimestamp(now);
        activeLog.setDurationSeconds(Duration.between(activeLog.getStartTimestamp(), now).getSeconds());
        productionLogRepository.save(activeLog);

        // Cria o registro pontual de liberação com a quantidade parcial produzida.
        ProductionLog releaseLog = new ProductionLog(null, activeLog.getProductionOrder(), workstation, activeLog.getOperator(), LogType.HANDOVER_RELEASE, now);
        releaseLog.setEndTimestamp(now);
        releaseLog.setCompletedQuantity(partialQuantity != null ? partialQuantity : 0);
        releaseLog.setRemarks(remarks);

        // Consolida a quantidade parcial montada na Ordem de Produção.
        ProductionOrder order = activeLog.getProductionOrder();
        if (partialQuantity != null && partialQuantity > 0) {
            order.setProducedQuantity(order.getProducedQuantity() + partialQuantity);
        }

        // Pausa a OP temporariamente até que o novo operador assuma.
        order.setStatus(OrderStatus.PAUSED);
        productionOrderRepository.save(order);

        return productionLogRepository.save(releaseLog);
    }

    // 6. Novo operador assume a OP na bancada, vinculando o histórico do operador anterior (Quantidade de placas montadas).
    @Transactional
    public ProductionLog manageHandoverTake(Long orderId, Long workstationId, Long newOperatorId, Long previousOperatorId) {
        ProductionOrder order = productionOrderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Ordem de Produção não encontrada"));
        Workstation workstation = workstationRepository.findById(workstationId)
                .orElseThrow(() -> new IllegalArgumentException("Bancada não encontrada"));
        User newOperator = userRepository.findById(newOperatorId)
                .orElseThrow(() -> new IllegalArgumentException("Novo operador não encontrado"));
        User previousOperator = userRepository.findById(previousOperatorId)
                .orElseThrow(() -> new IllegalArgumentException("Operador anterior não encontrado"));

        // Valida se a bancada está livre para receber o novo operador.
        productionLogRepository.findFirstByWorkstationOrderByStartTimestampDesc(workstation)
                .ifPresent(lastLog -> {
                    if (lastLog.getEndTimestamp() == null) {
                        throw new IllegalStateException("A bancada possui uma atividade em andamento. Conclua ou libere antes de assumir.");
                    }
                });

        // Cria o evento de assunção do operador 2 para a Ordem de Produção.
        ProductionLog takeLog = new ProductionLog(null, order, workstation, newOperator, LogType.HANDOVER_TAKE, LocalDateTime.now());

        // Mantém a rastreabilidade de quem originou a transferência.
        takeLog.setTransferredFromUser(previousOperator);

        order.setStatus(OrderStatus.IN_PROGRESS);
        productionOrderRepository.save(order);

        return productionLogRepository.save(takeLog);
    }

    // ----------------------------------------------------------------------------------------------------------------------------------------------------------------
    // REGRAS DE NEGÓCIO - SINCRONIZAÇÃO OFFLINE (TABLET)
    // ----------------------------------------------------------------------------------------------------------------------------------------------------------------

    // 7. Processa um lote de apontamentos retidos no tablet durante a queda de rede.
    @Transactional
    public void syncOfflineLogs(List<OfflineLogSyncDTO> offlineLogs) {
        // Se a lista estiver vazia, nenhum procedimento é realizado.
        if (offlineLogs == null || offlineLogs.isEmpty()) {
            return;
        }

        // Ordena a lista cronologicamente (Do evento mais antigo ao mais recente).
        offlineLogs.sort(Comparator.comparing(OfflineLogSyncDTO::recordedTimestamp));

        // Processa cada evento respeitando o horário retroativo exato.
        for (OfflineLogSyncDTO dto :  offlineLogs) {
            ProductionOrder order = productionOrderRepository.findById(dto.productionOrderId())
                    .orElseThrow(() -> new IllegalArgumentException("Ordem de Produção não encontrada: " + dto.productionOrderId()));
            Workstation workstation = workstationRepository.findById(dto.workstationId())
                    .orElseThrow(() -> new IllegalArgumentException("Bancada não encontrada: " + dto.workstationId()));
            User operator = userRepository.findById(dto.operatorId())
                    .orElseThrow(() -> new IllegalArgumentException("Operador não encontrado: " + dto.operatorId()));

            // Utiliza-se a hora do tablet em vez do tempo atual do servidor.
            LocalDateTime eventTime = dto.recordedTimestamp();

            switch (dto.logType()){
                case START:
                    boolean hasOpenLog = productionLogRepository.findFirstByWorkstationOrderByStartTimestampDesc(workstation)
                            .map(log -> log.getEndTimestamp() == null)
                            .orElse(false);

                    if(!hasOpenLog){
                        ProductionLog startLog = new ProductionLog(null, order, workstation, operator, LogType.START, eventTime);
                        if (order.getStatus() == OrderStatus.PENDING) {
                            order.setStatus(OrderStatus.IN_PROGRESS);
                            order.setStartTime(eventTime);
                            productionOrderRepository.save(order);
                        }
                        productionLogRepository.save(startLog);
                    }
                    break;

                case PAUSE:
                    productionLogRepository.findFirstByWorkstationOrderByStartTimestampDesc(workstation)
                            .filter(log -> log.getEndTimestamp() == null && (log.getLogType() == LogType.START || log.getLogType() == LogType.RESUME))
                            .ifPresent(activeLog -> {
                                activeLog.setEndTimestamp(eventTime);
                                activeLog.setDurationSeconds(Duration.between(activeLog.getStartTimestamp(), eventTime).getSeconds());
                                productionLogRepository.save(activeLog);

                                ProductionLog pauseLog = new ProductionLog(null, order, workstation, operator, LogType.PAUSE, eventTime);
                                pauseLog.setPauseReason(dto.pauseReason());
                                pauseLog.setRemarks(dto.remarks());
                                productionLogRepository.save(pauseLog);

                                order.setStatus(OrderStatus.PAUSED);
                                productionOrderRepository.save(order);
                            });
                    break;

                case RESUME:
                    productionLogRepository.findFirstByWorkstationOrderByStartTimestampDesc(workstation)
                            .filter(log -> log.getEndTimestamp() == null && log.getLogType() == LogType.PAUSE)
                            .ifPresent(activePause -> {
                                activePause.setEndTimestamp(eventTime);
                                activePause.setDurationSeconds(Duration.between(activePause.getStartTimestamp(), eventTime).getSeconds());
                                productionLogRepository.save(activePause);

                                ProductionLog resumeLog = new ProductionLog(null, order, workstation, operator, LogType.RESUME, eventTime);
                                productionLogRepository.save(resumeLog);

                                order.setStatus(OrderStatus.IN_PROGRESS);
                                productionOrderRepository.save(order);
                            });
                    break;

                case FINISH_BATCH:
                    productionLogRepository.findFirstByWorkstationOrderByStartTimestampDesc(workstation)
                            .filter(log -> log.getEndTimestamp() == null && (log.getLogType() == LogType.START || log.getLogType() == LogType.RESUME))
                            .ifPresent(activeLog -> {
                                activeLog.setEndTimestamp(eventTime);
                                activeLog.setDurationSeconds(Duration.between(activeLog.getStartTimestamp(), eventTime).getSeconds());
                                productionLogRepository.save(activeLog);

                                ProductionLog finishLog = new ProductionLog(null, order, workstation, operator, LogType.FINISH_BATCH, eventTime);
                                finishLog.setEndTimestamp(eventTime);
                                productionLogRepository.save(finishLog);

                                order.setStatus(OrderStatus.COMPLETED);
                                order.setEndTime(eventTime);
                                productionOrderRepository.save(order);
                            });
                    break;

                default:
                    // Açoes exclusivas do gestor ocorrem online e não trafegam por este DTO.
                    break;
            }
        }
    }
}
