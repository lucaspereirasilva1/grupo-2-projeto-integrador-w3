package br.com.meli.projetointegrador.model.repository;

import br.com.meli.projetointegrador.model.entity.Warehouse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Jhony Zuim / Lucas Pereira / Edmilson Nobre / Rafael Vicente
 * @version 1.0.0
 * @since 15/10/2021
 * Repository de teste para trabalhar como uma porta ou janela de acesso a camada do banco da entity warehouse
 */

@DataMongoTest
class WarahouseRepositoryTest {

    @Autowired
    private WarehouseRepository warehouseRepository;

    @BeforeEach
    void setUp() {
        warehouseRepository.deleteAll();
        Warehouse warehouse = new Warehouse()
                .warehouseCode("SP")
                .warehouseName("sao paulo")
                .build();
        warehouseRepository.save(warehouse);
    }

    @AfterEach
    void cleanUpDatabase() {
        warehouseRepository.deleteAll();
    }

    @Test
    void findByWarehouseCodeTest() {
        final Optional<Warehouse> warehouse = warehouseRepository.findByWarehouseCode("SP");
        assertEquals("SP", warehouse.orElse(new Warehouse()).getWarehouseCode());
    }

    @Test
    void existsSectionBySectionCode() {
        assertTrue(warehouseRepository.existsByWarehouseCode("SP"));
    }

}
