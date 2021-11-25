package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.exception.PersistenceException;
import br.com.meli.projetointegrador.exception.ProductException;
import br.com.meli.projetointegrador.exception.ProductExceptionNotFound;
import br.com.meli.projetointegrador.model.dto.ProductDTO;
import br.com.meli.projetointegrador.model.entity.Product;
import br.com.meli.projetointegrador.model.entity.SectionCategory;
import br.com.meli.projetointegrador.model.enums.ESectionCategory;
import br.com.meli.projetointegrador.model.repository.ProductRepository;
import br.com.meli.projetointegrador.utils.ConstantsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
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
    private final SectionCategoryService sectionCategoryService;
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    public ProductService(ProductRepository productRepository,
                          SectionCategoryService sectionCategoryService) {
        this.productRepository = productRepository;
        this.sectionCategoryService = sectionCategoryService;
    }

    /**
     * @param productId, recebe um Id de produto;
     * @return produto ou exception.
     */
    public Product find(String productId) {
        Optional<Product> product = productRepository.findDistinctFirstByProductId(productId);
        if (product.isPresent()){
            return product.get();
        } else {
            throw new ProductException("Produto (" + productId + ") nao cadastrado!!! Por gentileza cadastrar");
        }
    }

    /**
     * @param category, recebe uma categoria de section;
     * @return uma lista por categoria.
     */
    public List<ProductDTO> listProdutcByCategory(String category) {
        final SectionCategory sectionCategory = sectionCategoryService.find(ESectionCategory.valueOf(category));
        List<Product> productList = productRepository.findProductByCategory(sectionCategory);
        if (!productList.isEmpty()){
            return converteProductlist(productList);
        } else {
            throw new ProductExceptionNotFound("Nao temos produtos nessa categoria " + category + ", por favor informar a categoria correta!");
        }
    }

    /**
     * @param productList, recebe uma lista de produto;
     * @return uma lista de produtoDTO.
     */
    public List <ProductDTO> converteProductlist  (List<Product> productList) {
        List <ProductDTO> productDTOList = new ArrayList<>();
        for (Product p : productList) {
            ProductDTO productDTO = new ProductDTO()
                    .productId(p.getProductId())
                    .productName(p.getProductName())
                    .category(p.getCategory().getName())
                    .productPrice(p.getProductPrice())
                    .dueDate(p.getDueDate())
                    .build();
            productDTOList.add(productDTO);
        }
        return productDTOList;
    }

    public List<ProductDTO> findAllProducts() {
        return converteProductlist(productRepository.findAll());
    }

    public void save(Product product) {
        try {
            productRepository.save(product);
        }catch (DataAccessException e) {
            logger.error(ConstantsUtil.PERSISTENCE_ERROR, e);
            throw new PersistenceException(ConstantsUtil.PERSISTENCE_ERROR);
        }
    }

}