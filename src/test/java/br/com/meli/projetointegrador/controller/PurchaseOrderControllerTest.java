package br.com.meli.projetointegrador.controller;

import br.com.meli.projetointegrador.model.dto.LoginRequest;
import br.com.meli.projetointegrador.model.dto.ProductPurchaseOrderDTO;
import br.com.meli.projetointegrador.model.dto.PurchaseOrderDTO;
import br.com.meli.projetointegrador.model.dto.SignupRequest;
import br.com.meli.projetointegrador.model.entity.*;
import br.com.meli.projetointegrador.model.enums.EOrderStatus;
import br.com.meli.projetointegrador.model.enums.ERole;
import br.com.meli.projetointegrador.model.enums.ESectionCategory;
import br.com.meli.projetointegrador.model.repository.*;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureDataMongo
public class PurchaseOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BatchStockRepository batchStockRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private SectionCategoryRepository sectionCategoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BuyerRepository buyerRepository;

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private TokenTest tokenTest = new TokenTest();

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @BeforeEach
    void setup() throws Exception {
        Role role = new Role();
        role.setName(ERole.ROLE_USER);
        roleRepository.save(role);

        Role role2 = new Role();
        role.setName(ERole.ROLE_MODERATOR);
        roleRepository.save(role2);

        Role role3 = new Role();
        role.setName(ERole.ROLE_ADMIN);
        roleRepository.save(role3);

        Set<String> roles = new HashSet<>();
        roles.add(ERole.ROLE_USER.toString());
        roles.add(ERole.ROLE_ADMIN.toString());
        roles.add(ERole.ROLE_MODERATOR.toString());
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername("lucas");
        signupRequest.setEmail("lucas@gmail.com");
        signupRequest.setPassword("12345678");
        signupRequest.setRole(roles);
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

    @Test
    void postControllerTest() throws Exception {
        Warehouse warehouse = new Warehouse()
                .warehouseCode("SP")
                .warehouseName("Sao Paulo")
                .build();
        warehouseRepository.save(warehouse);

        Section section = new Section()
                .sectionCode("LA")
                .sectionName("Laticionios")
                .warehouse(warehouse)
                .maxLength(10)
                .build();
        sectionRepository.save(section);

        Buyer buyer = new Buyer()
                .name("lucas")
                .cpf("22233344411")
                .build();
        buyerRepository.save(buyer);

        SectionCategory sectionCategory = new SectionCategory()
                .name(ESectionCategory.FF)
                .build();
        sectionCategoryRepository.save(sectionCategory);

        Product product = new Product()
                .productId("LE")
                .productName("leite")
                .section(section)
                .productPrice(new BigDecimal(2))
                .dueDate(LocalDate.of(2021,11,30))
                .category(sectionCategory)
                .build();

        Product productUm = new Product()
                .productId("QJ")
                .productName("queijo")
                .section(section)
                .productPrice(new BigDecimal(3))
                .dueDate(LocalDate.of(2021,11,30))
                .category(sectionCategory)
                .build();
        productRepository.saveAll(Arrays.asList(product, productUm));

        ProductPurchaseOrderDTO productPurchaseOrderDTO1 = new ProductPurchaseOrderDTO()
                .productId("LE")
                .quantity(5)
                .build();
        ProductPurchaseOrderDTO productPurchaseOrderDTO2 = new ProductPurchaseOrderDTO()
                .productId("QJ")
                .quantity(3)
                .build();

        PurchaseOrderDTO purchaseOrderDTO = new PurchaseOrderDTO()
                .data(LocalDate.now())
                .buyerId(buyer.getId())
                .orderStatus(EOrderStatus.IN_PROGRESS)
                .listProductPurchaseOrderDTO(Arrays.asList(productPurchaseOrderDTO1,
                        productPurchaseOrderDTO2));

        Agent agent = new Agent()
                .name("lucas")
                .cpf("11122233344")
                .warehouse(warehouse)
                .build();
        agentRepository.save(agent);

        BatchStock batchStock = new BatchStock()
                .batchNumber(1)
                .productId("LE")
                .currentTemperature(10.0F)
                .minimumTemperature(5.0F)
                .initialQuantity(1)
                .currentQuantity(5)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalDateTime.now())
                .dueDate(LocalDate.now())
                .agent(agent)
                .section(section)
                .build();

        BatchStock batchStockDois = new BatchStock()
                .batchNumber(1)
                .productId("QJ")
                .currentTemperature(10.0F)
                .minimumTemperature(5.0F)
                .initialQuantity(1)
                .currentQuantity(10)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalDateTime.now())
                .dueDate(LocalDate.now())
                .agent(agent)
                .section(section)
                .build();
        batchStockRepository.saveAll(Arrays.asList(batchStock, batchStockDois));

        MockHttpServletResponse response = mockMvc.perform(post("http://localhost:8080/api/v1/fresh-products/order")
                .header("Authorization", "Bearer " + tokenTest.getAccessToken())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(purchaseOrderDTO)))
                .andReturn().getResponse();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    }

    @Test
    void getControllerTest() throws Exception {
        Warehouse warehouse = new Warehouse()
                .warehouseCode("SP")
                .warehouseName("Sao Paulo")
                .build();
        warehouseRepository.save(warehouse);

        Section section = new Section()
                .sectionCode("LA")
                .sectionName("Laticionios")
                .warehouse(warehouse)
                .maxLength(10)
                .build();
        sectionRepository.save(section);

        Buyer buyer = new Buyer()
                .name("lucas")
                .cpf("22233344411")
                .build();
        buyerRepository.save(buyer);

        SectionCategory sectionCategory = new SectionCategory()
                .name(ESectionCategory.FF)
                .build();
        sectionCategoryRepository.save(sectionCategory);

        Product product = new Product()
                .productId("LE")
                .productName("leite")
                .section(section)
                .productPrice(new BigDecimal(2))
                .dueDate(LocalDate.of(2021,11,30))
                .category(sectionCategory)
                .build();

        Product productUm = new Product()
                .productId("QJ")
                .productName("queijo")
                .section(section)
                .productPrice(new BigDecimal(3))
                .dueDate(LocalDate.of(2021,11,30))
                .category(sectionCategory)
                .build();
        productRepository.saveAll(Arrays.asList(product, productUm));

        Agent agent = new Agent()
                .name("lucas")
                .cpf("11122233344")
                .warehouse(warehouse)
                .build();
        agentRepository.save(agent);

        BatchStock batchStock = new BatchStock()
                .batchNumber(1)
                .productId("LE")
                .currentTemperature(10.0F)
                .minimumTemperature(5.0F)
                .initialQuantity(1)
                .currentQuantity(5)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalDateTime.now())
                .dueDate(LocalDate.now())
                .agent(agent)
                .section(section)
                .build();

        BatchStock batchStockDois = new BatchStock()
                .batchNumber(1)
                .productId("QJ")
                .currentTemperature(10.0F)
                .minimumTemperature(5.0F)
                .initialQuantity(1)
                .currentQuantity(10)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalDateTime.now())
                .dueDate(LocalDate.now())
                .agent(agent)
                .section(section)
                .build();
        batchStockRepository.saveAll(Arrays.asList(batchStock, batchStockDois));

        PurchaseOrder purchaseOrder = new PurchaseOrder()
                .date(LocalDate.now())
                .buyer(buyer)
                .orderStatus(EOrderStatus.IN_PROGRESS)
                .productList(Arrays.asList(product,
                        productUm));
        purchaseOrderRepository.save(purchaseOrder);

        MockHttpServletResponse response = mockMvc.perform(get("http://localhost:8080/api/v1/fresh-products/order/")
                .param("id", purchaseOrder.getId())
                .header("Authorization", "Bearer " + tokenTest.getAccessToken())
                .contentType("application/json"))
                .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

}
