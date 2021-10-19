package br.com.meli.projetointegrador.model.repository;

import br.com.meli.projetointegrador.model.entity.InboudOrder;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InboundOrderRepository extends MongoRepository<InboudOrder, String> {
}
