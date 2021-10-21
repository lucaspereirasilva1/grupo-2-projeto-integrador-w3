package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.exception.AgentException;
import br.com.meli.projetointegrador.model.dto.BatchStockDTO;
import br.com.meli.projetointegrador.model.dto.InboundOrderDTO;
import br.com.meli.projetointegrador.model.dto.SectionDTO;
import br.com.meli.projetointegrador.model.entity.Agent;
import br.com.meli.projetointegrador.model.entity.BatchStock;
import br.com.meli.projetointegrador.model.entity.InboudOrder;
import br.com.meli.projetointegrador.model.entity.Warehouse;
import br.com.meli.projetointegrador.model.repository.BatchStockRepository;
import br.com.meli.projetointegrador.model.repository.InboundOrderRepository;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class InboundOrderServiceTest {

    private final InboundOrderRepository mockInboundOrderRepository = mock(InboundOrderRepository.class);
    private final BatchStockRepository mockBatchStockRepository = mock(BatchStockRepository.class);
    private final InboundOrderService inboundOrderService = new InboundOrderService(mockInboundOrderRepository,
                                                                                    mockBatchStockRepository);
    private final List<BatchStock> listBatchStock = new ArrayList<>();
    private final ModelMapper modelMapper = new ModelMapper();

    public InboundOrderServiceTest() {
        makeData();
    }

    @Test
    void putTest() {
        boolean resultTest = false;

        List<InboudOrder> listInboudOrders = new ArrayList<>();

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

        listInboudOrders.add(modelMapper.map(inboundOrderDTO, InboudOrder.class));

        when((mockInboundOrderRepository).saveAll(any()))
                .thenReturn(listInboudOrders);

        InboundOrderDTO inboundOrderDTOReturn = inboundOrderService.put(inboundOrderDTO);

        for (InboudOrder i: listInboudOrders) {
            if (i.getOrderNumber().equals(inboundOrderDTOReturn.getOrderNumber())) {
                resultTest = true;
                break;
            }
        }

        assertTrue(resultTest);
    }

    @Test
    void validAgentNotExistTest() {
        Agent agent = new Agent()
                .id("3")
                .name("lucas")
                .build();

        when((mockBatchStockRepository).findByAgent(any()))
                .thenReturn(Optional.empty());

        AgentException agentException = assertThrows(AgentException.class, () ->
                inboundOrderService.validBatchStockAgent(agent));

        String expectedMessage = "Representante nao foi vinculado ao estoque, por gentileza reenviar a request!!!";
        String receivedMessage = agentException.getMessage();

        assertTrue(expectedMessage.contains(receivedMessage));
    }

    @Test
    void validAgentExistTest() {
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

        boolean exist = inboundOrderService.validBatchStockAgent(agent);

        assertTrue(exist);
    }


    void makeData() {
        Agent agent = new Agent()
                .id("1")
                .name("lucas")
                .build();

        Agent agent2 = new Agent()
                .id("2")
                .name("ed")
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
                .agent(agent2)
                .build();

        listBatchStock.add(batchStock);
        listBatchStock.add(batchStock1);
    }

    @Test
     void notValidWarehouse() {
        Warehouse warehouse = new Warehouse();
    }

}
