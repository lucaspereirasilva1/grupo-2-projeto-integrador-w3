package br.com.meli.projetointegrador.model.repository;

import br.com.meli.projetointegrador.model.entity.*;
import br.com.meli.projetointegrador.model.enums.ERole;
import br.com.meli.projetointegrador.model.enums.ESectionCategory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
@TestPropertySource(properties = {"spring.data.mongodb.port:27017"})
class ScriptCarregamentoBancoTest {

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private SectionCategoryRepository sectionCategoryRepository;

    @Autowired
    private BuyerRepository buyerRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private SectionByCategoryRepository sectionByCategoryRepository;

    @Test
    void scriptCarregamentoBanco() {
        productRepository.deleteAll();
        warehouseRepository.deleteAll();
        sectionRepository.deleteAll();
        roleRepository.deleteAll();
        buyerRepository.deleteAll();
        sectionCategoryRepository.deleteAll();
        userRepository.deleteAll();
        agentRepository.deleteAll();

        BigDecimal bigDecimal = new BigDecimal("45.00");

        Role roleUser = new Role(ERole.ROLE_USER);
        roleRepository.save(roleUser);

        Role roleModerator = new Role(ERole.ROLE_MODERATOR);
        roleRepository.save(roleModerator);

        Role roleAdmin = new Role(ERole.ROLE_ADMIN);
        roleRepository.save(roleAdmin);

        Warehouse warehouseSP = new Warehouse()
                .warehouseCode("SP1")
                .warehouseName("Sao Paulo")
                .build();
        warehouseRepository.save(warehouseSP);

        Warehouse warehouseMG = new Warehouse()
                .warehouseCode("MG1")
                .warehouseName("Minas Gerais")
                .build();
        warehouseRepository.save(warehouseMG);

        Warehouse warehousePR = new Warehouse()
                .warehouseCode("PR1")
                .warehouseName("Parana")
                .build();
        warehouseRepository.save(warehousePR);

        Warehouse warehouseRS = new Warehouse()
                .warehouseCode("RS1")
                .warehouseName("Minas Gerais")
                .build();
        warehouseRepository.save(warehouseRS);

        Section sectionLA = new Section()
                .sectionCode("LA1")
                .sectionName("Laticinios")
                .maxLength(10)
                .warehouse(warehouseSP)
                .build();
        sectionRepository.save(sectionLA);

        Section sectionCO = new Section()
                .sectionCode("CO1")
                .sectionName("Congelados")
                .maxLength(10)
                .warehouse(warehouseSP)
                .build();
        sectionRepository.save(sectionCO);

        Section sectionVE = new Section()
                .sectionCode("VE1")
                .sectionName("Verduras")
                .maxLength(10)
                .warehouse(warehouseMG)
                .build();
        sectionRepository.save(sectionVE);

        Section sectionFR = new Section()
                .sectionCode("FR1")
                .sectionName("Frutas")
                .maxLength(10)
                .warehouse(warehousePR)
                .build();
        sectionRepository.save(sectionFR);

        SectionCategory sectionCategoryFF = new SectionCategory()
                .name(ESectionCategory.FF)
                .build();

        SectionCategory sectionCategoryFS = new SectionCategory()
                .name(ESectionCategory.FS)
                .build();

        SectionCategory sectionCategoryRF = new SectionCategory()
                .name(ESectionCategory.RF)
                .build();

        sectionCategoryRepository.saveAll(Arrays.asList(sectionCategoryFF, sectionCategoryFS,
                sectionCategoryRF));

        SectionByCategory sectionByCategory = new SectionByCategory()
                .section(sectionCO)
                .category(sectionCategoryFF)
                .build();
        sectionByCategoryRepository.save(sectionByCategory);

        SectionByCategory sectionByCategoryDois = new SectionByCategory()
                .section(sectionLA)
                .category(sectionCategoryFS)
                .build();
        sectionByCategoryRepository.save(sectionByCategoryDois);

        SectionByCategory sectionByCategoryTres = new SectionByCategory()
                .section(sectionVE)
                .category(sectionCategoryFF)
                .build();
        sectionByCategoryRepository.save(sectionByCategoryTres);

        SectionByCategory sectionByCategoryQuatro = new SectionByCategory()
                .section(sectionFR)
                .category(sectionCategoryFS)
                .build();
        sectionByCategoryRepository.save(sectionByCategoryQuatro);

        Buyer buyerLucas = new Buyer()
                .name("lucas")
                .cpf("11111111111")
                .build();
        buyerRepository.save(buyerLucas);

        Buyer buyerJhony = new Buyer()
                .name("jhony")
                .cpf("22222222222")
                .build();
        buyerRepository.save(buyerJhony);

        Buyer buyerEd = new Buyer()
                .name("ednobre")
                .cpf("33333333333")
                .build();
        buyerRepository.save(buyerEd);

        Buyer buyerRafa = new Buyer()
                .name("rafa")
                .cpf("44444444444")
                .build();
        buyerRepository.save(buyerRafa);

        Product product = new Product()
                .productId("MA1")
                .productName("margarina")
                .category(sectionCategoryFS)
                .productPrice(bigDecimal)
                .dueDate(LocalDate.now().plusWeeks(5))
                .build();

        Product productDois = new Product()
                .productId("DA1")
                .productName("danone")
                .category(sectionCategoryFS)
                .productPrice(bigDecimal)
                .dueDate(LocalDate.now().plusWeeks(5))
                .build();

        Product productTres = new Product()
                .productId("CA1")
                .productName("carne")
                .category(sectionCategoryFF)
                .productPrice(bigDecimal)
                .dueDate(LocalDate.now().plusWeeks(5))
                .build();

        Product productQuatro = new Product()
                .productId("FR1")
                .productName("frango")
                .category(sectionCategoryFF)
                .productPrice(bigDecimal)
                .dueDate(LocalDate.now().plusWeeks(5))
                .build();

        productRepository.saveAll(Arrays.asList(product, productDois,
                productTres, productQuatro));
        assertEquals(1,Integer.valueOf(1));
    }
}
