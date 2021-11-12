package br.com.meli.projetointegrador.controller;

import br.com.meli.projetointegrador.model.dto.ProductDTO;
import br.com.meli.projetointegrador.model.dto.PurchaseOrderDTO;
import br.com.meli.projetointegrador.model.service.PurchaseOrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

/**
 * @author Jhony Zuim / Lucas Pereira / Edemilson Nobre / Rafael Vicente
 * @version 1.0.0
 * @since 15/10/2021
 * Camada de controller responsavel pela regra de negocio relacionada ao purchaseOrderController
 */

@Controller
@RestController
@RequestMapping("/api/v1/fresh-products")
public class PurchaseOrderController {

    private final PurchaseOrderService purchaseOrderService;

    public PurchaseOrderController(PurchaseOrderService purchaseOrderService) {
        this.purchaseOrderService = purchaseOrderService;
    }

    @GetMapping(value = "/order")
    public ResponseEntity<List<ProductDTO>> getProductByPurchaseOrder(@RequestParam("id") String id){
        List<ProductDTO> listProductDTO = purchaseOrderService.showOrderProduct(id);
        return ResponseEntity.ok(listProductDTO);
    }

    @PostMapping(value = "/order", produces = "application/json")
    public ResponseEntity<BigDecimal> post(@Valid @RequestBody PurchaseOrderDTO purchaseOrderDTO,
                                                          UriComponentsBuilder uriComponentsBuilder) {
        BigDecimal total = purchaseOrderService.save(purchaseOrderDTO);
        URI uri = uriComponentsBuilder.path("/order/1").buildAndExpand(1).toUri();
        return ResponseEntity.created(uri).body(total);
    }

    @PutMapping(value = "/order", produces = "application/json")
    public ResponseEntity<BigDecimal> put(@Valid @RequestBody PurchaseOrderDTO purchaseOrderDTO,
                                                          UriComponentsBuilder uriComponentsBuilder) {
        return post(purchaseOrderDTO, uriComponentsBuilder);
    }

}
