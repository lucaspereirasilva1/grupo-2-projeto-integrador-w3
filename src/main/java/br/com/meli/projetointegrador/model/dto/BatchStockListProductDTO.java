package br.com.meli.projetointegrador.model.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * @author Jhony Zuim / Lucas Pereira / Edmilson Nobre / Rafael Vicente
 * @version 1.0.0
 * @since 15/10/2021
 * Objeto de Transferência de Dados do BatchStockListProductDTO
 */

@Data
public class BatchStockListProductDTO {

    @NotNull(message = "batchNumber cannot be null")
    @Min(value = 1, message = "batchNumber minimum value is 1")
    private Integer batchNumber;

    @NotNull(message = "currentQuantity cannot be null")
    @Min(value = 1, message = "currentQuantity minimum value is 1")
    private Integer currentQuantity;

    @NotNull(message = "dueDate cannot be null")
    private LocalDate dueDate;

    public BatchStockListProductDTO batchNumber(Integer batchNumber) {
        this.batchNumber = batchNumber;
        return this;
    }

    public BatchStockListProductDTO currentQuantity(Integer currentQuantity) {
        this.currentQuantity = currentQuantity;
        return this;
    }

    public BatchStockListProductDTO dueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public BatchStockListProductDTO build() {
        return this;
    }

}