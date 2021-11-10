package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.exception.WarehouseException;
import br.com.meli.projetointegrador.model.entity.Warehouse;
import br.com.meli.projetointegrador.model.repository.WarehouseRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Jhony Zuim / Lucas Pereira / Edmilson Nobre / Rafael Vicente
 * @version 1.0.0
 * @since 15/10/2021
 * Camada de testes unitarios do service responsavel pela regra de negocio relacionada ao warehouse
 */

public class WarehouseServiceTest {

    private final WarehouseRepository mockWarehouseRepository = mock(WarehouseRepository.class);
    private final BatchStockService mockbatchStockService = mock(BatchStockService.class);
    private final WarehouseService warehouseService = new WarehouseService(mockWarehouseRepository, mockbatchStockService);

    @Test
    void validWarehouseExist(){
        when(mockWarehouseRepository.existsByWarehouseCode(anyString()))
                .thenReturn(true);

        assertTrue(warehouseService.validWarehouse("MG"));
    }

    @Test
    void notValidWarehouseExist() {
        when(mockWarehouseRepository.existsByWarehouseCode(anyString()))
                .thenReturn(false);

        WarehouseException warehouseException = assertThrows(WarehouseException.class, () ->
                warehouseService.validWarehouse(""));

        String expectedMessage = "Armazem nao cadastrado!!! Por gentileza cadastrar!!!";
        String receivedMessage = warehouseException.getMessage();

        assertTrue(expectedMessage.contains(receivedMessage));
    }

    @Test
    void findExistTest() {
        Warehouse warehouse = new Warehouse()
                .warehouseCode("RS")
                .warehouseName("POA")
                .build();
        when(mockWarehouseRepository.findByWarehouseCode(anyString()))
                .thenReturn(Optional.of(warehouse));
        assertEquals("RS", warehouse.getWarehouseCode());
    }

    @Test
    void findNotExistTest() {
        when(mockWarehouseRepository.findByWarehouseCode(anyString()))
                .thenReturn(Optional.empty());

        WarehouseException warehouseException = assertThrows(WarehouseException.class, () ->
                warehouseService.find(""));

        String expectedMessage = "Armazem nao encontrado!!! Por gentileza reenviar com um armazem valido";
        String receivedMessage = warehouseException.getMessage();

        assertTrue(expectedMessage.contains(receivedMessage));
    }

}
