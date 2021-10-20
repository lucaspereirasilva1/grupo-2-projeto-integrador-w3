package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.exception.ProductException;
import br.com.meli.projetointegrador.model.entity.Product;
import br.com.meli.projetointegrador.model.entity.Section;
import br.com.meli.projetointegrador.model.entity.Warehouse;
import br.com.meli.projetointegrador.model.repository.ProductRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Jhony Zuim
 * @version 1.0.0
 * @since 15/10/2021
 */

public class ProductServiceTest {

    private ProductRepository mockProductRepository = mock(ProductRepository.class);
    private ProductService productService = new ProductService(mockProductRepository);

    /**
     * @author Jhony Zuim
     * @version 1.0.0
     *  Teste unitario para validar se um produto corresponde a section
     */
    @Test
    void validProductSectionExistTest(){

        Warehouse warehouse = new Warehouse()
                .warehouseCode("SP")
                .warehouseName("Sao Paulo")
                .build();

        Section section = new Section()
                .id("1")
                .sectionCode("LA")
                .sectionName("Laticionios")
                .maxLength(10)
                .warehouse(warehouse)
                .build();

        Product product = new Product()
                .id("1")
                .productCode("LEI")
                .productName("Leite")
                .section(section)
                .build();

        when(mockProductRepository.findBySection(any()))
                .thenReturn(Optional.of(new Product()
                        .id("1")
                        .productCode("LEI")
                        .productName("Leite")
                        .section(section)
                        .build()));

        assertTrue(productService.validProductSection(product));
    }

    /**
     * @author Jhony Zuim
     * @version 1.0.0
     *  Teste para validar se uma produto nao corresponde a section
     */

    @Test
    void validProductSectionNotExistTest() {

        Warehouse warehouse = new Warehouse()
                .warehouseCode("SP")
                .warehouseName("Sao Paulo")
                .build();

        Section section = new Section()
                .id("1")
                .sectionCode("LA")
                .sectionName("Laticionios")
                .maxLength(10)
                .warehouse(warehouse)
                .build();

        Product product = new Product()
                .id("1")
                .productCode("LEI")
                .productName("Leite")
                .section(section)
                .build();

        when(mockProductRepository.findBySection(any()))
                .thenReturn(Optional.empty());

        ProductException productException = assertThrows(ProductException.class, () ->
                productService.validProductSection(product));

        String expectedMessage = "Produto nao faz parte do setor, por favor verifique o setor correto!";

        assertTrue(expectedMessage.contains(productException.getMessage()));
    }
}