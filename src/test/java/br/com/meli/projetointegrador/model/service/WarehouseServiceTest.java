package br.com.meli.projetointegrador.model.service;



import br.com.meli.projetointegrador.exception.WarehouseException;
import br.com.meli.projetointegrador.model.entity.Warehouse;
import br.com.meli.projetointegrador.model.repository.WarehouseRepository;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class WarehouseServiceTest {

    private WarehouseRepository mockWarehouseRepository = mock(WarehouseRepository.class);
    private WarehouseService warehouseService = new WarehouseService(mockWarehouseRepository);

    /**
     * Teste unitarios
     * validWarehouseExist - metodo para validar se Warehouse é valido
     *  Verificar se Warehouse é invalido.
     */

    @Test
    void validWarehouseExist(){

        Warehouse warehouse = new Warehouse()
                .warehouseCode("MG")
                .warehouseName("Minas")
                .build();

        when(mockWarehouseRepository.findById(any()))
                .thenReturn(Optional.of(new Warehouse()
                        .warehouseCode("MG")
                        .warehouseName("Minas")
                        .build()));

        assertTrue(warehouseService.validWarehouse(warehouse));

    }

    @Test
    void notValidWarehouseExist() {

        Warehouse warehouse = new Warehouse()
                .warehouseCode("MG")
                .warehouseName("Minas")
                .build();

        when((mockWarehouseRepository).findByWarehouse(any()))
                .thenReturn(Optional.empty());

        WarehouseException warehouseException = assertThrows(WarehouseException.class, () ->
                warehouseService.validWarehouse(warehouse));

        String expectedMessage = "Representante nao foi vinculado ao estoque, por gentileza reenviar a request!!!";
        String receivedMessage = warehouseException.getMessage();

        assertFalse(expectedMessage.contains(receivedMessage));
    }

}
