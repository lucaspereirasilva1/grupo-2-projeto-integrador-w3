package br.com.meli.projetointegrador.model.repository;

import br.com.meli.projetointegrador.model.entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Jhony Zuim / Lucas Pereira / Edmilson Nobre / Rafael Vicente
 * @version 1.0.0
 * @since 15/10/2021
 * Repository para trabalhar como uma porta ou janela de acesso a camada do banco da entity product
 */

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

    Boolean existsProductBySection_SectionCode(String sectionCode);
    Optional<Product> findByProductId(String productId);
    List<Product> findAllBySection_SectionCode(String sectionCode);
}
