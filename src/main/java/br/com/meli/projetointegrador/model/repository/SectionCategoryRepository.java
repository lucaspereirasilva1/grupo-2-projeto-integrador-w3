package br.com.meli.projetointegrador.model.repository;

import br.com.meli.projetointegrador.model.entity.SectionCategory;
import br.com.meli.projetointegrador.model.enums.ESectionCategory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SectionCategoryRepository extends MongoRepository<SectionCategory, String> {

    Optional<SectionCategory> findByName(ESectionCategory eSectionCategory);

}
