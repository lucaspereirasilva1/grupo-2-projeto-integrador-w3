package br.com.meli.projetointegrador.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class InboundOrderDTO {

    //orientacoes de como usar a anotation jsonproperties
    private Integer orderNumber;
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
