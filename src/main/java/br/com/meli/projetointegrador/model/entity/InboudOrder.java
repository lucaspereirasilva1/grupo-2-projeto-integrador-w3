package br.com.meli.projetointegrador.model.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Document(collection = "inboundorder")
public class InboudOrder {

    @MongoId(FieldType.OBJECT_ID)
    @Setter(AccessLevel.NONE)
    private String id;

    private Integer orderNumber;
    private LocalDate orderDate;

    @DBRef
    private Section section;

    @DBRef
    @Field(name = "batchstock")
    private List<BatchStock> listBatchStock;

    public InboudOrder id(String id) {
        this.id = id;
        return this;
    }

    public InboudOrder orderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
        return this;
    }

    public InboudOrder orderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
        return this;
    }

    public InboudOrder section(Section section) {
        this.section = section;
        return this;
    }

    public InboudOrder listBatchStock(List<BatchStock> listBatchStock) {
        this.listBatchStock = listBatchStock;
        return this;
    }

    public InboudOrder build() {
        return this;
    }

}
