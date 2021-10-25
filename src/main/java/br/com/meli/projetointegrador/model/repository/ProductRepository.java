package br.com.meli.projetointegrador.model.repository;

import br.com.meli.projetointegrador.model.entity.Product;
import br.com.meli.projetointegrador.model.entity.Section;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Jhony Zuim
 * @version 1.0.0
 * @since 15/10/2021
 * Repositorio para collection produto
 */

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

    Boolean existsProductBySection(Section section);
    Optional<Product> findByProductId(String productId);

}
