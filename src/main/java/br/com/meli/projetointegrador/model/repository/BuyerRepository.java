package br.com.meli.projetointegrador.model.repository;

import br.com.meli.projetointegrador.model.entity.Buyer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BuyerRepository extends MongoRepository<Buyer, String> {

    Optional<Buyer> findByCpf(String cpf);

}
