package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.model.dto.*;
import br.com.meli.projetointegrador.model.entity.*;
import br.com.meli.projetointegrador.model.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Jhony Zuim / Lucas Pereira / Edmilson Nobre / Rafael Vicente
 * @version 1.0.0
 * @since 15/10/2021
 * Camada de teste integrado do service responsavel pela regra de negocio relacionada ao inboundOrder
 */

@SpringBootTest
public class InboundOrderServiceIntegrationTest {

    @Autowired
    private InboundOrderService inboundOrderService;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private InboundOrderRepository inboundOrderRepository;

    @Autowired
    private BatchStockRepository batchStockRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        clearBase();
        createData();
    }

    @AfterEach
    void cleanUpDatabase() {
        clearBase();
    }

    @Test
    void postIntegrationTest() {
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

        AgentDTO agentDTO = new AgentDTO().
                name("lucas").
                cpf("11122233344");

        List<BatchStockDTO> listBatchStockDTO = inboundOrderService.post(inboundOrderDTO, agentDTO);
        Optional<InboundOrder> inboudOrder = inboundOrderRepository.findByOrderNumber(inboundOrderDTO.getOrderNumber());

        assertTrue(inboudOrder.isPresent());
        assertFalse(listBatchStockDTO.isEmpty());
        assertEquals(inboundOrderDTO.getOrderNumber(), inboudOrder.orElse(new InboundOrder()).getOrderNumber());
        assertEquals(inboundOrderDTO.getOrderDate(), inboudOrder.orElse(new InboundOrder()).getOrderDate());
        assertEquals(inboundOrderDTO.getSectionDTO().getSectionCode(), inboudOrder.orElse(new InboundOrder()).getSection().getSectionCode());
    }

    @Test
    void putTest() {
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

        AgentDTO agentDTO = new AgentDTO().
                name("lucas").
                cpf("11122233344").
                warehouseCode("SP").
                build();

        Optional<Section> section = sectionRepository.findBySectionCode(inboundOrderDTO.getSectionDTO().getSectionCode());
        Optional<Agent> agent = agentRepository.findByCpf(agentDTO.getCpf());

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
                .section(section.orElse(new Section()))
                .agent(agent.orElse(new Agent()))
                .build();
        BatchStock batchStockUm = new BatchStock()
                .batchNumber(2)
                .productId("LE")
                .currentTemperature(20.0F)
                .minimumTemperature(15.0F)
                .initialQuantity(1)
                .currentQuantity(5)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalDateTime.now())
                .dueDate(LocalDate.now())
                .section(section.orElse(new Section()))
                .agent(agent.orElse(new Agent()))
                .build();
        batchStockRepository.saveAll(Arrays.asList(batchStock, batchStockUm));

        InboundOrder inboundOrder = new InboundOrder()
                .orderNumber(1)
                .orderDate(LocalDate.now())
                .section(section.orElse(new Section()))
                .listBatchStock(Arrays.asList(batchStock, batchStockUm))
                .build();
        inboundOrderRepository.save(inboundOrder);

        List<BatchStockDTO> listBatchStockDTO = inboundOrderService.put(inboundOrderDTO, agentDTO);
        Optional<InboundOrder> inboudOrder = inboundOrderRepository.findByOrderNumber(inboundOrderDTO.getOrderNumber());

        assertTrue(inboudOrder.isPresent());
        assertFalse(listBatchStockDTO.isEmpty());
        assertEquals(inboundOrderDTO.getOrderNumber(), inboudOrder.orElse(new InboundOrder()).getOrderNumber());
        assertEquals(inboundOrderDTO.getOrderDate(), inboudOrder.orElse(new InboundOrder()).getOrderDate());
        assertEquals(inboundOrderDTO.getSectionDTO().getSectionCode(), inboudOrder.orElse(new InboundOrder()).getSection().getSectionCode());
    }

    void clearBase() {
        sectionRepository.deleteAll();
        inboundOrderRepository.deleteAll();
        agentRepository.deleteAll();
        batchStockRepository.deleteAll();
        warehouseRepository.deleteAll();
    }

    void createData() {
        List<Product> listProduct = new ArrayList<>();

        Warehouse warehouse = new Warehouse()
                .warehouseCode("SP")
                .warehouseName("sao paulo")
                .build();
        warehouseRepository.save(warehouse);

        Section section = new Section()
                .sectionCode("LA")
                .sectionName("laticinios")
                .maxLength(10)
                .warehouse(warehouse)
                .build();
        sectionRepository.save(section);

        Section sectionDois = new Section()
                .sectionCode("CO")
                .sectionName("Congelados")
                .maxLength(10)
                .warehouse(warehouse)
                .build();
        sectionRepository.save(sectionDois);

        Agent agent = new Agent().
                cpf("11122233344").
                name("lucas").
                warehouse(warehouse).
                build();
        agentRepository.save(agent);

        Product product = new Product()
                .productCode("LE")
                .productName("leite")
                .section(section)
                .build();

        Product productUm = new Product()
                .productCode("QJ")
                .productName("queijo")
                .section(section)
                .build();
        listProduct.add(product);
        listProduct.add(productUm);
        productRepository.saveAll(listProduct);
    }

}
