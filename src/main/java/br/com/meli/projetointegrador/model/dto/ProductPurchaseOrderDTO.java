package br.com.meli.projetointegrador.model.dto;

import lombok.Data;

@Data
public class ProductPurchaseOrderDTO {

    private String productId;
    private Integer quantity;

    public ProductPurchaseOrderDTO productId(String productId) {
        this.productId = productId;
        return this;
    }
    public ProductPurchaseOrderDTO quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }
    public ProductPurchaseOrderDTO build(){
        return this;
    }
}
