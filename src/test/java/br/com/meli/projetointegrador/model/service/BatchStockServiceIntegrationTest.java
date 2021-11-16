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
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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

    @BeforeEach
    void setUp() {
        clearBase();
        createData();
    }

    @Test
    void listProductId() {
        final BatchStockResponseDTO productResponseDTO = batchStockService.listProductId("QJ", "");
        assertFalse(ObjectUtils.isEmpty(productResponseDTO));
        assertEquals("QJ", productResponseDTO.getProductId());
    }

    @AfterEach
    void cleanUpDatabase() {
        clearBase();
    }

    @Test
    void postAllIntegrationTest() {
        SectionDTO sectionDTO = new SectionDTO()
                .sectionCode("LA")
                .warehouseCode("SP")
                .build();

        AgentDTO agentDTO = new AgentDTO()
                .cpf("11122233344")
                .name("lucas")
                .build();

        BatchStock batchStock = new BatchStock()
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

        batchStockService.postAll(Collections.singletonList(batchStock), agentDTO, sectionDTO);

        final Optional<BatchStock> batchStockOptional = batchStockRepository.findById(batchStock.getId());
        assertTrue(batchStockOptional.isPresent());
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

        BatchStock batchStock = new BatchStock()
                .batchNumber(1)
                .productId("LE")
                .currentTemperature(10.0F)
                .minimumTemperature(5.0F)
                .initialQuantity(1)
                .currentQuantity(5)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalTime.now())
                .dueDate(LocalDate.now())
                .agent(agentRepository.findByCpf("11122233344").orElse(new Agent()))
                .section(sectionRepository.findBySectionCode("LA").orElse(new Section()))
                .build();
        batchStockRepository.save(batchStock);

        batchStockService.putAll(Collections.singletonList(batchStock), Collections.singletonList(batchStockDTO),
                agentDTO , sectionDTO);

        final Optional<BatchStock> batchStockOptional = batchStockRepository.findById(batchStock.getId());
        assertEquals(2, batchStockOptional.orElse(new BatchStock()).getCurrentQuantity());
        assertEquals(2, batchStockOptional.orElse(new BatchStock()).getInitialQuantity());
    }

    @Test
    void validListProductIdExpiredDate() {

        BatchStock batchStock = new BatchStock()
                .batchNumber(1)
                .productId("LE")
                .currentTemperature(10.0F)
                .minimumTemperature(5.0F)
                .initialQuantity(1)
                .currentQuantity(5)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalTime.now())
                .dueDate(LocalDate.of(2021, 12, 1))
                .agent(agentRepository.findByCpf("11122233344").orElse(new Agent()))
                .section(sectionRepository.findBySectionCode("LA").orElse(new Section()))
                .build();
        batchStockRepository.save(batchStock);

        ProductExceptionNotFound productExceptionNotFound = assertThrows
                (ProductExceptionNotFound.class, () ->
                        batchStockService.listProductId("LE", "asc"));

        String menssagemEsperada = "Nao existe estoques vigentes para esse produto, por favor verifique os dados inseridos!!!";

        assertTrue(menssagemEsperada.contains(productExceptionNotFound.getMessage()));
    }

    @Test
    void validListProductIdByOrderStock(){
        List<BatchStockListProductDTO> batchStockListProductDTOList = new ArrayList<>();

        BatchStockListProductDTO batchStockListProductDTO = new BatchStockListProductDTO()
                .batchNumber(1)
                .currentQuantity(5)
                .dueDate(LocalDate.of(2022, 2, 1))
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
                .dueDate(LocalDate.of(2022, 2, 1))
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
                .dueDate(LocalDate.of(2022, 2, 1))
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
                        batchStockService.updateBatchStock(listProductPurchaseOrderDTO));

        String menssagemEsperada = "Nao foi encontrado estoque para esse produto!!!";

        assertTrue(menssagemEsperada.contains(batchStockException.getMessage()));
    }

    @Test
    void dueDataProduct() {
        assertTrue(batchStockService.dueDataProduct(LocalDate.of(2022, 3, 21)));
    }

    @Test
    void updateBatchStockIntegration() {
        List<ProductPurchaseOrderDTO> listProductPurchaseOrderDTO = new ArrayList<>();
        ProductPurchaseOrderDTO productPurchaseOrderDTO2 = new ProductPurchaseOrderDTO()
                .productId("QJ")
                .quantity(3)
                .build();
        listProductPurchaseOrderDTO.add(productPurchaseOrderDTO2);

        batchStockService.updateBatchStock(listProductPurchaseOrderDTO);

        batchStockRepository.findAllByProductId(productPurchaseOrderDTO2.getProductId()).forEach(b ->
                assertEquals(2, b.getCurrentQuantity()));
    }


//////////////////////////////////////////

    @Test
    void listDueDateDaysTestIntegration(){
        Warehouse warehouse = new Warehouse()
                .warehouseCode("SP")
                .warehouseName("sao paulo")
                .build();
        warehouseRepository.save(warehouse);

        Section section = new Section()
                .sectionCode("LA")
                .sectionName("laticinios")
                .maxLength(10)
                .warehouse(warehouse)
                .build();
        sectionRepository.save(section);

        Agent agent = new Agent().
                cpf("11122233344").
                name("lucas").
                build();
        agentRepository.save(agent);

        BatchStock batchStockUm = new BatchStock()
                .batchNumber(2)
                .productId("QJ")
                .currentTemperature(10.0F)
                .minimumTemperature(5.0F)
                .initialQuantity(1)
                .currentQuantity(5)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalTime.now())
                .dueDate(LocalDate.now().plusWeeks(+2))
                .agent(agent)
                .section(section)
                .build();
        batchStockRepository.save(batchStockUm);

        BatchStock batchStockDois = new BatchStock()
                .batchNumber(1)
                .productId("LE")
                .currentTemperature(10.0F)
                .minimumTemperature(5.0F)
                .initialQuantity(1)
                .currentQuantity(5)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalTime.now())
                .dueDate(LocalDate.now().plusWeeks(+3))
                .agent(agent)
                .section(section)
                .build();
        batchStockRepository.save(batchStockDois);

        BatchStockListDueDateDTO batchStockListDueDateDTO = batchStockService.listDueDateDays(30);

        List<BatchStockServiceDueDateDTO> list = batchStockListDueDateDTO.getBatchStock().stream()
                .filter(b -> (LocalDate.now().plusDays(30).isAfter(b.getDueDate())))
                .collect(Collectors.toList());

        assertFalse(list.isEmpty());
        assertFalse(ObjectUtils.isEmpty(batchStockListDueDateDTO));
    }

    @Test
    void listDueDateDaysExceptionTestIntegration(){

        Warehouse warehouse = new Warehouse()
                .warehouseCode("SP")
                .warehouseName("sao paulo")
                .build();
        warehouseRepository.save(warehouse);

        Section section = new Section()
                .sectionCode("LA")
                .sectionName("laticinios")
                .maxLength(10)
                .warehouse(warehouse)
                .build();
        sectionRepository.save(section);

        Agent agent = new Agent()
                .cpf("11122233344")
                .name("lucas")
                .build();
        agentRepository.save(agent);

        BatchStock batchStockUm = new BatchStock()
                .batchNumber(1)
                .productId("QJ")
                .currentTemperature(10.0F)
                .minimumTemperature(5.0F)
                .initialQuantity(1)
                .currentQuantity(5)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalTime.now())
                .dueDate(LocalDate.now().plusWeeks(+12))
                .agent(agent)
                .section(section)
                .build();
        batchStockRepository.save(batchStockUm);


        ProductExceptionNotFound productExceptionNotFound = assertThrows
                (ProductExceptionNotFound.class,() -> batchStockService.listDueDateDays(30));

        String menssagemEsperada = "Nao existe estoque com este filtro!!!";

        assertTrue(menssagemEsperada.contains(productExceptionNotFound.getMessage()));
    }

    @Test
    void listBatchStockDueDateAscTestIntegration(){

        Warehouse warehouse = new Warehouse()
                .warehouseCode("SP")
                .warehouseName("sao paulo")
                .build();
        warehouseRepository.save(warehouse);

        Section section = new Section()
                .sectionCode("LA")
                .sectionName("laticinios")
                .maxLength(10)
                .warehouse(warehouse)
                .build();
        sectionRepository.save(section);

        Agent agent = new Agent().
                cpf("11122233344").
                name("lucas").
                build();
        agentRepository.save(agent);

        BatchStock batchStockUm = new BatchStock()
                .batchNumber(1)
                .productId("QJ")
                .currentTemperature(10.0F)
                .minimumTemperature(5.0F)
                .initialQuantity(1)
                .currentQuantity(5)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalTime.now())
                .dueDate(LocalDate.now().plusWeeks(+1))
                .agent(agent)
                .section(section)
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
                .manufacturingTime(LocalTime.now())
                .dueDate(LocalDate.now().plusWeeks(+2))
                .agent(agent)
                .section(section)
                .build();
        batchStockRepository.save(batchStockDois);

        Product productUm = new Product()
                .productId("LE")
                .productName("leite")
                .section(section)
                .category(new SectionCategory().name(ESectionCategory.FF))
                .build();
        productRepository.save(productUm);

        Product productDois = new Product()
                .productId("QJ")
                .productName("queijo")
                .section(section)
                .category(new SectionCategory().name(ESectionCategory.FF))
                .build();
        productRepository.save(productDois);

        BatchStockListDueDateDTO batchStockListDueDateDTO = batchStockService.listBatchStockDueDate
                (30,"FF","asc");

        assertTrue(batchStockListDueDateDTO.getBatchStock().get(0).getDueDate().isBefore(batchStockListDueDateDTO.getBatchStock().get(1).getDueDate()));
    }

    @Test
    void listBatchStockDueDateDescTestIntegration(){

        Warehouse warehouse = new Warehouse()
                .warehouseCode("SP")
                .warehouseName("sao paulo")
                .build();
        warehouseRepository.save(warehouse);

        Section section = new Section()
                .sectionCode("LA")
                .sectionName("laticinios")
                .maxLength(10)
                .warehouse(warehouse)
                .build();
        sectionRepository.save(section);

        Agent agent = new Agent().
                cpf("11122233344").
                name("lucas").
                build();
        agentRepository.save(agent);

        BatchStock batchStockUm = new BatchStock()
                .batchNumber(1)
                .productId("QJ")
                .currentTemperature(10.0F)
                .minimumTemperature(5.0F)
                .initialQuantity(1)
                .currentQuantity(5)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalTime.now())
                .dueDate(LocalDate.now().plusWeeks(+1))
                .agent(agent)
                .section(section)
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
                .manufacturingTime(LocalTime.now())
                .dueDate(LocalDate.now().plusWeeks(+2))
                .agent(agent)
                .section(section)
                .build();
        batchStockRepository.save(batchStockDois);

        Product productUm = new Product()
                .productId("LE")
                .productName("leite")
                .section(section)
                .category(new SectionCategory().name(ESectionCategory.FF))
                .build();
        productRepository.save(productUm);

        Product productDois = new Product()
                .productId("QJ")
                .productName("queijo")
                .section(section)
                .category(new SectionCategory().name(ESectionCategory.FF))
                .build();
        productRepository.save(productDois);

        BatchStockListDueDateDTO batchStockListDueDateDTO = batchStockService.listBatchStockDueDate
                (30,"FF","desc");

        assertTrue(batchStockListDueDateDTO.getBatchStock().get(0).getDueDate()
                .isAfter(batchStockListDueDateDTO.getBatchStock().get(1).getDueDate()));
    }

    @Test
    void listBatchStockDueDateExceptionOrderTestIntegration(){
        Warehouse warehouse = new Warehouse()
                .warehouseCode("SP")
                .warehouseName("sao paulo")
                .build();
        warehouseRepository.save(warehouse);

        Section section = new Section()
                .sectionCode("LA")
                .sectionName("laticinios")
                .maxLength(10)
                .warehouse(warehouse)
                .build();
        sectionRepository.save(section);

        Agent agent = new Agent().
                cpf("11122233344").
                name("lucas").
                build();
        agentRepository.save(agent);

        BatchStock batchStockUm = new BatchStock()
                .batchNumber(2)
                .productId("QJ")
                .currentTemperature(10.0F)
                .minimumTemperature(5.0F)
                .initialQuantity(1)
                .currentQuantity(5)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalTime.now())
                .dueDate(LocalDate.now().plusWeeks(+2))
                .agent(agent)
                .section(section)
                .build();
        batchStockRepository.save(batchStockUm);

        Product productUm = new Product()
                .productId("LE")
                .productName("leite")
                .section(section)
                .category(new SectionCategory().name(ESectionCategory.FF))
                .build();
        productRepository.save(productUm);

        Product productDois = new Product()
                .productId("QJ")
                .productName("queijo")
                .section(section)
                .category(new SectionCategory().name(ESectionCategory.FF))
                .build();
        productRepository.save(productDois);

        ProductExceptionNotFound productExceptionNotFound = assertThrows
                (ProductExceptionNotFound.class,() -> batchStockService.listBatchStockDueDate
                        (30,"FF","a5sc"));

        String menssagemEsperada = "Codigo da ordenacao nao existe!!!";
        assertTrue(menssagemEsperada.contains(productExceptionNotFound.getMessage()));
    }

    @Test
    void listBatchStockDueDateExceptionCategoryTestIntegration(){
        Warehouse warehouse = new Warehouse()
                .warehouseCode("SP")
                .warehouseName("sao paulo")
                .build();
        warehouseRepository.save(warehouse);

        Section section = new Section()
                .sectionCode("LA")
                .sectionName("laticinios")
                .maxLength(10)
                .warehouse(warehouse)
                .build();
        sectionRepository.save(section);

        Agent agent = new Agent().
                cpf("11122233344").
                name("lucas").
                build();
        agentRepository.save(agent);

        BatchStock batchStockUm = new BatchStock()
                .batchNumber(2)
                .productId("QJ")
                .currentTemperature(10.0F)
                .minimumTemperature(5.0F)
                .initialQuantity(1)
                .currentQuantity(5)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalTime.now())
                .dueDate(LocalDate.now().plusWeeks(+2))
                .agent(agent)
                .section(section)
                .build();
        batchStockRepository.save(batchStockUm);

        Product productUm = new Product()
                .productId("LE")
                .productName("leite")
                .section(section)
                .category(new SectionCategory().name(ESectionCategory.FF))
                .build();
        productRepository.save(productUm);

        Product productDois = new Product()
                .productId("QJ")
                .productName("queijo")
                .section(section)
                .category(new SectionCategory().name(ESectionCategory.FF))
                .build();
        productRepository.save(productDois);

        ProductExceptionNotFound productExceptionNotFound = assertThrows
                (ProductExceptionNotFound.class,() -> batchStockService.listBatchStockDueDate
                        (30,"F5","a5sc"));

        String menssagemEsperada = "Nao existe esta categoria!!!";
        assertTrue(menssagemEsperada.contains(productExceptionNotFound.getMessage()));
    }

//////////////////////////////////////////
    void createData() {
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

        Product product = new Product()
                .productId("QJ")
                .productName("queijo")
                .productPrice(new BigDecimal("2.0"))
                .dueDate(LocalDate.of(2022, 2, 1))
                .category(sectionCategory)
                .section(section)
                .build();
        productRepository.save(product);

        BatchStock batchStock = new BatchStock()
                .batchNumber(1)
                .productId("QJ")
                .currentTemperature(10.0F)
                .minimumTemperature(5.0F)
                .initialQuantity(1)
                .currentQuantity(5)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalTime.now())
                .dueDate(LocalDate.of(2022, 2, 1))
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
    }
}
