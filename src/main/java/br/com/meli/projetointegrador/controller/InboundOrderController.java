package br.com.meli.projetointegrador.controller;

import br.com.meli.projetointegrador.model.dto.AgentDTO;
import br.com.meli.projetointegrador.model.dto.BatchStockDTO;
import br.com.meli.projetointegrador.model.dto.InboundOrderDTO;
import br.com.meli.projetointegrador.model.service.InboundOrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/fresh-products")
public class InboundOrderController {

    private final InboundOrderService inboundOrderService;

    public InboundOrderController(InboundOrderService inboundOrderService) {
        this.inboundOrderService = inboundOrderService;
    }

    @PostMapping(value = "/inboundorder", produces = "application/json")
    public ResponseEntity<List<BatchStockDTO>> post(@Valid @RequestBody InboundOrderDTO inboundOrderDTO,
                                                   UriComponentsBuilder uriComponentsBuilder) {
        AgentDTO agentDTO = new AgentDTO()
                .name("lucas")
                .cpf("11122233344")
                .warehouseCode("SP")
                .build();
        inboundOrderService.inputValid(inboundOrderDTO, agentDTO);
        List<BatchStockDTO> listBatchStockDTO = inboundOrderService.post(inboundOrderDTO, agentDTO);
        URI uri = uriComponentsBuilder.path("/inboundorder/1").buildAndExpand(1).toUri();
        return ResponseEntity.created(uri).body(listBatchStockDTO);
    }

    @PutMapping(value = "/inboundorder", produces = "application/json")
    public ResponseEntity<List<BatchStockDTO>> put(@RequestBody InboundOrderDTO inboundOrderDTO,
                                                   UriComponentsBuilder uriComponentsBuilder) {
        AgentDTO agentDTO = new AgentDTO()
                .name("lucas")
                .cpf("11122233344")
                .warehouseCode("SP")
                .build();
        inboundOrderService.inputValid(inboundOrderDTO, agentDTO);
        List<BatchStockDTO> listBatchStockDTO = inboundOrderService.put(inboundOrderDTO, agentDTO);
        URI uri = uriComponentsBuilder.path("/inboundorder/1").buildAndExpand(1).toUri();
        return ResponseEntity.created(uri).body(listBatchStockDTO);
    }

}
