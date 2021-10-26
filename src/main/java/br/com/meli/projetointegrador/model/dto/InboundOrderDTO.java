package br.com.meli.projetointegrador.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Jhony Zuim / Lucas Pereira / Edmilson Nobre / Rafael Vicente
 * @version 1.0.0
 * @since 15/10/2021
 * Objeto de Transferência de Dados do InboundOrder
 */

@Data
public class InboundOrderDTO {

    private Integer orderNumber;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern="dd/MM/yyyy")
    private LocalDate orderDate;

    private SectionDTO sectionDTO;
    private List<BatchStockDTO> listBatchStockDTO;

    public InboundOrderDTO orderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
        return this;
    }

    public InboundOrderDTO orderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
        return this;
    }

    public InboundOrderDTO sectionDTO(SectionDTO sectionDTO) {
        this.sectionDTO = sectionDTO;
        return this;
    }

    public InboundOrderDTO batchStockDTO(List<BatchStockDTO> listBatchStockDTO) {
        this.listBatchStockDTO = listBatchStockDTO;
        return this;
    }

    public InboundOrderDTO build() {
        return this;
    }

}
