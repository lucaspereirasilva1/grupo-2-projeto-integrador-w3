package br.com.meli.projetointegrador.model.dto;

import lombok.Data;

/**
 * @author Jhony Zuim / Lucas Pereira / Edmilson Nobre / Rafael Vicente
 * @version 1.0.0
 * @since 15/10/2021
 * Objeto de Transferência de Dados do section
 */

@Data
public class SectionDTO {

    private String sectionCode;
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
