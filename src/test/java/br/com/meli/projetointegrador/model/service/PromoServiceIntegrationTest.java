package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.exception.PromoException;
import br.com.meli.projetointegrador.model.entity.Product;
import br.com.meli.projetointegrador.model.entity.Promo;
import br.com.meli.projetointegrador.model.entity.SectionByCategory;
import br.com.meli.projetointegrador.model.entity.SectionCategory;
import br.com.meli.projetointegrador.model.enums.ESectionCategory;
import br.com.meli.projetointegrador.model.repository.ProductRepository;
import br.com.meli.projetointegrador.model.repository.PromoRepository;
import br.com.meli.projetointegrador.model.repository.SectionCategoryRepository;
import org.apache.commons.lang.text.StrBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class PromoServiceIntegrationTest {

    @Autowired
    private PromoService promoService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PromoRepository promoRepository;

    @Autowired
    private SectionCategoryRepository sectionCategoryRepository;

    @BeforeEach
    void setUp() {
        clearBase();
        SectionCategory sectionCategory = new SectionCategory()
                .name(ESectionCategory.FS)
                .build();
        sectionCategoryRepository.save(sectionCategory);

        Product product = new Product()
                .productId("CA1")
                .productName("carne")
                .category(sectionCategory)
                .productPrice(new BigDecimal("45.0"))
                .dueDate(LocalDate.now().plusWeeks(1))
                .build();
        productRepository.save(product);

        Product productDois = new Product()
                .productId("CA2")
                .productName("carne")
                .category(sectionCategory)
                .productPrice(new BigDecimal("45.0"))
                .dueDate(LocalDate.now().plusWeeks(2))
                .build();
        productRepository.save(productDois);
    }

    @AfterEach
    void tearDown() {
        clearBase();
    }

    @Test
    void apllyPromoTenPercentTest() {
        BigDecimal pricePromo =  promoService.apllyPromo("CA1");
        assertEquals(new BigDecimal("42.750"), pricePromo);
        assertFalse(promoRepository.findAll().isEmpty());
        assertEquals(new BigDecimal("42.750"), productRepository.findDistinctFirstByProductId("CA1").orElse(new Product()).getProductPrice());
    }

    @Test
    void apllyPromoExceptionTest() {
        PromoException promoException = assertThrows(PromoException.class, () ->
                promoService.apllyPromo("CA2"));

        String expectedMessage = "Produto nao apto a promocao de vencimento!!!";

        assertTrue(expectedMessage.contains(promoException.getMessage()));
    }

    private void clearBase() {
        productRepository.deleteAll();
        promoRepository.deleteAll();
        sectionCategoryRepository.deleteAll();
    }

}