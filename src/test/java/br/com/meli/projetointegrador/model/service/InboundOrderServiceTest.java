package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.model.dto.BatchStockDTO;
import br.com.meli.projetointegrador.model.dto.InboundOrderDTO;
import br.com.meli.projetointegrador.model.dto.SectionDTO;
import br.com.meli.projetointegrador.model.entity.InboudOrder;
import br.com.meli.projetointegrador.model.repository.InboundOrderRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class InboundOrderServiceTest {

    private final InboundOrderRepository mockInboundOrderRepository = mock(InboundOrderRepository.class);
    private final InboundOrderService inboundOrderService = new InboundOrderService(mockInboundOrderRepository);

    @Test
    void putInboundOrderTest() {

        boolean resultTest = false;

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

        when((mockInboundOrderRepository).saveAll(any()))
                .thenReturn(null);

        InboundOrderDTO inboundOrderDTOReturn = inboundOrderService.put(inboundOrderDTO);

        for (InboudOrder i: inboundOrderService.getListInboudOrders()) {
            if (i.getOrderNumber().equals(inboundOrderDTOReturn.getOrderNumber())) {
                resultTest = true;
                break;
            }
        }

        assertTrue(resultTest);
    }
}
