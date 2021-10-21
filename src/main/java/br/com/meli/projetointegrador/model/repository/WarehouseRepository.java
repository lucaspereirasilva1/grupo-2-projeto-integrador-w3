package br.com.meli.projetointegrador.model.repository;

import br.com.meli.projetointegrador.model.entity.Warehouse;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WarehouseRepository  extends MongoRepository <Warehouse, String> {

    Optional<Warehouse> findByWarehouse(Warehouse warehouse);
}
