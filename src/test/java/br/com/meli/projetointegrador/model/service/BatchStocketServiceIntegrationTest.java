package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.exception.AgentException;
import br.com.meli.projetointegrador.model.entity.Agent;
import br.com.meli.projetointegrador.model.entity.BatchStock;
import br.com.meli.projetointegrador.model.repository.BatchStockRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class BatchStocketServiceIntegrationTest {

    @Autowired
    private BatchStockRepository batchStockRepository;

    @Autowired
    private BatchStockService batchStockService;

    @BeforeEach
    void setUp() {
        Agent agent = new Agent()
                .name("lucas")
                .build();

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
        batchStockRepository.deleteAll();
    }

    @Test
    void validAgentExistTest() {

        Agent agent = new Agent()
                .name("lucas")
                .build();

        assertTrue(batchStockService.validBatchStockAgent(agent));

    }

    @Test
    void validAgentNotExistTest() {

        Agent agent = new Agent()
                .name("ed")
                .build();

        AgentException agentException = assertThrows(AgentException.class, () ->
                batchStockService.validBatchStockAgent(agent));

        String expectedMessage = "Representante nao foi vinculado ao estoque, por gentileza reenviar a request!!!";
        String receivedMessage = agentException.getMessage();

        assertTrue(expectedMessage.contains(receivedMessage));

    }

}
