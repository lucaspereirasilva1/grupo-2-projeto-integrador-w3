package br.com.meli.projetointegrador.model.repository;

import br.com.meli.projetointegrador.model.entity.SectionCategory;
import br.com.meli.projetointegrador.model.enums.ESectionCategory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataMongoTest
class SectionCategoryRespositoryTest {

    @Autowired
    private SectionCategoryRepository sectionCategoryRepository;

    @BeforeEach
    void setUp() {
        sectionCategoryRepository.deleteAll();

        SectionCategory sectionCategory = new SectionCategory()
                .name(ESectionCategory.FF)
                .build();
        sectionCategoryRepository.save(sectionCategory);
    }

    @AfterEach
    void tearDown() {
        sectionCategoryRepository.deleteAll();
    }

    @Test
    void find() {
        assertTrue(sectionCategoryRepository.findByName(ESectionCategory.FF).isPresent());
    }

}
