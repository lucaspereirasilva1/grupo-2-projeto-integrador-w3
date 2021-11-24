package br.com.meli.projetointegrador.model.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Document(collection = "promo")
@CompoundIndexes({
        @CompoundIndex(name = "promo-productid", def = "{'productId' : 1}", unique = true)
})
public class Promo {

    @MongoId(FieldType.OBJECT_ID)
    @Setter(AccessLevel.NONE)
    private String id;

    private String productId;
    private LocalDate productDueDate;
    private BigDecimal originalValue;
    private Double percentDiscount;
    private BigDecimal finalValue;

    public Promo id(String id) {
        this.id = id;
        return this;
    }

    public Promo productId(String productId) {
        this.productId = productId;
        return this;
    }

    public Promo productDueDate(LocalDate productDueDate) {
        this.productDueDate = productDueDate;
        return this;
    }

    public Promo originalValue(BigDecimal originalValue) {
        this.originalValue = originalValue;
        return this;
    }

    public Promo percentDiscount(Double percentDiscount) {
        this.percentDiscount = percentDiscount;
        return this;
    }

    public Promo finalValue(BigDecimal finalValue) {
        this.finalValue = finalValue;
        return this;
    }

    public Promo build() {
        return this;
    }
}
