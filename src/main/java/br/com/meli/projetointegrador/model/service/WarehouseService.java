package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.exception.WarehouseException;
import br.com.meli.projetointegrador.model.dto.BatchStockResponseWarehousesDTO;
import br.com.meli.projetointegrador.model.dto.WarehouseQuantityDTO;
import br.com.meli.projetointegrador.model.entity.Warehouse;
import br.com.meli.projetointegrador.model.repository.WarehouseRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Jhony Zuim / Lucas Pereira / Edmilson Nobre / Rafael Vicente
 * @version 1.0.0
 * @since 15/10/2021
 * Camada service responsavel pela regra de negocio relacionada ao warehouse
 */

@Service
public class WarehouseService {

    private final WarehouseRepository warehouseRepository;
    private final BatchStockService batchStockService;

    public WarehouseService(WarehouseRepository warehouseRepository, BatchStockService batchStockService) {
        this.warehouseRepository = warehouseRepository;
        this.batchStockService = batchStockService;
    }

    /**
     * @param warehouseCode recebe o codigo do warehouse para validar se existe no banco;
     * @return verdadeiro ou exception.
     */
    public Boolean validWarehouse(String warehouseCode) {
        Boolean existsByWarehouseCode = warehouseRepository.existsByWarehouseCode(warehouseCode);
        if (Boolean.TRUE.equals(existsByWarehouseCode)) {
            return true;
        } else {
            throw new WarehouseException("Armazem nao cadastrado!!! Por gentileza cadastrar!!!");
        }
    }

    /**
     * @param warehouseCode recebe um codigo de warehouse;
     * @return optional de warehouse ou exception.
     */
    public Warehouse find(String warehouseCode) {
        Optional<Warehouse> warehouseOptional = warehouseRepository.findByWarehouseCode(warehouseCode);
        if (warehouseOptional.isEmpty()) {
            throw new WarehouseException("Armazem nao encontrado!!! Por gentileza reenviar com um armazem valido");
        }
        return warehouseOptional.get();
    }

    /**
     * @param productId recebe um codigo de produto;
     * @return batchResponseWarehouseDTO com totais de produtos por armazem.
     */
    public BatchStockResponseWarehousesDTO listQuantityProduct(String productId) {
        BatchStockResponseWarehousesDTO batchStockResponseWarehousesDTO = new BatchStockResponseWarehousesDTO();
        batchStockResponseWarehousesDTO.productId(productId);
        List<Warehouse> warehouseList = warehouseRepository.findWarehouseBy(productId);
        List<WarehouseQuantityDTO> warehousesList = new ArrayList<>();
        for (Warehouse w: warehouseList){
            WarehouseQuantityDTO warehouseQuantityDTO = new WarehouseQuantityDTO()
                    .warehouseCode(w.getWarehouseCode())
                    .totalQuantity(batchStockService.quantityProductBatchStock(productId, w.getWarehouseCode()))
                    .build();
            warehousesList.add(warehouseQuantityDTO);
            batchStockResponseWarehousesDTO.warehouses(warehousesList);
        }
        return batchStockResponseWarehousesDTO;
    }
}