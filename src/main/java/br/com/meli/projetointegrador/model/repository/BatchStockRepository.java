package br.com.meli.projetointegrador.model.repository;

import br.com.meli.projetointegrador.model.entity.BatchStock;
import br.com.meli.projetointegrador.model.entity.Section;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Jhony Zuim / Lucas Pereira / Edmilson Nobre / Rafael Vicente
 * @version 1.0.0
 * @since 15/10/2021
 * Repository para trabalhar como uma porta ou janela de acesso a camada do banco da entity batchStock
 */

@Repository
public interface BatchStockRepository extends MongoRepository<BatchStock, String> {

    Long countBySection(Section section);

}
