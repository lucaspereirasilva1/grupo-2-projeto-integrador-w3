package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.exception.SectionCategoryException;
import br.com.meli.projetointegrador.model.entity.SectionCategory;
import br.com.meli.projetointegrador.model.enums.ESectionCategory;
import br.com.meli.projetointegrador.model.repository.SectionCategoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ObjectUtils;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SectionCategoryServiceIntegrationTest {

    @Autowired
    private SectionCategoryRepository sectionCategoryRepository;

    @Autowired
    private SectionCategoryService sectionCategoryService;

    @BeforeEach
    void setup() {
        clearBase();

        SectionCategory sectionCategory = new SectionCategory()
                .name(ESectionCategory.FF)
                .build();
        sectionCategoryRepository.save(sectionCategory);
    }

    @AfterEach
    void tearDown() {
        clearBase();
    }

    @Test
    void find() {
        final SectionCategory sectionCategory = sectionCategoryService.find(ESectionCategory.FF);
        assertFalse(ObjectUtils.isEmpty(sectionCategory));
        assertEquals(ESectionCategory.FF, sectionCategory.getName());
    }

    @Test
    void findNot() {
        SectionCategoryException sectionCategoryException = assertThrows
                (SectionCategoryException.class,() -> sectionCategoryService.find(ESectionCategory.RF));

        String mensagemEsperada = "Categoria " + ESectionCategory.RF.toString() + " encontrada!!!";
        String mensagemRecebida = sectionCategoryException.getMessage();

        assertTrue(mensagemEsperada.contains(mensagemRecebida));
    }

    void clearBase() {
        sectionCategoryRepository.deleteAll();
    }

}