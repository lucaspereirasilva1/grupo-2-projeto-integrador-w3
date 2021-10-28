package br.com.meli.projetointegrador.model.entity;

import br.com.meli.projetointegrador.model.enums.EOrderStatus;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDate;
import java.util.List;

@Data
@Document(collection = "purchaseorder")
public class PurchaseOrder {

    @MongoId(FieldType.OBJECT_ID)
    @Setter(AccessLevel.NONE)
    private String id;

    private LocalDate date;

    @DBRef
    private Buyer buyer;

    private EOrderStatus orderStatus;

    @DBRef
    private List<Product> product;

    public PurchaseOrder date(LocalDate date) {
        this.date = date;
        return this;
    }

    public PurchaseOrder buyer(Buyer buyer) {
        this.buyer = buyer;
        return this;
    }

    public PurchaseOrder orderStatus(EOrderStatus orderStatus) {
        this.orderStatus = orderStatus;
        return this;
    }

    public PurchaseOrder product(List<Product> product) {
        this.product = product;
        return this;
    }

    public PurchaseOrder build(){
        return this;
    }

}
