package br.com.meli.projetointegrador.controller;

import br.com.meli.projetointegrador.model.dto.*;
import br.com.meli.projetointegrador.model.entity.*;
import br.com.meli.projetointegrador.model.enums.ERole;
import br.com.meli.projetointegrador.model.enums.ESectionCategory;
import br.com.meli.projetointegrador.model.repository.*;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureDataMongo
public class PromoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RoleRepository roleRepository;

    private TokenTest tokenTest = new TokenTest();

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PromoRepository promoRepository;

    @Autowired
    private SectionCategoryRepository sectionCategoryRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @BeforeEach
    void setUp() throws Exception {
        clearBase();
        createData();

        Set<String> roles = new HashSet<>();
        roles.add("admin");
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername("lucas");
        signupRequest.setEmail("lucas@gmail.com");
        signupRequest.setPassword("12345678");
        signupRequest.setRole(roles);
        signupRequest.setCpf("11122233344");
        signupRequest.setWarehouseCode("SP");
        mockMvc.perform(post("http://localhost:8080/api/auth/signup")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(signupRequest)))
                .andReturn().getResponse();

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("lucas");
        loginRequest.setPassword("12345678");
        MockHttpServletResponse responseSignin = mockMvc.perform(post("http://localhost:8080/api/auth/signin")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andReturn().getResponse();
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        String tokenNoFormat = responseSignin.getContentAsString();
        tokenTest = objectMapper.readValue(tokenNoFormat, TokenTest.class);
    }

    @AfterEach
    void tearDown() {
        clearBase();
    }

    @Test
    void apllyPromoControllerTest() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(post("http://localhost:8080/api/v1/fresh-products/promocomingdue/")
                .param("productId", "CA1")
                .header("Authorization", "Bearer " + tokenTest.getAccessToken())
                .contentType("application/json"))
                .andReturn().getResponse();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    }

    @Test
    void updatePromoControllerTest() throws Exception {
        PromoRequestDTO promoDTO = new PromoRequestDTO()
                .productId("FR1")
                .percentDiscount(0.25)
                .build();

        MockHttpServletResponse response = mockMvc.perform(put("http://localhost:8080/api/v1/fresh-products/updatepromo/")
                .header("Authorization", "Bearer " + tokenTest.getAccessToken())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(promoDTO)))
                .andReturn().getResponse();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    }

    @Test
    void listPromoByDiscountControllerTest() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("http://localhost:8080/api/v1/fresh-products/listbydiscount")
                .header("Authorization", "Bearer " + tokenTest.getAccessToken())
                .contentType("application/json"))
                .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    void applyPromoFullControllerTest() throws Exception {
        PromoFullRequestDTO promoFullRequestDTO = new PromoFullRequestDTO()
                .productId("CA1")
                .cpf("11122233344")
                .percent(0.50)
                .build();

        MockHttpServletResponse response = mockMvc.perform(put("http://localhost:8080/api/v1/fresh-products/promofull/")
                .header("Authorization", "Bearer " + tokenTest.getAccessToken())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(promoFullRequestDTO)))
                .andReturn().getResponse();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    }

    private void createData() {
        SectionCategory sectionCategory = new SectionCategory()
                .name(ESectionCategory.FS)
                .build();
        sectionCategoryRepository.save(sectionCategory);

        Warehouse warehouse = new Warehouse()
                .warehouseCode("SP")
                .warehouseName("sao paulo")
                .build();
        warehouseRepository.save(warehouse);

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

        Product productTres = new Product()
                .productId("FR1")
                .productName("frango")
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

        Role role = new Role();
        role.setName(ERole.ROLE_USER);
        roleRepository.save(role);

        Role role2 = new Role();
        role2.setName(ERole.ROLE_MODERATOR);
        roleRepository.save(role2);

        Role role3 = new Role();
        role3.setName(ERole.ROLE_ADMIN);
        roleRepository.save(role3);
    }

    private void clearBase() {
        productRepository.deleteAll();
        promoRepository.deleteAll();
        sectionCategoryRepository.deleteAll();
        warehouseRepository.deleteAll();
    }


}
