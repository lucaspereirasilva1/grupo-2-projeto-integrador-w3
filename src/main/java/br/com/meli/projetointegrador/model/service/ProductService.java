package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.exception.ProductException;
import br.com.meli.projetointegrador.model.entity.Product;
import br.com.meli.projetointegrador.model.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Jhony Zuim / Lucas Pereira / Edmilson Nobre / Rafael Vicente
 * @version 1.0.0
 * @since 15/10/2021
 * Camada service responsavel pela regra de negocio relacionada ao product
 */

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * @author Jhony Zuim
     * @param sectionCode, recebe um produto para validar se esta na section correta
     * @return true ou exception personalizada
     */

    public Boolean validProductSection(String sectionCode){
        if (productRepository.existsProductBySection_SectionCode(sectionCode)){
            return true;
        } else {
            throw new ProductException("Produto nao faz parte do setor, por favor verifique o setor correto!");
        }
    }

    public Product find(String productId) {
        Optional<Product> product = productRepository.findDistinctFirstByProductId(productId);
        if (product.isPresent()){
            return product.get();
        } else {
            throw new ProductException("Produto nao cadastrado!!! Por gentileza cadastrar");
        }
    }
}
