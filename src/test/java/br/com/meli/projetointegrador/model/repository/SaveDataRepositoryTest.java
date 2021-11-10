package br.com.meli.projetointegrador.model.repository;

import br.com.meli.projetointegrador.model.entity.*;
import br.com.meli.projetointegrador.model.enums.EOrderStatus;
import br.com.meli.projetointegrador.model.enums.ERole;
import br.com.meli.projetointegrador.model.enums.ESectionCategory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author Jhony Zuim / Lucas Pereira / Edmilson Nobre / Rafael Vicente
 * @version 1.0.0
 * @since 15/10/2021
 * Repository de teste para trabalhar como uma porta ou janela de acesso a camada do banco
 */

@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
public class SaveDataRepositoryTest {

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private SectionCategoryRepository sectionCategoryRepository;

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private BuyerRepository buyerRepository;

    @Autowired
    private InboundOrderRepository inboundOrderRepository;

    @Autowired
    private BatchStockRepository batchStockRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void saveProduct() {
        productRepository.deleteAll();

        List<Product> listProduct = new ArrayList<>();
        final Optional<Section> section = sectionRepository.findBySectionCode("LA");

        SectionCategory sectionCategory = new SectionCategory()
                .name(ESectionCategory.FF)
                .build();

        Product product = new Product()
                .productId("LE")
                .productName("leite")
                .section(section.orElse(new Section()))
                .category(sectionCategory)
                .dueDate(LocalDate.now())
                .productPrice(new BigDecimal("2.0"))
                .build();

        Product productUm = new Product()
                .productId("QJ")
                .productName("queijo")
                .section(section.orElse(new Section()))
                .category(sectionCategory)
                .dueDate(LocalDate.now())
                .productPrice(new BigDecimal("2.0"))
                .build();

        listProduct.add(product);
        listProduct.add(productUm);

        productRepository.saveAll(listProduct);
    }

    @Test
    void saveSectionCategory() {
        SectionCategory sectionCategory = new SectionCategory()
                .name(ESectionCategory.FF)
                .build();
        sectionCategoryRepository.save(sectionCategory);
    }

    @Test
    void savePurchaseOrder() {
        purchaseOrderRepository.deleteAll();
        final List<Product> listProduct = productRepository.findAll();
        final Optional<Buyer> buyer = buyerRepository.findByCpf("22233344411");

        PurchaseOrder purchaseOrder = new PurchaseOrder()
                .date(LocalDate.now())
                .buyer(buyer.orElse(new Buyer()))
                .orderStatus(EOrderStatus.ORDER_CHART)
                .productList(listProduct)
                .build();

        purchaseOrderRepository.save(purchaseOrder);
    }

    @Test
    void saveBuyer() {
        Buyer buyer = new Buyer()
                .name("lucas")
                .cpf("22233344411")
                .build();
        buyerRepository.save(buyer);
    }

    @Test
    void scriptCarregamentoBanco() {
        productRepository.deleteAll();
        warehouseRepository.deleteAll();
        sectionRepository.deleteAll();
        agentRepository.deleteAll();
        inboundOrderRepository.deleteAll();
        batchStockRepository.deleteAll();
        roleRepository.deleteAll();

        BigDecimal bigDecimal = new BigDecimal(45.00);

        Role roleUser = new Role(ERole.ROLE_USER);
        roleRepository.save(roleUser);

        Role roleModerator = new Role(ERole.ROLE_MODERATOR);
        roleRepository.save(roleModerator);

        Role roleAdmin = new Role(ERole.ROLE_ADMIN);
        roleRepository.save(roleAdmin);

        Warehouse warehouse = new Warehouse()
                .warehouseCode("SP")
                .warehouseName("sao paulo")
                .build();

        warehouseRepository.save(warehouse);

        Warehouse warehouseMG = new Warehouse()
                .warehouseCode("MG")
                .warehouseName("Minas Gerais")
                .build();

        warehouseRepository.save(warehouseMG);

        Section section = new Section()
                .sectionCode("LA")
                .sectionName("laticinios")
                .maxLength(10)
                .warehouse(warehouse)
                .build();

        sectionRepository.save(section);

        Section sectionCO = new Section()
                .sectionCode("CO")
                .sectionName("Congelados")
                .maxLength(10)
                .warehouse(warehouseMG)
                .build();

        sectionRepository.save(sectionCO);

        Product product = new Product()
                .productId("LE")
                .productName("leite")
                .section(section)
                .category(new SectionCategory().name(ESectionCategory.FF))
                .productPrice(bigDecimal)
                .dueDate(LocalDate.now())
                .build();

        productRepository.save(product);

        Product productDois = new Product()
                .productId("LE")
                .productName("danone")
                .section(section)
                .category(new SectionCategory().name(ESectionCategory.FF))
                .productPrice(bigDecimal)
                .dueDate(LocalDate.now())
                .build();

        productRepository.save(productDois);

        Agent agent = new Agent().
                cpf("11122233344").
                name("lucas").
                warehouse(warehouse).
                build();

        agentRepository.save(agent);

        BatchStock batchStockDois = new BatchStock()
                .batchNumber(2)
                .productId("LE")
                .currentTemperature(10.0F)
                .minimumTemperature(5.0F)
                .initialQuantity(1)
                .currentQuantity(7)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalDateTime.now())
                .dueDate(LocalDate.of(2021,12,04))
                .agent(agent)
                .section(section)
                .build();
        batchStockRepository.save(batchStockDois);

        BatchStock batchStockTres= new BatchStock()
                .batchNumber(3)
                .productId("LE")
                .currentTemperature(10.0F)
                .minimumTemperature(5.0F)
                .initialQuantity(1)
                .currentQuantity(5)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalDateTime.now())
                .dueDate(LocalDate.of(2021,11,23))
                .agent(agent)
                .section(section)
                .build();
        batchStockRepository.save(batchStockTres);

        BatchStock batchStockUm = new BatchStock()
                .batchNumber(1)
                .productId("LE")
                .currentTemperature(10.0F)
                .minimumTemperature(5.0F)
                .initialQuantity(1)
                .currentQuantity(2)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalDateTime.now())
                .dueDate(LocalDate.of(2021,12,13))
                .agent(agent)
                .section(section)
                .build();
        batchStockRepository.save(batchStockUm);

        InboundOrder inboundOrder = new InboundOrder()
                .orderNumber(1)
                .orderDate(LocalDate.now())
                .section(section)
                .listBatchStock(Collections.singletonList(batchStockDois))
                .build();
        inboundOrderRepository.save(inboundOrder);
    }

    @Test
    void saveUser() {

    }

}
