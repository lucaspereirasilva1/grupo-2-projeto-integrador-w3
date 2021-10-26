package br.com.meli.projetointegrador.model.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.*;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Jhony Zuim / Lucas Pereira / Edmilson Nobre / Rafael Vicente
 * @version 1.0.0
 * @since 15/10/2021
 * Objeto criado para o InboundOrder/Ordem de entrada e seus atributos
 */

@Data
@Document(collection = "inboundorder")
public class InboundOrder {

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

    public InboundOrder id(String id) {
        this.id = id;
        return this;
    }

    public InboundOrder orderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
        return this;
    }

    public InboundOrder orderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
        return this;
    }

    public InboundOrder section(Section section) {
        this.section = section;
        return this;
    }

    public InboundOrder listBatchStock(List<BatchStock> listBatchStock) {
        this.listBatchStock = listBatchStock;
        return this;
    }

    public InboundOrder build() {
        return this;
    }

}
