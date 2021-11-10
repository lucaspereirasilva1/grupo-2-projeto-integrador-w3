package br.com.meli.projetointegrador.model.repository;

import br.com.meli.projetointegrador.model.entity.*;
import br.com.meli.projetointegrador.model.enums.EOrderStatus;
import br.com.meli.projetointegrador.model.enums.ESectionCategory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Test
    void saveProduct() {
        productRepository.deleteAll();

        List<Product> listProduct = new ArrayList<>();
        final Optional<Section> section = sectionRepository.findBySectionCode("LA");

        SectionCategory sectionCategory = new SectionCategory()
                .name(ESectionCategory.FF)
                .build();

        Product product = new Product()
                .productId("LE")
                .productName("leite")
                .section(section.orElse(new Section()))
                .category(sectionCategory)
                .dueDate(LocalDate.now())
                .productPrice(new BigDecimal("2.0"))
                .build();

        Product productUm = new Product()
                .productId("QJ")
                .productName("queijo")
                .section(section.orElse(new Section()))
                .category(sectionCategory)
                .dueDate(LocalDate.now())
                .productPrice(new BigDecimal("2.0"))
                .build();

        listProduct.add(product);
        listProduct.add(productUm);

        productRepository.saveAll(listProduct);
    }

    @Test
    void saveSectionCategory() {
        SectionCategory sectionCategory = new SectionCategory()
                .name(ESectionCategory.FF)
                .build();
        sectionCategoryRepository.save(sectionCategory);
    }

    @Test
    void savePurchaseOrder() {
        purchaseOrderRepository.deleteAll();
        final List<Product> listProduct = productRepository.findAll();
        final Optional<Buyer> buyer = buyerRepository.findByCpf("22233344411");

        PurchaseOrder purchaseOrder = new PurchaseOrder()
                .date(LocalDate.now())
                .buyer(buyer.orElse(new Buyer()))
                .orderStatus(EOrderStatus.ORDER_CHART)
                .productList(listProduct)
                .build();

        purchaseOrderRepository.save(purchaseOrder);
    }

    @Test
    void saveBuyer() {
        Buyer buyer = new Buyer()
                .name("lucas")
                .cpf("22233344411")
                .build();
        buyerRepository.save(buyer);
    }

}
