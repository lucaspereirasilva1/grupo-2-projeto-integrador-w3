package br.com.meli.projetointegrador.model.repository;

import br.com.meli.projetointegrador.model.entity.Product;
import br.com.meli.projetointegrador.model.entity.Section;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private SectionRepository sectionRepository;

    @BeforeEach
    void setUp() {
        Section section = new Section()
                .sectionCode("LA")
                .sectionName("Laticionios")
                .maxLength(10)
                .build();

        Product product = new Product()
                .productCode("LEI")
                .productName("Leite")
                .section(section)
                .build();

        sectionRepository.save(section);
        productRepository.save(product);
    }

    @AfterEach
    void cleanUpDataBase(){
        sectionRepository.deleteAll();
        productRepository.deleteAll();
    }

    @Test
    void findBySection(){
        Optional<Section> section = sectionRepository.findBySectionCode("LA");
        Optional<Product> productOptional = productRepository.findBySection(section.get());
        assertTrue(productOptional.isPresent());
    }
}
