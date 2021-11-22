package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.exception.PurchaseOrderException;
import br.com.meli.projetointegrador.model.dto.OrderStatusDTO;
import br.com.meli.projetointegrador.model.dto.ProductDTO;
import br.com.meli.projetointegrador.model.dto.ProductPurchaseOrderDTO;
import br.com.meli.projetointegrador.model.dto.PurchaseOrderDTO;
import br.com.meli.projetointegrador.model.entity.*;
import br.com.meli.projetointegrador.model.enums.EOrderStatus;
import br.com.meli.projetointegrador.model.enums.ESectionCategory;
import br.com.meli.projetointegrador.model.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Jhony Zuim / Lucas Pereira / Edmilson Nobre / Rafael Vicente
 * @version 1.0.0
 * @since 15/10/2021
 * Camada de teste integrado do service responsavel pela regra de negocio relacionada ao purchaseOrder
 */

@SpringBootTest
class PurchaseOrderServiceIntegrationTest {

    @Autowired
    private PurchaseOrderService purchaseOrderService;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private SectionCategoryRepository sectionCategoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BuyerRepository buyerRepository;

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private BatchStockRepository batchStockRepository;

    @Autowired
    private AgentRepository agentRepository;

    @Test
    void showOrderProduct() {
        clearBase();

        Warehouse warehouse = new Warehouse()
                .warehouseCode("MG")
                .warehouseName("Minas Gerais")
                .build();
        warehouseRepository.save(warehouse);

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
                .productId("LE")
                .productName("leite")
                .productPrice(new BigDecimal(2))
                .dueDate(LocalDate.of(2022,11,30))
                .category(sectionCategory)
                .build();

        Product productUm = new Product()
                .productId("QJ")
                .productName("queijo")
                .productPrice(new BigDecimal(3))
                .dueDate(LocalDate.of(2022,11,30))
                .category(sectionCategory)
                .build();
        productRepository.saveAll(Arrays.asList(product, productUm));

        Buyer buyer = new Buyer()
                .name("lucas")
                .cpf("22233344411")
                .build();
        buyerRepository.save(buyer);

        PurchaseOrder purchaseOrder = new PurchaseOrder()
                .date(LocalDate.now())
                .buyer(buyer)
                .orderStatus(EOrderStatus.IN_PROGRESS)
                .productList(Arrays.asList(product, productUm))
                .build();
        purchaseOrderRepository.save(purchaseOrder);

        List<ProductDTO> listProductDTO = purchaseOrderService.showOrderProduct(purchaseOrder.getId());
        assertFalse(listProductDTO.isEmpty());
    }

    @Test
    void postIntegrationTest() {
        clearBase();
        Warehouse warehouse = new Warehouse()
                .warehouseCode("SP")
                .warehouseName("Sao Paulo")
                .build();
        warehouseRepository.save(warehouse);

        Section section = new Section()
                .sectionCode("LA")
                .sectionName("Laticionios")
                .warehouse(warehouse)
                .maxLength(10)
                .build();
        sectionRepository.save(section);

        Buyer buyer = new Buyer()
                .name("lucas")
                .cpf("22233344411")
                .build();
        buyerRepository.save(buyer);

        SectionCategory sectionCategory = new SectionCategory()
                .name(ESectionCategory.FF)
                .build();
        sectionCategoryRepository.save(sectionCategory);

        Product product = new Product()
                .productId("LE")
                .productName("leite")
                .productPrice(new BigDecimal(2))
                .dueDate(LocalDate.of(2022,11,30))
                .category(sectionCategory)
                .build();

        Product productUm = new Product()
                .productId("QJ")
                .productName("queijo")
                .productPrice(new BigDecimal(3))
                .dueDate(LocalDate.of(2022,11,30))
                .category(sectionCategory)
                .build();
        productRepository.saveAll(Arrays.asList(product, productUm));

        ProductPurchaseOrderDTO productPurchaseOrderDTO1 = new ProductPurchaseOrderDTO()
                .productId("LE")
                .quantity(5)
                .build();
        ProductPurchaseOrderDTO productPurchaseOrderDTO2 = new ProductPurchaseOrderDTO()
                .productId("QJ")
                .quantity(3)
                .build();

        PurchaseOrderDTO purchaseOrderDTO = new PurchaseOrderDTO()
                .data(LocalDate.now())
                .buyerId(buyer.getId())
                .orderStatus(new OrderStatusDTO().statusCode(EOrderStatus.IN_PROGRESS))
                .listProductPurchaseOrderDTO(Arrays.asList(productPurchaseOrderDTO1,
                        productPurchaseOrderDTO2));

        Agent agent = new Agent()
                .name("lucas")
                .cpf("11122233344")
                .warehouse(warehouse)
                .build();
        agentRepository.save(agent);

        BatchStock batchStock = new BatchStock()
                .batchNumber(1)
                .productId("LE")
                .currentTemperature(10.0F)
                .minimumTemperature(5.0F)
                .initialQuantity(1)
                .currentQuantity(5)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalDateTime.now())
                .dueDate(LocalDate.of(2022, 1, 3))
                .agent(agent)
                .section(section)
                .build();

        BatchStock batchStockDois = new BatchStock()
                .batchNumber(2)
                .productId("QJ")
                .currentTemperature(10.0F)
                .minimumTemperature(5.0F)
                .initialQuantity(1)
                .currentQuantity(10)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalDateTime.now())
                .dueDate(LocalDate.of(2022, 1, 3))
                .agent(agent)
                .section(section)
                .build();
        batchStockRepository.saveAll(Arrays.asList(batchStock, batchStockDois));

        final BigDecimal total = purchaseOrderService.save(purchaseOrderDTO);

        assertFalse(ObjectUtils.isEmpty(total));
        assertEquals(new BigDecimal(19), total);
    }

    @Test
    void putIntegrationTest() {
        clearBase();

        Warehouse warehouse = new Warehouse()
                .warehouseCode("MG")
                .warehouseName("Minas Gerais")
                .build();
        warehouseRepository.save(warehouse);

        Section section = new Section()
                .sectionCode("FR")
                .sectionName("Frios")
                .warehouse(warehouse)
                .maxLength(10)
                .build();
        sectionRepository.save(section);

        Buyer buyer = new Buyer()
                .name("lucas")
                .cpf("22233344411")
                .build();
        buyerRepository.save(buyer);

        SectionCategory sectionCategory = new SectionCategory()
                .name(ESectionCategory.RF)
                .build();
        sectionCategoryRepository.save(sectionCategory);

        Product product = new Product()
                .productId("MU")
                .productName("mussarela")
                .productPrice(new BigDecimal(2))
                .dueDate(LocalDate.of(2022,11,30))
                .category(sectionCategory)
                .build();

        Product productUm = new Product()
                .productId("CA")
                .productName("carne")
                .productPrice(new BigDecimal(3))
                .dueDate(LocalDate.of(2022,11,30))
                .category(sectionCategory)
                .build();

        Product productTres = new Product()
                .productId("ME")
                .productName("melao")
                .productPrice(new BigDecimal(3))
                .dueDate(LocalDate.of(2000,11,30))
                .category(sectionCategory)
                .build();
        productRepository.saveAll(Arrays.asList(product, productUm, productTres));

        Agent agent = new Agent()
                .name("lucas")
                .cpf("11122233344")
                .warehouse(warehouse)
                .build();
        agentRepository.save(agent);

        BatchStock batchStock = new BatchStock()
                .batchNumber(20)
                .productId("MU")
                .currentTemperature(10.0F)
                .minimumTemperature(5.0F)
                .initialQuantity(1)
                .currentQuantity(5)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalDateTime.now())
                .dueDate(LocalDate.of(2022, 1, 3))
                .agent(agent)
                .section(section)
                .build();

        BatchStock batchStockDois = new BatchStock()
                .batchNumber(21)
                .productId("CA")
                .currentTemperature(10.0F)
                .minimumTemperature(5.0F)
                .initialQuantity(1)
                .currentQuantity(10)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalDateTime.now())
                .dueDate(LocalDate.of(2022, 1, 3))
                .agent(agent)
                .section(section)
                .build();
        batchStockRepository.saveAll(Arrays.asList(batchStock, batchStockDois));

        PurchaseOrder purchaseOrder = new PurchaseOrder()
                .date(LocalDate.now())
                .buyer(buyer)
                .orderStatus(EOrderStatus.ORDER_CHART)
                .productList(Arrays.asList(product, productUm))
                .build();
        purchaseOrderRepository.save(purchaseOrder);

        ProductPurchaseOrderDTO productPurchaseOrderDTO1 = new ProductPurchaseOrderDTO()
                .productId("MU")
                .quantity(5)
                .build();
        ProductPurchaseOrderDTO productPurchaseOrderDTO2 = new ProductPurchaseOrderDTO()
                .productId("CA")
                .quantity(3)
                .build();

        PurchaseOrderDTO purchaseOrderPutDTO = new PurchaseOrderDTO()
                .id(purchaseOrder.getId())
                .data(LocalDate.now())
                .buyerId(buyer.getId())
                .orderStatus(new OrderStatusDTO().statusCode(EOrderStatus.ORDER_CHART))
                .listProductPurchaseOrderDTO(Arrays.asList(productPurchaseOrderDTO1,
                        productPurchaseOrderDTO2));


        final BigDecimal total = purchaseOrderService.save(purchaseOrderPutDTO);

        assertFalse(ObjectUtils.isEmpty(total));
        assertEquals(new BigDecimal(19), total);
        assertEquals(purchaseOrderPutDTO.getBuyerId(), purchaseOrder.getBuyer().getId());
        assertEquals(purchaseOrderPutDTO.getData(), purchaseOrder.getDate());
        assertEquals(purchaseOrderPutDTO.getOrderStatus().getStatusCode(), purchaseOrder.getOrderStatus());
    }

    @Test
    void showOrderProductEmptyList() {
        PurchaseOrderException purchaseOrderException = assertThrows
                (PurchaseOrderException.class,() -> purchaseOrderService.showOrderProduct("6189a1191f37283c5f32c029"));

        String mensagemEsperada = "Ordem de compra nao encontrada!!!";
        String mensagemRecebida = purchaseOrderException.getMessage();

        assertTrue(mensagemEsperada.contains(mensagemRecebida));
    }

    @Test
    void calculeTotalDueDateInvalidTest(){
        clearBase();

        Warehouse warehouse = new Warehouse()
                .warehouseCode("MG")
                .warehouseName("Minas Gerais")
                .build();
        warehouseRepository.save(warehouse);

        Section section = new Section()
                .sectionCode("FR")
                .sectionName("Frios")
                .warehouse(warehouse)
                .maxLength(10)
                .build();
        sectionRepository.save(section);

        Buyer buyer = new Buyer()
                .name("lucas")
                .cpf("22233344411")
                .build();
        buyerRepository.save(buyer);

        SectionCategory sectionCategory = new SectionCategory()
                .name(ESectionCategory.RF)
                .build();
        sectionCategoryRepository.save(sectionCategory);

        Product product = new Product()
                .productId("MU")
                .productName("mussarela")
                .productPrice(new BigDecimal(2))
                .dueDate(LocalDate.of(2022,11,30))
                .category(sectionCategory)
                .build();

        Product productUm = new Product()
                .productId("CA")
                .productName("carne")
                .productPrice(new BigDecimal(3))
                .dueDate(LocalDate.of(2022,11,30))
                .category(sectionCategory)
                .build();

        Product productTres = new Product()
                .productId("ME")
                .productName("melao")
                .productPrice(new BigDecimal(3))
                .dueDate(LocalDate.of(2000,11,30))
                .category(sectionCategory)
                .build();
        productRepository.saveAll(Arrays.asList(product, productUm, productTres));

        Agent agent = new Agent()
                .name("lucas")
                .cpf("11122233344")
                .warehouse(warehouse)
                .build();
        agentRepository.save(agent);

        BatchStock batchStock = new BatchStock()
                .batchNumber(22)
                .productId("MU")
                .currentTemperature(10.0F)
                .minimumTemperature(5.0F)
                .initialQuantity(1)
                .currentQuantity(5)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalDateTime.now())
                .dueDate(LocalDate.of(2022, 1, 3))
                .agent(agent)
                .section(section)
                .build();

        BatchStock batchStockDois = new BatchStock()
                .batchNumber(23)
                .productId("CA")
                .currentTemperature(10.0F)
                .minimumTemperature(5.0F)
                .initialQuantity(1)
                .currentQuantity(10)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalDateTime.now())
                .dueDate(LocalDate.of(2022, 1, 3))
                .agent(agent)
                .section(section)
                .build();
        batchStockRepository.saveAll(Arrays.asList(batchStock, batchStockDois));

        PurchaseOrder purchaseOrder = new PurchaseOrder()
                .date(LocalDate.now())
                .buyer(buyer)
                .orderStatus(EOrderStatus.ORDER_CHART)
                .productList(Arrays.asList(product, productUm))
                .build();
        purchaseOrderRepository.save(purchaseOrder);
        List<ProductPurchaseOrderDTO> listProductPurchaseOrderDTO = new ArrayList<>();
        ProductPurchaseOrderDTO productPurchaseOrderDTO1 = new ProductPurchaseOrderDTO()
                .productId("ME")
                .quantity(5)
                .build();
        listProductPurchaseOrderDTO.add(productPurchaseOrderDTO1);

        PurchaseOrderDTO purchaseOrderDTO = new PurchaseOrderDTO()
                .data(LocalDate.now())
                .buyerId("1")
                .orderStatus(new OrderStatusDTO().statusCode(EOrderStatus.IN_PROGRESS))
                .listProductPurchaseOrderDTO(listProductPurchaseOrderDTO);

        PurchaseOrderException purchaseOrderException = assertThrows
                (PurchaseOrderException.class,() -> purchaseOrderService.save(purchaseOrderDTO));

        String mensagemEsperada = "Vencimento inferior a 3 semanas!!! Produto: " + "melao" + " Vencimento: " + LocalDate.of(2000,11,30);
        String mensagemRecebida = purchaseOrderException.getMessage();

        assertTrue(mensagemEsperada.contains(mensagemRecebida));
    }

    void clearBase() {
        warehouseRepository.deleteAll();
        sectionRepository.deleteAll();
        buyerRepository.deleteAll();
        sectionCategoryRepository.deleteAll();
        productRepository.deleteAll();
        agentRepository.deleteAll();
        batchStockRepository.deleteAll();
        purchaseOrderRepository.deleteAll();
    }


}
