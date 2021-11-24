package br.com.meli.projetointegrador.model.dto;

import br.com.meli.projetointegrador.model.entity.Promo;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PromoResponseDTO {

    private String productId;
    private LocalDate productDueDate;
    private BigDecimal originalValue;
    private Double percentDiscount;
    private BigDecimal finalValue;

    public PromoResponseDTO productId(String productId) {
        this.productId = productId;
        return this;
    }

    public PromoResponseDTO productDueDate(LocalDate productDueDate) {
        this.productDueDate = productDueDate;
        return this;
    }

    public PromoResponseDTO originalValue(BigDecimal originalValue) {
        this.originalValue = originalValue;
        return this;
    }

    public PromoResponseDTO percentDiscount(Double percentDiscount) {
        this.percentDiscount = percentDiscount;
        return this;
    }

    public PromoResponseDTO finalValue(BigDecimal finalValue) {
        this.finalValue = finalValue;
        return this;
    }

    public PromoResponseDTO build() {
        return this;
    }

}
