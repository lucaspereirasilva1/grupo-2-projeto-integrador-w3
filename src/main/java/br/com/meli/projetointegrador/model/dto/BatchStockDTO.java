package br.com.meli.projetointegrador.model.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Jhony Zuim / Lucas Pereira / Edmilson Nobre / Rafael Vicente
 * @version 1.0.0
 * @since 15/10/2021
 * Objeto de Transferência de Dados do BatchStock
 */

@Data
public class BatchStockDTO {

    private Integer batchNumber;
    private String productId;
    private Float currentTemperature;
    private Float minimumTemperature;
    private Integer initialQuantity;
    private Integer currentQuantity;
    private LocalDate manufacturingDate;
    private LocalDateTime manufacturingTime;
    private LocalDate dueDate;

    public BatchStockDTO batchNumber(Integer batchNumber) {
        this.batchNumber = batchNumber;
        return this;
    }

    public BatchStockDTO productId(String productId) {
        this.productId = productId;
        return this;
    }

    public BatchStockDTO currentTemperature(Float currentTemperature) {
        this.currentTemperature = currentTemperature;
        return this;
    }

    public BatchStockDTO minimumTemperature(Float minimumTemperature) {
        this.minimumTemperature = minimumTemperature;
        return this;
    }

    public BatchStockDTO initialQuantity(Integer initialQuantity) {
        this.initialQuantity = initialQuantity;
        return this;
    }

    public BatchStockDTO currentQuantity(Integer currentQuantity) {
        this.currentQuantity = currentQuantity;
        return this;
    }

    public BatchStockDTO manufacturingDate(LocalDate manufacturingDate) {
        this.manufacturingDate = manufacturingDate;
        return this;
    }

    public BatchStockDTO manufacturingTime(LocalDateTime manufacturingTime) {
        this.manufacturingTime = manufacturingTime;
        return this;
    }

    public BatchStockDTO dueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public BatchStockDTO build() {
        return this;
    }

}
