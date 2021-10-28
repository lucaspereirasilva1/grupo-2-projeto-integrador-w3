package br.com.meli.projetointegrador.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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

    @NotNull(message = "orderNumber cannot be null")
    @Min(value = 1, message = "orderNumber minimum value is 1")
    private Integer orderNumber;

    @NotNull(message = "orderDate cannot be null")
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern="dd/MM/yyyy")
    private LocalDate orderDate;

    @JsonProperty("section")
    @NotNull(message = "section cannot be null")
    @Valid
    private SectionDTO sectionDTO;

    @JsonProperty("listBatchStock")
    @NotNull(message = "listBatchStock cannot be null")
    @Size(min = 1, message = "listBatchStock minimum size is 1")
    @Valid
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
