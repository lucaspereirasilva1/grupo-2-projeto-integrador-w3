package br.com.meli.projetointegrador.controller;

import br.com.meli.projetointegrador.model.dto.PromoFullRequestDTO;
import br.com.meli.projetointegrador.model.dto.PromoRequestDTO;
import br.com.meli.projetointegrador.model.dto.PromoResponseDTO;
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

    @PutMapping(value = "/updatepromo", produces = "application/json")
    public ResponseEntity<PromoResponseDTO> put(@Valid @RequestBody PromoRequestDTO promoRequestDTO,
                                                    UriComponentsBuilder uriComponentsBuilder) {
        final PromoResponseDTO promoResponseDTO = promoService.updatePromo(promoRequestDTO);
        URI uri = uriComponentsBuilder.path("/updatepromo/1").buildAndExpand(1).toUri();
        return ResponseEntity.created(uri).body(promoResponseDTO);
    }

    @GetMapping(value = "/listbydiscount") // Chamada do endpoint: /lists?productId=LE ou DA
    public ResponseEntity<List<PromoResponseDTO>> listByDiscount() {
        List<PromoResponseDTO> promoResponseDTOList = promoService.listByDiscount();
        return ResponseEntity.ok(promoResponseDTOList);
    }

    @PutMapping(value = "/promofull", produces = "application/json")
    public ResponseEntity<BigDecimal> postPromoFull(@Valid @RequestBody PromoFullRequestDTO promoFullRequestDTO,
                                           UriComponentsBuilder uriComponentsBuilder) {
        final BigDecimal bigDecimal = promoService.apllyPromoFull(promoFullRequestDTO);
        URI uri = uriComponentsBuilder.path("/promofull/1").buildAndExpand(1).toUri();
        return ResponseEntity.created(uri).body(bigDecimal);
    }

}
