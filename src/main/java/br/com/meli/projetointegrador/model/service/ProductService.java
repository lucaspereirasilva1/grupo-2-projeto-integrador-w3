package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.exception.ProductException;
import br.com.meli.projetointegrador.model.entity.Product;
import br.com.meli.projetointegrador.model.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Jhony Zuim
 * @version 1.0.0
 * @since 15/10/2021
 */

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * @author Jhony Zuim
     * @version 1.0.0
     * @param product, recebe um produto para validar se esta na section correta
     * @return true ou exception personalizada
     */

    public boolean validProductSection(Product product){
        Optional<Product> productOptional = productRepository.findBySection(product.getSection());
        if (productOptional.isPresent()){
            return true;
        } else {
            throw new ProductException("Produto nao faz parte do setor, por favor verifique o setor correto!");
        }
    }
}
