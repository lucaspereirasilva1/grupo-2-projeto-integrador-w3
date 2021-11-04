package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.exception.ProductException;
import br.com.meli.projetointegrador.model.entity.Product;
import br.com.meli.projetointegrador.model.entity.Section;
import br.com.meli.projetointegrador.model.entity.Warehouse;
import br.com.meli.projetointegrador.model.repository.ProductRepository;
import br.com.meli.projetointegrador.model.repository.SectionRepository;
import br.com.meli.projetointegrador.model.repository.WarehouseRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Jhony Zuim / Lucas Pereira / Edmilson Nobre / Rafael Vicente
 * @version 1.0.0
 * @since 15/10/2021
 * Camada de teste integrado do service responsavel pela regra de negocio relacionada ao product
 */

@SpringBootTest
public class ProductServiceIntegrationTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @BeforeEach
    void setUp(){
        clearBase();

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

        Product product = new Product()
                .productId("LEI")
                .productName("Leite")
                .section(section)
                .build();

        sectionRepository.save(section);
        productRepository.save(product);
        warehouseRepository.save(warehouse);
    }

    @AfterEach
    void cleanUpDataBase(){
        clearBase();
    }

    @Test
    void validProductExistTest(){
        assertTrue(productService.validProductSection("LA"));
    }
    @Test
    void validProductNotExistTest(){
        ProductException productException = assertThrows(ProductException.class, () ->
                productService.validProductSection("XX"));

        String expectedMessage = "Produto nao faz parte do setor, por favor verifique o setor correto!";
        assertTrue(expectedMessage.contains(productException.getMessage()));
    }

    void clearBase() {
        sectionRepository.deleteAll();
        productRepository.deleteAll();
        warehouseRepository.deleteAll();
    }
}
