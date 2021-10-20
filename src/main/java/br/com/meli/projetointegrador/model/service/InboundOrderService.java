package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.model.dto.InboundOrderDTO;
import br.com.meli.projetointegrador.model.entity.InboudOrder;
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

    public InboundOrderDTO put(InboundOrderDTO inboundOrderDTO) {
        InboudOrder inboudOrder = modelMapper.map(inboundOrderDTO, InboudOrder.class);
        inboundOrderRepository.save(inboudOrder);
//        batchStockService
        return inboundOrderDTO;
    }

}
