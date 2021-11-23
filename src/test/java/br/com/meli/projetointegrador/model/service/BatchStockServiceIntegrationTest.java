package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.exception.BatchStockException;
import br.com.meli.projetointegrador.exception.ProductExceptionNotFound;
import br.com.meli.projetointegrador.model.dto.*;
import br.com.meli.projetointegrador.model.entity.*;
import br.com.meli.projetointegrador.model.enums.ESectionCategory;
import br.com.meli.projetointegrador.model.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Jhony Zuim / Lucas Pereira / Edemilson Nobre / Rafael Vicente
 * @version 1.0.0
 * @since 15/10/2021
 * Camada de teste integrado do service responsavel pela regra de negocio relacionada ao batchStock
 */

@SpringBootTest
public class BatchStockServiceIntegrationTest {

    @Autowired
    private BatchStockRepository batchStockRepository;

    @Autowired
    private BatchStockService batchStockService;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SectionCategoryRepository sectionCategoryRepository;

    @Autowired
    private SectionByCategoryRepository sectionByCategoryRepository;

    @BeforeEach
    void setUp() {
        clearBase();
        createData();
    }

    @AfterEach
    void cleanUpDatabase() {
        clearBase();
    }

    @Test
    void listProductId() {
        final BatchStockResponseDTO productResponseDTO = batchStockService.listProductId("QJ", "");
        assertFalse(ObjectUtils.isEmpty(productResponseDTO));
        assertEquals("QJ", productResponseDTO.getProductId());
    }

    @Test
    void postAllIntegrationTest() {
        batchStockRepository.deleteAll();
        SectionDTO sectionDTO = new SectionDTO()
                .sectionCode("LA")
                .warehouseCode("SP")
                .build();

        AgentDTO agentDTO = new AgentDTO()
                .cpf("11122233344")
                .name("lucas")
                .build();

        BatchStockDTO batchStockDTO = new BatchStockDTO()
                .batchNumber(2)
                .productId("QJ")
                .currentTemperature(10.0F)
                .minimumTemperature(5.0F)
                .initialQuantity(1)
                .currentQuantity(5)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalTime.now())
                .dueDate(LocalDate.now())
                .build();

        batchStockService.postAll(Collections.singletonList(batchStockDTO), agentDTO, sectionDTO);
        final List<BatchStock> batchStockRepositoryAll = batchStockRepository.findAll();
        assertFalse(batchStockRepositoryAll.isEmpty());
        batchStockRepositoryAll.forEach(b -> {
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
    void putAllIntegrationTest() {
        SectionDTO sectionDTO = new SectionDTO()
                .sectionCode("LA")
                .warehouseCode("SP")
                .build();

        AgentDTO agentDTO = new AgentDTO()
                .cpf("11122233344")
                .name("lucas")
                .build();

        BatchStockDTO batchStockDTO = new BatchStockDTO()
                .batchNumber(1)
                .productId("QJ")
                .currentTemperature(10.0F)
                .minimumTemperature(5.0F)
                .initialQuantity(2)
                .currentQuantity(2)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalTime.now())
                .dueDate(LocalDate.now())
                .build();

        BatchStock batchStock = batchStockRepository.findAll().get(0);

        batchStockService.putAll(Collections.singletonList(batchStock), Collections.singletonList(batchStockDTO),
                agentDTO , sectionDTO);

        final Optional<BatchStock> batchStockOptional = batchStockRepository.findById(batchStock.getId());
        assertEquals(2, batchStockOptional.orElse(new BatchStock()).getCurrentQuantity());
        assertEquals(2, batchStockOptional.orElse(new BatchStock()).getInitialQuantity());
    }

    @Test
    void validListProductIdExpiredDate() {
        batchStockRepository.deleteAll();
        BatchStock batchStock = new BatchStock()
                .batchNumber(1)
                .productId("QJ")
                .currentTemperature(10.0F)
                .minimumTemperature(5.0F)
                .initialQuantity(1)
                .currentQuantity(5)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalDateTime.now())
                .dueDate(LocalDate.of(2021, 12, 1))
                .agent(agentRepository.findByCpf("11122233344").orElse(new Agent()))
                .section(sectionRepository.findBySectionCode("LA").orElse(new Section()))
                .build();
        batchStockRepository.save(batchStock);

        ProductExceptionNotFound productExceptionNotFound = assertThrows
                (ProductExceptionNotFound.class, () ->
                        batchStockService.listProductId("QJ", "asc"));

        String menssagemEsperada = "Nao existe estoques vigentes para esse produto, por favor verifique os dados inseridos!!!";

        assertTrue(menssagemEsperada.contains(productExceptionNotFound.getMessage()));
    }

    @Test
    void validListProductIdByOrderStock(){
        List<BatchStockListProductDTO> batchStockListProductDTOList = new ArrayList<>();

        BatchStockListProductDTO batchStockListProductDTO = new BatchStockListProductDTO()
                .batchNumber(1)
                .currentQuantity(5)
                .dueDate(LocalDate.now().plusWeeks(4))
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

        assertEquals(batchStockResponseDTO, batchStockService.listProductId(batchStockResponseDTO.getProductId(),"L"));
    }

    @Test
    void validListProductIdByOrderQuantity(){
        List<BatchStockListProductDTO> batchStockListProductDTOList = new ArrayList<>();

        BatchStockListProductDTO batchStockListProductDTO = new BatchStockListProductDTO()
                .batchNumber(1)
                .currentQuantity(5)
                .dueDate(LocalDate.now().plusWeeks(4))
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

        assertEquals(batchStockResponseDTO, batchStockService.listProductId(batchStockResponseDTO.getProductId(),"C"));
    }

    @Test
    void validListProductIdByOrderDueDate(){
        List<BatchStockListProductDTO> batchStockListProductDTOList = new ArrayList<>();

        BatchStockListProductDTO batchStockListProductDTO = new BatchStockListProductDTO()
                .batchNumber(1)
                .currentQuantity(5)
                .dueDate(LocalDate.now().plusWeeks(4))
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

        assertEquals(batchStockResponseDTO, batchStockService.listProductId(batchStockResponseDTO.getProductId(),"F"));
    }

    @Test
    void validListProductIdByOrderNotExist(){
        ProductExceptionNotFound productExceptionNotFound = assertThrows
                (ProductExceptionNotFound.class,() ->
                        batchStockService.listProductId("QJ","T"));

        String menssagemEsperada = "Codigo do filtro nao existe!";

        assertTrue(menssagemEsperada.contains(productExceptionNotFound.getMessage()));
    }

    @Test
    void updateBatchStockNotExist() {
        List<ProductPurchaseOrderDTO> listProductPurchaseOrderDTO = new ArrayList<>();
        ProductPurchaseOrderDTO productPurchaseOrderDTO1 = new ProductPurchaseOrderDTO()
                .productId("CA")
                .quantity(5)
                .build();
        listProductPurchaseOrderDTO.add(productPurchaseOrderDTO1);

        BatchStockException batchStockException = assertThrows
                (BatchStockException.class,() ->
                        batchStockService.updateBatchStock(listProductPurchaseOrderDTO, ""));

        String menssagemEsperada = "Nao foi encontrado estoque para esse produto!!!";

        assertTrue(menssagemEsperada.contains(batchStockException.getMessage()));
    }

    @Test
    void updateBatchStockIntegration() {
        List<ProductPurchaseOrderDTO> listProductPurchaseOrderDTO = new ArrayList<>();
        ProductPurchaseOrderDTO productPurchaseOrderDTO2 = new ProductPurchaseOrderDTO()
                .productId("QJ")
                .quantity(3)
                .build();
        listProductPurchaseOrderDTO.add(productPurchaseOrderDTO2);

        batchStockService.updateBatchStock(listProductPurchaseOrderDTO, "");

        batchStockRepository.findAllByProductId(productPurchaseOrderDTO2.getProductId()).forEach(b ->
                assertEquals(2, b.getCurrentQuantity()));
    }

    @Test
    void listDueDateDaysTestIntegration(){
        BatchStockListDueDateDTO batchStockListDueDateDTO = batchStockService.listDueDateDays(30);

        List<BatchStockServiceDueDateDTO> list = batchStockListDueDateDTO.getBatchStock().stream()
                .filter(b -> (LocalDate.now().plusDays(30).isAfter(b.getDueDate())))
                .collect(Collectors.toList());

        assertFalse(list.isEmpty());
        assertFalse(ObjectUtils.isEmpty(batchStockListDueDateDTO));
    }

    @Test
    void listDueDateDaysExceptionTestIntegration(){
        batchStockRepository.deleteAll();
        BatchStock batchStockUm = new BatchStock()
                .batchNumber(1)
                .productId("QJ")
                .currentTemperature(10.0F)
                .minimumTemperature(5.0F)
                .initialQuantity(1)
                .currentQuantity(5)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalDateTime.now())
                .dueDate(LocalDate.now().plusWeeks(+12))
                .agent(agentRepository.findByCpf("11122233344").orElse(new Agent()))
                .section(sectionRepository.findBySectionCode("LA").orElse(new Section()))
                .build();
        batchStockRepository.save(batchStockUm);

        ProductExceptionNotFound productExceptionNotFound = assertThrows
                (ProductExceptionNotFound.class,() -> batchStockService.listDueDateDays(30));

        String menssagemEsperada = "Nao existe estoque com este filtro!!!";

        assertTrue(menssagemEsperada.contains(productExceptionNotFound.getMessage()));
    }

    @Test
    void listBatchStockDueDateAscTestIntegration(){
        batchStockRepository.deleteAll();
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
                .agent(agentRepository.findByCpf("11122233344").orElse(new Agent()))
                .section(sectionRepository.findBySectionCode("LA").orElse(new Section()))
                .build();
        batchStockRepository.save(batchStockUm);

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
                .agent(agentRepository.findByCpf("11122233344").orElse(new Agent()))
                .section(sectionRepository.findBySectionCode("LA").orElse(new Section()))
                .build();
        batchStockRepository.save(batchStockDois);

        BatchStockListDueDateDTO batchStockListDueDateDTO = batchStockService.listBatchStockDueDate
                (30,"FF","asc");

        assertTrue(batchStockListDueDateDTO.getBatchStock().get(0).getDueDate().isBefore(batchStockListDueDateDTO.getBatchStock().get(1).getDueDate()));
    }

    @Test
    void listBatchStockDueDateDescTestIntegration(){
        batchStockRepository.deleteAll();
        productRepository.deleteAll();

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
                .agent(agentRepository.findByCpf("11122233344").orElse(new Agent()))
                .section(sectionRepository.findBySectionCode("LA").orElse(new Section()))
                .build();
        batchStockRepository.save(batchStockUm);

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
                .agent(agentRepository.findByCpf("11122233344").orElse(new Agent()))
                .section(sectionRepository.findBySectionCode("LA").orElse(new Section()))
                .build();
        batchStockRepository.save(batchStockDois);

        Product productUm = new Product()
                .productId("LE")
                .productName("leite")
                .category(sectionCategoryRepository.findByName(ESectionCategory.FF).orElse(new SectionCategory()))
                .build();
        productRepository.save(productUm);

        Product productDois = new Product()
                .productId("QJ")
                .productName("queijo")
                .category(sectionCategoryRepository.findByName(ESectionCategory.FF).orElse(new SectionCategory()))
                .build();
        productRepository.save(productDois);

        BatchStockListDueDateDTO batchStockListDueDateDTO = batchStockService.listBatchStockDueDate
                (30,"FF","desc");

        assertTrue(batchStockListDueDateDTO.getBatchStock().get(0).getDueDate()
                .isAfter(batchStockListDueDateDTO.getBatchStock().get(1).getDueDate()));
    }

    @Test
    void listBatchStockDueDateExceptionOrderTestIntegration(){
        ProductExceptionNotFound productExceptionNotFound = assertThrows
                (ProductExceptionNotFound.class,() -> batchStockService.listBatchStockDueDate
                        (30,"FF","a5sc"));

        String menssagemEsperada = "Codigo da ordenacao nao existe!!!";
        assertTrue(menssagemEsperada.contains(productExceptionNotFound.getMessage()));
    }

    @Test
    void listBatchStockDueDateExceptionCategoryNonexistentTestIntegration(){
        ProductExceptionNotFound productExceptionNotFound = assertThrows
                (ProductExceptionNotFound.class,() -> batchStockService.listBatchStockDueDate
                        (30,"F5","asc"));

        String menssagemEsperada = "Nao existe esta categoria!!!";
        assertTrue(menssagemEsperada.contains(productExceptionNotFound.getMessage()));
    }

    @Test
    void listBatchStockDueDateExceptionCategoryTestIntegration(){
        ProductExceptionNotFound productExceptionNotFound = assertThrows
                (ProductExceptionNotFound.class,() -> batchStockService.listBatchStockDueDate
                        (30,"RF","asc"));

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

        BatchStockDTO batchStockParamDTO = new BatchStockDTO()
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

        DataAccessException dataAccessException = assertThrows
                (DataAccessException.class,() ->
                        batchStockService.postAll(Collections.singletonList(batchStockParamDTO), agentDTO, sectionDTO));

        String menssagemEsperada = "Erro durante a persistencia no banco!!!";

        assertTrue(menssagemEsperada.contains(Objects.requireNonNull(dataAccessException.getMessage())));
    }

    @Test
    void updateBatchStockIdTest() {
        List<ProductPurchaseOrderDTO> listProductPurchaseOrderDTO = new ArrayList<>();
        ProductPurchaseOrderDTO productPurchaseOrderDTO2 = new ProductPurchaseOrderDTO()
                .productId("QJ")
                .quantity(1)
                .build();
        listProductPurchaseOrderDTO.add(productPurchaseOrderDTO2);

        batchStockService.updateBatchStock(listProductPurchaseOrderDTO, "teste");
        final List<BatchStock> batchStockRepositoryAll = batchStockRepository.findAll();
        assertEquals(4, batchStockRepositoryAll.get(0).getCurrentQuantity());
    }

    void createData() {
        clearBase();
        Warehouse warehouse = new Warehouse()
                .warehouseCode("SP")
                .warehouseName("sao paulo")
                .build();
        warehouseRepository.save(warehouse);

        Agent agent = new Agent()
                .name("lucas")
                .cpf("11122233344")
                .warehouse(warehouse)
                .build();
        agentRepository.save(agent);

        Section section = new Section()
                .sectionCode("LA")
                .sectionName("laticinios")
                .maxLength(10)
                .warehouse(warehouse)
                .build();
        sectionRepository.save(section);

        SectionCategory sectionCategory = new SectionCategory()
                .name(ESectionCategory.FF)
                .build();
        sectionCategoryRepository.save(sectionCategory);

        SectionByCategory sectionByCategory = new SectionByCategory()
                .category(sectionCategory)
                .section(section)
                .build();
        sectionByCategoryRepository.save(sectionByCategory);

        Product product = new Product()
                .productId("QJ")
                .productName("queijo")
                .productPrice(new BigDecimal("2.0"))
                .dueDate(LocalDate.of(2022, 2, 1))
                .category(sectionCategory)
                .build();
        productRepository.save(product);

        BatchStock batchStock = new BatchStock()
                .batchNumber(1)
                .productId("QJ")
                .currentTemperature(10.0F)
                .minimumTemperature(5.0F)
                .initialQuantity(5)
                .currentQuantity(5)
                .manufacturingDate(LocalDate.now())
                .dueDate(LocalDate.now().plusWeeks(4))
                .manufacturingTime(LocalDateTime.now())
                .agent(agent)
                .section(section)
                .build();
        batchStockRepository.save(batchStock);
    }

    void clearBase() {
        batchStockRepository.deleteAll();
        warehouseRepository.deleteAll();
        agentRepository.deleteAll();
        sectionRepository.deleteAll();
        productRepository.deleteAll();
        sectionCategoryRepository.deleteAll();
        sectionByCategoryRepository.deleteAll();
    }
}
