package br.com.meli.projetointegrador.model.repository;

import br.com.meli.projetointegrador.model.entity.Agent;
import br.com.meli.projetointegrador.model.entity.BatchStock;
import br.com.meli.projetointegrador.model.entity.Section;
import br.com.meli.projetointegrador.model.entity.Warehouse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
public class BatchStockRepositoryTest {

    @Autowired
    private BatchStockRepository batchStockRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private AgentRepository agentRepository;

    @BeforeEach
    void setUp() {
        clearBase();

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

        Agent agent = new Agent()
                .name("lucas")
                .cpf("11122233344")
                .build();
        agentRepository.save(agent);

        BatchStock batchStock = new BatchStock()
                .batchNumber(1)
                .productId("LE")
                .currentTemperature(20.0F)
                .minimumTemperature(15.0F)
                .initialQuantity(1)
                .currentQuantity(5)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalDateTime.now())
                .dueDate(LocalDate.now())
                .agent(agent)
                .build();

        batchStockRepository.save(batchStock);
    }

    @AfterEach
    void cleanUpDatabase() {
        clearBase();
    }

    @Test
    void findByAgentTest() {
        Agent agent = new Agent()
                .name("lucas")
                .build();
        Optional<BatchStock> batchStockOptional = batchStockRepository.findByAgent(agent);
        assertTrue(batchStockOptional.isPresent());
        assertEquals(agent, batchStockOptional.get().getAgent());
    }

    void clearBase() {
        agentRepository.deleteAll();
        warehouseRepository.deleteAll();
        sectionRepository.deleteAll();
        batchStockRepository.deleteAll();
    }

}
