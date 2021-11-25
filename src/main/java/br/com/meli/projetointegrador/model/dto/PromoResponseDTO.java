package br.com.meli.projetointegrador.model.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author Lucas Pereira
 * @version 1.5.0
 * @since 25/11/2021
 * DTO de response referente a feature atualizar promocoes
 */

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
