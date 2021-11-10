package br.com.meli.projetointegrador.model.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

/**
 * @author Jhony Zuim / Lucas Pereira / Edmilson Nobre / Rafael Vicente
 * @version 1.0.0
 * @since 15/10/2021
 * Repository de teste para trabalhar como uma porta ou janela de acesso a camada do banco
 */

@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
public class SaveDataRepositoryTest {

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private SectionCategoryRepository sectionCategoryRepository;

    @Autowired
    private BuyerRepository buyerRepository;

    @Autowired
    private InboundOrderRepository inboundOrderRepository;

    @Autowired
    private BatchStockRepository batchStockRepository;

    @Autowired
    private RoleRepository roleRepository;

//    @Test
//    void saveProduct() {
//        productRepository.deleteAll();
//
//        List<Product> listProduct = new ArrayList<>();
//        final Optional<Section> section = sectionRepository.findBySectionCode("LA");
//
//        SectionCategory sectionCategory = new SectionCategory()
//                .name(ESectionCategory.FF)
//                .build();
//
//        Product product = new Product()
//                .productId("LE")
//                .productName("leite")
//                .section(section.orElse(new Section()))
//                .category(sectionCategory)
//                .dueDate(LocalDate.now())
//                .productPrice(new BigDecimal("2.0"))
//                .build();
//
//        Product productUm = new Product()
//                .productId("QJ")
//                .productName("queijo")
//                .section(section.orElse(new Section()))
//                .category(sectionCategory)
//                .dueDate(LocalDate.now())
//                .productPrice(new BigDecimal("2.0"))
//                .build();
//
//        listProduct.add(product);
//        listProduct.add(productUm);
//
//        productRepository.saveAll(listProduct);
//    }
//
//    @Test
//    void saveSectionCategory() {
//        SectionCategory sectionCategory = new SectionCategory()
//                .name(ESectionCategory.FF)
//                .build();
//        sectionCategoryRepository.save(sectionCategory);
//    }
//
//    @Test
//    void savePurchaseOrder() {
//        purchaseOrderRepository.deleteAll();
//        final List<Product> listProduct = productRepository.findAll();
//        final Optional<Buyer> buyer = buyerRepository.findByCpf("22233344411");
//
//        PurchaseOrder purchaseOrder = new PurchaseOrder()
//                .date(LocalDate.now())
//                .buyer(buyer.orElse(new Buyer()))
//                .orderStatus(EOrderStatus.ORDER_CHART)
//                .productList(listProduct)
//                .build();
//
//        purchaseOrderRepository.save(purchaseOrder);
//    }
//
//    @Test
//    void saveBuyer() {
//        Buyer buyer = new Buyer()
//                .name("lucas")
//                .cpf("22233344411")
//                .build();
//        buyerRepository.save(buyer);
//    }

//    @Test
//    void scriptCarregamentoBanco() {
//        productRepository.deleteAll();
//        warehouseRepository.deleteAll();
//        sectionRepository.deleteAll();
//        agentRepository.deleteAll();
//        roleRepository.deleteAll();
//
//        BigDecimal bigDecimal = new BigDecimal("45.00");
//
//        Role roleUser = new Role(ERole.ROLE_USER);
//        roleRepository.save(roleUser);
//
//        Role roleModerator = new Role(ERole.ROLE_MODERATOR);
//        roleRepository.save(roleModerator);
//
//        Role roleAdmin = new Role(ERole.ROLE_ADMIN);
//        roleRepository.save(roleAdmin);
//
//        Warehouse warehouse = new Warehouse()
//                .warehouseCode("SP")
//                .warehouseName("sao paulo")
//                .build();
//
//        warehouseRepository.save(warehouse);
//
//        Warehouse warehouseMG = new Warehouse()
//                .warehouseCode("MG")
//                .warehouseName("Minas Gerais")
//                .build();
//
//        warehouseRepository.save(warehouse);
//
//        Section section = new Section()
//                .sectionCode("LA")
//                .sectionName("laticinios")
//                .maxLength(10)
//                .warehouse(warehouse)
//                .build();
//
//        sectionRepository.save(section);
//
//        Section sectionCO = new Section()
//                .sectionCode("CO")
//                .sectionName("Congelados")
//                .maxLength(10)
//                .warehouse(warehouseMG)
//                .build();
//
//        sectionRepository.save(sectionCO);
//
//        SectionCategory sectionCategoryFF = new SectionCategory()
//                .name(ESectionCategory.FF)
//                .build();
//
//        SectionCategory sectionCategoryFS = new SectionCategory()
//                .name(ESectionCategory.FS)
//                .build();
//
//
//        SectionCategory sectionCategoryRF = new SectionCategory()
//                .name(ESectionCategory.RF)
//                .build();
//
//        sectionCategoryRepository.saveAll(Arrays.asList(sectionCategoryFF, sectionCategoryFS,
//                sectionCategoryRF));
//
//        Buyer buyer = new Buyer()
//                .name("lucas")
//                .cpf("22233344411")
//                .build();
//        buyerRepository.save(buyer);
//
//        Product product = new Product()
//                .productId("MA")
//                .productName("margarina")
//                .section(section)
//                .category(sectionCategoryFS)
//                .productPrice(bigDecimal)
//                .dueDate(LocalDate.of(2022, 3, 23))
//                .build();
//
//        productRepository.save(product);
//
//        Product productDois = new Product()
//                .productId("DA")
//                .productName("danone")
//                .section(section)
//                .category(sectionCategoryFS)
//                .productPrice(bigDecimal)
//                .dueDate(LocalDate.of(2022, 3, 23))
//                .build();
//
//        Product productTres = new Product()
//                .productId("CA")
//                .productName("carne")
//                .section(sectionCO)
//                .category(sectionCategoryFF)
//                .productPrice(bigDecimal)
//                .dueDate(LocalDate.of(2022, 3, 23))
//                .build();
//
//        Product productQuatro = new Product()
//                .productId("FR")
//                .productName("frango")
//                .section(sectionCO)
//                .category(sectionCategoryFF)
//                .productPrice(bigDecimal)
//                .dueDate(LocalDate.of(2022, 3, 23))
//                .build();
//
//        productRepository.saveAll(Arrays.asList(product, productDois,
//                productTres, productQuatro));
//
//        Agent agent = new Agent().
//                cpf("11122233344").
//                name("lucas").
//                warehouse(warehouse).
//                build();
//
//        agentRepository.save(agent);
//    }

}
