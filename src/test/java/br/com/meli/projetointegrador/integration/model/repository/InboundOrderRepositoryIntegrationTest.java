package br.com.meli.projetointegrador.integration.model.repository;

import br.com.meli.projetointegrador.model.entity.*;
import br.com.meli.projetointegrador.model.repository.InboundOrderRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
public class InboundOrderRepositoryIntegrationTest {

    @Autowired
    InboundOrderRepository inboundOrderRepository;

    @AfterEach
    void cleanUpDatabase() {
        inboundOrderRepository.deleteByOrderNumber(1);
    }

//    @Before
//    public void setUp() throws Exception {
//        inboundOrderRepository.save(new Foo());
//    }

    @Test
    void saveTest() {
        Section section = new Section()
                .id("1")
                .sectionCode("LA")
                .warehouse(new Warehouse().
                        id("1").
                        warehouseCode("SP").
                        warehouseName("sao paulo").
                        build())
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
                .build();

        InboudOrder inboudOrder = new InboudOrder()
                .orderNumber(1)
                .orderDate(LocalDate.now())
                .section(section)
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
                .warehouse(new Warehouse().
                        id("1").
                        warehouseCode("SP").
                        warehouseName("sao paulo").
                        build())
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

}