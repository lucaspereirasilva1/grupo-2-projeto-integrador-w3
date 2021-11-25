package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.exception.PromoException;
import br.com.meli.projetointegrador.model.dto.PromoFullRequestDTO;
import br.com.meli.projetointegrador.model.dto.PromoRequestDTO;
import br.com.meli.projetointegrador.model.dto.PromoResponseDTO;
import br.com.meli.projetointegrador.model.entity.*;
import br.com.meli.projetointegrador.model.enums.ERole;
import br.com.meli.projetointegrador.model.enums.ESectionCategory;
import br.com.meli.projetointegrador.model.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Lucas Pereira
 * @version 1.5.0
 * @since 25/11/2021
 * Camada de testes integrados do service responsavel pelas promocoes
 */

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

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

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

        Set<Role> roles = new HashSet<>();
        Role role = new Role();
        role.setName(ERole.ROLE_ADMIN);
        roleRepository.save(role);
        roles.add(role);

        User user = new User();
        user.setUsername("lucas");
        user.setRoles(roles);
        user.setEmail("teste@gmail.com");
        user.setPassword("123");
        user.setCpf("11122233344");
        userRepository.save(user);
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

    @Test
    void apllyPromoFullTest() {
        PromoFullRequestDTO promoFullRequestDTO = new PromoFullRequestDTO()
                .productId("CA1")
                .cpf("11122233344")
                .percent(0.50)
                .build();

        Set<Role> roles = new HashSet<>();
        Role role = new Role();
        role.setName(ERole.ROLE_ADMIN);
        roles.add(role);
        User user = new User();
        user.setUsername("lucas");
        user.setRoles(roles);
        user.setEmail("teste@gmail.com");
        user.setPassword("123");
        user.setCpf("11122233344");

        BigDecimal pricePromo = promoService.apllyPromoFull(promoFullRequestDTO);
        assertEquals(new BigDecimal("22.50"), pricePromo);
    }

    private void clearBase() {
        productRepository.deleteAll();
        promoRepository.deleteAll();
        sectionCategoryRepository.deleteAll();
        roleRepository.deleteAll();
        userRepository.deleteAll();
    }

}