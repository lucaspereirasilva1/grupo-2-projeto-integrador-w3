package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.model.dto.AgentDTO;
import br.com.meli.projetointegrador.model.dto.InboundOrderDTO;
import br.com.meli.projetointegrador.model.entity.Agent;
import br.com.meli.projetointegrador.model.entity.InboudOrder;
import br.com.meli.projetointegrador.model.entity.Section;
import br.com.meli.projetointegrador.model.repository.InboundOrderRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class InboundOrderService {

    private final ModelMapper modelMapper = new ModelMapper();
    private final InboundOrderRepository inboundOrderRepository;
    private final BatchStockService batchStockService;

    public InboundOrderService(InboundOrderRepository inboudOrderRepository,
                               BatchStockService batchStockService) {
        this.inboundOrderRepository = inboudOrderRepository;
        this.batchStockService = batchStockService;
    }

    public InboundOrderDTO put(InboundOrderDTO inboundOrderDTO, AgentDTO agentDTO) {
        InboudOrder inboudOrder = modelMapper.map(inboundOrderDTO, InboudOrder.class);
        Section section = modelMapper.map(inboundOrderDTO.getSectionDTO(), Section.class);
        Agent agent = modelMapper.map(agentDTO, Agent.class);

        inboudOrder.getListBatchStock().forEach(b -> {
            batchStockService.put(b, section, agent);
        });

        inboundOrderRepository.save(inboudOrder);

        return inboundOrderDTO;
    }

}
