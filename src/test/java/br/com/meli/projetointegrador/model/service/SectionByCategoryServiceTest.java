package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.exception.ProductException;
import br.com.meli.projetointegrador.model.entity.Section;
import br.com.meli.projetointegrador.model.entity.SectionByCategory;
import br.com.meli.projetointegrador.model.entity.SectionCategory;
import br.com.meli.projetointegrador.model.entity.Warehouse;
import br.com.meli.projetointegrador.model.enums.ESectionCategory;
import br.com.meli.projetointegrador.model.repository.SectionByCategoryRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class SectionByCategoryServiceTest {

    private final SectionByCategoryRepository mockSectionByCategoryRepository = mock(SectionByCategoryRepository.class);
    private final SectionByCategoryService sectionByCategoryService = new SectionByCategoryService(mockSectionByCategoryRepository);

    @Test
    void validProductSection() {
        Warehouse warehouse = new Warehouse()
                .warehouseCode("SP")
                .warehouseName("sao paulo")
                .build();

        Section section = new Section()
                .sectionCode("LA")
                .sectionName("laticinios")
                .maxLength(10)
                .warehouse(warehouse)
                .build();

        SectionCategory sectionCategory = new SectionCategory()
                .name(ESectionCategory.FS)
                .build();

        SectionByCategory sectionByCategory = new SectionByCategory()
                .category(sectionCategory)
                .section(section)
                .build();

        when(mockSectionByCategoryRepository.findByCategoryAndSection(any(),any()))
                .thenReturn(Optional.of(sectionByCategory));

        assertTrue(sectionByCategoryService.validProductSection(section, sectionCategory));
    }

    @Test
    void validProductNotExistTest(){
        Warehouse warehouse = new Warehouse()
                .warehouseCode("SP")
                .warehouseName("sao paulo")
                .build();

        Section sectionCO = new Section()
                .sectionCode("CO")
                .sectionName("congelados")
                .maxLength(10)
                .warehouse(warehouse)
                .build();

        SectionCategory sectionCategoryFS = new SectionCategory()
                .name(ESectionCategory.FS)
                .build();

        when(mockSectionByCategoryRepository.findByCategoryAndSection(any(SectionCategory.class),any(Section.class)))
                .thenReturn(Optional.empty());

        ProductException productException = assertThrows(ProductException.class, () ->
                sectionByCategoryService.validProductSection(sectionCO, sectionCategoryFS));

        String expectedMessage = "Categoria e setor nao parametrizados!!! Por gentileza verificar o base";
        assertTrue(expectedMessage.contains(productException.getMessage()));
    }

    @Test
    void validProductNotExistCategoryTest(){
        Warehouse warehouse = new Warehouse()
                .warehouseCode("SP")
                .warehouseName("sao paulo")
                .build();

        Section section = new Section()
                .sectionCode("LA")
                .sectionName("laticinios")
                .maxLength(10)
                .warehouse(warehouse)
                .build();

        SectionCategory sectionCategoryFS = new SectionCategory()
                .name(ESectionCategory.FS)
                .build();

        when(mockSectionByCategoryRepository.findByCategoryAndSection(any(),any()))
                .thenReturn(Optional.empty());

        ProductException productException = assertThrows(ProductException.class, () ->
                sectionByCategoryService.validProductSection(section, sectionCategoryFS));

        String expectedMessage = "Categoria e setor nao parametrizados!!! Por gentileza verificar o base";
        assertTrue(expectedMessage.contains(productException.getMessage()));
    }

}
