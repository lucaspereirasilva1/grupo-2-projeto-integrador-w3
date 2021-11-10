package br.com.meli.projetointegrador.model.repository;

import br.com.meli.projetointegrador.model.entity.Warehouse;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Jhony Zuim / Lucas Pereira / Edmilson Nobre / Rafael Vicente
 * @version 1.0.0
 * @since 15/10/2021
 * Repository para trabalhar como uma porta ou janela de acesso a camada do banco da entity warehouse
 */

@Repository
public interface WarehouseRepository  extends MongoRepository <Warehouse, String> {

    Optional<Warehouse> findByWarehouseCode(String warehouseCode);
    Boolean existsByWarehouseCode(String warehouseCode);
    List<Warehouse> findWarehouseBy(String productId);
}
