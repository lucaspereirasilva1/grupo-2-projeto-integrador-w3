package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.exception.PromoException;
import br.com.meli.projetointegrador.model.dto.PromoRequestDTO;
import br.com.meli.projetointegrador.model.dto.PromoResponseDTO;
import br.com.meli.projetointegrador.model.entity.Product;
import br.com.meli.projetointegrador.model.entity.Promo;
import br.com.meli.projetointegrador.model.entity.SectionCategory;
import br.com.meli.projetointegrador.model.enums.ESectionCategory;
import br.com.meli.projetointegrador.model.repository.ProductRepository;
import br.com.meli.projetointegrador.model.repository.PromoRepository;
import br.com.meli.projetointegrador.model.repository.SectionCategoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

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

        Product productFr1 = new Product()
                .productId("FR1")
                .productName("frango")
                .category(sectionCategory)
                .productPrice(new BigDecimal("45.0"))
                .dueDate(LocalDate.now().plusWeeks(1))
                .build();
        productRepository.save(productFr1);

        Product productDois = new Product()
                .productId("CA2")
                .productName("carne")
                .category(sectionCategory)
                .productPrice(new BigDecimal("45.0"))
                .dueDate(LocalDate.now().plusWeeks(2))
                .build();
        productRepository.save(productDois);

        Product productTres = new Product()
                .productId("FR2")
                .productName("frango sadia")
                .category(sectionCategory)
                .productPrice(new BigDecimal("45.0"))
                .dueDate(LocalDate.now().plusWeeks(2))
                .build();
        productRepository.save(productTres);

        Promo promo = new Promo()
                .productId("FR1")
                .productDueDate(LocalDate.now().plusDays(5))
                .originalValue(new BigDecimal("45"))
                .percentDiscount(0.15)
                .finalValue(new BigDecimal("42.75"))
                .build();
        promoRepository.save(promo);

        Promo promoDois = new Promo()
                .productId("FR2")
                .productDueDate(LocalDate.now().plusDays(5))
                .originalValue(new BigDecimal("45"))
                .percentDiscount(0.20)
                .finalValue(new BigDecimal("42.75"))
                .build();
        promoRepository.save(promoDois);
    }

    @AfterEach
    void tearDown() {
        clearBase();
    }

    @Test
    void apllyPromoTenPercentTest() {
        BigDecimal pricePromo =  promoService.apllyPromo("CA1");
        assertEquals(new BigDecimal("42.75"), pricePromo);
        assertFalse(promoRepository.findAll().isEmpty());
        assertEquals(new BigDecimal("42.75"), productRepository.findDistinctFirstByProductId("CA1").orElse(new Product()).getProductPrice());
    }

    @Test
    void apllyPromoExceptionTest() {
        PromoException promoException = assertThrows(PromoException.class, () ->
                promoService.apllyPromo("CA2"));

        String expectedMessage = "Produto nao apto a promocao de vencimento!!!";
        assertTrue(expectedMessage.contains(promoException.getMessage()));
    }

    @Test
    void updatePromoTest() {
        PromoRequestDTO promoDTO = new PromoRequestDTO()
                .productId("FR1")
                .percentDiscount(0.25)
                .build();

        PromoResponseDTO promoResponseDTO = promoService.updatePromo(promoDTO);
        assertEquals(new BigDecimal("33.75"), promoResponseDTO.getFinalValue());
    }

    @Test
    void findTest() {
        final Promo promo = promoService.find("FR1");
        assertEquals("FR1", promo.getProductId());
    }

    @Test
    void listPromoByDiscountTest() {
        List<PromoResponseDTO> promoResponseDTOS = promoService.listByDiscount();
        assertEquals(2, promoResponseDTOS.size());
        assertEquals(0.20, promoResponseDTOS.get(0).getPercentDiscount());
    }



    private void clearBase() {
        productRepository.deleteAll();
        promoRepository.deleteAll();
        sectionCategoryRepository.deleteAll();
    }

}