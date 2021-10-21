package br.com.meli.projetointegrador.model.repository;

import br.com.meli.projetointegrador.model.entity.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
public class InboundOrderRepositoryTest {

    @Autowired
    private InboundOrderRepository inboundOrderRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private BatchStockRepository batchStockRepository;

    @Autowired
    private AgentRepository agentRepository;

    @BeforeEach
    void setUp() {
        clearBase();

        Section section = new Section()
                .sectionCode("LA")
                .sectionName("laticinios")
                .maxLength(10)
                .build();
        sectionRepository.save(section);

        Agent agent = new Agent().
                cpf("11122233344").
                name("lucas").
                build();

        agentRepository.save(agent);
    }

    @AfterEach
    void cleanUpDatabase() {
        clearBase();
    }

    @Test
    void saveTest() {

        Optional<Section> section = sectionRepository.findBySectionCode("LA");
        Optional<Agent> agent = agentRepository.findByCpf("11122233344");


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
                .agent(agent.orElse(new Agent()))
                .section(section.get())
                .build();
        batchStockRepository.save(batchStock);

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
                .agent(agent.orElse(new Agent()))
                .section(section.get())
                .build();
        batchStockRepository.save(batchStockUm);

        InboudOrder inboudOrder = new InboudOrder()
                .orderNumber(1)
                .orderDate(LocalDate.now())
                .section(section.orElse(new Section()))
                .listBatchStock(Arrays.asList(batchStock, batchStockUm))
                .build();
        inboundOrderRepository.save(inboudOrder);
        assertFalse(inboundOrderRepository.findById(inboudOrder.getId()).isEmpty());
    }

    @Test
    void deleteByOrderNumberTest() {
        Section section = new Section()
                .id("1")
                .sectionCode("LA")
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
                .agent(new Agent().
                        id("1").
                        name("lucas").
                        build())
                .section(section)
                .build();

        BatchStock batchStockUm = new BatchStock()
                .id("2")
                .batchNumber(2)
                .productId("LE")
                .currentTemperature(20.0F)
                .minimumTemperature(15.0F)
                .initialQuantity(1)
                .currentQuantity(5)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalDateTime.now())
                .dueDate(LocalDate.now())
                .agent(new Agent().
                        id("2").
                        name("ed").
                        build())
                .section(section)
                .build();

        InboudOrder inboudOrder = new InboudOrder()
                .orderNumber(2)
                .orderDate(LocalDate.now())
                .section(section)
                .listBatchStock(Arrays.asList(batchStock, batchStockUm))
                .build();

        inboundOrderRepository.save(inboudOrder);
        inboundOrderRepository.deleteByOrderNumber(inboudOrder.getOrderNumber());

        assertTrue(inboundOrderRepository.findById(inboudOrder.getId()).isEmpty());
    }

    void clearBase() {
        sectionRepository.deleteAll();
        inboundOrderRepository.deleteAll();
        agentRepository.deleteAll();
        batchStockRepository.deleteAll();
    }

}