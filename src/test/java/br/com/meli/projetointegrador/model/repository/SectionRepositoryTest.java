package br.com.meli.projetointegrador.model.repository;

import br.com.meli.projetointegrador.model.entity.Section;
import br.com.meli.projetointegrador.model.entity.Warehouse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Jhony Zuim / Lucas Pereira / Edmilson Nobre / Rafael Vicente
 * @version 1.0.0
 * @since 15/10/2021
 * Repository de teste para trabalhar como uma porta ou janela de acesso a camada do banco da entity section
 */

@DataMongoTest
public class SectionRepositoryTest {

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private BatchStockRepository batchStockRepository;

    @BeforeEach
    void setUp() {
        Warehouse warehouse = new Warehouse()
                .warehouseCode("SP")
                .warehouseName("sao paulo")
                .build();
        warehouseRepository.save(warehouse);

        Section section = new Section()
                .sectionCode("FR")
                .sectionName("frios")
                .maxLength(10)
                .warehouse(warehouse)
                .build();
        sectionRepository.save(section);
    }

    @AfterEach
    void cleanUpDatabase() {
        sectionRepository.deleteAll();
        warehouseRepository.deleteAll();
    }

    @Test
    void findBySectionCodeTest() {
        assertTrue(sectionRepository.existsSectionBySectionCode("FR"));
    }

    @Test
    void registerBatchIfSectorNoFull() {
        Warehouse warehouse = new Warehouse()
                .warehouseCode("RS")
                .warehouseName("Porto Alegre")
                .build();
        warehouseRepository.save(warehouse);

        Section section = new Section()
                .sectionCode("FR")
                .sectionName("frios")
                .maxLength(1)
                .warehouse(warehouse)
                .build();
        sectionRepository.save(section);

        assertTrue(batchStockRepository.countBySection(section) < section.getMaxLength());
    }

    @Test
    void existsSectionBySectionCodeTest() {
        assertTrue(sectionRepository.existsSectionBySectionCode("FR"));
    }

}
