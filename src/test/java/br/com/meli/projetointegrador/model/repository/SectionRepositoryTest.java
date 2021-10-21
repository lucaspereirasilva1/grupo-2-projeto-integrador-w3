package br.com.meli.projetointegrador.model.repository;

import br.com.meli.projetointegrador.model.entity.Section;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
public class SectionRepositoryTest {

    @Autowired
    private SectionRepository sectionRepository;

    @BeforeEach
    void setUp() {
        Section section = new Section()
                .sectionCode("FR")
                .sectionName("frios")
                .maxLength(10)
                .build();
        sectionRepository.save(section);
    }

    @AfterEach
    void cleanUpDatabase() {
        sectionRepository.deleteAll();
    }

    @Test
    void findBySectionCode() {
        Optional<Section> section = sectionRepository.findBySectionCode("FR");
        assertTrue(section.isPresent());
    }

}
