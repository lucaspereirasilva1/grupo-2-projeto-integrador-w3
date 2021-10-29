package br.com.meli.projetointegrador.model.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

/**
 * @author Jhony Zuim / Lucas Pereira / Edmilson Nobre / Rafael Vicente
 * @version 1.0.0
 * @since 15/10/2021
 * Objeto criado para o produto e seus atributos
 */

@Data
@Document(collection = "product")
public class Product {

    @MongoId(FieldType.OBJECT_ID)
    @Setter(AccessLevel.NONE)
    private String id;

    private String productId;
    private String productName;

    @DBRef
    private Section section;

    private SectionCategory category;

    /**
     * @author Jhony Zuim
     * Construcao de construtores fluentes para a classe produto
     */

    public Product id(String id) {
        this.id = id;
        return this;
    }

    public Product productId(String productId) {
        this.productId = productId;
        return this;
    }

    public Product productName(String productName) {
        this.productName = productName;
        return this;
    }

    public Product section(Section section) {
        this.section = section;
        return this;
    }

    public Product category(SectionCategory category) {
        this.category = category;
        return this;
    }

    public Product build(){
        return this;
    }
}
