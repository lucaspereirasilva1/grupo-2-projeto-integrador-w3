package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.model.dto.AgentDTO;
import br.com.meli.projetointegrador.model.dto.BatchStockDTO;
import br.com.meli.projetointegrador.model.dto.InboundOrderDTO;
import br.com.meli.projetointegrador.model.dto.SectionDTO;
import br.com.meli.projetointegrador.model.entity.*;
import br.com.meli.projetointegrador.model.repository.InboundOrderRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

public class InboundOrderServiceTest {

    private final InboundOrderRepository mockInboundOrderRepository = mock(InboundOrderRepository.class);
    private final BatchStockService mockBatchStockService = mock(BatchStockService.class);
    private final SectionService mockSectionService = mock(SectionService.class);
    private final WarehouseService mockWarehouseService = mock(WarehouseService.class);
    private final InboundOrderService inboundOrderService = new InboundOrderService(mockInboundOrderRepository,
            mockBatchStockService, mockSectionService, mockWarehouseService);
    private final List<BatchStock> listBatchStock = new ArrayList<>();

    public InboundOrderServiceTest() {
        makeData();
    }

    @Test
    void postTest() {
        SectionDTO sectionDTO = new SectionDTO()
                .sectionCode("LA")
                .warehouseCode("SP")
                .build();

        BatchStockDTO batchStockDTO = new BatchStockDTO()
                .batchNumber(1)
                .productId("QJ")
                .currentTemperature(10.0F)
                .minimumTemperature(5.0F)
                .initialQuantity(1)
                .currentQuantity(5)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalDateTime.now())
                .dueDate(LocalDate.now())
                .build();

        BatchStockDTO batchStockDTO1 = new BatchStockDTO()
                .batchNumber(2)
                .productId("LE")
                .currentTemperature(20.0F)
                .minimumTemperature(15.0F)
                .initialQuantity(1)
                .currentQuantity(5)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalDateTime.now())
                .dueDate(LocalDate.now())
                .build();

        InboundOrderDTO inboundOrderDTO = new InboundOrderDTO()
                .orderNumber(1)
                .orderDate(LocalDate.now())
                .sectionDTO(sectionDTO)
                .batchStockDTO(Arrays.asList(batchStockDTO, batchStockDTO1))
                .build();

        AgentDTO agentDTO = new AgentDTO().
                name("lucas").
                cpf("11122233344");

        Warehouse warehouse = new Warehouse()
                .warehouseCode("SP")
                .warehouseName("sao paulo")
                .build();

        Agent agent = new Agent().
                cpf("11122233344").
                name("lucas")
                .warehouse(warehouse)
                .build();

        Section section = new Section()
                .sectionCode("LA")
                .sectionName("laticinios")
                .maxLength(10)
                .warehouse(warehouse)
                .build();

        BatchStock batchStock = new BatchStock()
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

        InboundOrder inboundOrder = new InboundOrder()
                .orderNumber(1)
                .orderDate(LocalDate.now())
                .section(section)
                .listBatchStock(Collections.singletonList(batchStock))
                .build();

        when(mockSectionService.find(anyString()))
                .thenReturn(section);
        when(mockInboundOrderRepository.save(any(InboundOrder.class)))
                .thenReturn(inboundOrder);

        doNothing().when(mockBatchStockService).postAll(anyList(),
                any(AgentDTO.class),
                any(SectionDTO.class));

        List<BatchStockDTO> listBatchStockDTO = inboundOrderService.post(inboundOrderDTO, agentDTO);

        verify(mockInboundOrderRepository, times(1)).save(any(InboundOrder.class));

        verify(mockBatchStockService, times(1)).postAll(anyList(),
                any(AgentDTO.class),
                any(SectionDTO.class));

        assertFalse(listBatchStockDTO.isEmpty());

    }

    void makeData() {
        Agent agent = new Agent()
                .name("lucas")
                .build();

        BatchStock batchStock = new BatchStock()
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
                .build();

        BatchStock batchStock1 = new BatchStock()
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
                .build();

        listBatchStock.add(batchStock);
        listBatchStock.add(batchStock1);
    }

}
