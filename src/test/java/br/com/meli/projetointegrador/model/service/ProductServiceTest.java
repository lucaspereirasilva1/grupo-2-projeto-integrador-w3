package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.exception.ProductException;
import br.com.meli.projetointegrador.exception.ProductExceptionNotFound;
import br.com.meli.projetointegrador.model.entity.Product;
import br.com.meli.projetointegrador.model.entity.SectionCategory;
import br.com.meli.projetointegrador.model.enums.ESectionCategory;
import br.com.meli.projetointegrador.model.repository.ProductRepository;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Jhony Zuim / Lucas Pereira / Edemilson Nobre / Rafael Vicente
 * @version 1.0.0
 * @since 15/10/2021
 * Camada de testes unitarios do service responsavel pela regra de negocio relacionada ao product
 */

class ProductServiceTest {

    private final ProductRepository mockProductRepository = mock(ProductRepository.class);
    private final SectionCategoryService mockSectionCategoryService = mock(SectionCategoryService.class);
    private final ProductService productService = new ProductService(mockProductRepository, mockSectionCategoryService);

    @Test
    void findExistTest() {
        Product product = new Product()
                .productId("LE")
                .productName("leite")
                .build();

        when(mockProductRepository.findDistinctFirstByProductId(product.getProductId()))
                .thenReturn(Optional.of(product));

        Product productReturn = productService.find(product.getProductId());

        assertEquals(product, productReturn);
    }

    @Test
    void findNotExistTest() {
        when(mockProductRepository.findDistinctFirstByProductId("LE"))
                .thenReturn(Optional.empty());

        ProductException productException = assertThrows(ProductException.class, () ->
                productService.find("LE"));

        String expectedMessage = "Produto (" + "LE" + ") nao cadastrado!!! Por gentileza cadastrar";

        assertTrue(expectedMessage.contains(productException.getMessage()));
    }

    @Test
    void validListProductByCategoryTest(){
        List<Product> productList = new ArrayList<>();

        Product productUm = new Product()
                .productId("LE")
                .productName("leite")
                .category(new SectionCategory().name(ESectionCategory.FF))
                .build();

        productList.add(productUm);

        Product productDois = new Product()
                .productId("LE")
                .productName("leite")
                .category(new SectionCategory().name(ESectionCategory.FF))
                .build();

        productList.add(productDois);

        when(mockProductRepository.findProductByCategory(any(SectionCategory.class)))
                .thenReturn(productList);
        when(mockSectionCategoryService.find(any(ESectionCategory.class)))
                .thenReturn(new SectionCategory().name(ESectionCategory.FF).build());

        assertEquals(2, productService.listProdutcByCategory(ESectionCategory.FF.toString()).size());
    }

    @Test
    void validListProductByCategoryTestEmpty(){
        when(mockProductRepository.findProductByCategory(any(SectionCategory.class)))
                .thenReturn(new ArrayList<>());
        when(mockSectionCategoryService.find(any(ESectionCategory.class)))
                .thenReturn(new SectionCategory().name(ESectionCategory.FF).build());

        ProductExceptionNotFound productExceptionNotFound = assertThrows
                (ProductExceptionNotFound.class,() -> productService.listProdutcByCategory("FF"));

        String mensagemEsperada = "Nao temos produtos nessa categoria " + "FF" + ", por favor informar a categoria correta!";
        String mensagemRecebida = productExceptionNotFound.getMessage();

        assertTrue(mensagemEsperada.contains(mensagemRecebida));
    }

    @Test
    void findAllProducts() {
        List<Product> productList = new ArrayList<>();

        Product productUm = new Product()
                .productId("LE")
                .productName("leite")
                .category(new SectionCategory().name(ESectionCategory.FF))
                .build();

        productList.add(productUm);

        Product productDois = new Product()
                .productId("LE")
                .productName("leite")
                .category(new SectionCategory().name(ESectionCategory.FF))
                .build();

        productList.add(productDois);
        when(mockProductRepository.findAll())
                .thenReturn(productList);

        assertFalse(productService.findAllProducts().isEmpty());
    }

}