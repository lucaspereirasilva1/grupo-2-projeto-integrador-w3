package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.model.dto.*;
import br.com.meli.projetointegrador.model.entity.*;
import br.com.meli.projetointegrador.model.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Jhony Zuim / Lucas Pereira / Edmilson Nobre / Rafael Vicente
 * @version 1.0.0
 * @since 15/10/2021
 * Camada de teste integrado do service responsavel pela regra de negocio relacionada ao batchStock
 */

@SpringBootTest
public class BatchStocketServiceIntegrationTest {

    @Autowired
    private BatchStockRepository batchStockRepository;

    @Autowired
    private BatchStockService batchStockService;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        Warehouse warehouse = new Warehouse()
                .warehouseCode("SP")
                .warehouseName("sao paulo")
                .build();
        warehouseRepository.save(warehouse);

        Agent agent = new Agent()
                .name("lucas")
                .cpf("11122233344")
                .warehouse(warehouse)
                .build();
        agentRepository.save(agent);

        Section section = new Section()
                .sectionCode("LA")
                .sectionName("laticinios")
                .maxLength(10)
                .warehouse(warehouse)
                .build();
        sectionRepository.save(section);

        Product product = new Product()
                .productId("LE")
                .productName("leite")
                .section(section)
                .build();
        productRepository.save(product);

        Product productDois = new Product()
                .productId("QJ")
                .productName("queijo")
                .section(section)
                .build();
        productRepository.save(productDois);

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
        batchStockRepository.saveAll(Collections.singletonList(batchStock));
    }

    @AfterEach
    void cleanUpDatabase() {
        batchStockRepository.deleteAll();
        warehouseRepository.deleteAll();
        agentRepository.deleteAll();
        sectionRepository.deleteAll();
        productRepository.deleteAll();
    }

    @Test
    void postAllIntegrationTest() {
        SectionDTO sectionDTO = new SectionDTO()
                .sectionCode("LA")
                .warehouseCode("SP")
                .build();

        AgentDTO agentDTO = new AgentDTO()
                .cpf("11122233344")
                .name("lucas")
                .build();

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
                .build();

        batchStockService.postAll(Collections.singletonList(batchStock), agentDTO, sectionDTO);

        final Optional<BatchStock> batchStockOptional = batchStockRepository.findById(batchStock.getId());
        assertTrue(batchStockOptional.isPresent());
    }

    @Test
    void putAllIntegrationTest() {
        SectionDTO sectionDTO = new SectionDTO()
                .sectionCode("LA")
                .warehouseCode("SP")
                .build();

        AgentDTO agentDTO = new AgentDTO()
                .cpf("11122233344")
                .name("lucas")
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
                .agent(agentRepository.findByCpf("11122233344").orElse(new Agent()))
                .section(sectionRepository.findBySectionCode("LA").orElse(new Section()))
                .build();
        batchStockRepository.save(batchStock);

        batchStockService.putAll(Collections.singletonList(batchStock), Collections.singletonList(batchStockDTO),
                agentDTO , sectionDTO);

        final Optional<BatchStock> batchStockOptional = batchStockRepository.findById(batchStock.getId());
        assertEquals(2, batchStockOptional.orElse(new BatchStock()).getCurrentQuantity());
        assertEquals(2, batchStockOptional.orElse(new BatchStock()).getInitialQuantity());
    }

    @Test
    void updateBatchStockIntegration() {
        List<ProductPurchaseOrderDTO> listProductPurchaseOrderDTO = new ArrayList<>();
        ProductPurchaseOrderDTO productPurchaseOrderDTO2 = new ProductPurchaseOrderDTO()
                .productId("QJ")
                .quantity(3)
                .build();
        listProductPurchaseOrderDTO.add(productPurchaseOrderDTO2);

        batchStockService.updateBatchStock(listProductPurchaseOrderDTO);

        batchStockRepository.findAllByProductId(productPurchaseOrderDTO2.getProductId()).forEach(b ->
                assertEquals(2, b.getCurrentQuantity()));
    }

    @Test
    void listProductId() {
        final BatchStockResponseDTO productResponseDTO = batchStockService.listProductId("QJ");
        assertFalse(ObjectUtils.isEmpty(productResponseDTO));
        assertEquals("QJ", productResponseDTO.getProductId());
    }

}
