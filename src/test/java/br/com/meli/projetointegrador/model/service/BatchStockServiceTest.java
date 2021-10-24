package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.model.entity.Agent;
import br.com.meli.projetointegrador.model.entity.BatchStock;
import br.com.meli.projetointegrador.model.entity.Section;
import br.com.meli.projetointegrador.model.entity.Warehouse;
import br.com.meli.projetointegrador.model.repository.BatchStockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class BatchStockServiceTest {

    private final BatchStockRepository mockBatchStockRepository = mock(BatchStockRepository.class);
    private final SectionService mockSectionService = mock(SectionService.class);
    private final AgentService mockAgentService = mock(AgentService.class);
    private final ProductService mockProductService = mock(ProductService.class);
    private final BatchStockService batchStockService = new BatchStockService(mockBatchStockRepository,
            mockSectionService, mockAgentService, mockProductService);
    private final List<BatchStock> listBatchStock = new ArrayList<>();

    @BeforeEach
    void setUp() {
        Warehouse warehouse = new Warehouse()
                .id("1")
                .warehouseCode("SP")
                .warehouseName("sao paulo")
                .build();

        Section section = new Section()
                .id("1")
                .sectionCode("LA")
                .sectionName("laticinios")
                .maxLength(10)
                .warehouse(warehouse)
                .build();

        Agent agent = new Agent().
                id("1").
                cpf("11122233344").
                name("lucas").
                build();

        BatchStock batchStock = new BatchStock()
                .id("1")
                .batchNumber(1)
                .productId("QJ")
                .currentTemperature(10.0F)
                .minimumTemperature(5.0F)
                .initialQuantity(1)
                .currentQuantity(5)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalDateTime.now())
                .dueDate(LocalDate.now())
                .agent(agent)
                .section(section)
                .build();

        listBatchStock.add(batchStock);
    }

    @Test
    void putTest() {
        Warehouse warehouse = new Warehouse()
                .id("1")
                .warehouseCode("SP")
                .warehouseName("sao paulo")
                .build();

        Section section = new Section()
                .id("1")
                .sectionCode("LA")
                .sectionName("laticinios")
                .maxLength(10)
                .warehouse(warehouse)
                .build();

        Agent agent = new Agent()
                .cpf("11122233344")
                .name("lucas")
                .build();

        BatchStock batchStock = new BatchStock()
                .id("1")
                .batchNumber(1)
                .productId("QJ")
                .currentTemperature(10.0F)
                .minimumTemperature(5.0F)
                .initialQuantity(1)
                .currentQuantity(5)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalDateTime.now())
                .dueDate(LocalDate.now())
                .agent(agent)
                .section(section)
                .build();

//        when(mockSectionService.find(anyString())).
 //               thenReturn(section);
        when(mockAgentService.find(anyString())).
                thenReturn(agent);

        batchStockService.put(batchStock, section, agent);

        assertEquals(listBatchStock.get(0).getId(), batchStock.getId());
    }
}
