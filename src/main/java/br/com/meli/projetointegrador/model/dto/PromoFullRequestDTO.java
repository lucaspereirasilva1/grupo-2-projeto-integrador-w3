package br.com.meli.projetointegrador.model.dto;

import lombok.Data;

@Data
public class PromoFullRequestDTO {

    private String productId;
    private String cpf;
    private Double percent;

    public PromoFullRequestDTO productId(String productId) {
        this.productId = productId;
        return this;
    }

    public PromoFullRequestDTO cpf(String cpf) {
        this.cpf = cpf;
        return this;
    }

    public PromoFullRequestDTO percent(Double percent) {
        this.percent = percent;
        return this;
    }

    public PromoFullRequestDTO build() {
        return this;
    }

}
