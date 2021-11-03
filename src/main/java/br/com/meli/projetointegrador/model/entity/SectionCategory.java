package br.com.meli.projetointegrador.model.entity;

import br.com.meli.projetointegrador.model.enums.ESectionCategory;
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
 * Objeto criado para o SectionCategory e seus atributos
 */

@Data
@Document(collection = "sectioncategory")
public class SectionCategory {

    @MongoId(FieldType.OBJECT_ID)
    @Setter(AccessLevel.NONE)
    private String id;

    private ESectionCategory name;

    public SectionCategory name(ESectionCategory name) {
        this.name = name;
        return this;
    }

    public SectionCategory build() {
        return this;
    }

}
