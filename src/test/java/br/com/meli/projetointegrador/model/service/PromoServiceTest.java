package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.exception.PromoException;
import br.com.meli.projetointegrador.model.dto.PromoRequestDTO;
import br.com.meli.projetointegrador.model.dto.PromoResponseDTO;
import br.com.meli.projetointegrador.model.entity.BatchStock;
import br.com.meli.projetointegrador.model.entity.Product;
import br.com.meli.projetointegrador.model.entity.Promo;
import br.com.meli.projetointegrador.model.entity.SectionCategory;
import br.com.meli.projetointegrador.model.enums.ESectionCategory;
import br.com.meli.projetointegrador.model.repository.PromoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PromoServiceTest {

    private final ProductService mockProductService = mock(ProductService.class);
    private final PromoRepository mockPromoRepository = mock(PromoRepository.class);
    private final PromoService promoService = new PromoService(mockProductService, mockPromoRepository);

    @Test
    void apllyPromoFivePercentTest() {
        Product product = new Product()
                .productId("CA1")
                .productName("carne")
                .category(new SectionCategory().name(ESectionCategory.FF))
                .productPrice(new BigDecimal("45.0"))
                .dueDate(LocalDate.now().plusWeeks(1))
                .build();

        when(mockProductService.find(anyString()))
                .thenReturn(product);
        doNothing().when(mockProductService).save(any(Product.class));
        when(mockPromoRepository.save(any(Promo.class)))
                .thenReturn(new Promo()
                        .productId(product.getProductId())
                        .productDueDate(product.getDueDate())
                        .originalValue(product.getProductPrice())
                        .percentDiscount(0.05)
                        .finalValue(new BigDecimal("42.75"))
                        .build());
        BigDecimal pricePromo =  promoService.apllyPromo("CA1");
        assertEquals(new BigDecimal("42.75"), pricePromo);
    }

    @Test
    void apllyPromoTeenFivePercentTest() {
        Product product = new Product()
                .productId("CA1")
                .productName("carne")
                .category(new SectionCategory().name(ESectionCategory.FF))
                .productPrice(new BigDecimal("45.0"))
                .dueDate(LocalDate.now().plusDays(5))
                .build();

        when(mockProductService.find(anyString()))
                .thenReturn(product);
        doNothing().when(mockProductService).save(any(Product.class));
        when(mockPromoRepository.save(any(Promo.class)))
                .thenReturn(new Promo()
                        .productId(product.getProductId())
                        .productDueDate(product.getDueDate())
                        .originalValue(product.getProductPrice())
                        .percentDiscount(0.15)
                        .finalValue(new BigDecimal("38.25"))
                        .build());

        BigDecimal pricePromo =  promoService.apllyPromo("CA1");
        assertEquals(new BigDecimal("38.25"), pricePromo);
    }

    @Test
    void apllyPromoTwentyFivePercentTest() {
        Product product = new Product()
                .productId("CA1")
                .productName("carne")
                .category(new SectionCategory().name(ESectionCategory.FF))
                .productPrice(new BigDecimal("45.0"))
                .dueDate(LocalDate.now().plusDays(3))
                .build();

        when(mockProductService.find(anyString()))
                .thenReturn(product);
        doNothing().when(mockProductService).save(any(Product.class));
        when(mockPromoRepository.save(any(Promo.class)))
                .thenReturn(new Promo()
                        .productId(product.getProductId())
                        .productDueDate(product.getDueDate())
                        .originalValue(product.getProductPrice())
                        .percentDiscount(0.25)
                        .finalValue(new BigDecimal("33.75"))
                        .build());

        BigDecimal pricePromo =  promoService.apllyPromo("CA1");
        assertEquals(new BigDecimal("33.75"), pricePromo);
    }

    @Test
    void apllyPromoExceptionTest() {
        Product product = new Product()
                .productId("CA1")
                .productName("carne")
                .category(new SectionCategory().name(ESectionCategory.FF))
                .productPrice(new BigDecimal("45.0"))
                .dueDate(LocalDate.now().plusWeeks(2))
                .build();

        when(mockProductService.find(anyString()))
                .thenReturn(product);
        doNothing().when(mockProductService).save(any(Product.class));

        PromoException promoException = assertThrows(PromoException.class, () ->
                promoService.apllyPromo("CA1"));

        String expectedMessage = "Produto nao apto a promocao de vencimento!!!";

        assertTrue(expectedMessage.contains(promoException.getMessage()));
    }

    @Test
    void updatePromoTest() {
        PromoRequestDTO promoDTO = new PromoRequestDTO()
                .productId("CA1")
                .percentDiscount(0.25)
                .build();

        Promo promo = new Promo()
                .productId("CA1")
                .productDueDate(LocalDate.now().plusDays(5))
                .originalValue(new BigDecimal("45"))
                .percentDiscount(0.15)
                .finalValue(new BigDecimal("42.75"))
                .build();

        Product product = new Product()
                .productId("CA1")
                .productName("carne")
                .category(new SectionCategory().name(ESectionCategory.FF))
                .productPrice(new BigDecimal("45.0"))
                .dueDate(LocalDate.now().plusDays(5))
                .build();

        when(mockProductService.find(anyString()))
                .thenReturn(product);
        when(mockPromoRepository.findByProductId(anyString()))
                .thenReturn(Optional.of(promo));
        doNothing().when(mockProductService).save(any(Product.class));

        PromoResponseDTO promoResponseDTO = promoService.updatePromo(promoDTO);
        assertEquals(new BigDecimal("33.75"), promoResponseDTO.getFinalValue());
    }

    @Test
    void updatePromoExceptionProductTest() {
        Promo promo = new Promo()
                .productId("CA1")
                .productDueDate(LocalDate.now().plusDays(5))
                .originalValue(new BigDecimal("45"))
                .percentDiscount(0.15)
                .finalValue(new BigDecimal("42.75"))
                .build();

        Product product = new Product()
                .productId("CA1")
                .productName("carne")
                .category(new SectionCategory().name(ESectionCategory.FF))
                .productPrice(new BigDecimal("45"))
                .dueDate(LocalDate.now().plusDays(5))
                .build();

        when(mockProductService.find(anyString()))
                .thenReturn(product);
        when(mockPromoRepository.findByProductId(anyString()))
                .thenReturn(Optional.of(promo));
        doThrow(new DataAccessException("") {
        }).when(mockProductService).save(product);

        DataAccessException dataAccessException = assertThrows
                (DataAccessException.class,() ->
                        promoService.apllyPromo("CA1"));

        String menssagemEsperada = "Erro durante a persistencia no banco!!!";

        assertTrue(menssagemEsperada.contains(Objects.requireNonNull(dataAccessException.getMessage())));
    }

    @Test
    void updatePromoExceptionPromoTest() {
        Promo promo = new Promo()
                .productId("CA1")
                .productDueDate(LocalDate.now().plusDays(5))
                .originalValue(new BigDecimal("45"))
                .percentDiscount(0.15)
                .finalValue(new BigDecimal("42.75"))
                .build();

        Product product = new Product()
                .productId("CA1")
                .productName("carne")
                .category(new SectionCategory().name(ESectionCategory.FF))
                .productPrice(new BigDecimal("45"))
                .dueDate(LocalDate.now().plusDays(5))
                .build();

        when(mockProductService.find(anyString()))
                .thenReturn(product);
        when(mockPromoRepository.findByProductId(anyString()))
                .thenReturn(Optional.of(promo));
        when(mockPromoRepository.save(any(Promo.class)))
                .thenThrow(new DataAccessException("") {
                });

        DataAccessException dataAccessException = assertThrows
                (DataAccessException.class,() ->
                        promoService.apllyPromo("CA1"));

        String menssagemEsperada = "Erro durante a persistencia no banco!!!";

        assertTrue(menssagemEsperada.contains(Objects.requireNonNull(dataAccessException.getMessage())));
    }

    @Test
    void updatePromoExceptionTest() {
        PromoRequestDTO promoDTO = new PromoRequestDTO()
                .productId("CA1")
                .percentDiscount(0.25)
                .build();

        Promo promo = new Promo()
                .productId("CA1")
                .productDueDate(LocalDate.now().plusDays(5))
                .originalValue(new BigDecimal("45"))
                .percentDiscount(0.15)
                .finalValue(new BigDecimal("42.75"))
                .build();

        Product product = new Product()
                .productId("CA1")
                .productName("carne")
                .category(new SectionCategory().name(ESectionCategory.FF))
                .productPrice(new BigDecimal("45.0"))
                .dueDate(LocalDate.now().plusDays(5))
                .build();

        when(mockProductService.find(anyString()))
                .thenReturn(product);
        when(mockPromoRepository.findByProductId(anyString()))
                .thenReturn(Optional.of(promo));
        doThrow(new DataAccessException("") {
        }).when(mockProductService).save(product);

        DataAccessException dataAccessException = assertThrows
                (DataAccessException.class,() ->
                        promoService.updatePromo(promoDTO));

        String menssagemEsperada = "Erro durante a persistencia no banco!!!";

        assertTrue(menssagemEsperada.contains(Objects.requireNonNull(dataAccessException.getMessage())));
    }

    @Test
    void updatePromoExceptionPromoSaveTest() {
        PromoRequestDTO promoDTO = new PromoRequestDTO()
                .productId("CA1")
                .percentDiscount(0.25)
                .build();

        Promo promo = new Promo()
                .productId("CA1")
                .productDueDate(LocalDate.now().plusDays(5))
                .originalValue(new BigDecimal("45"))
                .percentDiscount(0.15)
                .finalValue(new BigDecimal("42.75"))
                .build();

        Product product = new Product()
                .productId("CA1")
                .productName("carne")
                .category(new SectionCategory().name(ESectionCategory.FF))
                .productPrice(new BigDecimal("45.0"))
                .dueDate(LocalDate.now().plusDays(5))
                .build();

        when(mockProductService.find(anyString()))
                .thenReturn(product);
        when(mockPromoRepository.findByProductId(anyString()))
                .thenReturn(Optional.of(promo));
        when(mockPromoRepository.save(any(Promo.class)))
                .thenThrow(new DataAccessException("") {
                });

        DataAccessException dataAccessException = assertThrows
                (DataAccessException.class,() ->
                        promoService.updatePromo(promoDTO));

        String menssagemEsperada = "Erro durante a persistencia no banco!!!";

        assertTrue(menssagemEsperada.contains(Objects.requireNonNull(dataAccessException.getMessage())));
    }

    @Test
    void findExceptionTest() {
        when(mockPromoRepository.findByProductId(anyString()))
                .thenReturn(Optional.empty());

        PromoException promoException = assertThrows
                (PromoException.class,() ->
                        promoService.find("CA1"));

        String menssagemEsperada = "Codigo do produto invalido!!!";

        assertTrue(menssagemEsperada.contains(Objects.requireNonNull(promoException.getMessage())));
    }

    @Test
    void updatePromoInvalidDiscountTest() {
        PromoRequestDTO promoDTO = new PromoRequestDTO()
                .productId("CA1")
                .percentDiscount(0.01)
                .build();

        PromoException promoException = assertThrows
                (PromoException.class,() ->
                        promoService.updatePromo(promoDTO));

        String menssagemEsperada = "Valor do desconto de vencimento deve ser 0.05, 0.15 ou 0.25!!!";

        assertTrue(menssagemEsperada.contains(Objects.requireNonNull(promoException.getMessage())));
    }

    @Test
    void listPromoByDiscountTest() {
        Promo promo = new Promo()
                .productId("CA1")
                .productDueDate(LocalDate.now().plusDays(5))
                .originalValue(new BigDecimal("45"))
                .percentDiscount(0.15)
                .finalValue(new BigDecimal("42.75"))
                .build();

        Promo promoDois = new Promo()
                .productId("FR1")
                .productDueDate(LocalDate.now().plusDays(5))
                .originalValue(new BigDecimal("45"))
                .percentDiscount(0.20)
                .finalValue(new BigDecimal("42.75"))
                .build();

        when(mockPromoRepository.findAll())
                .thenReturn(Arrays.asList(promo, promoDois));

        List<PromoResponseDTO> promoResponseDTOS = promoService.listByDiscount();
        assertEquals(2, promoResponseDTOS.size());
        assertEquals(0.20, promoResponseDTOS.get(0).getPercentDiscount());
    }

    @Test
    void listPromoByDiscountExceptionTest() {
        when(mockPromoRepository.findAll())
                .thenReturn(Collections.emptyList());

        PromoException promoException = assertThrows
                (PromoException.class, promoService::listByDiscount);

        String menssagemEsperada = "Nenhuma promocao de vencimento cadastrada!!!";
        assertTrue(menssagemEsperada.contains(Objects.requireNonNull(promoException.getMessage())));
    }

}