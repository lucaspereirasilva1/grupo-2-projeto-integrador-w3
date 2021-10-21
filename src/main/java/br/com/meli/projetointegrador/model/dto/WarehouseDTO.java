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

        public WarehouseDTO warehouseName(String warehouseName) {
            this.warehouseName = warehouseName;
            return this;
        }

        public WarehouseDTO build() {
            return this;
        }

}
