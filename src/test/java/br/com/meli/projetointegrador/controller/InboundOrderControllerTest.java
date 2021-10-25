package br.com.meli.projetointegrador.controller;

import br.com.meli.projetointegrador.model.dto.BatchStockDTO;
import br.com.meli.projetointegrador.model.dto.InboundOrderDTO;
import br.com.meli.projetointegrador.model.dto.SectionDTO;
import br.com.meli.projetointegrador.model.entity.*;
import br.com.meli.projetointegrador.model.repository.*;
import br.com.meli.projetointegrador.model.service.SectionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

/**
 * @author Jhony Zuim / Lucas Pereira / Edmilson Nobre / Rafael Vicente
 * @version 1.0.0
 * @since 15/10/2021
 * Camada de teste do controller responsavel pela regra de negocio relacionada ao inboundOrderController
 */

@SpringBootTest
@AutoConfigureMockMvc
public class InboundOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SectionRepository sectionRepository;

    @MockBean
    private AgentRepository agentRepository;

    @MockBean
    private InboundOrderRepository inboundOrderRepository;

    @MockBean
    private WarehouseRepository warehouseRepository;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private BatchStockRepository batchStockRepository;

    @Test
    void postTest() throws Exception {
        SectionDTO sectionDTO = new SectionDTO()
                .sectionCode("FR")
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

        BatchStockDTO batchStockDTOUm = new BatchStockDTO()
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
                .batchStockDTO(Arrays.asList(batchStockDTO, batchStockDTOUm))
                .build();

        Warehouse warehouse = new Warehouse()
                .id("6171dd92a488fe7100a796b1")
                .warehouseName("sao paulo")
                .warehouseCode("SP")
                .build();

        Section section = new Section()
                .id("6171dd92a488fe7100a796b2")
                .sectionCode("FR")
                .sectionName("frios")
                .warehouse(warehouse)
                .maxLength(10)
                .build();

        Agent agent = new Agent()
                .id("6171de17f68cfd1376441264")
                .name("lucas")
                .cpf("11122233344")
                .build();

        Product product = new Product()
                .productCode("LEI")
                .productName("Leite")
                .section(section)
                .build();

        when(sectionRepository.findBySectionCode(anyString()))
                .thenReturn(Optional.of(section));
        when(agentRepository.findByCpf(anyString()))
                .thenReturn(Optional.of(agent));
        when(warehouseRepository.existsByWarehouseCode(anyString()))
                .thenReturn(true);
        when(productRepository.existsProductBySection(any(Section.class)))
                .thenReturn(true);
        when(productRepository.findByProductId(anyString()))
                .thenReturn(Optional.of(product));
        when(batchStockRepository.countBySection(any(Section.class)))
                .thenReturn((9L));

        MockHttpServletResponse response = mockMvc.perform(post("http://localhost:8080/api/v1/fresh-products/inboundorder")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(inboundOrderDTO)))
                .andReturn().getResponse();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    }

    @Test
    void updateTest() throws Exception {
        SectionDTO sectionDTO = new SectionDTO()
                .sectionCode("FR")
                .warehouseCode("SP")
                .build();

        BatchStockDTO batchStockDTO = new BatchStockDTO()
                .batchNumber(1)
                .productId("MU")
                .currentTemperature(10.0F)
                .minimumTemperature(5.0F)
                .initialQuantity(1)
                .currentQuantity(5)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalDateTime.now())
                .dueDate(LocalDate.now())
                .build();

        BatchStockDTO batchStockDTOUm = new BatchStockDTO()
                .batchNumber(2)
                .productId("PR")
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
                .batchStockDTO(Arrays.asList(batchStockDTO, batchStockDTOUm))
                .build();

        Warehouse warehouse = new Warehouse()
                .id("6171dd92a488fe7100a796b1")
                .warehouseName("sao paulo")
                .warehouseCode("SP")
                .build();

        Section section = new Section()
                .id("6171dd92a488fe7100a796b2")
                .sectionCode("FR")
                .sectionName("frios")
                .warehouse(warehouse)
                .build();

        Agent agent = new Agent()
                .id("6171de17f68cfd1376441264")
                .name("lucas")
                .cpf("11122233344")
                .build();

        BatchStock batchStock = new BatchStock()
                .id("6172d46f40032058ac5e6bf2")
                .batchNumber(1)
                .productId("QJ")
                .currentTemperature(10.0F)
                .minimumTemperature(5.0F)
                .initialQuantity(1)
                .currentQuantity(5)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalDateTime.now())
                .dueDate(LocalDate.now())
                .section(section)
                .agent(agent)
                .build();

        BatchStock batchStockUm = new BatchStock()
                .id("6172d46f40032058ac5e6bf3")
                .batchNumber(2)
                .productId("LE")
                .currentTemperature(20.0F)
                .minimumTemperature(15.0F)
                .initialQuantity(1)
                .currentQuantity(5)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalDateTime.now())
                .dueDate(LocalDate.now())
                .section(section)
                .agent(agent)
                .build();

        InboundOrder inboundOrder = new InboundOrder()
                .id("6172d46f40032058ac5e6bf4")
                .orderNumber(1)
                .orderDate(LocalDate.now())
                .section(section)
                .listBatchStock(Arrays.asList(batchStock, batchStockUm))
                .build();


        when(sectionRepository.findBySectionCode(anyString()))
                .thenReturn(Optional.of(section));

        when(agentRepository.findByCpf(anyString()))
                .thenReturn(Optional.of(agent));

        when(inboundOrderRepository.findByOrderNumber(anyInt()))
                .thenReturn(Optional.of(inboundOrder));

        MockHttpServletResponse response = mockMvc.perform(put("http://localhost:8080/api/v1/fresh-products/inboundorder")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(inboundOrderDTO)))
                .andReturn().getResponse();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    }

}
