package br.com.meli.projetointegrador.model.dto;

import lombok.Data;

@Data
public class PromoRequestDTO {

    private String productId;
    private Double percentDiscount;

    public PromoRequestDTO productId(String productId) {
        this.productId = productId;
        return this;
    }

    public PromoRequestDTO percentDiscount(Double percentDiscount) {
        this.percentDiscount = percentDiscount;
        return this;
    }

    public PromoRequestDTO build() {
        return this;
    }

}
