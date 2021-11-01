package br.com.meli.projetointegrador.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.*;

@Data
public class ProductPurchaseOrderDTO {

    @JsonProperty("productId")
    @NotNull(message = "productId cannot be null")
    @NotEmpty(message = "productId cannot be empty")
    @NotBlank(message = "productId cannot be blank")
    @Size(min = 1, message = "productId most be minimum size 1")
    private String productId;

    @JsonProperty("quantity")
    @NotNull(message = "quantity cannot be null")
    @Min(value = 1, message = "quantity minimum value is 1")
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
