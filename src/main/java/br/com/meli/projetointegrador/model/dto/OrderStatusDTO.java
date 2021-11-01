package br.com.meli.projetointegrador.model.dto;

import br.com.meli.projetointegrador.model.enums.EOrderStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class OrderStatusDTO {

    @JsonProperty("statusCode")
    @NotNull(message = "statusCode cannot be null")
    private EOrderStatus statusCode;

    public OrderStatusDTO statusCode(EOrderStatus statusCode) {
        this.statusCode = statusCode;
        return this;
    }

}
