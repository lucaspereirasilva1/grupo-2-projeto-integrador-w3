package br.com.meli.projetointegrador.model.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

@Data
@Document(collection = "agent")
public class Agent {

    @MongoId(FieldType.OBJECT_ID)
    @Setter(AccessLevel.NONE)
    private String id;

    private String name;

    @DBRef(lazy = true)
    private List<BatchStock> listBatchStock;

    public Agent id(String id) {
        this.id = id;
        return this;
    }

    public Agent name(String name) {
        this.name = name;
        return this;
    }

    public Agent build() {
        return this;
    }

}
