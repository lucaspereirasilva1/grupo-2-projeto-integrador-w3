package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.exception.BatchStockException;
import br.com.meli.projetointegrador.exception.ProductExceptionNotFound;
import br.com.meli.projetointegrador.model.dto.*;
import br.com.meli.projetointegrador.model.entity.*;
import br.com.meli.projetointegrador.model.enums.ESectionCategory;
import br.com.meli.projetointegrador.model.repository.BatchStockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * @author Jhony Zuim / Lucas Pereira / Edemilson Nobre / Rafael Vicente
 * @version 1.0.0
 * @since 15/10/2021
 * Camada de testes unitarios do service responsavel pela regra de negocio relacionada ao batchStock
 */

public class BatchStockServiceTest {

    private final BatchStockRepository mockBatchStockRepository = mock(BatchStockRepository.class);
    private final SectionService mockSectionService = mock(SectionService.class);
    private final AgentService mockAgentService = mock(AgentService.class);
    private final ProductService mockProductService = mock(ProductService.class);
    private final BatchStockService batchStockService = new BatchStockService(mockBatchStockRepository,
            mockSectionService, mockAgentService, mockProductService);

    @BeforeEach
    void setUp() {
    }

    @Test
    void postAllTest() {
        SectionDTO sectionDTO = new SectionDTO()
                .sectionCode("LA")
                .warehouseCode("SP")
                .build();

        AgentDTO agentDTO = new AgentDTO()
                .cpf("11122233344")
                .name("lucas")
                .build();

        Warehouse warehouse = new Warehouse()
                .warehouseCode("SP")
                .warehouseName("sao paulo")
                .build();

        Section section = new Section()
                .sectionCode("LA")
                .sectionName("laticinios")
                .maxLength(10)
                .warehouse(warehouse)
                .build();

        Agent agent = new Agent().
                cpf("11122233344").
                name("lucas").
                build();

        Product product = new Product()
                .productId("LE")
                .productName("leite")
                .section(section)
                .build();

        BatchStock batchStock = new BatchStock()
                .batchNumber(1)
                .productId("QJ")
                .currentTemperature(10.0F)
                .minimumTemperature(5.0F)
                .initialQuantity(1)
                .currentQuantity(5)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalDateTime.now())
                .dueDate(LocalDate.now())
                .agent(agent)
                .section(section)
                .build();

        when(mockProductService.find(anyString()))
                .thenReturn(product);
        when(mockProductService.validProductSection(anyString())).
                thenReturn(true);
        when(mockSectionService.validSectionLength(any(Section.class))).
                thenReturn(true);
        when(mockAgentService.find(anyString())).
                thenReturn(agent);
        when(mockSectionService.find(anyString())).
                thenReturn(section);
        when(mockBatchStockRepository.saveAll(anyList()))
                .thenReturn(Collections.singletonList(batchStock));

        batchStockService.postAll(Collections.singletonList(batchStock), agentDTO, sectionDTO);

        verify(mockBatchStockRepository, times(1)).saveAll(anyList());
    }

    @Test
    void putAllTest() {
        SectionDTO sectionDTO = new SectionDTO()
                .sectionCode("LA")
                .warehouseCode("SP")
                .build();

        AgentDTO agentDTO = new AgentDTO()
                .cpf("11122233344")
                .name("lucas")
                .build();

        Warehouse warehouse = new Warehouse()
                .warehouseCode("SP")
                .warehouseName("sao paulo")
                .build();

        Section section = new Section()
                .sectionCode("LA")
                .sectionName("laticinios")
                .maxLength(10)
                .warehouse(warehouse)
                .build();

        Agent agent = new Agent().
                cpf("11122233344").
                name("lucas").
                build();

        BatchStock batchStock = new BatchStock()
                .batchNumber(1)
                .productId("QJ")
                .currentTemperature(10.0F)
                .minimumTemperature(5.0F)
                .initialQuantity(1)
                .currentQuantity(5)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalDateTime.now())
                .dueDate(LocalDate.now())
                .agent(agent)
                .section(section)
                .build();

        BatchStockDTO batchStockDTO = new BatchStockDTO()
                .batchNumber(1)
                .productId("QJ")
                .currentTemperature(10.0F)
                .minimumTemperature(5.0F)
                .initialQuantity(1)
                .currentQuantity(5)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalDateTime.now())
                .dueDate(LocalDate.now())
                .build();

        when(mockProductService.validProductSection(anyString())).
                thenReturn(true);
        when(mockSectionService.validSectionLength(any(Section.class))).
                thenReturn(true);
        when(mockBatchStockRepository.save(any(BatchStock.class)))
                .thenReturn(batchStock);

        batchStockService.putAll(Collections.singletonList(batchStock), Collections.singletonList(batchStockDTO)
                ,agentDTO, sectionDTO);

        verify(mockBatchStockRepository, times(1)).save(any(BatchStock.class));
    }

    @Test
    void updateBatchStock() {
        List<ProductPurchaseOrderDTO> listProductPurchaseOrderDTO = new ArrayList<>();
        ProductPurchaseOrderDTO productPurchaseOrderDTO1 = new ProductPurchaseOrderDTO()
                .productId("LE")
                .quantity(5)
                .build();
        ProductPurchaseOrderDTO productPurchaseOrderDTO2 = new ProductPurchaseOrderDTO()
                .productId("QJ")
                .quantity(3)
                .build();
        listProductPurchaseOrderDTO.add(productPurchaseOrderDTO1);
        listProductPurchaseOrderDTO.add(productPurchaseOrderDTO2);

        Warehouse warehouse = new Warehouse()
                .warehouseCode("SP")
                .warehouseName("Sao Paulo")
                .build();

        Section section = new Section()
                .sectionCode("LA")
                .sectionName("Laticionios")
                .warehouse(warehouse)
                .maxLength(10)
                .build();

        Agent agent = new Agent()
                .name("lucas")
                .cpf("11122233344")
                .warehouse(warehouse)
                .build();

        BatchStock batchStock = new BatchStock()
                .batchNumber(1)
                .productId("QJ")
                .currentTemperature(10.0F)
                .minimumTemperature(5.0F)
                .initialQuantity(1)
                .currentQuantity(2)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalDateTime.now())
                .dueDate(LocalDate.now())
                .agent(agent)
                .section(section)
                .build();

        BatchStock batchStockDois = new BatchStock()
                .batchNumber(2)
                .productId("QJ")
                .currentTemperature(10.0F)
                .minimumTemperature(5.0F)
                .initialQuantity(1)
                .currentQuantity(2)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalDateTime.now())
                .dueDate(LocalDate.now())
                .agent(agent)
                .section(section)
                .build();

        when(mockBatchStockRepository.findAllByProductId(anyString()))
                .thenReturn(Arrays.asList(batchStock, batchStockDois));
        when(mockBatchStockRepository.save(any(BatchStock.class)))
                .thenReturn(new BatchStock());

        batchStockService.updateBatchStock(listProductPurchaseOrderDTO);
        verify(mockBatchStockRepository, times(listProductPurchaseOrderDTO.size())).findAllByProductId(anyString());
        verify(mockBatchStockRepository, times(listProductPurchaseOrderDTO.size() + listProductPurchaseOrderDTO.size())).save(any(BatchStock.class));
    }

    @Test
    void validListProductId(){
        List<BatchStock> batchStockList = new ArrayList<>();
        List<BatchStockListProductDTO> batchStockListProductDTOList = new ArrayList<>();

        BatchStockListProductDTO batchStockListProductDTO = new BatchStockListProductDTO()
                .batchNumber(1)
                .currentQuantity(5)
                .dueDate(LocalDate.now().plusWeeks(+4))
                .build();

        batchStockListProductDTOList.add(batchStockListProductDTO);

        SectionDTO sectionDTO = new SectionDTO()
                .sectionCode("LA")
                .warehouseCode("SP")
                .build();

        BatchStockResponseDTO batchStockResponseDTO = new BatchStockResponseDTO()
                .sectionDTO(sectionDTO)
                .productId("QJ")
                .batchStock(batchStockListProductDTOList)
                .build();

        Warehouse warehouse = new Warehouse()
                .warehouseCode("SP")
                .warehouseName("sao paulo")
                .build();

        Section section = new Section()
                .sectionCode("LA")
                .sectionName("laticinios")
                .maxLength(10)
                .warehouse(warehouse)
                .build();

        Agent agent = new Agent().
                cpf("11122233344").
                name("lucas").
                build();

        BatchStock batchStockUm = new BatchStock()
                .batchNumber(1)
                .productId("QJ")
                .currentTemperature(10.0F)
                .minimumTemperature(5.0F)
                .initialQuantity(1)
                .currentQuantity(5)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalDateTime.now())
                .dueDate(LocalDate.now().plusWeeks(+4))
                .agent(agent)
                .section(section)
                .build();

        batchStockList.add(batchStockUm);

        Product product = new Product()
                .productId("QJ")
                .productName("Queijo")
                .section(section)
                .category(new SectionCategory().name(ESectionCategory.FF))
                .build();

        when(mockBatchStockRepository.findAllByProductId(anyString())).thenReturn(batchStockList);
        when(mockProductService.find(anyString())).thenReturn(product);

        assertEquals(batchStockResponseDTO, batchStockService.listProductId(batchStockResponseDTO.getProductId(),""));
    }

    @Test
    void validListProductIdNotExist(){
        when(mockBatchStockRepository.findAllByProductId(anyString()))
                .thenReturn(Collections.emptyList());

        BatchStockException batchStockException = assertThrows
                (BatchStockException.class,() ->
                        batchStockService.listProductId("ME",""));

        String menssagemEsperada = "Nao existe estoques para esse produto!!!";

        assertTrue(menssagemEsperada.contains(batchStockException.getMessage()));
    }

    @Test
    void validListProductIdExpiredDate(){
        Warehouse warehouse = new Warehouse()
                .warehouseCode("SP")
                .warehouseName("sao paulo")
                .build();

        Section section = new Section()
                .sectionCode("LA")
                .sectionName("laticinios")
                .maxLength(10)
                .warehouse(warehouse)
                .build();

        Agent agent = new Agent().
                cpf("11122233344").
                name("lucas").
                build();

        when(mockBatchStockRepository.findAllByProductId(anyString()))
                .thenReturn(Collections.singletonList(new BatchStock()
                        .batchNumber(1)
                        .productId("QJ")
                        .currentTemperature(10.0F)
                        .minimumTemperature(5.0F)
                        .initialQuantity(1)
                        .currentQuantity(5)
                        .manufacturingDate(LocalDate.now())
                        .manufacturingTime(LocalDateTime.now())
                        .dueDate(LocalDate.now())
                        .agent(agent)
                        .section(section)
                        .build()));

        ProductExceptionNotFound productExceptionNotFound = assertThrows
                (ProductExceptionNotFound.class,() ->
                        batchStockService.listProductId("ME",""));

        String menssagemEsperada = "Nao existe estoques vigentes para esse produto, por favor verifique os dados inseridos!!!";

        assertTrue(menssagemEsperada.contains(productExceptionNotFound.getMessage()));
    }

    @Test
    void validListProductIdByOrderStockTest(){
        List<BatchStock> batchStockList = new ArrayList<>();
        List<BatchStockListProductDTO> batchStockListProductDTOList = new ArrayList<>();

        BatchStockListProductDTO batchStockListProductDTO = new BatchStockListProductDTO()
                .batchNumber(1)
                .currentQuantity(5)
                .dueDate(LocalDate.now().plusWeeks(+4))
                .build();

        BatchStockListProductDTO batchStockListProductDTODois = new BatchStockListProductDTO()
                .batchNumber(2)
                .currentQuantity(5)
                .dueDate(LocalDate.now().plusWeeks(+4))
                .build();

        batchStockListProductDTOList.add(batchStockListProductDTO);
        batchStockListProductDTOList.add(batchStockListProductDTODois);

        SectionDTO sectionDTO = new SectionDTO()
                .sectionCode("LA")
                .warehouseCode("SP")
                .build();

        BatchStockResponseDTO batchStockResponseDTO = new BatchStockResponseDTO()
                .sectionDTO(sectionDTO)
                .productId("QJ")
                .batchStock(batchStockListProductDTOList)
                .build();

        Warehouse warehouse = new Warehouse()
                .warehouseCode("SP")
                .warehouseName("sao paulo")
                .build();

        Section section = new Section()
                .sectionCode("LA")
                .sectionName("laticinios")
                .maxLength(10)
                .warehouse(warehouse)
                .build();

        Agent agent = new Agent().
                cpf("11122233344").
                name("lucas").
                build();

        BatchStock batchStockUm = new BatchStock()
                .batchNumber(2)
                .productId("QJ")
                .currentTemperature(10.0F)
                .minimumTemperature(5.0F)
                .initialQuantity(1)
                .currentQuantity(5)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalDateTime.now())
                .dueDate(LocalDate.now().plusWeeks(+4))
                .agent(agent)
                .section(section)
                .build();
        BatchStock batchStockDois = new BatchStock()
                .batchNumber(1)
                .productId("QJ")
                .currentTemperature(10.0F)
                .minimumTemperature(5.0F)
                .initialQuantity(1)
                .currentQuantity(5)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalDateTime.now())
                .dueDate(LocalDate.now().plusWeeks(+4))
                .agent(agent)
                .section(section)
                .build();

        batchStockList.add(batchStockUm);
        batchStockList.add(batchStockDois);

        Product product = new Product()
                .productId("QJ")
                .productName("Queijo")
                .section(section)
                .category(new SectionCategory().name(ESectionCategory.FF))
                .build();

        when(mockBatchStockRepository.findAllByProductId(anyString())).thenReturn(batchStockList);
        when(mockProductService.find(anyString())).thenReturn(product);

        assertEquals(batchStockResponseDTO, batchStockService.listProductId(batchStockResponseDTO.getProductId(),"L"));
    }

    @Test
    void validListProductIdByOrderQuantity(){
        List<BatchStock> batchStockList = new ArrayList<>();
        List<BatchStockListProductDTO> batchStockListProductDTOList = new ArrayList<>();

        BatchStockListProductDTO batchStockListProductDTO = new BatchStockListProductDTO()
                .batchNumber(1)
                .currentQuantity(4)
                .dueDate(LocalDate.now().plusWeeks(+4))
                .build();

        BatchStockListProductDTO batchStockListProductDTODois = new BatchStockListProductDTO()
                .batchNumber(2)
                .currentQuantity(5)
                .dueDate(LocalDate.now().plusWeeks(+4))
                .build();

        batchStockListProductDTOList.add(batchStockListProductDTO);
        batchStockListProductDTOList.add(batchStockListProductDTODois);

        SectionDTO sectionDTO = new SectionDTO()
                .sectionCode("LA")
                .warehouseCode("SP")
                .build();

        BatchStockResponseDTO batchStockResponseDTO = new BatchStockResponseDTO()
                .sectionDTO(sectionDTO)
                .productId("QJ")
                .batchStock(batchStockListProductDTOList)
                .build();

        Warehouse warehouse = new Warehouse()
                .warehouseCode("SP")
                .warehouseName("sao paulo")
                .build();

        Section section = new Section()
                .sectionCode("LA")
                .sectionName("laticinios")
                .maxLength(10)
                .warehouse(warehouse)
                .build();

        Agent agent = new Agent().
                cpf("11122233344").
                name("lucas").
                build();

        BatchStock batchStockUm = new BatchStock()
                .batchNumber(2)
                .productId("QJ")
                .currentTemperature(10.0F)
                .minimumTemperature(5.0F)
                .initialQuantity(1)
                .currentQuantity(5)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalDateTime.now())
                .dueDate(LocalDate.now().plusWeeks(+4))
                .agent(agent)
                .section(section)
                .build();
        BatchStock batchStockDois = new BatchStock()
                .batchNumber(1)
                .productId("QJ")
                .currentTemperature(10.0F)
                .minimumTemperature(5.0F)
                .initialQuantity(1)
                .currentQuantity(4)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalDateTime.now())
                .dueDate(LocalDate.now().plusWeeks(+4))
                .agent(agent)
                .section(section)
                .build();

        batchStockList.add(batchStockUm);
        batchStockList.add(batchStockDois);

        Product product = new Product()
                .productId("QJ")
                .productName("Queijo")
                .section(section)
                .category(new SectionCategory().name(ESectionCategory.FF))
                .build();

        when(mockBatchStockRepository.findAllByProductId(anyString())).thenReturn(batchStockList);
        when(mockProductService.find(anyString())).thenReturn(product);

        assertEquals(batchStockResponseDTO, batchStockService.listProductId(batchStockResponseDTO.getProductId(),"C"));
    }

    @Test
    void validListProductIdByOrderDueDate(){
        List<BatchStock> batchStockList = new ArrayList<>();
        List<BatchStockListProductDTO> batchStockListProductDTOList = new ArrayList<>();

        BatchStockListProductDTO batchStockListProductDTO = new BatchStockListProductDTO()
                .batchNumber(1)
                .currentQuantity(4)
                .dueDate(LocalDate.now().plusWeeks(+4))
                .build();

        BatchStockListProductDTO batchStockListProductDTODois = new BatchStockListProductDTO()
                .batchNumber(2)
                .currentQuantity(5)
                .dueDate(LocalDate.now().plusWeeks(+7))
                .build();

        batchStockListProductDTOList.add(batchStockListProductDTO);
        batchStockListProductDTOList.add(batchStockListProductDTODois);

        SectionDTO sectionDTO = new SectionDTO()
                .sectionCode("LA")
                .warehouseCode("SP")
                .build();

        BatchStockResponseDTO batchStockResponseDTO = new BatchStockResponseDTO()
                .sectionDTO(sectionDTO)
                .productId("QJ")
                .batchStock(batchStockListProductDTOList)
                .build();

        Warehouse warehouse = new Warehouse()
                .warehouseCode("SP")
                .warehouseName("sao paulo")
                .build();

        Section section = new Section()
                .sectionCode("LA")
                .sectionName("laticinios")
                .maxLength(10)
                .warehouse(warehouse)
                .build();

        Agent agent = new Agent().
                cpf("11122233344").
                name("lucas").
                build();

        BatchStock batchStockUm = new BatchStock()
                .batchNumber(2)
                .productId("QJ")
                .currentTemperature(10.0F)
                .minimumTemperature(5.0F)
                .initialQuantity(1)
                .currentQuantity(5)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalDateTime.now())
                .dueDate(LocalDate.now().plusWeeks(+7))
                .agent(agent)
                .section(section)
                .build();
        BatchStock batchStockDois = new BatchStock()
                .batchNumber(1)
                .productId("QJ")
                .currentTemperature(10.0F)
                .minimumTemperature(5.0F)
                .initialQuantity(1)
                .currentQuantity(4)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalDateTime.now())
                .dueDate(LocalDate.now().plusWeeks(+4))
                .agent(agent)
                .section(section)
                .build();

        batchStockList.add(batchStockUm);
        batchStockList.add(batchStockDois);

        Product product = new Product()
                .productId("QJ")
                .productName("Queijo")
                .section(section)
                .category(new SectionCategory().name(ESectionCategory.FF))
                .build();

        when(mockBatchStockRepository.findAllByProductId(anyString())).thenReturn(batchStockList);
        when(mockProductService.find(anyString())).thenReturn(product);

        assertEquals(batchStockResponseDTO, batchStockService.listProductId(batchStockResponseDTO.getProductId(),"F"));
    }

    @Test
    void validListProductIdByOrderNotExist(){
        List<BatchStock> batchStockList = new ArrayList<>();
        List<BatchStockListProductDTO> batchStockListProductDTOList = new ArrayList<>();

        BatchStockListProductDTO batchStockListProductDTO = new BatchStockListProductDTO()
                .batchNumber(1)
                .currentQuantity(4)
                .dueDate(LocalDate.now().plusWeeks(+4))
                .build();

        BatchStockListProductDTO batchStockListProductDTODois = new BatchStockListProductDTO()
                .batchNumber(2)
                .currentQuantity(5)
                .dueDate(LocalDate.now().plusWeeks(+7))
                .build();

        batchStockListProductDTOList.add(batchStockListProductDTO);
        batchStockListProductDTOList.add(batchStockListProductDTODois);

        SectionDTO sectionDTO = new SectionDTO()
                .sectionCode("LA")
                .warehouseCode("SP")
                .build();

        BatchStockResponseDTO batchStockResponseDTO = new BatchStockResponseDTO()
                .sectionDTO(sectionDTO)
                .productId("QJ")
                .batchStock(batchStockListProductDTOList)
                .build();

        Warehouse warehouse = new Warehouse()
                .warehouseCode("SP")
                .warehouseName("sao paulo")
                .build();

        Section section = new Section()
                .sectionCode("LA")
                .sectionName("laticinios")
                .maxLength(10)
                .warehouse(warehouse)
                .build();

        Agent agent = new Agent().
                cpf("11122233344").
                name("lucas").
                build();

        BatchStock batchStockUm = new BatchStock()
                .batchNumber(2)
                .productId("QJ")
                .currentTemperature(10.0F)
                .minimumTemperature(5.0F)
                .initialQuantity(1)
                .currentQuantity(5)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalDateTime.now())
                .dueDate(LocalDate.now().plusWeeks(+7))
                .agent(agent)
                .section(section)
                .build();

        BatchStock batchStockDois = new BatchStock()
                .batchNumber(1)
                .productId("QJ")
                .currentTemperature(10.0F)
                .minimumTemperature(5.0F)
                .initialQuantity(1)
                .currentQuantity(4)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalDateTime.now())
                .dueDate(LocalDate.now().plusWeeks(+4))
                .agent(agent)
                .section(section)
                .build();

        batchStockList.add(batchStockUm);
        batchStockList.add(batchStockDois);

        Product product = new Product()
                .productId("QJ")
                .productName("Queijo")
                .section(section)
                .category(new SectionCategory().name(ESectionCategory.FF))
                .build();

        when(mockBatchStockRepository.findAllByProductId(anyString())).thenReturn(batchStockList);
        when(mockProductService.find(anyString())).thenReturn(product);

        ProductExceptionNotFound productExceptionNotFound = assertThrows
                (ProductExceptionNotFound.class,() ->
                                batchStockService.listProductId(batchStockResponseDTO.getProductId(),"T"));

        String menssagemEsperada = "Codigo do filtro nao existe!";

        assertTrue(menssagemEsperada.contains(productExceptionNotFound.getMessage()));
    }

    @Test
    void updateBatchStockNotExist() {
        List<ProductPurchaseOrderDTO> listProductPurchaseOrderDTO = new ArrayList<>();
        ProductPurchaseOrderDTO productPurchaseOrderDTO1 = new ProductPurchaseOrderDTO()
                .productId("LE")
                .quantity(5)
                .build();
        ProductPurchaseOrderDTO productPurchaseOrderDTO2 = new ProductPurchaseOrderDTO()
                .productId("QJ")
                .quantity(3)
                .build();
        listProductPurchaseOrderDTO.add(productPurchaseOrderDTO1);
        listProductPurchaseOrderDTO.add(productPurchaseOrderDTO2);

        when(mockBatchStockRepository.findAllByProductId(anyString()))
                .thenReturn(new ArrayList<>());
        when(mockBatchStockRepository.save(any(BatchStock.class)))
                .thenReturn(new BatchStock());

        BatchStockException batchStockException = assertThrows
                (BatchStockException.class,() ->
                        batchStockService.updateBatchStock(listProductPurchaseOrderDTO));

        String menssagemEsperada = "Nao foi encontrado estoque para esse produto!!!";

        assertTrue(menssagemEsperada.contains(batchStockException.getMessage()));
    }

    @Test
    void dueDataProductTest() {
        assertTrue(batchStockService.dueDataProduct(LocalDate.of(2022, 3, 21)));
    }

    @Test
    void validListDueDateDaysTest(){

        List<BatchStock> batchStockList = new ArrayList<>();

        Warehouse warehouse = new Warehouse()
                .warehouseCode("SP")
                .warehouseName("sao paulo")
                .build();

        Section section = new Section()
                .sectionCode("LA")
                .sectionName("laticinios")
                .maxLength(10)
                .warehouse(warehouse)
                .build();

        Agent agent = new Agent().
                cpf("11122233344").
                name("lucas").
                build();

        BatchStock batchStockUm = new BatchStock()
                .batchNumber(2)
                .productId("QJ")
                .currentTemperature(10.0F)
                .minimumTemperature(5.0F)
                .initialQuantity(1)
                .currentQuantity(5)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalDateTime.now())
                .dueDate(LocalDate.now().plusWeeks(+2))
                .agent(agent)
                .section(section)
                .build();

        batchStockList.add(batchStockUm);
        when(mockBatchStockRepository.findAll()).thenReturn(batchStockList);
        BatchStockListDueDateDTO batchStockListDueDateDTO = batchStockService.listDueDateDays(30);

        List<BatchStockServiceDueDateDTO> list = batchStockListDueDateDTO.getBatchStock().stream()
                .filter(b -> (LocalDate.now().plusDays(30).isAfter(b.getDueDate())))
                .collect(Collectors.toList());

        assertFalse(list.isEmpty());
        assertFalse(ObjectUtils.isEmpty(batchStockListDueDateDTO));
    }

    @Test
    void validNoListDueDateDaysTest(){
        List<BatchStock> batchStockList = new ArrayList<>();

        Warehouse warehouse = new Warehouse()
                .warehouseCode("SP")
                .warehouseName("sao paulo")
                .build();

        Section section = new Section()
                .sectionCode("LA")
                .sectionName("laticinios")
                .maxLength(10)
                .warehouse(warehouse)
                .build();

        Agent agent = new Agent().
                cpf("11122233344").
                name("lucas").
                build();

        BatchStock batchStockUm = new BatchStock()
                .batchNumber(2)
                .productId("QJ")
                .currentTemperature(10.0F)
                .minimumTemperature(5.0F)
                .initialQuantity(1)
                .currentQuantity(5)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalDateTime.now())
                .dueDate(LocalDate.now().plusWeeks(+12))
                .agent(agent)
                .section(section)
                .build();

        batchStockList.add(batchStockUm);
        when(mockBatchStockRepository.findAll()).thenReturn(batchStockList);

        ProductExceptionNotFound productExceptionNotFound = assertThrows
                (ProductExceptionNotFound.class,() -> batchStockService.listDueDateDays(30));

        String menssagemEsperada = "Nao existe estoque com este filtro!!!";

        assertTrue(menssagemEsperada.contains(productExceptionNotFound.getMessage()));
    }

}