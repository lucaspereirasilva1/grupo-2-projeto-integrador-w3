package br.com.meli.projetointegrador.model.dto;

import lombok.Data;

@Data
public class AgentDTO {

    private String name;
    private String cpf;
    private String warehouseCode;

    public AgentDTO name(String name) {
        this.name = name;
        return this;
    }

    public AgentDTO cpf(String cpf) {
        this.cpf = cpf;
        return this;
    }

    public AgentDTO warehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
        return this;
    }

    public AgentDTO build() {
        return this;
    }

}
