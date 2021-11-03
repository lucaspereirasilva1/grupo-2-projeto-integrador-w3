package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.exception.WarehouseException;
import br.com.meli.projetointegrador.model.entity.Warehouse;
import br.com.meli.projetointegrador.model.repository.WarehouseRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @BeforeEach
    void setUp() {
        Warehouse warehouse = new Warehouse()
                .warehouseCode("RS")
                .warehouseName("POA")
                .build();
        warehouseRepository.save(warehouse);
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


}
