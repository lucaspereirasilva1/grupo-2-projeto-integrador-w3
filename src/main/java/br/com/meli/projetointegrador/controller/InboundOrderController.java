package br.com.meli.projetointegrador.controller;

import br.com.meli.projetointegrador.model.dto.AgentDTO;
import br.com.meli.projetointegrador.model.dto.BatchStockDTO;
import br.com.meli.projetointegrador.model.dto.InboundOrderDTO;
import br.com.meli.projetointegrador.model.service.AgentService;
import br.com.meli.projetointegrador.model.service.InboundOrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

/**
 * @author Jhony Zuim / Lucas Pereira / Edemilson Nobre / Rafael Vicente
 * @version 1.0.0
 * @since 15/10/2021
 * Camada de controller responsavel pela regra de negocio relacionada ao inboundOrderController
 */

@RestController
@RequestMapping("/api/v1/fresh-products")
public class InboundOrderController {

    private final InboundOrderService inboundOrderService;
    private final AgentService agentService;

    public InboundOrderController(InboundOrderService inboundOrderService, AgentService agentService) {
        this.inboundOrderService = inboundOrderService;
        this.agentService = agentService;
    }

    /**
     * @param inboundOrderDTO, ordem de entrada DTO;
     * @param cpf, um CPF de agente
     * @return ResponseEntity.created com listBatchStockDTO
     * requisito 1 - endpoint 1: Cadastro de lote com produtos que compoe e devolva status 201.
     */
    @PostMapping(value = "/inboundorder", produces = "application/json")
    public ResponseEntity<List<BatchStockDTO>> post(@Valid @RequestBody InboundOrderDTO inboundOrderDTO,
                                                   UriComponentsBuilder uriComponentsBuilder,
                                                    @RequestParam String cpf) {
        final AgentDTO agentDTO = agentService.findByCpf(cpf);
        inboundOrderService.inputValid(inboundOrderDTO, agentDTO);
        List<BatchStockDTO> listBatchStockDTO = inboundOrderService.post(inboundOrderDTO, agentDTO);
        URI uri = uriComponentsBuilder.path("/inboundorder/1").buildAndExpand(1).toUri();
        return ResponseEntity.created(uri).body(listBatchStockDTO);
    }

    /**
     * @param inboundOrderDTO, ordem de entrada DTO;
     * @param cpf, um CPF de agente
     * @return ResponseEntity.created com listBatchStockDTO
     * requisito 1 - endpoint 2: Caso exista  lote ele deve ser atualizado Cadastro de lote com produtos que compoe e devolva status 201.
     */
    @PutMapping(value = "/inboundorder", produces = "application/json")
    public ResponseEntity<List<BatchStockDTO>> put(@Valid @RequestBody InboundOrderDTO inboundOrderDTO,
                                                   UriComponentsBuilder uriComponentsBuilder,
                                                   @RequestParam String cpf) {
        final AgentDTO agentDTO = agentService.findByCpf(cpf);
        inboundOrderService.inputValid(inboundOrderDTO, agentDTO);
        List<BatchStockDTO> listBatchStockDTO = inboundOrderService.put(inboundOrderDTO, agentDTO);
        URI uri = uriComponentsBuilder.path("/inboundorder/1").buildAndExpand(1).toUri();
        return ResponseEntity.created(uri).body(listBatchStockDTO);
    }

}
