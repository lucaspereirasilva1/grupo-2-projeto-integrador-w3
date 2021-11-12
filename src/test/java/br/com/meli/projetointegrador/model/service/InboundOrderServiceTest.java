package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.exception.InboundOrderException;
import br.com.meli.projetointegrador.exception.ValidInputException;
import br.com.meli.projetointegrador.model.dto.AgentDTO;
import br.com.meli.projetointegrador.model.dto.BatchStockDTO;
import br.com.meli.projetointegrador.model.dto.InboundOrderDTO;
import br.com.meli.projetointegrador.model.dto.SectionDTO;
import br.com.meli.projetointegrador.model.entity.*;
import br.com.meli.projetointegrador.model.repository.InboundOrderRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Jhony Zuim / Lucas Pereira / Edmilson Nobre / Rafael Vicente
 * @version 1.0.0
 * @since 15/10/2021
 * Camada de testes unitarios do service responsavel pela regra de negocio relacionada ao inboundOrder
 */

class InboundOrderServiceTest {

    private final InboundOrderRepository mockInboundOrderRepository = mock(InboundOrderRepository.class);
    private final BatchStockService mockBatchStockService = mock(BatchStockService.class);
    private final SectionService mockSectionService = mock(SectionService.class);
    private final WarehouseService mockWarehouseService = mock(WarehouseService.class);
    private final InboundOrderService inboundOrderService = new InboundOrderService(mockInboundOrderRepository,
            mockBatchStockService, mockSectionService, mockWarehouseService);

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

    @Test
    void putTest() {
        boolean validUpdate = false;

        SectionDTO sectionDTO = new SectionDTO()
                .sectionCode("LA")
                .warehouseCode("SP")
                .build();

        BatchStockDTO batchStockDTO = new BatchStockDTO()
                .batchNumber(1)
                .productId("QJ")
                .currentTemperature(10.0F)
                .minimumTemperature(5.0F)
                .initialQuantity(2)
                .currentQuantity(2)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalDateTime.now())
                .dueDate(LocalDate.now())
                .build();

        BatchStockDTO batchStockDTO1 = new BatchStockDTO()
                .batchNumber(2)
                .productId("LE")
                .currentTemperature(20.0F)
                .minimumTemperature(15.0F)
                .initialQuantity(2)
                .currentQuantity(2)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalDateTime.now())
                .dueDate(LocalDate.now())
                .build();

        InboundOrderDTO inboundOrderDTO = new InboundOrderDTO()
                .orderNumber(1)
                .orderDate(LocalDate.of(2000, 1, 1))
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

        when(mockInboundOrderRepository.findByOrderNumber(anyInt()))
                .thenReturn(Optional.of(inboundOrder));
        doNothing().when(mockBatchStockService).putAll(anyList(),
                anyList(),
                any(AgentDTO.class),
                any(SectionDTO.class));

        List<BatchStockDTO> listBatchStockDTO = inboundOrderService.put(inboundOrderDTO, agentDTO);

        verify(mockBatchStockService, times(1)).putAll(anyList(),
                anyList(),
                any(AgentDTO.class),
                any(SectionDTO.class));
        verify(mockInboundOrderRepository, times(1)).save(any(InboundOrder.class));

        for (BatchStockDTO b:listBatchStockDTO) {
            if (b.getCurrentQuantity() != 2 |
            b.getInitialQuantity() != 2) {
                break;
            }else {
                validUpdate = true;
            }
        }

        assertFalse(listBatchStockDTO.isEmpty());
        assertTrue(validUpdate);
    }

    @Test
    void putNotTest() {
        SectionDTO sectionDTO = new SectionDTO()
                .sectionCode("LA")
                .warehouseCode("SP")
                .build();

        BatchStockDTO batchStockDTO = new BatchStockDTO()
                .batchNumber(1)
                .productId("QJ")
                .currentTemperature(10.0F)
                .minimumTemperature(5.0F)
                .initialQuantity(2)
                .currentQuantity(2)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalDateTime.now())
                .dueDate(LocalDate.now())
                .build();

        BatchStockDTO batchStockDTO1 = new BatchStockDTO()
                .batchNumber(2)
                .productId("LE")
                .currentTemperature(20.0F)
                .minimumTemperature(15.0F)
                .initialQuantity(2)
                .currentQuantity(2)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalDateTime.now())
                .dueDate(LocalDate.now())
                .build();

        InboundOrderDTO inboundOrderDTO = new InboundOrderDTO()
                .orderNumber(1)
                .orderDate(LocalDate.of(2000, 1, 1))
                .sectionDTO(sectionDTO)
                .batchStockDTO(Arrays.asList(batchStockDTO, batchStockDTO1))
                .build();

        AgentDTO agentDTO = new AgentDTO().
                name("lucas").
                cpf("11122233344");

        when(mockInboundOrderRepository.findByOrderNumber(anyInt()))
                .thenReturn(Optional.empty());
        doNothing().when(mockBatchStockService).putAll(anyList(),
                anyList(),
                any(AgentDTO.class),
                any(SectionDTO.class));

        InboundOrderException inboundOrderException = assertThrows
                (InboundOrderException.class,() -> inboundOrderService.put(inboundOrderDTO, agentDTO));

        String mensagemEsperada = "Ordem de entrada nao existe!!! Por gentileza realizar o cadastro antes de atualizar";
        String mensagemRecebida = inboundOrderException.getMessage();

        assertTrue(mensagemEsperada.contains(mensagemRecebida));
    }

    @Test
    void inputValid() {
        SectionDTO sectionDTO = new SectionDTO()
                .sectionCode("LA")
                .warehouseCode("MG")
                .build();

        BatchStockDTO batchStockDTO = new BatchStockDTO()
                .batchNumber(1)
                .productId("QJ")
                .currentTemperature(10.0F)
                .minimumTemperature(5.0F)
                .initialQuantity(2)
                .currentQuantity(2)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalDateTime.now())
                .dueDate(LocalDate.now())
                .build();

        InboundOrderDTO inboundOrderDTO = new InboundOrderDTO()
                .orderNumber(1)
                .orderDate(LocalDate.of(2000, 1, 1))
                .sectionDTO(sectionDTO)
                .batchStockDTO(Collections.singletonList(batchStockDTO))
                .build();

        AgentDTO agentDTO = new AgentDTO().
                name("lucas").
                cpf("11122233344").
                warehouseCode("SP").
                build();

        when(mockWarehouseService.validWarehouse(anyString()))
                .thenReturn(false);
        when(mockSectionService.validSection(anyString()))
                .thenReturn(false);

        ValidInputException validInputException = assertThrows
                (ValidInputException.class,() -> inboundOrderService.inputValid(inboundOrderDTO, agentDTO));

        String mensagemEsperada = "Problema na validacao dos dados de entrada!!!";
        String mensagemRecebida = validInputException.getMessage();

        assertTrue(mensagemEsperada.contains(mensagemRecebida));
    }

    @Test
    void postDueDateNotValidTest() {
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
                .dueDate(LocalDate.of(2020, 1, 1))
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
                .dueDate(LocalDate.of(2020, 1, 1))
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

        InboundOrderException inboundOrderException = assertThrows
                (InboundOrderException.class,() -> inboundOrderService.post(inboundOrderDTO, agentDTO));

        String mensagemEsperada = "Estoque com data retroativa: " + LocalDate.of(2020, 1, 1);
        String mensagemRecebida = inboundOrderException.getMessage();

        assertTrue(mensagemEsperada.contains(mensagemRecebida));
    }

    @Test
    void putDueDateNotValidTest() {
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
                .dueDate(LocalDate.of(2020, 1, 1))
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
                .dueDate(LocalDate.of(2020, 1, 1))
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

        InboundOrderException inboundOrderException = assertThrows
                (InboundOrderException.class,() -> inboundOrderService.put(inboundOrderDTO, agentDTO));

        String mensagemEsperada = "Estoque com data retroativa: " + LocalDate.of(2020, 1, 1);
        String mensagemRecebida = inboundOrderException.getMessage();

        assertTrue(mensagemEsperada.contains(mensagemRecebida));
    }

}
