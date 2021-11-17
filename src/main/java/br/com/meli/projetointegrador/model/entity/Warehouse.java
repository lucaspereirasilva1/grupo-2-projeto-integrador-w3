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
 * Objeto criado para o warehouse/armazem e seus atributos
 */

@Data
@Document(collection = "warehouse")
@CompoundIndexes({
        @CompoundIndex(name = "warehouse_code_name", def = "{'warehouseCode' : 1, 'warehouseName': 1}", unique = true)
})
public class Warehouse {

    @MongoId(FieldType.OBJECT_ID)
    @Setter(AccessLevel.NONE)
    private String id;

    private String warehouseCode;
    private String warehouseName;

    public Warehouse id(String id) {
        this.id = id;
        return this;
    }

    public Warehouse warehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
        return this;
    }

    public Warehouse warehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
        return this;
    }

    public Warehouse build() {
        return this;
    }

}
