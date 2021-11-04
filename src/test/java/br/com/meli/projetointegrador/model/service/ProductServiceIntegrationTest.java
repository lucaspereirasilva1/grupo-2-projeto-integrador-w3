package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.exception.ProductException;
import br.com.meli.projetointegrador.model.entity.Product;
import br.com.meli.projetointegrador.model.entity.Section;
import br.com.meli.projetointegrador.model.entity.SectionCategory;
import br.com.meli.projetointegrador.model.entity.Warehouse;
import br.com.meli.projetointegrador.model.enums.ESectionCategory;
import br.com.meli.projetointegrador.model.repository.ProductRepository;
import br.com.meli.projetointegrador.model.repository.SectionCategoryRepository;
import br.com.meli.projetointegrador.model.repository.SectionRepository;
import br.com.meli.projetointegrador.model.repository.WarehouseRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Jhony Zuim / Lucas Pereira / Edmilson Nobre / Rafael Vicente
 * @version 1.0.0
 * @since 15/10/2021
 * Camada de teste integrado do service responsavel pela regra de negocio relacionada ao product
 */

@SpringBootTest
public class ProductServiceIntegrationTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private SectionCategoryRepository sectionCategoryRepository;

    @BeforeEach
    void setUp(){
        clearBase();

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

        Section sectionDois = new Section()
                .sectionCode("FR")
                .sectionName("Frios")
                .warehouse(warehouse)
                .maxLength(10)
                .build();

        SectionCategory sectionCategory = new SectionCategory()
                .name(ESectionCategory.FF)
                .build();
        sectionCategoryRepository.save(sectionCategory);

        Product product = new Product()
                .productId("LE")
                .productName("Leite")
                .section(section)
                .productPrice(new BigDecimal("2.0"))
                .dueDate(LocalDate.now())
                .category(sectionCategory)
                .build();

        sectionRepository.saveAll(Arrays.asList(section, sectionDois));
        productRepository.save(product);
        warehouseRepository.save(warehouse);
    }

    @AfterEach
    void cleanUpDataBase(){
        clearBase();
    }

    @Test
    void validProductExistTest(){
        assertTrue(productService.validProductSection("LA"));
    }
    @Test
    void validProductNotExistTest(){
        ProductException productException = assertThrows(ProductException.class, () ->
                productService.validProductSection("FR"));

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

        Product productReturn = productService.find(product.getProductId());

        assertEquals("LE", productReturn.getProductId());
    }

    @Test
    void findNotExistTest() {
        Product product = new Product();

        ProductException productException = assertThrows(ProductException.class, () ->
                productService.find(product.getProductId()));

        String expectedMessage = "Produto nao cadastrado!!! Por gentileza cadastrar";

        assertTrue(expectedMessage.contains(productException.getMessage()));
    }

    @Test
    void validListProductByCategoryTest(){
        assertEquals(productService.listProdutcByCategory(ESectionCategory.FF.toString()).size(), 1);
    }

    void clearBase() {
        sectionRepository.deleteAll();
        productRepository.deleteAll();
        warehouseRepository.deleteAll();
    }
}
