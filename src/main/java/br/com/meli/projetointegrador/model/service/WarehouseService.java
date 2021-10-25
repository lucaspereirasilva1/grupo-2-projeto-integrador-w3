package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.exception.WarehouseException;
import br.com.meli.projetointegrador.model.entity.Warehouse;
import br.com.meli.projetointegrador.model.repository.WarehouseRepository;
import org.springframework.stereotype.Service;
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
    public WarehouseService(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    /** metodo para verificar se Warehouse é valido.
     *
     * @param warehouse
     * @return
     */

    public Boolean validWarehouse(String warehouseCode) {
        if (warehouseRepository.existsByWarehouseCode(warehouseCode)) {
            return true;
        } else {
            throw new WarehouseException("Armazem nao cadastrado!!! Por gentileza cadastrar!!!");
        }
    }

    public Warehouse find(String warehouseCode) {
        Optional<Warehouse> warehouseOptional = warehouseRepository.findByWarehouseCode(warehouseCode);
        if (warehouseOptional.isEmpty()) {
            throw new WarehouseException("Armazem nao cadastrado!!! Por gentileza reenviar com um armazem valido");
        }
        return warehouseOptional.get();
    }
}
