package br.com.meli.projetointegrador.model.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Document(collection = "batchstock")
public class BatchStock {

    @MongoId(FieldType.OBJECT_ID)
    @Setter(AccessLevel.NONE)
    private String id;

    private Integer batchNumber;
    private String productId;
    private Float currentTemperature;
    private Float minimumTemperature;
    private Integer initialQuantity;
    private Integer currentQuantity;
    private LocalDate manufacturingDate;
    private LocalDateTime manufacturingTime;
    private LocalDate dueDate;
    private Agent agent;

    public BatchStock id(String id) {
        this.id = id;
        return this;
    }

    public BatchStock batchNumber(Integer batchNumber) {
        this.batchNumber = batchNumber;
        return this;
    }

    public BatchStock id(String id) {
        this.id = id;
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

    public BatchStock build() {
        return this;
    }
}
