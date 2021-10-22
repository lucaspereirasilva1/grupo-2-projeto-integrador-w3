package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.exception.WarehouseException;
import br.com.meli.projetointegrador.model.entity.Warehouse;
import br.com.meli.projetointegrador.model.repository.WarehouseRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

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
        if (warehouseRepository.findByWarehouseCode(warehouseCode).isPresent()) {
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
