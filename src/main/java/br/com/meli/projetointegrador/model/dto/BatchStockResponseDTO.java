package br.com.meli.projetointegrador.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.util.List;

/**
 * @author Jhony Zuim / Lucas Pereira / Edmilson Nobre / Rafael Vicente
 * @version 1.0.0
 * @since 15/10/2021
 * Objeto de Transferência de Dados do product
 */

@Data
public class BatchStockResponseDTO {

    private SectionDTO sectionDTO;

    private String productId;

    private List<BatchStockListProductDTO> batchStock;

    public BatchStockResponseDTO sectionDTO(SectionDTO sectionDTO) {
        this.sectionDTO = sectionDTO;
        return this;
    }

    public BatchStockResponseDTO productId(String productId) {
        this.productId = productId;
        return this;
    }

    public BatchStockResponseDTO batchStock(List<BatchStockListProductDTO> batchStock) {
        this.batchStock = batchStock;
        return this;
    }

    public BatchStockResponseDTO build(){
        return this;
    }

}