package br.com.meli.projetointegrador.controller;

import br.com.meli.projetointegrador.model.dto.BatchStockListDueDateDTO;
import br.com.meli.projetointegrador.model.dto.BatchStockResponseDTO;
import br.com.meli.projetointegrador.model.service.BatchStockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Jhony Zuim / Lucas Pereira / Edemilson Nobre / Rafael Vicente
 * @version 1.0.0
 * @since 15/10/2021
 * Camada de controller responsavel pela regra de negocio relacionada ao batchStockController
 */

@RestController
@RequestMapping("/api/v1/fresh-products")
public class BatchStockController {

    private final BatchStockService batchStockService;

    public BatchStockController(BatchStockService batchStockService) {
        this.batchStockService = batchStockService;
    }

    /**
     * @param productId, id do produdo;
     * @return ResponseEntity do tipo batchStockResponseDTO;
     * requisito 3-endpoint 1: Veja uma lista de produtos com todos os lotes onde aparece.
     */
    @GetMapping(value = "/lists") // Chamada do endpoint: /lists?productId=LE ou DA
    public ResponseEntity<BatchStockResponseDTO> getProductById(@RequestParam("productId") String productId) {
        BatchStockResponseDTO batchStockResponseDTO = batchStockService.listProductId(productId, "");
        return ResponseEntity.ok(batchStockResponseDTO);
    }

    /**
     * @param productId, id do produdo;
     * @param order, codigo da ordenacao;
     * @return ResponseEntity do tipo batchStockResponseDTO;
     * requisito 3-endpoint 2: Veja uma lista de produtos com todos os lotes onde aparece.
     * Ordenados por:L = ordenado por lote, C = ordenado por quantidade ou F = ordenado por data vencimiento
     */
    @GetMapping(value = "/listsorder") // Chamada do endpoint: /lists?productId=LE&L, C ou F
    public ResponseEntity<BatchStockResponseDTO> getProductOrder(@RequestParam("productId") String productId,
                                                             @RequestParam("order") String order) {
        BatchStockResponseDTO batchStockResponseDTO = batchStockService.listProductId(productId, order);
        return ResponseEntity.ok(batchStockResponseDTO);
    }

    /**
     * @param days, id do produdo;
     * @return ResponseEntity do tipo batchStockListDueDateDTO;
     * requisito 5 - endpoint 1: Veja uma lista de produtos com todos os lotes onde aparece
     * ordenado pelo dias pra vencer.
     */
    @GetMapping(value = "/due-date/list") // Chamada do endpoint: /due-date/list?days=5}
    public ResponseEntity<BatchStockListDueDateDTO> getProductDyas(@RequestParam("days") Integer days) {
        BatchStockListDueDateDTO batchStockListDueDateDTO = batchStockService.listDueDateDays(days);
        return ResponseEntity.ok(batchStockListDueDateDTO);
    }

    /**
     * @param days, id do produdo;
     * @param category, categoria que o produdo se encontra;
     * @param order, tipo de ordenacao, asc/desc;
     * @return ResponseEntity do tipo batchStockListDueDateDTO;
     * requisito 5 - endpoint 2: Veja uma lista de produtos filtrado por dias de vencimentos, a sua categoria
     * e ordenado por data vencimiento asc/desc
     */
    @GetMapping(value = "/due-date/lists") // Chamada do endpoint: /due-date/lists?days={}&category={FS/FF/RF}&order={asc/desc}
    public ResponseEntity<BatchStockListDueDateDTO> getProduct(@RequestParam("days") Integer days,
                                                               @RequestParam("category") String category,
                                                               @RequestParam("order") String order) {
        BatchStockListDueDateDTO batchStockListDueDateDTO = batchStockService.listBatchStockDueDate(days, category, order);
        return ResponseEntity.ok(batchStockListDueDateDTO);
    }
}