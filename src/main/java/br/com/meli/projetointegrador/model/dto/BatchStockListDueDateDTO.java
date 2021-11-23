package br.com.meli.projetointegrador.model.dto;

import lombok.Data;

import java.util.List;

/**
 * @author Jhony Zuim / Lucas Pereira / Edemilson Nobre / Rafael Vicente
 * @version 1.0.0
 * @since 15/10/2021
 * Objeto de Transferência de Dados do BatchStockServiceDueDateDTO
 */

@Data
public class BatchStockListDueDateDTO {

    private List<BatchStockServiceDueDateDTO> batchStock;

    public BatchStockListDueDateDTO(List<BatchStockServiceDueDateDTO> batchStock) {
        this.batchStock = batchStock;
    }

}
