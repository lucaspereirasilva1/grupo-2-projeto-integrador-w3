package br.com.meli.projetointegrador.controller;

import br.com.meli.projetointegrador.model.dto.*;
import br.com.meli.projetointegrador.model.entity.*;
import br.com.meli.projetointegrador.model.enums.ERole;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

/**
 * @author Jhony Zuim / Lucas Pereira / Edmilson Nobre / Rafael Vicente
 * @version 1.0.0
 * @since 15/10/2021
 * Camada de teste do controller responsavel pela regra de negocio relacionada ao inboundOrderController
 */

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureDataMongo
@ActiveProfiles(value = "test")
public class InboundOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private InboundOrderRepository inboundOrderRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BatchStockRepository batchStockRepository;

    @Autowired
    private RoleRepository roleRepository;

    private TokenTest tokenTest = new TokenTest();

    @BeforeEach
    void setup() throws Exception{
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
    void cleanUpDatabase() {
        clearBase();
    }

    @Test
    void postTest() throws Exception {
        SectionDTO sectionDTO = new SectionDTO()
                .sectionCode("LA")
                .warehouseCode("SP")
                .build();

        BatchStockDTO batchStockDTO = new BatchStockDTO()
                .batchNumber(1)
                .productId("LE")
                .currentTemperature(10.0F)
                .minimumTemperature(5.0F)
                .initialQuantity(1)
                .currentQuantity(5)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalDateTime.now())
                .dueDate(LocalDate.now())
                .build();

        BatchStockDTO batchStockDTOUm = new BatchStockDTO()
                .batchNumber(2)
                .productId("LE")
                .currentTemperature(20.0F)
                .minimumTemperature(15.0F)
                .initialQuantity(1)
                .currentQuantity(5)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalDateTime.now())
                .dueDate(LocalDate.now())
                .build();

        InboundOrderDTO inboundOrderDTO = new InboundOrderDTO()
                .orderNumber(1)
                .orderDate(LocalDate.now())
                .sectionDTO(sectionDTO)
                .batchStockDTO(Arrays.asList(batchStockDTO, batchStockDTOUm))
                .build();

        MockHttpServletResponse response = mockMvc.perform(post("http://localhost:8080/api/v1/fresh-products/inboundorder")
                .header("Authorization", "Bearer " + tokenTest.getAccessToken())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(inboundOrderDTO)))
                .andReturn().getResponse();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    }

    @Test
    void putTest() throws Exception {
        SectionDTO sectionDTO = new SectionDTO()
                .sectionCode("LA")
                .warehouseCode("SP")
                .build();

        BatchStockDTO batchStockDTO = new BatchStockDTO()
                .batchNumber(1)
                .productId("LE")
                .currentTemperature(10.0F)
                .minimumTemperature(5.0F)
                .initialQuantity(1)
                .currentQuantity(5)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalDateTime.now())
                .dueDate(LocalDate.now())
                .build();

        BatchStockDTO batchStockDTOUm = new BatchStockDTO()
                .batchNumber(2)
                .productId("LE")
                .currentTemperature(20.0F)
                .minimumTemperature(15.0F)
                .initialQuantity(1)
                .currentQuantity(5)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalDateTime.now())
                .dueDate(LocalDate.now())
                .build();

        InboundOrderDTO inboundOrderDTO = new InboundOrderDTO()
                .orderNumber(1)
                .orderDate(LocalDate.now())
                .sectionDTO(sectionDTO)
                .batchStockDTO(Arrays.asList(batchStockDTO, batchStockDTOUm))
                .build();

        final Optional<Section> section = sectionRepository.findBySectionCode("LA");
        final Optional<Agent> agent = agentRepository.findByCpf("11122233344");

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
                .section(section.orElse(new Section()))
                .agent(agent.orElse(new Agent()))
                .build();
        batchStockRepository.save(batchStock);

        InboundOrder inboundOrder = new InboundOrder()
                .orderNumber(1)
                .orderDate(LocalDate.now())
                .section(section.orElse(new Section()))
                .listBatchStock(Collections.singletonList(batchStock))
                .build();
        inboundOrderRepository.save(inboundOrder);

        MockHttpServletResponse response = mockMvc.perform(put("http://localhost:8080/api/v1/fresh-products/inboundorder")
                .header("Authorization", "Bearer " + tokenTest.getAccessToken())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(inboundOrderDTO)))
                .andReturn().getResponse();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
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
                cpf("11122233344").
                name("lucas").
                warehouse(warehouse).
                build();
        agentRepository.save(agent);

        Product product = new Product()
                .productId("LE")
                .productName("leite")
                .section(section)
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
        sectionRepository.deleteAll();
        inboundOrderRepository.deleteAll();
        agentRepository.deleteAll();
        batchStockRepository.deleteAll();
        warehouseRepository.deleteAll();
    }

}
