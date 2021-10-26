package br.com.meli.projetointegrador.model.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

/**
 * @author Jhony Zuim / Lucas Pereira / Edmilson Nobre / Rafael Vicente
 * @version 1.0.0
 * @since 15/10/2021
 * Objeto criado para o agente/representante e seus atributos
 */

@Data
@Document(collection = "agent")
public class Agent {

    @MongoId(FieldType.OBJECT_ID)
    @Setter(AccessLevel.NONE)
    private String id;

    private String name;
    private String cpf;
    private Warehouse warehouse;

    public Agent id(String id) {
        this.id = id;
        return this;
    }

    public Agent name(String name) {
        this.name = name;
        return this;
    }

    public Agent cpf(String cpf) {
        this.cpf = cpf;
        return this;
    }

    public Agent warehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
        return this;
    }

    public Agent build() {
        return this;
    }

}
