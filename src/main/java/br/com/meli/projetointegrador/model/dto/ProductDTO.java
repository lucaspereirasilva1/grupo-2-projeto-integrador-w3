package br.com.meli.projetointegrador.model.dto;

import br.com.meli.projetointegrador.util.SectionCategory;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Jhony Zuim / Lucas Pereira / Edmilson Nobre / Rafael Vicente
 * @version 1.0.0
 * @since 15/10/2021
 * Objeto de Transferência de Dados do product
 */

@Data
public class ProductDTO {

    @NotNull(message = "productId cannot be null")
    @NotEmpty(message = "productId cannot be empty")
    @NotBlank(message = "productId cannot be blank")
    @Size(min = 1, message = "productId most be minimum size 1")
    private String productId;

    @NotNull(message = "productName cannot be null")
    @NotEmpty(message = "productName cannot be empty")
    @NotBlank(message = "productName cannot be blank")
    @Size(min = 1, message = "productName most be minimum size 1")
    private String productName;

    @NotNull(message = "sectionName cannot be null")
    @NotEmpty(message = "sectionName cannot be empty")
    @NotBlank(message = "sectionName cannot be blank")
    @Size(min = 1, message = "sectionName most be minimum size 1")
    private String sectionName;

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