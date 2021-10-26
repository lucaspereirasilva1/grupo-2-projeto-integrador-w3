package br.com.meli.projetointegrador.model.dto;

import lombok.Data;

/**
 * @author Jhony Zuim / Lucas Pereira / Edmilson Nobre / Rafael Vicente
 * @version 1.0.0
 * @since 15/10/2021
 * Objeto de Transferência de Dados do warehouse
 */

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
