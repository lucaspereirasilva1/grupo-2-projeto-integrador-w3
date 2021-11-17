package br.com.meli.projetointegrador.model.service;

import Utils.ConstantsUtil;
import br.com.meli.projetointegrador.exception.BatchStockException;
import br.com.meli.projetointegrador.exception.PersistenceException;
import br.com.meli.projetointegrador.exception.ProductExceptionNotFound;
import br.com.meli.projetointegrador.model.dto.*;
import br.com.meli.projetointegrador.model.entity.Agent;
import br.com.meli.projetointegrador.model.entity.BatchStock;
import br.com.meli.projetointegrador.model.entity.Product;
import br.com.meli.projetointegrador.model.enums.ESectionCategory;
import br.com.meli.projetointegrador.model.repository.BatchStockRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * @author Jhony Zuim / Lucas Pereira / Edmilson Nobre / Rafael Vicente
 * @version 1.0.0
 * @since 15/10/2021
 * Camada service responsavel pela regra de negocio relacionada ao batchStock
 */

@Service
public class BatchStockService {

    private final BatchStockRepository batchStockRepository;
    private final SectionService sectionService;
    private final AgentService agentService;
    private final ProductService productService;
    private static final Logger logger = LoggerFactory.getLogger(BatchStockService.class);

    public BatchStockService(BatchStockRepository batchStockRepository,
                             SectionService sectionService,
                             AgentService agentService,
                             ProductService productService) {
        this.batchStockRepository = batchStockRepository;
        this.sectionService = sectionService;
        this.agentService = agentService;
        this.productService = productService;
    }

    /**
     * @param listBatchStock recebe uma lista batchStock;
     * @param agentDTO recebe um agenteDTO;
     * @param sectionDTO recebe uma sectionDTO;
     */
    public void postAll(List<BatchStock> listBatchStock, AgentDTO agentDTO, SectionDTO sectionDTO) {
        listBatchStock.forEach(b -> {
            Product product = productService.find(b.getProductId());
            if (productService.validProductSection(sectionDTO.getSectionCode()) &&
                sectionService.validSectionLength(product.getSection())) {
                b.agent(agentService.find(agentDTO.getCpf()));
                b.section(sectionService.find(sectionDTO.getSectionCode()));
            }
        });
        try {
            batchStockRepository.saveAll(listBatchStock);
        }catch (DataAccessException e) {
            logger.error(ConstantsUtil.PERSISTENCE_ERROR, e);
            throw new PersistenceException(ConstantsUtil.PERSISTENCE_ERROR);
        }

    }

    /**
     * @param listBatchStock lista de batchstock enviada pela inboundorder;
     * @param listBatchStockDTO lista de batchstock recebida do controller;
     * @param agentDTO agente recebido do controller;
     * @param sectionDTO section recebida do controller;
     */
    public void putAll(final List<BatchStock> listBatchStock, List<BatchStockDTO> listBatchStockDTO, AgentDTO agentDTO, SectionDTO sectionDTO) {
        final List<BatchStock> batchStockList = new ArrayList<>();
        listBatchStock.forEach(b -> {
            if (productService.validProductSection(sectionDTO.getSectionCode()) &&
                    sectionService.validSectionLength(b.getSection())) {
                Optional<BatchStockDTO> batchStockDTO = listBatchStockDTO.stream()
                        .filter(bd -> bd.getBatchNumber().equals(b.getBatchNumber()))
                        .findFirst();
                if (batchStockDTO.isEmpty()) {
                    throw new BatchStockException("Divergencia entre dados de entrada e do banco!!!");
                }
                final BatchStock batchStock = fillBatchStock(batchStockDTO.get(), agentDTO, b);
                batchStockList.add(batchStock);
            }
        });
        try {
            batchStockRepository.saveAll(batchStockList);
        }catch (DataAccessException e) {
            logger.error(ConstantsUtil.PERSISTENCE_ERROR, e);
            throw new PersistenceException(ConstantsUtil.PERSISTENCE_ERROR);
        }
    }

    /**
     * Atualiza um objeto batchstock do banco com os dados recebidos pelo controller
     * @param batchStockDTO lista de batchStockDTO
     * @param agentDTO agentDTO
     * @param batchStock batchstock enviado pela inbound order
     * @return batchstock atualizado com os dados do DTO
     */
    public BatchStock fillBatchStock(BatchStockDTO batchStockDTO, AgentDTO agentDTO, BatchStock batchStock) {
        batchStock.setBatchNumber(batchStockDTO.getBatchNumber());
        batchStock.setProductId(batchStockDTO.getProductId());
        batchStock.setCurrentTemperature(batchStockDTO.getCurrentTemperature());
        batchStock.setMinimumTemperature(batchStockDTO.getMinimumTemperature());
        batchStock.setInitialQuantity(batchStockDTO.getInitialQuantity());
        batchStock.setCurrentQuantity(batchStockDTO.getCurrentQuantity());
        batchStock.setManufacturingDate(batchStockDTO.getManufacturingDate());
        batchStock.setManufacturingTime(batchStockDTO.getManufacturingTime());
        batchStock.setDueDate(batchStockDTO.getDueDate());
        Agent agent = agentService.find(agentDTO.getCpf());
        agent.setName(agentDTO.getName());
        agent.setCpf(agentDTO.getCpf());
        batchStock.setAgent(agent);
        return batchStock;
    }

    /**
     * @param productId recebe um Id de produto e codigo da ordenacao;
     * @return uma lista de de produtos baseado no Id.
     */
    public BatchStockResponseDTO listProductId(String productId, String order) {
        BatchStockResponseDTO batchStockResponseDTO = new BatchStockResponseDTO();
        List<BatchStock> batchStockList = findBatchStock(productId);
        final List<BatchStock> batchStockListNotExpired = batchStockList.stream()
                .filter(b -> dueDataProduct(b.getDueDate()))
                .collect(toList());
        if(!batchStockListNotExpired.isEmpty()){
            Product product = productService.find(productId);
            List<BatchStockListProductDTO> listBatchStockProductDTO = convertDTO(batchStockListNotExpired);
            SectionDTO sectionDTO = new SectionDTO()
                    .sectionCode(product.getSection().getSectionCode())
                    .warehouseCode(product.getSection().getWarehouse().getWarehouseCode())
                    .build();
            batchStockResponseDTO.sectionDTO(sectionDTO);
            batchStockResponseDTO.productId(product.getProductId());
            if (!order.equals("")){
                batchStockResponseDTO.batchStock(ordenar(order,listBatchStockProductDTO));
            }else {
                batchStockResponseDTO.batchStock(listBatchStockProductDTO);
            }
            return batchStockResponseDTO;
        } else {
            throw new ProductExceptionNotFound("Nao existe estoques vigentes para esse produto, por favor verifique os dados inseridos!!!");
        }
    }

    /**
     * @param batchStockList recebe uma lista batchStock;
     * @return uma lista convertida para batchStockDTO.
     */
    public List<BatchStockListProductDTO> convertDTO(List<BatchStock> batchStockList) {
        List<BatchStockListProductDTO> listBatchStockProductDTO = new ArrayList<>();
        for (BatchStock b : batchStockList) {
            BatchStockListProductDTO batchStockListProductDTO = new BatchStockListProductDTO()
                    .batchNumber(b.getBatchNumber())
                    .currentQuantity(b.getCurrentQuantity())
                    .dueDate(b.getDueDate())
                    .build();
            listBatchStockProductDTO.add(batchStockListProductDTO);
        }
        return listBatchStockProductDTO;
    }

    /**
     * @param productList recebe uma lista de ProductPurchaseOrder;
     */
    public void updateBatchStock(List<ProductPurchaseOrderDTO> productList) {
        productList.forEach(p -> {
            final List<BatchStock> listBatchStock = batchStockRepository.findAllByProductId(p.getProductId());
            if (listBatchStock.isEmpty()) {
                throw new BatchStockException("Nao foi encontrado estoque para esse produto!!!");
            }
            listBatchStock.forEach(b -> {
                b.setCurrentQuantity(b.getCurrentQuantity()-p.getQuantity());
                try {
                    batchStockRepository.save(b);
                }catch (DataAccessException e) {
                    logger.error("Erro durante a persistencia no banco!!!", e);
                    throw new PersistenceException("Erro durante a persistencia no banco!!!");
                }
            });
        });
    }

    /**
     * @param order tipo de ordenacao;
     * @param listBatchStockProductDTO lista de estoques;
     * @return lista convertida para DTO.
     */
    public List<BatchStockListProductDTO> ordenar(String order, List<BatchStockListProductDTO> listBatchStockProductDTO) {
        switch (order) {
            case ("L"): {//ordenar por lote
                return listBatchStockProductDTO.stream()
                        .sorted(Comparator.comparing(BatchStockListProductDTO::getBatchNumber)).collect(toList());
            }
            case ("C"): {//ordenar por quantidade
                return listBatchStockProductDTO.stream()
                        .sorted(Comparator.comparing(BatchStockListProductDTO::getCurrentQuantity)).collect(toList());
            }
            case ("F"): {//ordenar por vencimento
                return listBatchStockProductDTO.stream()
                        .sorted(Comparator.comparing(BatchStockListProductDTO::getDueDate)).collect(toList());
            }
            default:
                throw new ProductExceptionNotFound("Codigo do filtro nao existe!");
        }
    }

    /**
     * @param dueDate, recebe uma data de vencimento;
     * @return retorna verdadeiro se o vencimento for maior que 3 semanas e falso caso menor
     */
    public Boolean dueDataProduct(LocalDate dueDate){
        return dueDate.isAfter(LocalDate.now().plusWeeks(+3));
    }

    /**
     * @param productId, id do produto;
     * @return lista de batchstock
     */
    public List<BatchStock> findBatchStock(String productId) {
        List<BatchStock> batchStockList = batchStockRepository.findAllByProductId(productId);
        if (batchStockList.isEmpty()) {
            throw new BatchStockException("Nao existe estoques para esse produto!!!");
        }
        return batchStockList;
    }

    /**
     * @param productId, recebe um codigo de produto;
     * @param warehouseCode, recebe um codigo de armazem;
     * @return totalQuantity de produtos.
     */
    public Integer quantityProductBatchStock(String productId, String warehouseCode){
        List<BatchStock> productList = batchStockRepository.findAllByProductId(productId);
        List<BatchStock> productListWarehouseCode = productList.stream()
                .filter(product -> product.getSection().getWarehouse().getWarehouseCode().equals(warehouseCode))
                .collect(Collectors.toList());
        AtomicReference<Integer> totalQuantity = new AtomicReference<>(0);
        productListWarehouseCode.forEach(b -> totalQuantity.updateAndGet(v -> v + b.getCurrentQuantity()));
        return totalQuantity.get();
    }

    /**
     * @param days, periodo de dias;
     * @return lista de batchStockListDueDateDTO ordenado pela data
     */
    public BatchStockListDueDateDTO listDueDateDays(Integer days) {
        BatchStockListDueDateDTO batchStockListDueDateDTO = new BatchStockListDueDateDTO(new ArrayList<>());
        List<BatchStock> batchStockList = batchStockRepository.findAll();
        List<BatchStockServiceDueDateDTO> batchStockServiceDueDateList = new ArrayList<>();
        batchStockList.forEach(b -> {
            if (LocalDate.now().plusDays(days).isAfter(b.getDueDate())) {
                newList(batchStockServiceDueDateList,b.getBatchNumber(),b.getProductId(),b.getDueDate(),b.getCurrentQuantity());
            }
        });
        if (!batchStockServiceDueDateList.isEmpty()) {
            batchStockListDueDateDTO.setBatchStock(batchStockServiceDueDateList.stream()
                    .sorted(Comparator.comparing(BatchStockServiceDueDateDTO::getDueDate)).collect(toList()));
            return batchStockListDueDateDTO;
        } else {
            throw new ProductExceptionNotFound("Nao existe estoque com este filtro!!!");
        }
    }

    /**
     * @param days, periodo de dias;
     * @param category, categoria do produto;
     * @param order, orem do retorno, asc/desc;
     * @return lista de BatchStockListDueDateDTO ordenado pela data;ProductExceptionNotFound(Nao existe esta categoria
     */
    public BatchStockListDueDateDTO listBatchStockDueDate(Integer days, String category, String order) {
        BatchStockListDueDateDTO batchStockListDueDateDTO = listDueDateDays(days);
        List<BatchStock> batchStockList = batchStockRepository.findAll();
        List<BatchStockServiceDueDateDTO>  batchStockServiceDueDateList = new ArrayList<>();
        if (category.equals("FF")||category.equals("FS")||category.equals("RF")){
            batchStockList.forEach(b -> {
                 if (LocalDate.now().plusDays(days).isAfter(b.getDueDate()) &&
                         productService.find(b.getProductId()).getCategory().getName().equals(ESectionCategory.valueOf(category))) {
                     newList(batchStockServiceDueDateList,b.getBatchNumber(),b.getProductId(),b.getDueDate(),b.getCurrentQuantity());
                }
            });
            if (!batchStockServiceDueDateList.isEmpty()) {
                listAscDesc(order,batchStockListDueDateDTO,batchStockServiceDueDateList);
            }
                return batchStockListDueDateDTO;
        }else
            throw new ProductExceptionNotFound("Nao existe esta categoria!!!");

    }

    /**
     * @param order, orem do retorno, asc/desc;
     * @param batchStockListDueDateDTO;
     * @param batchStockServiceDueDateList ;
     */
    public void listAscDesc(String order,BatchStockListDueDateDTO batchStockListDueDateDTO,List<BatchStockServiceDueDateDTO> batchStockServiceDueDateList){
        switch (order) {
            case "desc":
            {
                batchStockListDueDateDTO.setBatchStock(batchStockServiceDueDateList.stream()
                        .sorted(Comparator.comparing(BatchStockServiceDueDateDTO::getDueDate).reversed()).collect(toList()));
                break;
            }
            case "asc": {
                batchStockListDueDateDTO.setBatchStock(batchStockServiceDueDateList.stream()
                        .sorted(Comparator.comparing(BatchStockServiceDueDateDTO::getDueDate)).collect(toList()));
                break;
            }
            default:
                throw new ProductExceptionNotFound("Codigo da ordenacao nao existe!!!");
        }
    }

    /**
     * @param batchStockServiceDueDateList;
     * @param number, batchNumber;
     * @param productId, id do produto;
     * @param dueDate, data de validade do produto;
     * @param quantity, qntidade do produto
     * @return batchStockServiceDueDateList;
     */
    public List<BatchStockServiceDueDateDTO> newList(List<BatchStockServiceDueDateDTO> batchStockServiceDueDateList,
                                                      Integer number, String productId,LocalDate dueDate, Integer quantity ){
        BatchStockServiceDueDateDTO batchStockServiceDueDateDTO = new BatchStockServiceDueDateDTO();
        batchStockServiceDueDateDTO.batchNumber(number);
        batchStockServiceDueDateDTO.productId(productId);
        batchStockServiceDueDateDTO.dueDate(dueDate);
        batchStockServiceDueDateDTO.quantity(quantity);
        batchStockServiceDueDateList.add(batchStockServiceDueDateDTO);
        return batchStockServiceDueDateList;
    }
}