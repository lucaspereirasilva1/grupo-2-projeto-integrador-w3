package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.exception.BatchStockException;
import br.com.meli.projetointegrador.exception.ProductExceptionNotFound;
import br.com.meli.projetointegrador.model.dto.*;
import br.com.meli.projetointegrador.model.entity.Agent;
import br.com.meli.projetointegrador.model.entity.BatchStock;
import br.com.meli.projetointegrador.model.entity.Product;
import br.com.meli.projetointegrador.model.enums.ESectionCategory;
import br.com.meli.projetointegrador.model.repository.BatchStockRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

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

    public BatchStockService(BatchStockRepository batchStockRepository,
                             SectionService sectionService,
                             AgentService agentService, ProductService productService) {
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
                sectionService.validSectionLength(product.getSection()))
                b.agent(agentService.find(agentDTO.getCpf()));
                b.section(sectionService.find(sectionDTO.getSectionCode()));
        });
        batchStockRepository.saveAll(listBatchStock);
    }

    /**
     * @param listBatchStock recebe uma lista bqaqtchStock;
     * @param agentDTO recebe um agenteDTO;
     * @param sectionDTO recebe uma sectionDTO;
     */
    public void putAll(List<BatchStock> listBatchStock, List<BatchStockDTO> listBatchStockDTO, AgentDTO agentDTO, SectionDTO sectionDTO) {
        for (int i = 0; i < listBatchStock.size(); i++) {
            for (int x = i; x < listBatchStockDTO.size(); x++) {
                if (productService.validProductSection(sectionDTO.getSectionCode()) &&
                        sectionService.validSectionLength(listBatchStock.get(i).getSection()))
                    listBatchStock.get(i).batchNumber(listBatchStockDTO.get(x).getBatchNumber());
                    listBatchStock.get(i).productId(listBatchStockDTO.get(x).getProductId());
                    listBatchStock.get(i).currentTemperature(listBatchStockDTO.get(x).getCurrentTemperature());
                    listBatchStock.get(i).minimumTemperature(listBatchStockDTO.get(x).getMinimumTemperature());
                    listBatchStock.get(i).initialQuantity(listBatchStockDTO.get(x).getInitialQuantity());
                    listBatchStock.get(i).currentQuantity(listBatchStockDTO.get(x).getCurrentQuantity());
                    listBatchStock.get(i).manufacturingDate(listBatchStockDTO.get(x).getManufacturingDate());
                    listBatchStock.get(i).manufacturingTime(listBatchStockDTO.get(x).getManufacturingTime());
                    listBatchStock.get(i).dueDate(listBatchStockDTO.get(x).getDueDate());
                    Agent agent = listBatchStock.get(i).getAgent();
                    agent.cpf(agentDTO.getCpf());
                    agent.name(agentDTO.getName());
                    listBatchStock.get(i).agent(agent);
                    x++;
            }
            batchStockRepository.save(listBatchStock.get(i));
        }
    }

    /**
     * @param productId recebe um Id de produto e codigo da ordenacao;
     * @return uma lista de de produtos baseado no Id.
     **********************************************/

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
                batchStockRepository.save(b);
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
     * @return lista de BatchStockListDueDateDTO ordenado pela data;
     * @return ProductExceptionNotFound(Nao existe esta categoria);
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
     * @return lista de BatchStockListDueDateDTO ordenado pela data asc/desc;
     * @return ProductExceptionNotFound(Nao existe esta categoria);
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
                throw new ProductExceptionNotFound("Nao existe esta categoria!!!");
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