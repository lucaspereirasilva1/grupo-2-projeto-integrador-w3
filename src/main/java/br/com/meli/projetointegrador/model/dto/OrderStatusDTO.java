package br.com.meli.projetointegrador.model.dto;

import br.com.meli.projetointegrador.model.enums.EOrderStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author Jhony Zuim / Lucas Pereira / Edmilson Nobre / Rafael Vicente
 * @version 1.0.0
 * @since 15/10/2021
 * Objeto de Transferência de Dados do OrderStatusDTO
 */

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
