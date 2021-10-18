package br.com.meli.projetointegrador.model.dto;

import lombok.Data;

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
