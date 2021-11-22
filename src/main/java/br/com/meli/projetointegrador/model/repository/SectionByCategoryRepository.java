package br.com.meli.projetointegrador.model.repository;

import br.com.meli.projetointegrador.model.entity.SectionByCategory;
import br.com.meli.projetointegrador.model.entity.SectionCategory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SectionByCategoryRepository extends MongoRepository<SectionByCategory, String> {

    Optional<SectionByCategory> findByCategory(SectionCategory sectionCategory);

}
