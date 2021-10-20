package br.com.meli.projetointegrador.model.repository;

import br.com.meli.projetointegrador.model.entity.Section;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SectionRepository extends MongoRepository<Section, String> {
}
