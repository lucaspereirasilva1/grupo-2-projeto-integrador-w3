package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.exception.AgentException;
import br.com.meli.projetointegrador.model.entity.Agent;
import br.com.meli.projetointegrador.model.entity.BatchStock;
import br.com.meli.projetointegrador.model.repository.BatchStockRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BatchStockServiceTest {

    private final BatchStockRepository mockBatchStockRepository = mock(BatchStockRepository.class);
    private final BatchStockService batchStockService = new BatchStockService(mockBatchStockRepository);

    @Test
    void validBatchStockAgentNotExistTest() {
        Agent agent = new Agent()
                .id("3")
                .name("lucas")
                .build();

        when((mockBatchStockRepository).findByAgent(any()))
                .thenReturn(Optional.empty());

        AgentException agentException = assertThrows(AgentException.class, () ->
                batchStockService.validBatchStockAgent(agent));

        String expectedMessage = "Representante nao foi vinculado ao estoque, por gentileza reenviar a request!!!";
        String receivedMessage = agentException.getMessage();

        assertTrue(expectedMessage.contains(receivedMessage));
    }

    @Test
    void validBatchStockAgentExistTest() {
        Agent agent = new Agent()
                .id("1")
                .name("lucas")
                .build();

        when(mockBatchStockRepository.findByAgent(any()))
                .thenReturn(Optional.of(new BatchStock()
                        .batchNumber(1)
                        .productId("QJ")
                        .currentTemperature(10.0F)
                        .minimumTemperature(5.0F)
                        .initialQuantity(2)
                        .currentQuantity(10)
                        .manufacturingDate(LocalDate.now())
                        .manufacturingTime(LocalDateTime.now())
                        .dueDate(LocalDate.now())
                        .agent(agent)
                        .build()));

        boolean exist = batchStockService.validBatchStockAgent(agent);

        assertTrue(exist);
    }
}
