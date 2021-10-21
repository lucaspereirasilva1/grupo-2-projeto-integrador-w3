package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.model.dto.AgentDTO;
import br.com.meli.projetointegrador.model.dto.BatchStockDTO;
import br.com.meli.projetointegrador.model.dto.InboundOrderDTO;
import br.com.meli.projetointegrador.model.dto.SectionDTO;
import br.com.meli.projetointegrador.model.entity.Agent;
import br.com.meli.projetointegrador.model.entity.InboudOrder;
import br.com.meli.projetointegrador.model.entity.Section;
import br.com.meli.projetointegrador.model.repository.AgentRepository;
import br.com.meli.projetointegrador.model.repository.BatchStockRepository;
import br.com.meli.projetointegrador.model.repository.InboundOrderRepository;
import br.com.meli.projetointegrador.model.repository.SectionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

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
    void putIntegrationTest() {
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

        inboundOrderService.put(inboundOrderDTO, agentDTO);
        Optional<InboudOrder> inboudOrder = inboundOrderRepository.findByOrderNumber(inboundOrderDTO.getOrderNumber());
        Assertions.assertTrue(inboudOrder.isPresent());
    }

    void clearBase() {
        sectionRepository.deleteAll();
        inboundOrderRepository.deleteAll();
        agentRepository.deleteAll();
        batchStockRepository.deleteAll();
    }

}
