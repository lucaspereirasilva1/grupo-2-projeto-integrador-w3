package br.com.meli.projetointegrador.model.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

/**
 * @author Jhony Zuim / Lucas Pereira / Edmilson Nobre / Rafael Vicente
 * @version 1.0.0
 * @since 15/10/2021
 * Objeto criado para o Buyer e seus atributos
 */

@Data
@Document(collection = "buyer")
@CompoundIndexes({
        @CompoundIndex(name = "buyer_cpf", def = "{'cpf' : 1}", unique = true)
})
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
