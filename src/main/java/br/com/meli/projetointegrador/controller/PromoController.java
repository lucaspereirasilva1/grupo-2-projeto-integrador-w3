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

/**
 * @author Lucas Pereira
 * @version 1.5.0
 * @since 25/11/2021
 * Controller responsavel por controlar as requisicoes referente a feature de promocoes
 */

@RestController
@RequestMapping("/api/v1/fresh-products")
public class PromoController {

    private final PromoService promoService;

    public PromoController(PromoService promoService) {
        this.promoService = promoService;
    }

    /**
     * @param productId, id do produto
     * @return ResponseEntity.created com um valor final do produto
     * requisito 6 - endpoint 1: Cadastrar uma promocao
     */
    @PostMapping(value = "/promocomingdue", produces = "application/json")
    public ResponseEntity<BigDecimal> post(@RequestParam String productId,
                                                    UriComponentsBuilder uriComponentsBuilder) {
        final BigDecimal bigDecimal = promoService.apllyPromo(productId);
        URI uri = uriComponentsBuilder.path("/promocomingdue/1").buildAndExpand(1).toUri();
        return ResponseEntity.created(uri).body(bigDecimal);
    }

    /**
     * @param promoRequestDTO, recebe uma request com id do produto e porcentagem de desconto
     * @return ResponseEntity.created as informacoes da promocao atualizada
     * requisito 6 - endpoint 2: Atualiza uma promocao
     */
    @PutMapping(value = "/updatepromo", produces = "application/json")
    public ResponseEntity<PromoResponseDTO> put(@Valid @RequestBody PromoRequestDTO promoRequestDTO,
                                                    UriComponentsBuilder uriComponentsBuilder) {
        final PromoResponseDTO promoResponseDTO = promoService.updatePromo(promoRequestDTO);
        URI uri = uriComponentsBuilder.path("/updatepromo/1").buildAndExpand(1).toUri();
        return ResponseEntity.created(uri).body(promoResponseDTO);
    }

    /**
     * @return ResponseEntity.ok lista com as promocoes cadastradas ordenadas pela porcetagem de desconto
     * requisito 6 - endpoint 3: Listar promocoes ordenadas por desconto
     */
    @GetMapping(value = "/listbydiscount")
    public ResponseEntity<List<PromoResponseDTO>> listByDiscount() {
        List<PromoResponseDTO> promoResponseDTOList = promoService.listByDiscount();
        return ResponseEntity.ok(promoResponseDTOList);
    }

    /**
     * @param promoFullRequestDTO, recebe uma request com id do produto, porcentagem de desconto e cpf
     * @return ResponseEntity.created e valor final do produto com o desconto aplicado
     * requisito 6 - endpoint 4: Cadastra/Atualiza uma promocao
     */
    @PutMapping(value = "/promofull", produces = "application/json")
    public ResponseEntity<BigDecimal> postPromoFull(@Valid @RequestBody PromoFullRequestDTO promoFullRequestDTO,
                                           UriComponentsBuilder uriComponentsBuilder) {
        final BigDecimal bigDecimal = promoService.apllyPromoFull(promoFullRequestDTO);
        URI uri = uriComponentsBuilder.path("/promofull/1").buildAndExpand(1).toUri();
        return ResponseEntity.created(uri).body(bigDecimal);
    }

}
