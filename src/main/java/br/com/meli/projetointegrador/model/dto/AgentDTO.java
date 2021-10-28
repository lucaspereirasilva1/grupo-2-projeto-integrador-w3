package br.com.meli.projetointegrador.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Jhony Zuim / Lucas Pereira / Edmilson Nobre / Rafael Vicente
 * @version 1.0.0
 * @since 15/10/2021
 * Objeto de Transferência de Dados do agent
 */

@Data
public class AgentDTO {

    @NotNull(message = "name cannot be null")
    @NotEmpty(message = "name cannot be empty")
    @NotBlank(message = "name cannot be blank")
    @Size(min = 1, message = "name most be minimum size 1")
    private String name;

    @NotNull(message = "cpf cannot be null")
    @NotEmpty(message = "cpf cannot be empty")
    @NotBlank(message = "cpf cannot be blank")
    @Size(min = 1, message = "cpf most be minimum size 1")
    private String cpf;

    @NotNull(message = "warehouseCode cannot be null")
    @NotEmpty(message = "warehouseCode cannot be empty")
    @NotBlank(message = "warehouseCode cannot be blank")
    @Size(min = 1, message = "warehouseCode most be minimum size 1")
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
