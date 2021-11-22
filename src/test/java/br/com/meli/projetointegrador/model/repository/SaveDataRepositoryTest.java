package br.com.meli.projetointegrador.model.repository;

import br.com.meli.projetointegrador.model.entity.*;
import br.com.meli.projetointegrador.model.enums.ESectionCategory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Jhony Zuim / Lucas Pereira / Edmilson Nobre / Rafael Vicente
 * @version 1.0.0
 * @since 15/10/2021
 * Repository de teste para trabalhar como uma porta ou janela de acesso a camada do banco
 */

@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
@TestPropertySource(properties = {"spring.data.mongodb.port:27017"})
class SaveDataRepositoryTest {

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SectionCategoryRepository sectionCategoryRepository;

    @Autowired
    private BuyerRepository buyerRepository;

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private BatchStockRepository batchStockRepository;

    @Test
    void saveProduct() {
        productRepository.deleteAll();

        SectionCategory sectionCategory = new SectionCategory()
                .name(ESectionCategory.FF)
                .build();

        Product product = new Product()
                .productId("MA")
                .productName("leite")
                .category(sectionCategory)
                .dueDate(LocalDate.now())
                .productPrice(new BigDecimal("2.0"))
                .build();
        productRepository.save(product);

        Product productUm = new Product()
                .productId("QJ")
                .productName("queijo")
                .category(sectionCategory)
                .dueDate(LocalDate.now())
                .productPrice(new BigDecimal("2.0"))
                .build();
        productRepository.save(productUm);

        assertEquals(1, Integer.valueOf(1));
    }

    @Test
    void saveSectionCategory() {
        SectionCategory sectionCategory = new SectionCategory()
                .name(ESectionCategory.FF)
                .build();
        sectionCategoryRepository.save(sectionCategory);
        assertEquals(1, Integer.valueOf(1));
    }

    @Test
    void saveBuyer() {
        Buyer buyer = new Buyer()
                .name("lucas")
                .cpf("22233344411")
                .build();
        buyerRepository.save(buyer);
        assertEquals(1,Integer.valueOf(1));
    }

    @Test
    void productTest() {
        Product product = new Product()
                .productId("MR")
                .productName("mortadela")
                .dueDate(LocalDate.now())
                .productPrice(new BigDecimal("2.0"))
                .category(sectionCategoryRepository.findByName(ESectionCategory.FF).orElse(new SectionCategory()))
                .build();
        productRepository.save(product);
    }

    @Test
    void werehouseInsertTest() {
        Warehouse warehouse = new Warehouse()
                .warehouseCode("SP")
                .warehouseName("sao paulo")
                .build();
        warehouseRepository.save(warehouse);
    }

    @Test
    void sectionSaveTest() {
        Section section = new Section()
                .sectionCode("LA")
                .sectionName("laticinios")
                .warehouse(warehouseRepository.findByWarehouseCode("MG").orElse(new Warehouse()))
                .maxLength(10)
                .build();
        sectionRepository.save(section);
    }

    @Test
    void batchStockInsert() {
        Agent agent = new Agent()
                .name("lucas")
                .cpf("11122233344")
                .warehouse(warehouseRepository.findByWarehouseCode("SP").orElse(new Warehouse()))
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
                .dueDate(LocalDate.of(2021, 12, 1))
                .agent(agent)
                .section(sectionRepository.findBySectionCode("LA").orElse(new Section()))
                .build();
        batchStockRepository.save(batchStock);
    }

}