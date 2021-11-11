package br.com.meli.projetointegrador.model.repository;

import br.com.meli.projetointegrador.model.entity.Product;
import br.com.meli.projetointegrador.model.entity.Section;
import br.com.meli.projetointegrador.model.entity.SectionCategory;
import br.com.meli.projetointegrador.model.entity.Warehouse;
import br.com.meli.projetointegrador.model.enums.ESectionCategory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Jhony Zuim / Lucas Pereira / Edmilson Nobre / Rafael Vicente
 * @version 1.0.0
 * @since 15/10/2021
 * Repository de teste para trabalhar como uma porta ou janela de acesso a camada do banco da entity product
 */

@DataMongoTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private SectionCategoryRepository sectionCategoryRepository;

    @BeforeEach
    void setUp() {
        sectionRepository.deleteAll();
        productRepository.deleteAll();
        warehouseRepository.deleteAll();
        sectionCategoryRepository.deleteAll();

        SectionCategory sectionCategory = new SectionCategory()
                .name(ESectionCategory.FF)
                .build();
        sectionCategoryRepository.save(sectionCategory);

        Warehouse warehouse = new Warehouse()
                .warehouseCode("SP")
                .warehouseName("Sao Paulo")
                .build();
        warehouseRepository.save(warehouse);

        Section section = new Section()
                .sectionCode("LA")
                .sectionName("Laticionios")
                .maxLength(10)
                .warehouse(warehouse)
                .build();
        sectionRepository.save(section);

        Product product = new Product()
                .productId("LE")
                .productName("Leite")
                .section(section)
                .productPrice(new BigDecimal("2.0"))
                .category(sectionCategory)
                .dueDate(LocalDate.now())
                .build();
        productRepository.save(product);
    }

    @AfterEach
    void cleanUpDataBase(){
        sectionRepository.deleteAll();
        productRepository.deleteAll();
        warehouseRepository.deleteAll();
        sectionCategoryRepository.deleteAll();
    }

    @Test
    void findBySection(){
        Optional<Product> product = productRepository.findDistinctFirstByProductId("LE");
        assertTrue(product.isPresent());
    }

    @Test
    void existsProductBySectionTest() {
        final Optional<Section> section = sectionRepository.findBySectionCode("LA");
        assertTrue(productRepository.existsProductBySection(section.orElse(null)));
    }

    @Test
    void findProductByCategory() {
        assertFalse(productRepository.findProductByCategory(new SectionCategory().name(ESectionCategory.FF)).isEmpty());
    }
}
