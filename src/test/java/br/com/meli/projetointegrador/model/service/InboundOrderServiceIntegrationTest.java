package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.exception.InboundOrderException;
import br.com.meli.projetointegrador.exception.ValidInputException;
import br.com.meli.projetointegrador.model.dto.AgentDTO;
import br.com.meli.projetointegrador.model.dto.BatchStockDTO;
import br.com.meli.projetointegrador.model.dto.InboundOrderDTO;
import br.com.meli.projetointegrador.model.dto.SectionDTO;
import br.com.meli.projetointegrador.model.entity.*;
import br.com.meli.projetointegrador.model.enums.ESectionCategory;
import br.com.meli.projetointegrador.model.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
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
class InboundOrderServiceIntegrationTest {

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

    @Autowired
    private SectionCategoryRepository sectionCategoryRepository;

    @Test
    void postIntegrationTest() {
        clearBase();

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

        Agent agent = new Agent().
                cpf("11122233344").
                name("lucas").
                warehouse(warehouse).
                build();
        agentRepository.save(agent);

        SectionCategory sectionCategory = new SectionCategory()
                .name(ESectionCategory.FF)
                .build();
        sectionCategoryRepository.save(sectionCategory);

        Product product = new Product()
                .productId("QJ")
                .productName("Leite")
                .section(section)
                .productPrice(new BigDecimal("2.0"))
                .dueDate(LocalDate.now())
                .category(sectionCategory)
                .build();
        productRepository.save(product);

        SectionDTO sectionDTO = new SectionDTO()
                .sectionCode("LA")
                .warehouseCode("SP")
                .build();

        BatchStockDTO batchStockDTO = new BatchStockDTO()
                .batchNumber(2)
                .productId("QJ")
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
                .batchStockDTO(Collections.singletonList(batchStockDTO))
                .build();

        AgentDTO agentDTO = new AgentDTO().
                name("lucas").
                cpf("11122233344")
                .warehouseCode("SP")
                .build();

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
        clearBase();

        Warehouse warehouse = new Warehouse()
                .warehouseCode("SP")
                .warehouseName("sao paulo")
                .build();
        warehouseRepository.save(warehouse);

        Section sectionBase = new Section()
                .sectionCode("LA")
                .sectionName("laticinios")
                .maxLength(10)
                .warehouse(warehouse)
                .build();
        sectionRepository.save(sectionBase);

        Agent agentBase = new Agent().
                cpf("11122233344").
                name("lucas").
                warehouse(warehouse).
                build();
        agentRepository.save(agentBase);

        SectionCategory sectionCategory = new SectionCategory()
                .name(ESectionCategory.FF)
                .build();
        sectionCategoryRepository.save(sectionCategory);

        Product product = new Product()
                .productId("LE")
                .productName("Leite")
                .section(sectionBase)
                .productPrice(new BigDecimal("2.0"))
                .dueDate(LocalDate.now())
                .category(sectionCategory)
                .build();
        productRepository.save(product);

        SectionDTO sectionDTO = new SectionDTO()
                .sectionCode("LA")
                .warehouseCode("SP")
                .build();

        BatchStockDTO batchStockDTO = new BatchStockDTO()
                .batchNumber(1)
                .productId("LE")
                .currentTemperature(10.0F)
                .minimumTemperature(5.0F)
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
                .batchStockDTO(Collections.singletonList(batchStockDTO))
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
                .productId("LE")
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
        batchStockRepository.saveAll(Collections.singletonList(batchStock));

        InboundOrder inboundOrder = new InboundOrder()
                .orderNumber(1)
                .orderDate(LocalDate.now())
                .section(section.orElse(new Section()))
                .listBatchStock(Collections.singletonList(batchStock))
                .build();
        inboundOrderRepository.save(inboundOrder);

        inboundOrderDTO.getSectionDTO().setSectionCode("LA");

        List<BatchStockDTO> listBatchStockDTO = inboundOrderService.put(inboundOrderDTO, agentDTO);
        Optional<InboundOrder> inboudOrder = inboundOrderRepository.findByOrderNumber(inboundOrderDTO.getOrderNumber());

        assertTrue(inboudOrder.isPresent());
        assertFalse(listBatchStockDTO.isEmpty());
        assertEquals(inboundOrderDTO.getOrderNumber(), inboudOrder.orElse(new InboundOrder()).getOrderNumber());
        assertEquals(inboundOrderDTO.getOrderDate(), inboudOrder.orElse(new InboundOrder()).getOrderDate());
        assertEquals(inboundOrderDTO.getSectionDTO().getSectionCode(), inboudOrder.orElse(new InboundOrder()).getSection().getSectionCode());
    }

    @Test
    void putNotTest() {
        clearBase();

        SectionDTO sectionDTO = new SectionDTO()
                .sectionCode("LA")
                .warehouseCode("SP")
                .build();

        BatchStockDTO batchStockDTO = new BatchStockDTO()
                .batchNumber(1)
                .productId("LE")
                .currentTemperature(10.0F)
                .minimumTemperature(5.0F)
                .initialQuantity(1)
                .currentQuantity(5)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalDateTime.now())
                .dueDate(LocalDate.now())
                .build();

        InboundOrderDTO inboundOrderDTO = new InboundOrderDTO()
                .orderNumber(50)
                .orderDate(LocalDate.now())
                .sectionDTO(sectionDTO)
                .batchStockDTO(Collections.singletonList(batchStockDTO))
                .build();

        AgentDTO agentDTO = new AgentDTO().
                name("lucas").
                cpf("11122233344").
                warehouseCode("SP").
                build();

        InboundOrderException inboundOrderException = assertThrows
                (InboundOrderException.class, () -> inboundOrderService.put(inboundOrderDTO, agentDTO));

        String mensagemEsperada = "Ordem de entrada nao existe!!! Por gentileza realizar o cadastro antes de atualizar";
        String mensagemRecebida = inboundOrderException.getMessage();

        assertTrue(mensagemEsperada.contains(mensagemRecebida));
    }

    @Test
    void inputValidIntegrationTest() {
        clearBase();

        clearBase();

        Warehouse warehouse = new Warehouse()
                .warehouseCode("SP")
                .warehouseName("sao paulo")
                .build();
        warehouseRepository.save(warehouse);

        Section sectionBase = new Section()
                .sectionCode("LA")
                .sectionName("laticinios")
                .maxLength(10)
                .warehouse(warehouse)
                .build();
        sectionRepository.save(sectionBase);

        Agent agentBase = new Agent().
                cpf("11122233344").
                name("lucas").
                warehouse(warehouse).
                build();
        agentRepository.save(agentBase);

        SectionCategory sectionCategory = new SectionCategory()
                .name(ESectionCategory.FF)
                .build();
        sectionCategoryRepository.save(sectionCategory);

        Product product = new Product()
                .productId("LE")
                .productName("Leite")
                .section(sectionBase)
                .productPrice(new BigDecimal("2.0"))
                .dueDate(LocalDate.now())
                .category(sectionCategory)
                .build();
        productRepository.save(product);

        SectionDTO sectionDTO = new SectionDTO()
                .sectionCode("LA")
                .warehouseCode("SP")
                .build();

        BatchStockDTO batchStockDTO = new BatchStockDTO()
                .batchNumber(1)
                .productId("LE")
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
                warehouseCode("XX").
                build();

        ValidInputException validInputException = assertThrows
                (ValidInputException.class,() -> inboundOrderService.inputValid(inboundOrderDTO, agentDTO));

        String mensagemEsperada = "Problema na validacao dos dados de entrada!!!";
        String mensagemRecebida = validInputException.getMessage();

        assertTrue(mensagemEsperada.contains(mensagemRecebida));
    }

    void clearBase() {
        sectionRepository.deleteAll();
        inboundOrderRepository.deleteAll();
        agentRepository.deleteAll();
        batchStockRepository.deleteAll();
        warehouseRepository.deleteAll();
        sectionCategoryRepository.deleteAll();
        productRepository.deleteAll();
    }

}
