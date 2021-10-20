package br.com.meli.projetointegrador.model.dto;

import lombok.Data;


    @Data
    public class WarehouseDTO {

        private String warehouseName;
        private String warehouseCode;


        public WarehouseDTO warehouseCode(String warehouseCode) {
            this.warehouseCode = warehouseCode;
            return this;
        }

        // assinatura estava escrito como br.com.meli.projetointegrador.model.dto.SectionDTO

        public WarehouseDTO warehouseName(String warehouseName) {
            this.warehouseName = warehouseName;
            return this;
        }

     //   public br.com.meli.projetointegrador.model.dto.WarehouseDTO build() {
       //     return this;
      //  }



}
