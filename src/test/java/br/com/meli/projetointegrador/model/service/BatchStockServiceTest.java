package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.exception.BatchStockException;
import br.com.meli.projetointegrador.exception.ProductExceptionNotFound;
import br.com.meli.projetointegrador.model.dto.*;
import br.com.meli.projetointegrador.model.entity.*;
import br.com.meli.projetointegrador.model.enums.ESectionCategory;
import br.com.meli.projetointegrador.model.repository.BatchStockRepository;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
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
    private final SectionByCategoryService mockSectionByCategoryService = mock(SectionByCategoryService.class);
    private final BatchStockService batchStockService = new BatchStockService(mockBatchStockRepository,
            mockSectionService, mockAgentService, mockProductService, mockSectionByCategoryService);

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
                warehouse(warehouse).
                build();

        Product product = new Product()
                .productId("LE")
                .productName("leite")
                .build();

        BatchStockDTO batchStockDTO = new BatchStockDTO()
                .batchNumber(1)
                .productId("QJ")
                .currentTemperature(10.0F)
                .minimumTemperature(5.0F)
                .initialQuantity(1)
                .currentQuantity(5)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalTime.now())
                .dueDate(LocalDate.now())
                .build();

        when(mockProductService.find(anyString()))
                .thenReturn(product);
        when(mockSectionByCategoryService.validProductSection(any(), any())).
                thenReturn(true);
        when(mockSectionService.validSectionLength(any(Section.class))).
                thenReturn(true);
        when(mockAgentService.find(anyString())).
                thenReturn(agent);
        when(mockSectionService.find(anyString())).
                thenReturn(section);
        when(mockBatchStockRepository.saveAll(anyList()))
                .thenReturn(Collections.emptyList());

        final List<BatchStock> batchStockList = batchStockService.postAll(Collections.singletonList(batchStockDTO), agentDTO, sectionDTO);
        verify(mockBatchStockRepository, times(1)).saveAll(anyList());
        assertFalse(batchStockList.isEmpty());
        batchStockList.forEach(b -> {
            assertEquals(batchStockDTO.getBatchNumber(), b.getBatchNumber());
            assertEquals(batchStockDTO.getProductId(), b.getProductId());
            assertEquals(batchStockDTO.getCurrentTemperature(), b.getCurrentTemperature());
            assertEquals(batchStockDTO.getMinimumTemperature(), b.getMinimumTemperature());
            assertEquals(batchStockDTO.getInitialQuantity(), b.getInitialQuantity());
            assertEquals(batchStockDTO.getCurrentQuantity(), b.getCurrentQuantity());
            assertEquals(batchStockDTO.getManufacturingDate(), b.getManufacturingDate());
            assertEquals(LocalTime.of(batchStockDTO.getManufacturingTime().getHour(),
                        batchStockDTO.getManufacturingTime().getMinute()),
                    LocalTime.of(b.getManufacturingTime().getHour(),
                            b.getManufacturingTime().getMinute()));
        });
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
                .warehouseCode("SP")
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
                warehouse(warehouse).
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
                .manufacturingTime(LocalTime.now())
                .dueDate(LocalDate.now())
                .build();

        Product product = new Product()
                .productId("DA")
                .productName("danone")
                .category(new SectionCategory())
                .productPrice(new BigDecimal("2.0"))
                .dueDate(LocalDate.now().plusWeeks(5))
                .build();

        when(mockProductService.find(anyString()))
                .thenReturn(product);
        when(mockSectionByCategoryService.validProductSection(any(), any())).
                thenReturn(true);
        when(mockSectionService.validSectionLength(any(Section.class))).
                thenReturn(true);
        when(mockBatchStockRepository.save(any(BatchStock.class)))
                .thenReturn(batchStock);
        when(mockAgentService.find(anyString()))
                .thenReturn(agent);


        batchStockService.putAll(Collections.singletonList(batchStock), Collections.singletonList(batchStockDTO)
                ,agentDTO, sectionDTO);

        verify(mockBatchStockRepository, times(1)).saveAll(anyList());
    }

    @Test
    void putAllExceptionTest() {
        SectionDTO sectionDTO = new SectionDTO()
                .sectionCode("LA")
                .warehouseCode("SP")
                .build();

        AgentDTO agentDTO = new AgentDTO()
                .cpf("11122233344")
                .name("lucas")
                .warehouseCode("SP")
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
                warehouse(warehouse).
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
                .batchNumber(5)
                .productId("oj")
                .currentTemperature(10.0F)
                .minimumTemperature(5.0F)
                .initialQuantity(1)
                .currentQuantity(5)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalTime.now())
                .dueDate(LocalDate.now())
                .build();

        Product product = new Product()
                .productId("DA")
                .productName("danone")
                .category(new SectionCategory())
                .productPrice(new BigDecimal("2.0"))
                .dueDate(LocalDate.now().plusWeeks(5))
                .build();

        when(mockProductService.find(anyString()))
                .thenReturn(product);

        when(mockSectionByCategoryService.validProductSection(any(Section.class), any(SectionCategory.class))).
                thenReturn(true);
        when(mockSectionService.validSectionLength(any(Section.class))).
                thenReturn(true);
        when(mockBatchStockRepository.save(any(BatchStock.class)))
                .thenReturn(batchStock);
        when(mockAgentService.find(anyString()))
                .thenReturn(agent);

        BatchStockException batchStockException = assertThrows
                (BatchStockException.class,() ->
                        batchStockService.putAll(Collections.singletonList(batchStock), Collections.singletonList(batchStockDTO)
                                ,agentDTO, sectionDTO));

        String menssagemEsperada = "Divergencia entre dados de entrada e do banco!!!";

        assertTrue(menssagemEsperada.contains(batchStockException.getMessage()));
    }

    @Test
    void updateBatchStockIdTest() {
        List<ProductPurchaseOrderDTO> listProductPurchaseOrderDTO = new ArrayList<>();
        ProductPurchaseOrderDTO productPurchaseOrderDTO1 = new ProductPurchaseOrderDTO()
                .productId("LE")
                .quantity(1)
                .build();
        ProductPurchaseOrderDTO productPurchaseOrderDTO2 = new ProductPurchaseOrderDTO()
                .productId("QJ")
                .quantity(1)
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
                .initialQuantity(2)
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
                .initialQuantity(2)
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

        batchStockService.updateBatchStock(listProductPurchaseOrderDTO, "teste");
        verify(mockBatchStockRepository, times(listProductPurchaseOrderDTO.size())).findAllByProductId(anyString());
        verify(mockBatchStockRepository, times(listProductPurchaseOrderDTO.size() + listProductPurchaseOrderDTO.size())).save(any(BatchStock.class));
    }

    @Test
    void updateBatchStockTest() {
        List<ProductPurchaseOrderDTO> listProductPurchaseOrderDTO = new ArrayList<>();
        ProductPurchaseOrderDTO productPurchaseOrderDTO1 = new ProductPurchaseOrderDTO()
                .productId("LE")
                .quantity(1)
                .build();
        ProductPurchaseOrderDTO productPurchaseOrderDTO2 = new ProductPurchaseOrderDTO()
                .productId("QJ")
                .quantity(1)
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
                .initialQuantity(2)
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
                .initialQuantity(2)
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

        batchStockService.updateBatchStock(listProductPurchaseOrderDTO, "");
        verify(mockBatchStockRepository, times(listProductPurchaseOrderDTO.size())).findAllByProductId(anyString());
        verify(mockBatchStockRepository, times(listProductPurchaseOrderDTO.size() +
                listProductPurchaseOrderDTO.size())).save(any(BatchStock.class));
    }

    @Test
    void validListProductIdTest(){
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
                .category(new SectionCategory().name(ESectionCategory.FF))
                .build();

        when(mockBatchStockRepository.findAllByProductId(anyString())).thenReturn(batchStockList);
        when(mockProductService.find(anyString())).thenReturn(product);

        assertEquals(batchStockResponseDTO, batchStockService.listProductId(batchStockResponseDTO.getProductId(),""));
    }

    @Test
    void validListProductIdNotExistTest(){
        when(mockBatchStockRepository.findAllByProductId(anyString()))
                .thenReturn(Collections.emptyList());

        BatchStockException batchStockException = assertThrows
                (BatchStockException.class,() ->
                        batchStockService.listProductId("ME",""));

        String menssagemEsperada = "Nao existe estoques para esse produto!!!";

        assertTrue(menssagemEsperada.contains(batchStockException.getMessage()));
    }

    @Test
    void validListProductIdExpiredDateTest(){
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
                .category(new SectionCategory().name(ESectionCategory.FF))
                .build();

        when(mockBatchStockRepository.findAllByProductId(anyString())).thenReturn(batchStockList);
        when(mockProductService.find(anyString())).thenReturn(product);

        assertEquals(batchStockResponseDTO, batchStockService.listProductId(batchStockResponseDTO.getProductId(),"L"));
    }

    @Test
    void validListProductIdByOrderQuantityTest(){
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
                .category(new SectionCategory().name(ESectionCategory.FF))
                .build();

        when(mockBatchStockRepository.findAllByProductId(anyString())).thenReturn(batchStockList);
        when(mockProductService.find(anyString())).thenReturn(product);

        assertEquals(batchStockResponseDTO, batchStockService.listProductId(batchStockResponseDTO.getProductId(),"C"));
    }

    @Test
    void validListProductIdByOrderDueDateTest(){
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
                .category(new SectionCategory().name(ESectionCategory.FF))
                .build();

        when(mockBatchStockRepository.findAllByProductId(anyString())).thenReturn(batchStockList);
        when(mockProductService.find(anyString())).thenReturn(product);

        assertEquals(batchStockResponseDTO, batchStockService.listProductId(batchStockResponseDTO.getProductId(),"F"));
    }

    @Test
    void validListProductIdByOrderNotExistTest(){
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
                .category(new SectionCategory().name(ESectionCategory.FF))
                .build();

        when(mockBatchStockRepository.findAllByProductId(anyString())).thenReturn(batchStockList);
        when(mockProductService.find(anyString())).thenReturn(product);

        ProductExceptionNotFound productExceptionNotFound = assertThrows
                (ProductExceptionNotFound.class,() ->
                        batchStockService.listProductId("QJ","T"));

        String menssagemEsperada = "Codigo do filtro nao existe!";

        assertTrue(menssagemEsperada.contains(productExceptionNotFound.getMessage()));
    }

    @Test
    void updateBatchStockNotExistTest() {
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
                        batchStockService.updateBatchStock(listProductPurchaseOrderDTO, ""));

        String menssagemEsperada = "Nao foi encontrado estoque para esse produto!!!";

        assertTrue(menssagemEsperada.contains(batchStockException.getMessage()));
    }

    @Test
    void dueDataProduct() {
        assertTrue(batchStockService.dueDataProduct(LocalDate.of(2022, 3, 21)));
    }

    @Test
    void validQuantityProductBatchStock(){
        List<BatchStock> listBatchStock = new ArrayList<>();

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
                name("jhony").
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
        listBatchStock.add(batchStockUm);

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
        listBatchStock.add(batchStockDois);

        when(mockBatchStockRepository.findAllByProductId(anyString())).thenReturn(listBatchStock);

        Integer quantityProductBatchStock = batchStockService.quantityProductBatchStock("QJ", "SP");

        assertEquals(quantityProductBatchStock,9);
    }

    @Test
    void listDueDateDaysTest(){

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
    void listDueDateDaysExceptionTest(){
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

    @Test
    void listBatchStockDueDateAscTest(){
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
                .batchNumber(1)
                .productId("QJ")
                .currentTemperature(10.0F)
                .minimumTemperature(5.0F)
                .initialQuantity(1)
                .currentQuantity(5)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalDateTime.now())
                .dueDate(LocalDate.now().plusWeeks(+1))
                .agent(agent)
                .section(section)
                .build();

        BatchStock batchStockDois = new BatchStock()
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

        Product productUm = new Product()
                .productId("LE")
                .productName("leite")
                .category(new SectionCategory().name(ESectionCategory.FF))
                .build();

        Product productDois = new Product()
                .productId("QJ")
                .productName("queijo")
                .category(new SectionCategory().name(ESectionCategory.FF))
                .build();

        when(mockProductService.find(productUm.getProductId()))
                .thenReturn(productUm);
        when(mockProductService.find(productDois.getProductId()))
                .thenReturn(productDois);

        batchStockList.add(batchStockUm);
        batchStockList.add(batchStockDois);
        when(mockBatchStockRepository.findAll()).thenReturn(batchStockList);
        BatchStockListDueDateDTO batchStockListDueDateDTO = batchStockService.listBatchStockDueDate
                (30,"FF","asc");

        assertTrue(batchStockListDueDateDTO.getBatchStock().get(0).getDueDate().isBefore(batchStockListDueDateDTO.getBatchStock().get(1).getDueDate()));
    }

    @Test
    void listBatchStockDueDateDescTest(){
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
                .batchNumber(1)
                .productId("QJ")
                .currentTemperature(10.0F)
                .minimumTemperature(5.0F)
                .initialQuantity(1)
                .currentQuantity(5)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalDateTime.now())
                .dueDate(LocalDate.now().plusWeeks(+1))
                .agent(agent)
                .section(section)
                .build();

        BatchStock batchStockDois = new BatchStock()
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

        Product productUm = new Product()
                .productId("LE")
                .productName("leite")
                .category(new SectionCategory().name(ESectionCategory.FF))
                .build();


        Product productDois = new Product()
                .productId("QJ")
                .productName("queijo")
                .category(new SectionCategory().name(ESectionCategory.FF))
                .build();


        when(mockProductService.find(productDois.getProductId()))
                .thenReturn(productUm);
        when(mockProductService.find(productDois.getProductId()))
                .thenReturn(productDois);

        batchStockList.add(batchStockUm);
        batchStockList.add(batchStockDois);
        when(mockBatchStockRepository.findAll()).thenReturn(batchStockList);
        BatchStockListDueDateDTO batchStockListDueDateDTO = batchStockService.listBatchStockDueDate
                (30,"FF","desc");

        assertTrue(batchStockListDueDateDTO.getBatchStock().get(0).getDueDate().isAfter(batchStockListDueDateDTO.getBatchStock().get(1).getDueDate()));
    }

    @Test
    void listBatchStockDueDateExceptionOrderTest(){
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

        Product productDois = new Product()
                .productId("QJ")
                .productName("queijo")  
                .category(new SectionCategory().name(ESectionCategory.FF))
                .build();

        when(mockProductService.find(anyString()))
                .thenReturn(productDois);

        batchStockList.add(batchStockUm);
        when(mockBatchStockRepository.findAll()).thenReturn(batchStockList);

        ProductExceptionNotFound productExceptionNotFound = assertThrows
                (ProductExceptionNotFound.class,() -> batchStockService.listBatchStockDueDate
                        (30,"FF","a5sc"));

        String menssagemEsperada = "Codigo da ordenacao nao existe!!!";

        assertTrue(menssagemEsperada.contains(productExceptionNotFound.getMessage()));

    }

    @Test
    void listBatchStockDueDateExceptionCategoryNonexistentTest(){
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

        Product productDois = new Product()
                .productId("QJ")
                .productName("queijo")
                .category(new SectionCategory().name(ESectionCategory.FF))
                .build();

        when(mockProductService.find(anyString()))
                .thenReturn(productDois);

        batchStockList.add(batchStockUm);
        when(mockBatchStockRepository.findAll()).thenReturn(batchStockList);

        ProductExceptionNotFound productExceptionNotFound = assertThrows
                (ProductExceptionNotFound.class,() -> batchStockService.listBatchStockDueDate
                        (30,"F5","asc"));

        String menssagemEsperada = "Nao existe esta categoria!!!";

        assertTrue(menssagemEsperada.contains(productExceptionNotFound.getMessage()));
    }

    @Test
    void listBatchStockDueDateExceptionCategoryTest(){
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

         Product productDois = new Product()
                .productId("QJ")
                .productName("queijo")
                .category(new SectionCategory().name(ESectionCategory.FF))
                .build();

        when(mockProductService.find(anyString()))
                .thenReturn(productDois);

        batchStockList.add(batchStockUm);
        when(mockBatchStockRepository.findAll()).thenReturn(batchStockList);

        ProductExceptionNotFound productExceptionNotFound = assertThrows
                (ProductExceptionNotFound.class,() -> batchStockService.listBatchStockDueDate
                        (30,"FS","asc"));

        String menssagemEsperada = "Nao existe estoque com esta categoria!!!";

        assertTrue(menssagemEsperada.contains(productExceptionNotFound.getMessage()));
    }

    @Test
    void postAllPersistenceError() {
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
                .build();

        BatchStockDTO batchStockDTO = new BatchStockDTO()
                .batchNumber(1)
                .productId("QJ")
                .currentTemperature(10.0F)
                .minimumTemperature(5.0F)
                .initialQuantity(1)
                .currentQuantity(5)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalTime.now())
                .dueDate(LocalDate.now().plusWeeks(5))
                .build();

        when(mockProductService.find(anyString()))
                .thenReturn(product);
        when(mockSectionByCategoryService.validProductSection(any(Section.class), any(SectionCategory.class))).
                thenReturn(true);
        when(mockSectionService.validSectionLength(any(Section.class))).
                thenReturn(true);
        when(mockAgentService.find(anyString())).
                thenReturn(agent);
        when(mockSectionService.find(anyString())).
                thenReturn(section);
        when(mockBatchStockRepository.saveAll(anyList()))
                .thenThrow(new DataAccessException("") {
                });

        DataAccessException dataAccessException = assertThrows
                (DataAccessException.class,() ->
                        batchStockService.postAll(Collections.singletonList(batchStockDTO), agentDTO, sectionDTO));

        String menssagemEsperada = "Erro durante a persistencia no banco!!!";

        assertTrue(menssagemEsperada.contains(Objects.requireNonNull(dataAccessException.getMessage())));
    }

    @Test
    void putAllPersistenceError() {
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
                .build();

        BatchStockDTO batchStockDTO = new BatchStockDTO()
                .batchNumber(1)
                .productId("QJ")
                .currentTemperature(10.0F)
                .minimumTemperature(5.0F)
                .initialQuantity(1)
                .currentQuantity(5)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalTime.now())
                .dueDate(LocalDate.now().plusWeeks(5))
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
        when(mockSectionByCategoryService.validProductSection(any(Section.class), any(SectionCategory.class))).
                thenReturn(true);
        when(mockSectionService.validSectionLength(any(Section.class))).
                thenReturn(true);
        when(mockAgentService.find(anyString())).
                thenReturn(agent);
        when(mockSectionService.find(anyString())).
                thenReturn(section);
        when(mockBatchStockRepository.saveAll(anyList()))
                .thenThrow(new DataAccessException("") {
                });

        DataAccessException dataAccessException = assertThrows
                (DataAccessException.class,() ->
                        batchStockService.putAll(Collections.singletonList(batchStock),
                                Collections.singletonList(batchStockDTO),
                                agentDTO,
                                sectionDTO));

        String menssagemEsperada = "Erro durante a persistencia no banco!!!";

        assertTrue(menssagemEsperada.contains(Objects.requireNonNull(dataAccessException.getMessage())));
    }

    @Test
    void updateBatchStockPersistenceError() {
        List<ProductPurchaseOrderDTO> listProductPurchaseOrderDTO = new ArrayList<>();
        ProductPurchaseOrderDTO productPurchaseOrderDTO1 = new ProductPurchaseOrderDTO()
                .productId("LE")
                .quantity(1)
                .build();
        ProductPurchaseOrderDTO productPurchaseOrderDTO2 = new ProductPurchaseOrderDTO()
                .productId("QJ")
                .quantity(1)
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
                .initialQuantity(2)
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
                .initialQuantity(2)
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
                .thenThrow(new DataAccessException("") {
                });

        DataAccessException dataAccessException = assertThrows
                (DataAccessException.class,() ->
                        batchStockService.updateBatchStock(listProductPurchaseOrderDTO, ""));

        String menssagemEsperada = "Erro durante a persistencia no banco!!!";

        assertTrue(menssagemEsperada.contains(Objects.requireNonNull(dataAccessException.getMessage())));
    }

}