package br.com.meli.projetointegrador.model.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@Document(collection = "buyer")
public class Buyer {

    @MongoId(FieldType.OBJECT_ID)
    @Setter(AccessLevel.NONE)
    private String id;

    private String name;
    private String cpf;

    public Buyer name(String name) {
        this.name = name;
        return this;
    }

    public Buyer cpf(String cpf) {
        this.cpf = cpf;
        return this;
    }

    public Buyer build() {
        return this;
    }

}
