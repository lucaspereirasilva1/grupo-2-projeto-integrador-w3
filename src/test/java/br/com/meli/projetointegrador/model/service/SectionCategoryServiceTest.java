package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.exception.SectionCategoryException;
import br.com.meli.projetointegrador.model.entity.SectionCategory;
import br.com.meli.projetointegrador.model.enums.ESectionCategory;
import br.com.meli.projetointegrador.model.repository.SectionCategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.util.ObjectUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SectionCategoryServiceTest {

    private final SectionCategoryRepository mockSectionRepository = mock(SectionCategoryRepository.class);
    private final SectionCategoryService sectionCategoryService = new SectionCategoryService(mockSectionRepository);

    @Test
    void find() {
        when(mockSectionRepository.findByName(any(ESectionCategory.class)))
                .thenReturn(Optional.of(new SectionCategory().name(ESectionCategory.FF).build()));
        final SectionCategory sectionCategory = sectionCategoryService.find(ESectionCategory.FF);
        assertFalse(ObjectUtils.isEmpty(sectionCategory));
        assertEquals(ESectionCategory.FF, sectionCategory.getName());
    }

    @Test
    void findNot() {
        when(mockSectionRepository.findByName(any(ESectionCategory.class)))
                .thenReturn(Optional.empty());
        SectionCategoryException sectionCategoryException = assertThrows
                (SectionCategoryException.class,() -> sectionCategoryService.find(ESectionCategory.FF));

        String mensagemEsperada = "Categoria " + ESectionCategory.FF.toString() + " encontrada!!!";
        String mensagemRecebida = sectionCategoryException.getMessage();

        assertTrue(mensagemEsperada.contains(mensagemRecebida));
    }

}