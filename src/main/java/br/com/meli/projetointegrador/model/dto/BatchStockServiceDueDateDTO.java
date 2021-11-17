package br.com.meli.projetointegrador.model.dto;

import lombok.Data;

import java.time.LocalDate;

/**
 * @author Jhony Zuim / Lucas Pereira / Edemilson Nobre / Rafael Vicente
 * @version 1.0.0
 * @since 15/10/2021
 * Objeto de Transferência de Dados do BatchStockServiceDueDateDTO
 */

@Data
public class BatchStockServiceDueDateDTO {

    private Integer batchNumber;
    private String productId;
    private LocalDate dueDate;
    private Integer quantity;

    public BatchStockServiceDueDateDTO batchNumber(Integer batchNumber) {
        this.batchNumber = batchNumber;
        return this;
    }

    public BatchStockServiceDueDateDTO productId(String productId) {
        this.productId = productId;
        return this;
    }

    public BatchStockServiceDueDateDTO dueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public BatchStockServiceDueDateDTO quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public BatchStockServiceDueDateDTO build() {
        return this;
    }
}
