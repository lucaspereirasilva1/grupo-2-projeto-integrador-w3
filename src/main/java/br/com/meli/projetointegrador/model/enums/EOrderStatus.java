package br.com.meli.projetointegrador.model.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum EOrderStatus {

    @JsonProperty("order chart")
    ORDER_CHART,

    @JsonProperty("in progress")
    IN_PROGRESS

}
