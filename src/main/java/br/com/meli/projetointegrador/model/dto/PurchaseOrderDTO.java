package br.com.meli.projetointegrador.model.dto;

import br.com.meli.projetointegrador.model.enums.EOrderStatus;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class PurchaseOrderDTO {

    private LocalDate data;
    private String buyerId;

    private EOrderStatus orderStatus;

    private List<ProductPurchaseOrderDTO> listProductPurchaseOrderDTO;

    public PurchaseOrderDTO data(LocalDate data) {
        this.data = data;
        return this;
    }

    public PurchaseOrderDTO buyerId(String buyerId) {
        this.buyerId = buyerId;
        return this;
    }

    public PurchaseOrderDTO orderStatus(EOrderStatus orderStatus) {
        this.orderStatus = orderStatus;
        return this;
    }

    public PurchaseOrderDTO listProductPurchaseOrderDTO(List<ProductPurchaseOrderDTO> listProductPurchaseOrderDTO) {
        this.listProductPurchaseOrderDTO = listProductPurchaseOrderDTO;
        return this;
    }

    public PurchaseOrderDTO build(){
        return this;
    }
}
