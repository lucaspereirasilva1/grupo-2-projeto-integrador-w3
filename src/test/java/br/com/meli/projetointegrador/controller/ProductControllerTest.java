package br.com.meli.projetointegrador.controller;

import br.com.meli.projetointegrador.model.dto.LoginRequest;
import br.com.meli.projetointegrador.model.dto.SignupRequest;
import br.com.meli.projetointegrador.model.dto.TokenTest;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureDataMongo
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RoleRepository roleRepository;

    private TokenTest tokenTest = new TokenTest();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private SectionCategoryRepository sectionCategoryRepository;

    @BeforeEach
    void setUp() throws Exception {
        clearBase();
        createData();

        Set<String> roles = new HashSet<>();
        roles.add(ERole.ROLE_USER.toString());
        roles.add(ERole.ROLE_ADMIN.toString());
        roles.add(ERole.ROLE_MODERATOR.toString());
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
    void getProductByCategory() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("http://localhost:8080/api/v1/fresh-products/list")
                .param("sectionCategory", "FF")
                .header("Authorization", "Bearer " + tokenTest.getAccessToken())
                .contentType("application/json"))
                .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    void getProductByCategoryBadRequest() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("http://localhost:8080/api/v1/fresh-products/list")
                .param("sectionCategory", "OO")
                .header("Authorization", "Bearer " + tokenTest.getAccessToken())
                .contentType("application/json"))
                .andReturn().getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    void getlistProductBylist() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("http://localhost:8080/api/v1/fresh-products/products")
                .header("Authorization", "Bearer " + tokenTest.getAccessToken())
                .contentType("application/json"))
                .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    void getlistProductBylistNotFound() throws Exception {
        productRepository.deleteAll();
        MockHttpServletResponse response = mockMvc.perform(get("http://localhost:8080/api/v1/fresh-products/products")
                .header("Authorization", "Bearer " + tokenTest.getAccessToken())
                .contentType("application/json"))
                .andReturn().getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    void createData() {
        Warehouse warehouse = new Warehouse()
                .warehouseCode("SP")
                .warehouseName("sao paulo")
                .build();
        warehouseRepository.save(warehouse);

        Section section = new Section()
                .sectionCode("LA")
                .sectionName("laticinios")
                .maxLength(10)
                .warehouse(warehouse)
                .build();
        sectionRepository.save(section);

        Agent agent = new Agent().
                cpf("33344455566").
                name("lucas").
                warehouse(warehouse).
                build();
        agentRepository.save(agent);

        SectionCategory sectionCategory = new SectionCategory()
                .name(ESectionCategory.FF)
                .build();
        sectionCategoryRepository.save(sectionCategory);

        Product product = new Product()
                .productId("LE")
                .productName("leite")
                .productPrice(new BigDecimal("2.0"))
                .dueDate(LocalDate.now())
                .category(sectionCategory)
                .build();
        productRepository.save(product);

        Role role = new Role();
        role.setName(ERole.ROLE_USER);
        roleRepository.save(role);

        Role role2 = new Role();
        role.setName(ERole.ROLE_MODERATOR);
        roleRepository.save(role2);

        Role role3 = new Role();
        role.setName(ERole.ROLE_ADMIN);
        roleRepository.save(role3);
    }

    void clearBase() {
        userRepository.deleteAll();
        productRepository.deleteAll();
        roleRepository.deleteAll();
        sectionRepository.deleteAll();
        agentRepository.deleteAll();
        warehouseRepository.deleteAll();
        sectionCategoryRepository.deleteAll();
    }
}