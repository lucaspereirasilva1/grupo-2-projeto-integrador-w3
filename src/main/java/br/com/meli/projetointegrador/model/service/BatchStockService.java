package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.exception.AgentException;
import br.com.meli.projetointegrador.model.entity.Agent;
import br.com.meli.projetointegrador.model.entity.BatchStock;
import br.com.meli.projetointegrador.model.repository.BatchStockRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BatchStockService {

    private final BatchStockRepository batchStockRepository;

    public BatchStockService(BatchStockRepository batchStockRepository) {
        this.batchStockRepository = batchStockRepository;
    }

    public boolean validBatchStockAgent(Agent agent) {
        Optional<BatchStock> batchStockOptional = batchStockRepository.findByAgent(agent);
        if (batchStockOptional.isPresent()) {
            return true;
        } else {
            throw new AgentException("Representante nao foi vinculado ao estoque, por gentileza reenviar a request!!!");
        }
    }

}
