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

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Jhony Zuim / Lucas Pereira / Edmilson Nobre / Rafael Vicente
 * @version 1.0.0
 * @since 15/10/2021
 * Objeto criado para o BatchStock/lote e seus atributos
 */

@Data
@Document(collection = "batchstock")
public class BatchStock {

    @MongoId(FieldType.OBJECT_ID)
    @Setter(AccessLevel.NONE)
    private String id;

    @Indexed(unique = true)
    private Integer batchNumber;

    private String productId;
    private Float currentTemperature;
    private Float minimumTemperature;
    private Integer initialQuantity;
    private Integer currentQuantity;
    private LocalDate manufacturingDate;
    private LocalDateTime manufacturingTime;
    private LocalDate dueDate;

    @DBRef
    @Reference
    private Agent agent;

    @DBRef
    @Reference
    private Section section;

    public BatchStock id(String id) {
        this.id = id;
        return this;
    }

    public BatchStock batchNumber(Integer batchNumber) {
        this.batchNumber = batchNumber;
        return this;
    }

    public BatchStock productId(String productId) {
        this.productId = productId;
        return this;
    }

    public BatchStock currentTemperature(Float currentTemperature) {
        this.currentTemperature = currentTemperature;
        return this;
    }

    public BatchStock minimumTemperature(Float minimumTemperature) {
        this.minimumTemperature = minimumTemperature;
        return this;
    }

    public BatchStock initialQuantity(Integer initialQuantity) {
        this.initialQuantity = initialQuantity;
        return this;
    }

    public BatchStock currentQuantity(Integer currentQuantity) {
        this.currentQuantity = currentQuantity;
        return this;
    }

    public BatchStock manufacturingDate(LocalDate manufacturingDate) {
        this.manufacturingDate = manufacturingDate;
        return this;
    }

    public BatchStock manufacturingTime(LocalDateTime manufacturingTime) {
        this.manufacturingTime = manufacturingTime;
        return this;
    }

    public BatchStock dueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public BatchStock agent(Agent agent) {
        this.agent = agent;
        return this;
    }

    public BatchStock section(Section section) {
        this.section = section;
        return this;
    }

    public BatchStock build() {
        return this;
    }
}
