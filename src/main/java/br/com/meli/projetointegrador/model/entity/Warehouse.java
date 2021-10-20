package br.com.meli.projetointegrador.model.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

@Data
@Document(collection = "warehouse")
public class Warehouse {

    @MongoId(FieldType.OBJECT_ID)
    @Setter(AccessLevel.NONE)
    private String id;

    private String warehouseCode;
    private String warehouseName;
    private List<Section> listSections;


    public Warehouse() {
        this.warehouseCode = warehouseCode;
        this.warehouseName = warehouseName;
    }

    // Metodo Construtor

    public Warehouse warehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
        return this ;
    }

    public Warehouse warehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
        return this;
    }

    public Warehouse build() {
        return this;
    }


}
