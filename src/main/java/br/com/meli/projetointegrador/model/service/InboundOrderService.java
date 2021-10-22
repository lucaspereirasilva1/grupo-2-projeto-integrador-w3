package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.exception.InboundOrderException;
import br.com.meli.projetointegrador.model.dto.AgentDTO;
import br.com.meli.projetointegrador.model.dto.BatchStockDTO;
import br.com.meli.projetointegrador.model.dto.InboundOrderDTO;
import br.com.meli.projetointegrador.model.entity.Agent;
import br.com.meli.projetointegrador.model.entity.BatchStock;
import br.com.meli.projetointegrador.model.entity.InboundOrder;
import br.com.meli.projetointegrador.model.repository.InboundOrderRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        InboundOrder inboundOrder = modelMapper.map(inboundOrderDTO, InboundOrder.class);
        inboundOrder.section(sectionService.find(inboundOrderDTO.getSectionDTO().getSectionCode()));
        batchStockService.postAll(inboundOrder.getListBatchStock(), agentDTO, inboundOrderDTO.getSectionDTO());
        inboundOrderRepository.save(inboundOrder);
        return inboundOrderDTO.getListBatchStockDTO();
    }

    public List<BatchStockDTO> put(InboundOrderDTO inboundOrderDTO, AgentDTO agentDTO) {
        Optional<InboundOrder> inboundOrderCheck = inboundOrderRepository.findByOrderNumber(inboundOrderDTO.getOrderNumber());
        if (inboundOrderCheck.isPresent()) {
            InboundOrder inboundOrder = inboundOrderCheck.get();
            inboundOrder.orderNumber(inboundOrderDTO.getOrderNumber());
            inboundOrder.orderDate(inboundOrderDTO.getOrderDate());
            inboundOrder.listBatchStock(fillInboundOrder(inboundOrder.getListBatchStock(),
                    inboundOrderDTO.getListBatchStockDTO(),
                    agentDTO));
            batchStockService.putAll(inboundOrder.getListBatchStock());
            inboundOrderRepository.save(inboundOrder);
        } else {
            throw new InboundOrderException("Ordem de entrada nao existe!!! Por gentileza realizar o cadastro antes de atualizar");
        }
        return inboundOrderDTO.getListBatchStockDTO();
    }

    private List<BatchStock> fillInboundOrder(List<BatchStock> listBatchStock,
                                               List<BatchStockDTO> listBatchStockDTO,
                                               AgentDTO agentDTO) {
        for (int i = 0; i < listBatchStock.size(); i++) {
            for (int x = i; x < listBatchStockDTO.size(); x++) {
                listBatchStock.get(i).batchNumber(listBatchStockDTO.get(x).getBatchNumber());
                listBatchStock.get(i).productId(listBatchStockDTO.get(x).getProductId());
                listBatchStock.get(i).currentTemperature(listBatchStockDTO.get(x).getCurrentTemperature());
                listBatchStock.get(i).minimumTemperature(listBatchStockDTO.get(x).getMinimumTemperature());
                listBatchStock.get(i).initialQuantity(listBatchStockDTO.get(x).getInitialQuantity());
                listBatchStock.get(i).manufacturingDate(listBatchStockDTO.get(x).getManufacturingDate());
                listBatchStock.get(i).manufacturingTime(listBatchStockDTO.get(x).getManufacturingTime());
                listBatchStock.get(i).dueDate(listBatchStockDTO.get(x).getDueDate());
                Agent agent = listBatchStock.get(i).getAgent();
                agent.cpf(agentDTO.getCpf());
                agent.name(agentDTO.getName());
                listBatchStock.get(i).agent(agent);
                x++;
            }
        }
        return listBatchStock;
    }

}
