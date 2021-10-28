package br.com.meli.projetointegrador.model.dto;

import br.com.meli.projetointegrador.model.enums.SectionCategory;
import lombok.Data;

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
    private SectionCategory sectionCategory;

    public ProductDTO productId(String productId) {
        this.productId = productId;
        return this;
    }

    public ProductDTO productName(String productName) {
        this.productName = productName;
        return this;
    }

    public ProductDTO sectionCategory(SectionCategory sectionCategory) {
        this.sectionCategory = sectionCategory;
        return this;
    }

    public ProductDTO build(){
        return this;
    }
}