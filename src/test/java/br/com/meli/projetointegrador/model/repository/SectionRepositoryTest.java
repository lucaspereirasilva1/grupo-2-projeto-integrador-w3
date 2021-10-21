package br.com.meli.projetointegrador.model.repository;

import br.com.meli.projetointegrador.model.entity.Section;
import br.com.meli.projetointegrador.model.entity.Warehouse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
public class SectionRepositoryTest {

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

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
    void findBySectionCode() {
        Optional<Section> section = sectionRepository.findBySectionCode("FR");
        assertTrue(section.isPresent());
    }

    @Test
    void saveTest() {
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

}
