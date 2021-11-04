package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.exception.ProductException;
import br.com.meli.projetointegrador.exception.ProductExceptionNotFound;
import br.com.meli.projetointegrador.model.entity.Product;
import br.com.meli.projetointegrador.model.entity.Section;
import br.com.meli.projetointegrador.model.entity.SectionCategory;
import br.com.meli.projetointegrador.model.entity.Warehouse;
import br.com.meli.projetointegrador.model.enums.ESectionCategory;
import br.com.meli.projetointegrador.model.repository.ProductRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
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

public class ProductServiceTest {

    private final ProductRepository mockProductRepository = mock(ProductRepository.class);
    private final SectionService mockSectionService = mock(SectionService.class);
    private final SectionCategoryService mockSectionCategoryService = mock(SectionCategoryService.class);
    private final ProductService productService = new ProductService(mockProductRepository, mockSectionService, mockSectionCategoryService);

    @Test
    void validProductSectionExistTest(){
        Warehouse warehouse = new Warehouse()
                .warehouseCode("SP")
                .warehouseName("Sao Paulo")
                .build();

        Section section = new Section()
                .sectionCode("LA")
                .sectionName("Laticionios")
                .maxLength(10)
                .warehouse(warehouse)
                .build();

        when(mockProductRepository.existsProductBySection(any(Section.class)))
                .thenReturn(true);
        when(mockSectionService.find(anyString()))
                .thenReturn(section);

        assertTrue(productService.validProductSection(section.getSectionCode()));
    }

    @Test
    void validProductSectionNotExistTest() {
        Warehouse warehouse = new Warehouse()
                .warehouseCode("SP")
                .warehouseName("Sao Paulo")
                .build();

        Section section = new Section()
                .sectionCode("LA")
                .sectionName("Laticionios")
                .warehouse(warehouse)
                .maxLength(10)
                .build();

        when(mockProductRepository.existsProductBySection(any(Section.class)))
                .thenReturn(false);

        ProductException productException = assertThrows(ProductException.class, () ->
                productService.validProductSection(section.getSectionCode()));

        String expectedMessage = "Produto nao faz parte do setor, por favor verifique o setor correto!";

        assertTrue(expectedMessage.contains(productException.getMessage()));
    }

    @Test
    void findExistTest() {
        Warehouse warehouse = new Warehouse()
                .warehouseCode("SP")
                .warehouseName("Sao Paulo")
                .build();

        Section section = new Section()
                .sectionCode("LA")
                .sectionName("Laticionios")
                .warehouse(warehouse)
                .maxLength(10)
                .build();

        Product product = new Product()
                .productId("LE")
                .productName("leite")
                .section(section)
                .build();

        when(mockProductRepository.findDistinctFirstByProductId(product.getProductId()))
                .thenReturn(Optional.of(product));

        Product productReturn = productService.find(product.getProductId());

        assertEquals(product, productReturn);
    }

    @Test
    void findNotExistTest() {
        Warehouse warehouse = new Warehouse()
                .warehouseCode("SP")
                .warehouseName("Sao Paulo")
                .build();

        Section section = new Section()
                .sectionCode("LA")
                .sectionName("Laticionios")
                .warehouse(warehouse)
                .maxLength(10)
                .build();

        Product product = new Product()
                .productId("LE")
                .productName("leite")
                .section(section)
                .build();

        when(mockProductRepository.findDistinctFirstByProductId(product.getProductId()))
                .thenReturn(Optional.empty());

        ProductException productException = assertThrows(ProductException.class, () ->
                productService.find(product.getProductId()));

        String expectedMessage = "Produto nao cadastrado!!! Por gentileza cadastrar";

        assertTrue(expectedMessage.contains(productException.getMessage()));
    }

    @Test
    void validListProductByCategoryTest(){
        List<Product> productList = new ArrayList<>();

        Warehouse warehouse = new Warehouse()
                .warehouseCode("SP")
                .warehouseName("Sao Paulo")
                .build();

        Section section = new Section()
                .sectionCode("LA")
                .sectionName("Laticionios")
                .maxLength(10)
                .warehouse(warehouse)
                .build();

        Product productUm = new Product()
                .productId("LE")
                .productName("leite")
                .section(section)
                .category(new SectionCategory().name(ESectionCategory.FF))
                .build();

        productList.add(productUm);

        Product productDois = new Product()
                .productId("LE")
                .productName("leite")
                .section(section)
                .category(new SectionCategory().name(ESectionCategory.FF))
                .build();

        productList.add(productDois);

        when(mockProductRepository.findProductByCategory(any(SectionCategory.class)))
                .thenReturn(productList);
        when(mockSectionCategoryService.find(any(ESectionCategory.class)))
                .thenReturn(new SectionCategory().name(ESectionCategory.FF).build());

        assertEquals(productService.listProdutcByCategory(ESectionCategory.FF.toString()).size(), 2);
    }

    @Test
    void validListProductByCategoryTestEmpty(){
        when(mockProductRepository.findProductByCategory(any(SectionCategory.class)))
                .thenReturn(new ArrayList<>());
        when(mockSectionCategoryService.find(any(ESectionCategory.class)))
                .thenReturn(new SectionCategory().name(ESectionCategory.FF).build());

        ProductExceptionNotFound productExceptionNotFound = assertThrows
                (ProductExceptionNotFound.class,() -> productService.listProdutcByCategory(ESectionCategory.FF.toString()));

        String mensagemEsperada = "Nao temos produtos nessa categoria " + ESectionCategory.FF.toString() + ", por favor informar a categoria correta!";
        String mensagemRecebida = productExceptionNotFound.getMessage();

        assertTrue(mensagemEsperada.contains(mensagemRecebida));
    }

    @Test
    void dueDataProduct() {
        ProductException productException = assertThrows
                (ProductException.class,() -> productService.dueDataProduct(LocalDate.now()));

        String mensagemEsperada = "Produto Vencido";
        String mensagemRecebida = productException.getMessage();

        assertTrue(mensagemEsperada.contains(mensagemRecebida));
    }

    @Test
    void findAllProducts() {
        List<Product> productList = new ArrayList<>();

        Warehouse warehouse = new Warehouse()
                .warehouseCode("SP")
                .warehouseName("Sao Paulo")
                .build();

        Section section = new Section()
                .sectionCode("LA")
                .sectionName("Laticionios")
                .maxLength(10)
                .warehouse(warehouse)
                .build();
        Product productUm = new Product()
                .productId("LE")
                .productName("leite")
                .section(section)
                .category(new SectionCategory().name(ESectionCategory.FF))
                .build();

        productList.add(productUm);

        Product productDois = new Product()
                .productId("LE")
                .productName("leite")
                .section(section)
                .category(new SectionCategory().name(ESectionCategory.FF))
                .build();

        productList.add(productDois);
        when(mockProductRepository.findAll())
                .thenReturn(productList);

        assertFalse(productService.findAllProducts().isEmpty());
    }


}