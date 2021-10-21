package br.com.meli.projetointegrador.model.repository;

import br.com.meli.projetointegrador.model.entity.Section;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Jhony Zuim
 * @version 1.0.0
 * @since 15/10/2021
 * Repositorio para collection section
 */

@Repository
public interface SectionRepository extends MongoRepository<Section, String> {
  
  Optional<Section> findBySectionCode(String sectionCode);

}
