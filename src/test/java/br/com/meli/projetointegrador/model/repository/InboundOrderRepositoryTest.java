package br.com.meli.projetointegrador.model.repository;

import br.com.meli.projetointegrador.model.entity.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Jhony Zuim / Lucas Pereira / Edmilson Nobre / Rafael Vicente
 * @version 1.0.0
 * @since 15/10/2021
 * Repository de teste para trabalhar como uma porta ou janela de acesso a camada do banco da entity inboundOrder
 */

@DataMongoTest
class InboundOrderRepositoryTest {

    @Autowired
    private InboundOrderRepository inboundOrderRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private BatchStockRepository batchStockRepository;

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @BeforeEach
    void setUp() {
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
        batchStockRepository.save(batchStock);

        InboundOrder inboundOrder = new InboundOrder()
                .orderNumber(1)
                .orderDate(LocalDate.now())
                .section(section)
                .listBatchStock(Collections.singletonList(batchStock))
                .build();
        inboundOrderRepository.save(inboundOrder);
    }

    @AfterEach
    void cleanUpDatabase() {
        clearBase();
    }

    @Test
    void findByOrderNumber() {
        final Optional<InboundOrder> inboundOrder = inboundOrderRepository.findByOrderNumber(1);
        assertTrue(inboundOrder.isPresent());
        assertEquals(1, inboundOrder.get().getOrderNumber());
    }

    void clearBase() {
        warehouseRepository.deleteAll();
        sectionRepository.deleteAll();
        inboundOrderRepository.deleteAll();
        agentRepository.deleteAll();
        batchStockRepository.deleteAll();
    }

}