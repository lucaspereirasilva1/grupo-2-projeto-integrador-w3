package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.model.entity.Product;
import br.com.meli.projetointegrador.model.entity.Promo;
import br.com.meli.projetointegrador.model.entity.SectionCategory;
import br.com.meli.projetointegrador.model.enums.ESectionCategory;
import br.com.meli.projetointegrador.model.repository.PromoRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PromoServiceTest {

    private final ProductService mockProductService = mock(ProductService.class);
    private final PromoRepository mockPromoRepository = mock(PromoRepository.class);
    private final PromoService promoService = new PromoService(mockProductService, mockPromoRepository);

    @Test
    void apllyPromoTenPercentTest() {
        Product product = new Product()
                .productId("CA1")
                .productName("carne")
                .category(new SectionCategory().name(ESectionCategory.FF))
                .productPrice(new BigDecimal("45.0"))
                .dueDate(LocalDate.now().plusWeeks(1))
                .build();

        when(mockProductService.find(anyString()))
                .thenReturn(product);
        when(mockPromoRepository.save(any(Promo.class)))
                .thenReturn(new Promo()
                        .productId(product.getProductId())
                        .productDueDate(product.getDueDate())
                        .originalValue(product.getProductPrice())
                        .percentDiscount(0.15)
                        .finalValue(new BigDecimal("38.250"))
                        .build());

        BigDecimal pricePromo =  promoService.apllyPromo("CA1");
        assertEquals(new BigDecimal("38.250"), pricePromo);
    }

}