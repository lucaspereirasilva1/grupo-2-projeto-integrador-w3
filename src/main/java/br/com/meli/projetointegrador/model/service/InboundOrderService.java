package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.model.dto.AgentDTO;
import br.com.meli.projetointegrador.model.dto.BatchStockDTO;
import br.com.meli.projetointegrador.model.dto.InboundOrderDTO;
import br.com.meli.projetointegrador.model.entity.InboudOrder;
import br.com.meli.projetointegrador.model.repository.InboundOrderRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InboundOrderService {

    private final ModelMapper modelMapper = new ModelMapper();
    private final InboundOrderRepository inboundOrderRepository;
    private final BatchStockService batchStockService;
    private final SectionService sectionService;

    public InboundOrderService(InboundOrderRepository inboudOrderRepository,
                               BatchStockService batchStockService,
                               SectionService sectionService) {
        this.inboundOrderRepository = inboudOrderRepository;
        this.batchStockService = batchStockService;
        this.sectionService = sectionService;

    }

    public List<BatchStockDTO> put(InboundOrderDTO inboundOrderDTO, AgentDTO agentDTO) {
        InboudOrder inboudOrder = modelMapper.map(inboundOrderDTO, InboudOrder.class);
        inboudOrder.section(sectionService.find(inboundOrderDTO.getSectionDTO().getSectionCode()));
        batchStockService.putAll(inboudOrder.getListBatchStock(), agentDTO, inboundOrderDTO.getSectionDTO());
        inboundOrderRepository.save(inboudOrder);

        return inboundOrderDTO.getListBatchStockDTO();
    }

}
