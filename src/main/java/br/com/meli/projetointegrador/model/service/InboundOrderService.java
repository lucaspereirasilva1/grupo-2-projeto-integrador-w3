package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.exception.InboundOrderException;
import br.com.meli.projetointegrador.model.dto.AgentDTO;
import br.com.meli.projetointegrador.model.dto.BatchStockDTO;
import br.com.meli.projetointegrador.model.dto.InboundOrderDTO;
import br.com.meli.projetointegrador.model.entity.BatchStock;
import br.com.meli.projetointegrador.model.entity.InboudOrder;
import br.com.meli.projetointegrador.model.entity.Section;
import br.com.meli.projetointegrador.model.repository.InboundOrderRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public List<BatchStockDTO> post(InboundOrderDTO inboundOrderDTO, AgentDTO agentDTO) {
        InboudOrder inboudOrder = modelMapper.map(inboundOrderDTO, InboudOrder.class);
        inboudOrder.section(sectionService.find(inboundOrderDTO.getSectionDTO().getSectionCode()));
        batchStockService.postAll(inboudOrder.getListBatchStock(), agentDTO, inboundOrderDTO.getSectionDTO());
        inboundOrderRepository.save(inboudOrder);
        return inboundOrderDTO.getListBatchStockDTO();
    }

    public List<BatchStockDTO> put(InboundOrderDTO inboundOrderDTO, AgentDTO agentDTO) {
        Optional<InboudOrder> inboundOrderCheck = inboundOrderRepository.findByOrderNumber(inboundOrderDTO.getOrderNumber());
        if (inboundOrderCheck.isPresent()) {
            InboudOrder inboudOrder = inboundOrderCheck.get();
            inboudOrder.orderNumber(inboundOrderDTO.getOrderNumber());
            inboudOrder.orderDate(inboundOrderDTO.getOrderDate());

            for (int i = 0; i < inboudOrder.getListBatchStock().size(); i++) {
                for (int x = i; x < inboundOrderDTO.getListBatchStockDTO().size(); x++) {
                    inboudOrder.getListBatchStock().get(i).batchNumber(inboundOrderDTO.getListBatchStockDTO().get(x).getBatchNumber());
                    inboudOrder.getListBatchStock().get(i).productId(inboundOrderDTO.getListBatchStockDTO().get(x).getProductId());
                    inboudOrder.getListBatchStock().get(i).currentTemperature(inboundOrderDTO.getListBatchStockDTO().get(x).getCurrentTemperature());
                    inboudOrder.getListBatchStock().get(i).minimumTemperature(inboundOrderDTO.getListBatchStockDTO().get(x).getMinimumTemperature());
                    inboudOrder.getListBatchStock().get(i).initialQuantity(inboundOrderDTO.getListBatchStockDTO().get(x).getInitialQuantity());
                    inboudOrder.getListBatchStock().get(i).manufacturingDate(inboundOrderDTO.getListBatchStockDTO().get(x).getManufacturingDate());
                    inboudOrder.getListBatchStock().get(i).manufacturingTime(inboundOrderDTO.getListBatchStockDTO().get(x).getManufacturingTime());
                    inboudOrder.getListBatchStock().get(i).dueDate(inboundOrderDTO.getListBatchStockDTO().get(x).getDueDate());
                    x++;
                }
            }

            batchStockService.putAll(inboudOrder.getListBatchStock());
            inboundOrderRepository.save(inboudOrder);
        } else {
            throw new InboundOrderException("Ordem de entrada nao existe!!! Por gentileza realizar o cadastro antes de atualizar");
        }
        return inboundOrderDTO.getListBatchStockDTO();
    }

}
