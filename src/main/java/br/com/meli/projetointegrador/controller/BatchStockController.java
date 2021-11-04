package br.com.meli.projetointegrador.controller;

import br.com.meli.projetointegrador.model.dto.BatchStockResponseDTO;
import br.com.meli.projetointegrador.model.service.BatchStockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Jhony Zuim / Lucas Pereira / Edemilson Nobre / Rafael Vicente
 * @version 1.0.0
 * @since 15/10/2021
 * Camada de controller responsavel pela regra de negocio relacionada ao productController
 */

@RestController
@RequestMapping("/api/v1/fresh-products")
public class BatchStockController {

    private final BatchStockService batchStockService;

    public BatchStockController(BatchStockService batchStockService) {
        this.batchStockService = batchStockService;
    }

    @GetMapping(value = "/lists") // Chamada do endpoint: /lists?productId=LE ou DA
    public ResponseEntity<BatchStockResponseDTO> getProductById(@RequestParam("productId") String productId){
        BatchStockResponseDTO batchStockResponseDTO = batchStockService.listProductId(productId);
        return ResponseEntity.ok(batchStockResponseDTO);
    }
}