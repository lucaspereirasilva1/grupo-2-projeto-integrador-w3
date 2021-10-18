package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.model.dto.InboundOrderDTO;
import br.com.meli.projetointegrador.model.entity.InboudOrder;
import br.com.meli.projetointegrador.model.repository.InboundOrderRepository;
import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InboundOrderService {

    @Getter
    private final List<InboudOrder> listInboudOrders = new ArrayList<>();

    private final ModelMapper modelMapper = new ModelMapper();
    private final InboundOrderRepository inboundOrderRepository;

    public InboundOrderService(InboundOrderRepository inboudOrderRepository) {
        this.inboundOrderRepository = inboudOrderRepository;
    }

    public InboundOrderDTO put(InboundOrderDTO inboundOrderDTO) {
        InboudOrder inboudOrder = modelMapper.map(inboundOrderDTO, InboudOrder.class);
        inboundOrderRepository.save(inboudOrder);
        listInboudOrders.add(inboudOrder);
        return inboundOrderDTO;
    }
}
