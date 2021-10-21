package br.com.meli.projetointegrador.model.entity;

import br.com.meli.projetointegrador.model.dto.BatchStockDTO;
import br.com.meli.projetointegrador.model.dto.InboundOrderDTO;
import br.com.meli.projetointegrador.model.dto.SectionDTO;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

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
    private Section section;

    @DBRef(lazy = true)
    private List<BatchStock> listBatchStock;

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
