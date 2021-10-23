package br.com.meli.projetointegrador.model.repository;

import br.com.meli.projetointegrador.model.entity.Agent;
import br.com.meli.projetointegrador.model.entity.BatchStock;
import br.com.meli.projetointegrador.model.entity.Section;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BatchStockRepository extends MongoRepository<BatchStock, String> {

    Optional<BatchStock> findByAgent(Agent agent);

    Optional<BatchStock> findBySection(BatchStock batchStock);

    Long countBySection(Section section);
}
