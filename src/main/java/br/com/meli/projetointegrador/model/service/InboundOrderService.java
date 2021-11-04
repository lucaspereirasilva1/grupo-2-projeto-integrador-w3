package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.exception.InboundOrderException;
import br.com.meli.projetointegrador.exception.ValidInputException;
import br.com.meli.projetointegrador.model.dto.AgentDTO;
import br.com.meli.projetointegrador.model.dto.BatchStockDTO;
import br.com.meli.projetointegrador.model.dto.InboundOrderDTO;
import br.com.meli.projetointegrador.model.entity.InboundOrder;
import br.com.meli.projetointegrador.model.repository.InboundOrderRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author Jhony Zuim / Lucas Pereira / Edmilson Nobre / Rafael Vicente
 * @version 1.0.0
 * @since 15/10/2021
 * Camada service responsavel pela regra de negocio relacionada ao inboundOrder
 */

@Service
public class InboundOrderService {

    private final ModelMapper modelMapper = new ModelMapper();
    private final InboundOrderRepository inboundOrderRepository;
    private final BatchStockService batchStockService;
    private final SectionService sectionService;
    private final WarehouseService warehouseService;

    public InboundOrderService(InboundOrderRepository inboudOrderRepository,
                               BatchStockService batchStockService,
                               SectionService sectionService,
                               WarehouseService warehouseService) {
        this.inboundOrderRepository = inboudOrderRepository;
        this.batchStockService = batchStockService;
        this.sectionService = sectionService;
        this.warehouseService = warehouseService;
    }

    /**
     * @param inboundOrderDTO recebe uma ordem de entrada;
     * @param agentDTO recebe um agenteDTO;
     * @return faz o post e retorna a lista salva.
     */
    public List<BatchStockDTO> post(InboundOrderDTO inboundOrderDTO, AgentDTO agentDTO) {
        InboundOrder inboundOrder = modelMapper.map(inboundOrderDTO, InboundOrder.class);
        inboundOrder.section(sectionService.find(inboundOrderDTO.getSectionDTO().getSectionCode()));
        batchStockService.postAll(inboundOrder.getListBatchStock(), agentDTO, inboundOrderDTO.getSectionDTO());
        inboundOrderRepository.save(inboundOrder);
        return inboundOrderDTO.getListBatchStockDTO();
    }

    /**
     * @param inboundOrderDTO recebe uma ordem de entrada;
     * @param agentDTO recebe um agenteDTO;
     * @return faz o put e retorna a lista alterada.
     */
    public List<BatchStockDTO> put(InboundOrderDTO inboundOrderDTO, AgentDTO agentDTO) {
        Optional<InboundOrder> inboundOrderCheck = inboundOrderRepository.findByOrderNumber(inboundOrderDTO.getOrderNumber());
        if (inboundOrderCheck.isPresent()) {
            InboundOrder inboundOrder = inboundOrderCheck.get();
            inboundOrder.orderNumber(inboundOrderDTO.getOrderNumber());
            inboundOrder.orderDate(inboundOrderDTO.getOrderDate());
            batchStockService.putAll(inboundOrder.getListBatchStock(),
                    inboundOrderDTO.getListBatchStockDTO(), agentDTO
                    , inboundOrderDTO.getSectionDTO());
            inboundOrderRepository.save(inboundOrder);
        } else {
            throw new InboundOrderException("Ordem de entrada nao existe!!! Por gentileza realizar o cadastro antes de atualizar");
        }
        return inboundOrderDTO.getListBatchStockDTO();
    }

    /**
     * @param inboundOrderDTO recebe uma ordem de entrada;
     * @param agentDTO recebe um agenteDTO
     * @return valida os dados se invalido retorna exception.
     */
    public void inputValid(InboundOrderDTO inboundOrderDTO, AgentDTO agentDTO) {
        if (!warehouseService.validWarehouse(inboundOrderDTO.getSectionDTO().getWarehouseCode()) |
            !inboundOrderDTO.getSectionDTO().getWarehouseCode().equals(agentDTO.getWarehouseCode()) |
            !sectionService.validSection(inboundOrderDTO.getSectionDTO().getSectionCode())) {
            throw new ValidInputException("Problema na validacao dos dados de entrada!!!");
        }
    }
}
