package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.exception.WarehouseException;
import br.com.meli.projetointegrador.model.entity.BatchStock;
import br.com.meli.projetointegrador.model.entity.Warehouse;
import br.com.meli.projetointegrador.model.repository.BatchStockRepository;
import br.com.meli.projetointegrador.model.repository.WarehouseRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Jhony Zuim / Lucas Pereira / Edmilson Nobre / Rafael Vicente
 * @version 1.0.0
 * @since 15/10/2021
 * Camada de teste integrado do service responsavel pela regra de negocio relacionada ao warehouse
 */

@SpringBootTest
public class WarehouseServiceIntegrationTest {

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private BatchStockRepository batchStockRepository;

    @BeforeEach
    void setUp() {
        Warehouse warehouse = new Warehouse()
                .warehouseCode("RS")
                .warehouseName("POA")
                .build();
        warehouseRepository.save(warehouse);

        BatchStock batchStock =new BatchStock()
                .batchNumber(1)
                .currentQuantity(10)
                .initialQuantity(1)
                .productId("LA")
                .currentTemperature(35.5F)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalDateTime.now())
                .dueDate(LocalDate.now())
                .build();
        batchStockRepository.save(batchStock);
    }

    @AfterEach
    void clearBase() {
        warehouseRepository.deleteAll();
    }

    @Test
    void validWarehouseExist() {
        assertTrue(warehouseService.validWarehouse("RS"));
    }

    @Test
    void validWarehouseNotExist() {
        WarehouseException warehouseException = assertThrows(WarehouseException.class, () ->
                warehouseService.validWarehouse(""));

        String expectedMessage = "Armazem nao cadastrado!!! Por gentileza cadastrar!!!";
        String receivedMessage = warehouseException.getMessage();

        assertTrue(expectedMessage.contains(receivedMessage));
    }

    @Test
    void findExistIntegrationTest() {
        final Optional<Warehouse> warehouse = warehouseRepository.findByWarehouseCode("RS");
        assertTrue(warehouse.isPresent());

    }

    @Test
    void findNotExistIntegrationTest() {
        WarehouseException warehouseException = assertThrows(WarehouseException.class, () ->
                warehouseService.find(""));

        String expectedMessage = "Armazem nao encontrado!!! Por gentileza reenviar com um armazem valido";
        String receivedMessage = warehouseException.getMessage();

        assertTrue(expectedMessage.contains(receivedMessage));
    }

    @Test
    void listQuantityProductExist(){
        Integer quantityWarehouse = warehouseRepository.findWarehouseBy("LA").size();
        Integer quantityBatchStock = batchStockRepository.findAllByProductId("LA").size();
        assertEquals(quantityWarehouse, quantityBatchStock);
    }

}
