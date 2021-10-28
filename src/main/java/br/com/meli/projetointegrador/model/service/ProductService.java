package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.exception.ProductException;
import br.com.meli.projetointegrador.exception.ProductExceptionNotFound;
import br.com.meli.projetointegrador.model.dto.ProductDTO;
import br.com.meli.projetointegrador.model.entity.Product;
import br.com.meli.projetointegrador.model.repository.ProductRepository;
import br.com.meli.projetointegrador.util.SectionCategory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public List<ProductDTO> listProdutcByCategory(SectionCategory sectionCategory) {
        List<ProductDTO> productListDTO = new ArrayList<>();
        List<Product> productList = productRepository.findProductBySectionCategory(sectionCategory);
        if (!productList.isEmpty()){
            for (Product p: productList) {
                ProductDTO productDTO = new ProductDTO()
                        .productId(p.getProductId())
                        .productName(p.getProductName())
                        .sectionCategory(p.getSectionCategory())
                        .build();
                productListDTO.add(productDTO);
            }
            return productListDTO;
        } else {
            throw new ProductExceptionNotFound("Nao temos o produtos nessa categoria, por favor informar a categoria correta!");
        }
    }
}