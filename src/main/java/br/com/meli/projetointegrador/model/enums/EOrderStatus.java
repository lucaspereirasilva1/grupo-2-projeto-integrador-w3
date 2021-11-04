package br.com.meli.projetointegrador.model.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Jhony Zuim / Lucas Pereira / Edmilson Nobre / Rafael Vicente
 * @version 1.0.0
 * @since 15/10/2021
 * Enum para trabalhar como dois parametros na orderStatus
 */

public enum EOrderStatus {

    @JsonProperty("order chart")
    ORDER_CHART,

    @JsonProperty("in progress")
    IN_PROGRESS

}
