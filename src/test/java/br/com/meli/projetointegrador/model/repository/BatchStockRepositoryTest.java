package br.com.meli.projetointegrador.model.repository;

import br.com.meli.projetointegrador.model.entity.Agent;
import br.com.meli.projetointegrador.model.entity.BatchStock;
import org.junit.jupiter.api.AfterEach;
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

    @AfterEach
    void cleanUpDatabase() {
        batchStockRepository.deleteAll();
    }

    @Test
    void findByAgentTest() {
        Agent agent = new Agent()
                .name("lucas")
                .build();

        BatchStock batchStock = new BatchStock()
                .batchNumber(2)
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
        Optional<BatchStock> batchStockOptional = batchStockRepository.findByAgent(agent);
        assertTrue(batchStockOptional.isPresent());
        assertEquals(agent, batchStockOptional.get().getAgent());
    }

}
