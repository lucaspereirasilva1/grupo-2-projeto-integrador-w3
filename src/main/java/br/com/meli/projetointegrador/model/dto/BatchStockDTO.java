package br.com.meli.projetointegrador.model.dto;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * @author Jhony Zuim / Lucas Pereira / Edemilson Nobre / Rafael Vicente
 * @version 1.0.0
 * @since 15/10/2021
 * Objeto de Transferência de Dados do BatchStock
 */

@Data
public class BatchStockDTO {

    @NotNull(message = "batchNumber cannot be null")
    @Min(value = 1, message = "batchNumber minimum value is 1")
    private Integer batchNumber;

    @NotNull(message = "productId cannot be null")
    @NotEmpty(message = "productId cannot be empty")
    @NotBlank(message = "productId cannot be blank")
    @Size(min = 1, message = "productId most be minimum size 1")
    private String productId;

    @NotNull(message = "currentTemperature cannot be null")
    private Float currentTemperature;

    @NotNull(message = "minimumTemperature cannot be null")
    private Float minimumTemperature;

    @NotNull(message = "initialQuantity cannot be null")
    @Min(value = 1, message = "initialQuantity minimum value is 1")
    private Integer initialQuantity;

    @NotNull(message = "currentQuantity cannot be null")
    @Min(value = 1, message = "currentQuantity minimum value is 1")
    private Integer currentQuantity;

    @NotNull(message = "manufacturingDate cannot be null")
    private LocalDate manufacturingDate;

    @NotNull(message = "manufacturingTime cannot be null")
    private LocalTime manufacturingTime;

    @NotNull(message = "dueDate cannot be null")
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

    public BatchStockDTO manufacturingTime(LocalTime manufacturingTime) {
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
