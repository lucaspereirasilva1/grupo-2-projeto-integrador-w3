package br.com.meli.projetointegrador.controller;

import br.com.meli.projetointegrador.model.dto.AgentDTO;
import br.com.meli.projetointegrador.model.dto.BatchStockDTO;
import br.com.meli.projetointegrador.model.dto.InboundOrderDTO;
import br.com.meli.projetointegrador.model.service.PromoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/fresh-products")
public class PromoController {

    private final PromoService promoService;

    public PromoController(PromoService promoService) {
        this.promoService = promoService;
    }

    @PostMapping(value = "/promocomingdue", produces = "application/json")
    public ResponseEntity<BigDecimal> post(@RequestParam String productId,
                                                    UriComponentsBuilder uriComponentsBuilder) {
        final BigDecimal bigDecimal = promoService.apllyPromo(productId);
        URI uri = uriComponentsBuilder.path("/promocomingdue/1").buildAndExpand(1).toUri();
        return ResponseEntity.created(uri).body(bigDecimal);
    }

}
