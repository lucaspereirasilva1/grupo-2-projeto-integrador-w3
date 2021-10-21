package br.com.meli.projetointegrador.model.repository;

import br.com.meli.projetointegrador.model.entity.Agent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AgentRepository extends MongoRepository<Agent, String> {

    Optional<Agent> findByCpf(String cpf);

}
