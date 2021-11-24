package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.exception.ProductException;
import br.com.meli.projetointegrador.model.entity.Section;
import br.com.meli.projetointegrador.model.entity.SectionByCategory;
import br.com.meli.projetointegrador.model.entity.SectionCategory;
import br.com.meli.projetointegrador.model.entity.Warehouse;
import br.com.meli.projetointegrador.model.enums.ESectionCategory;
import br.com.meli.projetointegrador.model.repository.SectionByCategoryRepository;
import br.com.meli.projetointegrador.model.repository.SectionCategoryRepository;
import br.com.meli.projetointegrador.model.repository.SectionRepository;
import br.com.meli.projetointegrador.model.repository.WarehouseRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class SectionByCategoryServiceIntegrationTest {

    @Autowired
    private SectionByCategoryService sectionByCategoryService;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private SectionCategoryRepository sectionCategoryRepository;

    @Autowired
    private SectionByCategoryRepository sectionByCategoryRepository;

    @BeforeEach
    void setUp() {
        clearBase();
        createData();
    }

    @AfterEach
    void cleanUpDatabase() {
        clearBase();
    }

    @Test
    void validProductSection() {
        final Optional<Section> section = sectionRepository.findBySectionCode("LA");
        final Optional<SectionCategory> sectionCategory = sectionCategoryRepository.findByName(ESectionCategory.FS);
        assertTrue(sectionByCategoryService.validProductSection(section.orElse(new Section())
                , sectionCategory.orElse(new SectionCategory())));
    }

    @Test
    void validProductNotExistTest(){
        Section sectionCO = new Section()
                .sectionCode("CO")
                .sectionName("congelados")
                .maxLength(10)
                .warehouse(warehouseRepository.findByWarehouseCode("SP").orElse(new Warehouse()))
                .build();
        sectionRepository.save(sectionCO);

        final Optional<SectionCategory> sectionCategory = sectionCategoryRepository.findByName(ESectionCategory.FS);

        ProductException productException = assertThrows(ProductException.class, () ->
                sectionByCategoryService.validProductSection(sectionCO, sectionCategory.orElse(new SectionCategory())));

        String expectedMessage = "Categoria e setor nao parametrizados!!! Por gentileza verificar o base";
        assertTrue(expectedMessage.contains(productException.getMessage()));
    }

    @Test
    void validProductNotExistCategoryTest(){
        SectionCategory sectionCategoryFS = new SectionCategory()
                .name(ESectionCategory.FF)
                .build();
        sectionCategoryRepository.save(sectionCategoryFS);

        final Optional<Section> section = sectionRepository.findBySectionCode("LA");

        ProductException productException = assertThrows(ProductException.class, () ->
                sectionByCategoryService.validProductSection(section.orElse(new Section()), sectionCategoryFS));

        String expectedMessage = "Categoria e setor nao parametrizados!!! Por gentileza verificar o base";
        assertTrue(expectedMessage.contains(productException.getMessage()));
    }

    void createData() {
        Warehouse warehouse = new Warehouse()
                .warehouseCode("SP")
                .warehouseName("sao paulo")
                .build();
        warehouseRepository.save(warehouse);

        Section section = new Section()
                .sectionCode("LA")
                .sectionName("laticinios")
                .maxLength(10)
                .warehouse(warehouse)
                .build();
        sectionRepository.save(section);

        SectionCategory sectionCategory = new SectionCategory()
                .name(ESectionCategory.FS)
                .build();
        sectionCategoryRepository.save(sectionCategory);

        SectionByCategory sectionByCategory = new SectionByCategory()
                .category(sectionCategory)
                .section(section)
                .build();
        sectionByCategoryRepository.save(sectionByCategory);
    }

    private void clearBase() {
        warehouseRepository.deleteAll();
        sectionRepository.deleteAll();
        sectionCategoryRepository.deleteAll();
        sectionByCategoryRepository.deleteAll();
    }

}
