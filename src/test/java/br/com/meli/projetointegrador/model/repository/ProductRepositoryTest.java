package br.com.meli.projetointegrador.model.repository;

import br.com.meli.projetointegrador.model.entity.Product;
import br.com.meli.projetointegrador.model.entity.Section;
import br.com.meli.projetointegrador.model.entity.Warehouse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Jhony Zuim / Lucas Pereira / Edmilson Nobre / Rafael Vicente
 * @version 1.0.0
 * @since 15/10/2021
 * Repository de teste para trabalhar como uma porta ou janela de acesso a camada do banco da entity product
 */

@DataMongoTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @BeforeEach
    void setUp() {
        Warehouse warehouse = new Warehouse()
                .warehouseCode("SP")
                .warehouseName("Sao Paulo")
                .build();

        Section section = new Section()
                .sectionCode("LA")
                .sectionName("Laticionios")
                .maxLength(10)
                .warehouse(warehouse)
                .build();

        Product product = new Product()
                .productId("LE")
                .productName("Leite")
                .section(section)
                .build();

        sectionRepository.save(section);
        productRepository.save(product);
        warehouseRepository.save(warehouse);
    }

    @AfterEach
    void cleanUpDataBase(){
        sectionRepository.deleteAll();
        productRepository.deleteAll();
        warehouseRepository.deleteAll();
    }

    @Test
    void findBySection(){
        Optional<Product> product = productRepository.findByProductId("LE");
        assertTrue(product.isPresent());
    }

    @Test
    void existsProductBySectionTest() {
        Boolean existProduct = productRepository.existsProductBySection_SectionCode("LA");
        assertTrue(existProduct);
    }
}
