package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.exception.AgentException;
import br.com.meli.projetointegrador.model.dto.InboundOrderDTO;
import br.com.meli.projetointegrador.model.entity.Agent;
import br.com.meli.projetointegrador.model.entity.BatchStock;
import br.com.meli.projetointegrador.model.entity.InboudOrder;
import br.com.meli.projetointegrador.model.repository.BatchStockRepository;
import br.com.meli.projetointegrador.model.repository.InboundOrderRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InboundOrderService {

    private final ModelMapper modelMapper = new ModelMapper();
    private final InboundOrderRepository inboundOrderRepository;
    private final BatchStockRepository batchStockRepository;

    public InboundOrderService(InboundOrderRepository inboudOrderRepository,
                               BatchStockRepository batchStockRepository) {
        this.inboundOrderRepository = inboudOrderRepository;
        this.batchStockRepository = batchStockRepository;
    }

    public InboundOrderDTO put(InboundOrderDTO inboundOrderDTO) {
        InboudOrder inboudOrder = modelMapper.map(inboundOrderDTO, InboudOrder.class);
        inboundOrderRepository.save(inboudOrder);
        return inboundOrderDTO;
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
