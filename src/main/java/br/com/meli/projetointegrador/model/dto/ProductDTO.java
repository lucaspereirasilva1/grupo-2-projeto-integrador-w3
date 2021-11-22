package br.com.meli.projetointegrador.model.dto;

import br.com.meli.projetointegrador.model.enums.ESectionCategory;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author Jhony Zuim / Lucas Pereira / Edmilson Nobre / Rafael Vicente
 * @version 1.0.0
 * @since 15/10/2021
 * Objeto de Transferência de Dados do product
 */

@Data
public class ProductDTO {

    private String productId;
    private String productName;
    private ESectionCategory category;
    private BigDecimal productPrice;
    private LocalDate dueDate;

    public ProductDTO productId(String productId) {
        this.productId = productId;
        return this;
    }

    public ProductDTO productName(String productName) {
        this.productName = productName;
        return this;
    }

    public ProductDTO category(ESectionCategory category) {
        this.category = category;
        return this;
    }

    public ProductDTO productPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
        return this;
    }

    public ProductDTO dueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public ProductDTO build(){
        return this;
    }
  
}