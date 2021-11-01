package br.com.meli.projetointegrador.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Data
public class PurchaseOrderDTO {

    @JsonProperty("id")
    private String id;

    @JsonProperty("data")
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern="dd/MM/yyyy")
    @NotNull(message = "data cannot be null")
    private LocalDate data;

    @JsonProperty("buyerId")
    @NotNull(message = "buyerId cannot be null")
    @NotEmpty(message = "buyerId cannot be empty")
    @NotBlank(message = "buyerId cannot be blank")
    @Size(min = 1, message = "productId most be minimum size 1")
    private String buyerId;

    @JsonProperty("orderStatus")
    @NotNull(message = "orderStatus cannot be null")
    @Valid
    private OrderStatusDTO orderStatus;

    @JsonProperty("products")
    @NotNull(message = "products cannot be null")
    @Size(min = 1, message = "products most be minimum size 1")
    @Valid
    private List<ProductPurchaseOrderDTO> listProductPurchaseOrderDTO;

    public PurchaseOrderDTO id(String id) {
        this.id = id;
        return this;
    }

    public PurchaseOrderDTO data(LocalDate data) {
        this.data = data;
        return this;
    }

    public PurchaseOrderDTO buyerId(String buyerId) {
        this.buyerId = buyerId;
        return this;
    }

    public PurchaseOrderDTO orderStatus(OrderStatusDTO orderStatus) {
        this.orderStatus = orderStatus;
        return this;
    }

    public PurchaseOrderDTO listProductPurchaseOrderDTO(List<ProductPurchaseOrderDTO> listProductPurchaseOrderDTO) {
        this.listProductPurchaseOrderDTO = listProductPurchaseOrderDTO;
        return this;
    }

    public PurchaseOrderDTO build(){
        return this;
    }
}
