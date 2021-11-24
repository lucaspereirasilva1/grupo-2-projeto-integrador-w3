package br.com.meli.projetointegrador.model.repository;

import br.com.meli.projetointegrador.model.entity.Promo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromoRepository extends MongoRepository<Promo, String> {
}
