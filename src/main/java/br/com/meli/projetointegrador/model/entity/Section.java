package br.com.meli.projetointegrador.model.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@Document(collection = "section")
public class Section {

    @MongoId(FieldType.OBJECT_ID)
    @Setter(AccessLevel.NONE)
    private String id;

    private String sectionCode;
    private String sectionName;
    private Integer maxLength;
    private Warehouse warehouse;

    public Section id(String id) {
        this.id = id;
        return this;
    }

    public Section sectionCode(String sectionCode) {
        this.sectionCode = sectionCode;
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
