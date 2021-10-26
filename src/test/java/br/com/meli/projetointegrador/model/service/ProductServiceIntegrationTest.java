package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.model.entity.Product;
import br.com.meli.projetointegrador.model.entity.Section;
import br.com.meli.projetointegrador.model.repository.ProductRepository;
import br.com.meli.projetointegrador.model.repository.SectionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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

    @BeforeEach
    void setUp(){
        clearBase();

        Section section = new Section()
                .sectionCode("LA")
                .sectionName("Laticionios")
                .maxLength(10)
                .build();

        Product product = new Product()
                .productId("LEI")
                .productName("Leite")
                .section(section)
                .build();

        sectionRepository.save(section);
        productRepository.save(product);
    }

    @AfterEach
    void cleanUpDataBase(){
        clearBase();
    }

//    @Test
//    void validProduct(){
//        List<Product> productOptional = productRepository.findAll();
//
//        assertTrue(productService.validProductSection(productOptional.get(0)));
//    }

    void clearBase() {
        sectionRepository.deleteAll();
        productRepository.deleteAll();
    }
}
