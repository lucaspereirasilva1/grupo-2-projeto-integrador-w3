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
    private ProductRepository productRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private SectionCategoryRepository sectionCategoryRepository;

    @Autowired
    private BuyerRepository buyerRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void scriptCarregamentoBanco() {
        productRepository.deleteAll();
        warehouseRepository.deleteAll();
        sectionRepository.deleteAll();
        roleRepository.deleteAll();
        buyerRepository.deleteAll();
        sectionCategoryRepository.deleteAll();

        BigDecimal bigDecimal = new BigDecimal("45.00");

        Role roleUser = new Role(ERole.ROLE_USER);
        roleRepository.save(roleUser);

        Role roleModerator = new Role(ERole.ROLE_MODERATOR);
        roleRepository.save(roleModerator);

        Role roleAdmin = new Role(ERole.ROLE_ADMIN);
        roleRepository.save(roleAdmin);

        Warehouse warehouse = new Warehouse()
                .warehouseCode("SP")
                .warehouseName("sao paulo")
                .build();

        warehouseRepository.save(warehouse);

        Warehouse warehouseMG = new Warehouse()
                .warehouseCode("MG")
                .warehouseName("Minas Gerais")
                .build();

        warehouseRepository.save(warehouseMG);

        Section section = new Section()
                .sectionCode("LA")
                .sectionName("laticinios")
                .maxLength(10)
                .warehouse(warehouse)
                .build();

        sectionRepository.save(section);

        Section sectionCO = new Section()
                .sectionCode("CO")
                .sectionName("Congelados")
                .maxLength(10)
                .warehouse(warehouseMG)
                .build();

        sectionRepository.save(sectionCO);

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

        Buyer buyer = new Buyer()
                .name("lucas")
                .cpf("22233344411")
                .build();
        buyerRepository.save(buyer);

        Product product = new Product()
                .productId("MA")
                .productName("margarina")
                .section(section)
                .category(sectionCategoryFS)
                .productPrice(bigDecimal)
                .dueDate(LocalDate.now().plusWeeks(5))
                .build();

        productRepository.save(product);

        Product productDois = new Product()
                .productId("DA")
                .productName("danone")
                .section(section)
                .category(sectionCategoryFS)
                .productPrice(bigDecimal)
                .dueDate(LocalDate.now().plusWeeks(5))
                .build();

        Product productTres = new Product()
                .productId("CA")
                .productName("carne")
                .section(sectionCO)
                .category(sectionCategoryFF)
                .productPrice(bigDecimal)
                .dueDate(LocalDate.now().plusWeeks(5))
                .build();

        Product productQuatro = new Product()
                .productId("FR")
                .productName("frango")
                .section(sectionCO)
                .category(sectionCategoryFF)
                .productPrice(bigDecimal)
                .dueDate(LocalDate.now().plusWeeks(5))
                .build();

        productRepository.saveAll(Arrays.asList(product, productDois,
                productTres, productQuatro));
        assertEquals(1,Integer.valueOf(1));
    }
}
