package br.com.meli.projetointegrador.controller;

import br.com.meli.projetointegrador.advisor.ResponseHandler;
import br.com.meli.projetointegrador.model.dto.BatchStockResponseWarehousesDTO;
import br.com.meli.projetointegrador.model.service.WarehouseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Jhony Zuim / Lucas Pereira / Edemilson Nobre / Rafael Vicente
 * @version 1.0.0
 * @since 15/10/2021
 * Camada de controller responsavel pela regra de negocio relacionada ao warehouseController
 */

@RestController
@RequestMapping("/api/v1/fresh-products")
public class WarehouseController {

    private final WarehouseService warehouseService;

    public WarehouseController(WarehouseService warehouseService){
        this.warehouseService = warehouseService;
    }

    /**
     * @param productId, id do produdo;
     * @return ResponseEntity do tipo batchStockResponseWarehousesDTO;
     * requisito 4 - endpoint 1: Veja quantidade de um determinado produto por armazem.
     */
    @GetMapping(value = "/warehouse") // Chamada do endpoint: /warehouse?productId=LE
    public ResponseEntity<Object> getQuantityProductsWarehouse(@RequestParam("productId") String productId) {
        BatchStockResponseWarehousesDTO batchStockResponseWarehousesDTO = warehouseService.listQuantityProduct(productId);
        if (!batchStockResponseWarehousesDTO.getProductId().isEmpty()) {
            return ResponseEntity.ok(batchStockResponseWarehousesDTO);
        } else {
            return ResponseHandler.generateResponse("Nao foi encontrado nenhum produto na base", HttpStatus.NOT_FOUND, "");
        }
    }
}
