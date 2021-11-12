package br.com.meli.projetointegrador.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

/**
 * @author Jhony Zuim / Lucas Pereira / Edmilson Nobre / Rafael Vicente
 * @version 1.0.0
 * @since 15/10/2021
 * Objeto de Transferência de Dados do warehouse
 */
@JsonPropertyOrder({"warehouseCode", "totalQuantity"})

@Data
public class WarehouseQuantityDTO {

    @JsonProperty("warehouseCode")
    private String warehouseCode;

    @JsonProperty("totalQuantity")
    private Integer totalQuantity;

    public WarehouseQuantityDTO warehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
        return this;
    }

    public WarehouseQuantityDTO totalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
        return this;
    }

    public WarehouseQuantityDTO build() {
        return this;
    }

}