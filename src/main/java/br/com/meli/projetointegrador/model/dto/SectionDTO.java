package br.com.meli.projetointegrador.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Jhony Zuim / Lucas Pereira / Edmilson Nobre / Rafael Vicente
 * @version 1.0.0
 * @since 15/10/2021
 * Objeto de Transferência de Dados do section
 */

@Data
public class SectionDTO {

    @NotNull(message = "sectionCode cannot be null")
    @NotEmpty(message = "sectionCode cannot be empty")
    @NotBlank(message = "sectionCode cannot be blank")
    @Size(min = 1, message = "sectionCode most be minimum size 1")
    private String sectionCode;

    @NotNull(message = "warehouseCode cannot be null")
    @NotEmpty(message = "warehouseCode cannot be empty")
    @NotBlank(message = "warehouseCode cannot be blank")
    @Size(min = 1, message = "warehouseCode most be minimum size 1")
    private String warehouseCode;

    public SectionDTO sectionCode(String sectionCode) {
        this.sectionCode = sectionCode;
        return this;
    }

    public SectionDTO warehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
        return this;
    }

    public SectionDTO build() {
        return this;
    }

}
