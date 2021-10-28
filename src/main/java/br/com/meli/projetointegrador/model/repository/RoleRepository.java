package br.com.meli.projetointegrador.model.repository;

import br.com.meli.projetointegrador.model.entity.Role;
import br.com.meli.projetointegrador.model.enums.ERole;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends MongoRepository<Role, String> {

    Optional<Role> findByName(ERole name);

}
