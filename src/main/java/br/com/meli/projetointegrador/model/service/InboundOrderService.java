package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.exception.AgentException;
import br.com.meli.projetointegrador.exception.SectionExecption;
import br.com.meli.projetointegrador.model.dto.AgentDTO;
import br.com.meli.projetointegrador.model.dto.InboundOrderDTO;
import br.com.meli.projetointegrador.model.entity.Agent;
import br.com.meli.projetointegrador.model.entity.InboudOrder;
import br.com.meli.projetointegrador.model.entity.Section;
import br.com.meli.projetointegrador.model.repository.InboundOrderRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InboundOrderService {

    private final ModelMapper modelMapper = new ModelMapper();
    private final InboundOrderRepository inboundOrderRepository;
    private final BatchStockService batchStockService;
    private final SectionService sectionService;
    private final AgentService agentService;

    public InboundOrderService(InboundOrderRepository inboudOrderRepository,
                               BatchStockService batchStockService,
                               SectionService sectionService,
                               AgentService agentService) {
        this.inboundOrderRepository = inboudOrderRepository;
        this.batchStockService = batchStockService;
        this.sectionService = sectionService;
        this.agentService = agentService;
    }

    public InboundOrderDTO put(InboundOrderDTO inboundOrderDTO, AgentDTO agentDTO) {
        InboudOrder inboudOrder = modelMapper.map(inboundOrderDTO, InboudOrder.class);

        Section section = sectionService.find(inboundOrderDTO.getSectionDTO().getSectionCode());
        inboudOrder.section(section);

        Agent agent = agentService.find(agentDTO.getCpf());
        inboudOrder.getListBatchStock().forEach(b -> {
            b.agent(agent);
        });

        inboundOrderRepository.save(inboudOrder);

        return inboundOrderDTO;
    }

}
