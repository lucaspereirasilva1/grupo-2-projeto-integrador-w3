package br.com.meli.projetointegrador.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.util.List;

/**
 * @author Jhony Zuim / Lucas Pereira / Edmilson Nobre / Rafael Vicente
 * @version 1.0.0
 * @since 15/10/2021
 * Objeto de Transferência de Dados do BatchStockResponseWarehousesDTO
 */
@JsonPropertyOrder({"productId", "warehouses"})

@Data
public class BatchStockResponseWarehousesDTO {

    @JsonProperty("productId")
    private String productId;
    @JsonProperty("warehouses")
    private List<WarehouseQuantityDTO> warehouses;

    public BatchStockResponseWarehousesDTO productId(String productId) {
        this.productId = productId;
        return this;
    }

    public BatchStockResponseWarehousesDTO warehouses(List<WarehouseQuantityDTO> warehouses) {
        this.warehouses = warehouses;
        return this;
    }

    public BatchStockResponseWarehousesDTO build(){
        return this;
    }
}
