package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.exception.ProductException;
import br.com.meli.projetointegrador.exception.ProductExceptionNotFound;
import br.com.meli.projetointegrador.model.dto.ProductDTO;
import br.com.meli.projetointegrador.model.entity.Product;
import br.com.meli.projetointegrador.model.entity.Section;
import br.com.meli.projetointegrador.model.entity.SectionCategory;
import br.com.meli.projetointegrador.model.enums.ESectionCategory;
import br.com.meli.projetointegrador.model.repository.ProductRepository;
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
    private final SectionService sectionService;
    private final SectionCategoryService sectionCategoryService;

    public ProductService(ProductRepository productRepository,
                          SectionService sectionService,
                          SectionCategoryService sectionCategoryService) {
        this.productRepository = productRepository;
        this.sectionService = sectionService;
        this.sectionCategoryService = sectionCategoryService;
    }

    /**
     * @param sectionCode, recebe um codigo de section para validar se o produto esta na section correta;
     * @return true ou exception.
     */
    public Boolean validProductSection(String sectionCode){
        final Section section = sectionService.find(sectionCode);
        Boolean existsProductBySection = productRepository.existsProductBySection(section);
        if (Boolean.TRUE.equals(existsProductBySection)){
            return true;
        } else {
            throw new ProductException("Produto nao faz parte do setor, por favor verifique o setor correto!");
        }
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
                    .sectionName(p.getSection().getSectionName())
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

}