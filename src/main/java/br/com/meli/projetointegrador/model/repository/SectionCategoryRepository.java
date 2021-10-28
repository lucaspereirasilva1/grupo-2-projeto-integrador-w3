package br.com.meli.projetointegrador.model.repository;

import br.com.meli.projetointegrador.model.entity.SectionCategory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SectionCategoryRepository extends MongoRepository<SectionCategory, String> {
}
