package br.com.meli.projetointegrador.model.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.springframework.data.annotation.Reference;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

/**
 * @author Jhony Zuim / Lucas Pereira / Edemilson Nobre / Rafael Vicente
 * @version 1.0.0
 * @since 15/10/2021
 * Objeto criado para o section/setor e seus atributos
 */

@Data
@Document(collection = "section")
public class Section {

    @MongoId(FieldType.OBJECT_ID)
    @Setter(AccessLevel.NONE)
    private String id;

    @Indexed(unique = true)
    private String sectionCode;

    private String sectionName;
    private Integer maxLength;

    @DBRef
    @Reference
    private Warehouse warehouse;

    public Section id(String id) {
        this.id = id;
        return this;
    }

    public Section sectionCode(String sectionCode) {
        this.sectionCode = sectionCode;
        return this;
    }

    public Section sectionName(String sectionName) {
        this.sectionName = sectionName;
        return this;
    }

    public Section maxLength(Integer maxLength) {
        this.maxLength = maxLength;
        return this;
    }

    public Section warehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
        return this;
    }

    public Section build() {
        return this;
    }

}
