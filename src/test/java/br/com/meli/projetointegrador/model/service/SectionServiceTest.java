package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.exception.SectionException;
import br.com.meli.projetointegrador.model.entity.Product;
import br.com.meli.projetointegrador.model.entity.Section;
import br.com.meli.projetointegrador.model.entity.Warehouse;
import br.com.meli.projetointegrador.model.repository.ProductRepository;
import br.com.meli.projetointegrador.model.repository.SectionRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SectionServiceTest {

    private SectionRepository mockSectionRepository = mock(SectionRepository.class);
    private ProductRepository mockProductRepository = mock(ProductRepository.class);
    private SectionService sectionService = new SectionService(mockSectionRepository);
    private ProductService productService = new ProductService(mockProductRepository);

    @Test
    void validSectionExistTest(){

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

        when(mockSectionRepository.findById(any()))
                .thenReturn(Optional.of(new Section()
                        .id("1")
                        .sectionCode("LA")
                        .sectionName("Laticionios")
                        .maxLength(10)
                        .warehouse(warehouse)
                        .build()));

        assertTrue(sectionService.validSection(section));
    }

    @Test
    void validSectionNotExistTest(){

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

        when(mockSectionRepository.findById(any()))
                .thenReturn(Optional.empty());

        SectionException sectionException = assertThrows(SectionException.class, () ->
                sectionService.validSection(section));

        String expectedMessage = "Nao existe esse setor, por gentileza verificar o setor!";

        assertTrue(expectedMessage.contains(sectionException.getMessage()));
    }

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


}
