package br.com.meli.projetointegrador.model.service;

import Utils.ConstantsUtil;
import br.com.meli.projetointegrador.exception.InboundOrderException;
import br.com.meli.projetointegrador.exception.PersistenceException;
import br.com.meli.projetointegrador.exception.ValidInputException;
import br.com.meli.projetointegrador.model.dto.AgentDTO;
import br.com.meli.projetointegrador.model.dto.BatchStockDTO;
import br.com.meli.projetointegrador.model.dto.InboundOrderDTO;
import br.com.meli.projetointegrador.model.entity.InboundOrder;
import br.com.meli.projetointegrador.model.entity.Section;
import br.com.meli.projetointegrador.model.repository.InboundOrderRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
    private static final Logger logger = LoggerFactory.getLogger(PurchaseOrderService.class);

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
        if (inboundOrderDTO.getOrderDate().isBefore(LocalDate.now())){
            throw new InboundOrderException("Order com data retroativa, favor inserir uma data valida!");
        }
        inboundOrderDTO.getListBatchStockDTO().forEach(b -> {
            if (b.getDueDate().isBefore(LocalDate.now())) {
                throw new InboundOrderException("Estoque com data retroativa: " + b.getDueDate()); }
        });
        InboundOrder inboundOrder = modelMapper.map(inboundOrderDTO, InboundOrder.class);
        Section section = sectionService.find(inboundOrderDTO.getSectionDTO().getSectionCode());
        inboundOrder.section(section);
        batchStockService.postAll(inboundOrder.getListBatchStock(), agentDTO, inboundOrderDTO.getSectionDTO());
        try {
            inboundOrderRepository.save(inboundOrder);
        }catch (DataAccessException e) {
            logger.error(ConstantsUtil.PERSISTENCE_ERROR, e);
            throw new PersistenceException(ConstantsUtil.PERSISTENCE_ERROR);
        }
        return inboundOrderDTO.getListBatchStockDTO();
    }

    /**
     * @param inboundOrderDTO recebe uma ordem de entrada;
     * @param agentDTO recebe um agenteDTO;
     * @return faz o put e retorna a lista alterada.
     */
    public List<BatchStockDTO> put(InboundOrderDTO inboundOrderDTO, AgentDTO agentDTO) {
        inboundOrderDTO.getListBatchStockDTO().forEach(b -> {
            if (b.getDueDate().isBefore(LocalDate.now())) {
                throw new InboundOrderException("Estoque com data retroativa: " + b.getDueDate());
            }
        });
        Optional<InboundOrder> inboundOrderCheck = inboundOrderRepository.findByOrderNumber(inboundOrderDTO.getOrderNumber());
        if (inboundOrderCheck.isPresent()) {
            InboundOrder inboundOrder = inboundOrderCheck.get();
            inboundOrder.orderNumber(inboundOrderDTO.getOrderNumber());
            inboundOrder.orderDate(inboundOrderDTO.getOrderDate());
            batchStockService.putAll(inboundOrder.getListBatchStock(),
                    inboundOrderDTO.getListBatchStockDTO(), agentDTO
                    , inboundOrderDTO.getSectionDTO());
            try {
                inboundOrderRepository.save(inboundOrder);
            }catch (DataAccessException e) {
                logger.error(ConstantsUtil.PERSISTENCE_ERROR, e);
                throw new PersistenceException(ConstantsUtil.PERSISTENCE_ERROR);
            }
        } else {
            throw new InboundOrderException("Ordem de entrada nao existe!!! Por gentileza realizar o cadastro antes de atualizar");
        }
        return inboundOrderDTO.getListBatchStockDTO();
    }

    /**
     * @param inboundOrderDTO recebe uma ordem de entrada;
     * @param agentDTO recebe um agenteDTO
     */
    public void inputValid(InboundOrderDTO inboundOrderDTO, AgentDTO agentDTO) {
        Boolean validAgentIntoWarehouse = inboundOrderDTO.getSectionDTO().getWarehouseCode().equals(agentDTO.getWarehouseCode());
        Boolean validWarehouse = warehouseService.validWarehouse(inboundOrderDTO.getSectionDTO().getWarehouseCode());
        Boolean validSection = sectionService.validSection(inboundOrderDTO.getSectionDTO().getSectionCode());
        if (Boolean.FALSE.equals(validWarehouse) ||
            Boolean.FALSE.equals(validAgentIntoWarehouse) ||
            Boolean.FALSE.equals(validSection))
            throw new ValidInputException("Problema na validacao dos dados de entrada!!!");
    }

}
