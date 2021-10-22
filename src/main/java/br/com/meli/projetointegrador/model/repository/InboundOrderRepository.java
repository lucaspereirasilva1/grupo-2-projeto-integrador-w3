package br.com.meli.projetointegrador.model.repository;

import br.com.meli.projetointegrador.model.entity.InboundOrder;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InboundOrderRepository extends MongoRepository<InboundOrder, String> {

    void deleteByOrderNumber(Integer orderNumber);
    Optional<InboundOrder> findByOrderNumber(Integer orderNumber);

}
