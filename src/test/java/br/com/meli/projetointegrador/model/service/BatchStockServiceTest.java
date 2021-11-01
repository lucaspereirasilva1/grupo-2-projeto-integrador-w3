package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.exception.ProductExceptionNotFound;
import br.com.meli.projetointegrador.model.dto.*;
import br.com.meli.projetointegrador.model.entity.*;
import br.com.meli.projetointegrador.model.enums.ESectionCategory;
import br.com.meli.projetointegrador.model.repository.BatchStockRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * @author Jhony Zuim / Lucas Pereira / Edmilson Nobre / Rafael Vicente
 * @version 1.0.0
 * @since 15/10/2021
 * Camada de testes unitarios do service responsavel pela regra de negocio relacionada ao batchStock
 */

public class BatchStockServiceTest {

    private final BatchStockRepository mockBatchStockRepository = mock(BatchStockRepository.class);
    private final SectionService mockSectionService = mock(SectionService.class);
    private final AgentService mockAgentService = mock(AgentService.class);
    private final ProductService mockProductService = mock(ProductService.class);
    private final BatchStockService batchStockService = new BatchStockService(mockBatchStockRepository,
            mockSectionService, mockAgentService, mockProductService);

    @BeforeEach
    void setUp() {
    }

    @Test
    void postAllTest() {
        SectionDTO sectionDTO = new SectionDTO()
                .sectionCode("LA")
                .warehouseCode("SP")
                .build();

        AgentDTO agentDTO = new AgentDTO()
                .cpf("11122233344")
                .name("lucas")
                .build();

        Warehouse warehouse = new Warehouse()
                .warehouseCode("SP")
                .warehouseName("sao paulo")
                .build();

        Section section = new Section()
                .sectionCode("LA")
                .sectionName("laticinios")
                .maxLength(10)
                .warehouse(warehouse)
                .build();

        Agent agent = new Agent().
                cpf("11122233344").
                name("lucas").
                build();

        Product product = new Product()
                .productId("LE")
                .productName("leite")
                .section(section)
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

        when(mockProductService.find(anyString()))
                .thenReturn(product);
        when(mockProductService.validProductSection(anyString())).
                thenReturn(true);
        when(mockSectionService.validSectionLength(any(Section.class))).
                thenReturn(true);
        when(mockAgentService.find(anyString())).
                thenReturn(agent);
        when(mockSectionService.find(anyString())).
                thenReturn(section);
        when(mockBatchStockRepository.saveAll(anyList()))
                .thenReturn(Collections.singletonList(batchStock));

       batchStockService.postAll(Collections.singletonList(batchStock), agentDTO, sectionDTO);

        verify(mockBatchStockRepository, times(1)).saveAll(anyList());
    }

    @Test
    void putAllTest() {
        SectionDTO sectionDTO = new SectionDTO()
                .sectionCode("LA")
                .warehouseCode("SP")
                .build();

        AgentDTO agentDTO = new AgentDTO()
                .cpf("11122233344")
                .name("lucas")
                .build();

        Warehouse warehouse = new Warehouse()
                .warehouseCode("SP")
                .warehouseName("sao paulo")
                .build();

        Section section = new Section()
                .sectionCode("LA")
                .sectionName("laticinios")
                .maxLength(10)
                .warehouse(warehouse)
                .build();

        Agent agent = new Agent().
                cpf("11122233344").
                name("lucas").
                build();

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

        when(mockProductService.validProductSection(anyString())).
                thenReturn(true);
        when(mockSectionService.validSectionLength(any(Section.class))).
                thenReturn(true);
        when(mockBatchStockRepository.save(any(BatchStock.class)))
                .thenReturn(batchStock);

        batchStockService.putAll(Collections.singletonList(batchStock), Collections.singletonList(batchStockDTO)
                ,agentDTO, sectionDTO);

        verify(mockBatchStockRepository, times(1)).save(any(BatchStock.class));
    }


    @Test
    void validListProductIdNotExist(){

        ProductExceptionNotFound productException = assertThrows
                (ProductExceptionNotFound.class,() ->
                        batchStockService.listProductId("ME"));

        String menssagemEsperada = "Nao existe produto para esse codigo, por favor verifique o codigo inserido!";

        assertTrue(menssagemEsperada.contains(productException.getMessage()));

    }
}
