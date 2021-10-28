package br.com.meli.projetointegrador.model.repository;

import br.com.meli.projetointegrador.model.entity.PurchaseOrder;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseOrderRepository extends MongoRepository<PurchaseOrder, String> {
}
