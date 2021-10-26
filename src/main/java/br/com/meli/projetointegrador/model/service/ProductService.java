package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.exception.ProductException;
import br.com.meli.projetointegrador.model.dto.ProductDTO;
import br.com.meli.projetointegrador.model.entity.Product;
import br.com.meli.projetointegrador.model.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public Boolean validProductSection(String sectionCode){
        if (productRepository.existsProductBySection_SectionCode(sectionCode)){
            return true;
        } else {
            throw new ProductException("Produto nao faz parte do setor, por favor verifique o setor correto!");
        }
    }

    public Product find(String productId) {
        Optional<Product> product = productRepository.findByProductId(productId);
        if (product.isPresent()){
            return product.get();
        } else {
            throw new ProductException("Produto nao cadastrado!!! Por gentileza cadastrar");
        }
    }

    public List<ProductDTO> listProdutcBySection(String sectionCode) {
        List<ProductDTO> productList = productRepository.findAllBySection_SectionCode(sectionCode);
        if (!productList.isEmpty()){
            return productList;
        } else {
            throw new ProductException("Nao temos o produtos nessa section, por favor informar a section correta!");
        }
    }
}
