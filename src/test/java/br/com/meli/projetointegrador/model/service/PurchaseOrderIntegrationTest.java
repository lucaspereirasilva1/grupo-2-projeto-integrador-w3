package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.model.dto.ProductDTO;
import br.com.meli.projetointegrador.model.entity.*;
import br.com.meli.projetointegrador.model.enums.EOrderStatus;
import br.com.meli.projetointegrador.model.enums.ESectionCategory;
import br.com.meli.projetointegrador.model.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
public class PurchaseOrderIntegrationTest {

    @Autowired
    private PurchaseOrderService purchaseOrderService;

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
    private PurchaseOrderRepository purchaseOrderRepository;

    @Test
    void showOrderProduct() {

        Warehouse warehouse = new Warehouse()
                .warehouseCode("MG")
                .warehouseName("Minas Gerais")
                .build();
        warehouseRepository.save(warehouse);

        Section section = new Section()
                .sectionCode("LA")
                .sectionName("laticinios")
                .maxLength(10)
                .warehouse(warehouse)
                .build();
        sectionRepository.save(section);

        SectionCategory sectionCategory = new SectionCategory()
                .name(ESectionCategory.FF)
                .build();
        sectionCategoryRepository.save(sectionCategory);

        Product product = new Product()
                .productId("LE")
                .productName("leite")
                .section(section)
                .category(sectionCategory)
                .build();

        Product productUm = new Product()
                .productId("QJ")
                .productName("queijo")
                .section(section)
                .category(sectionCategory)
                .build();
        productRepository.saveAll(Arrays.asList(product, productUm));

        Buyer buyer = new Buyer()
                .name("lucas")
                .cpf("22233344411")
                .build();
        buyerRepository.save(buyer);

        PurchaseOrder purchaseOrder = new PurchaseOrder()
                .date(LocalDate.now())
                .buyer(buyer)
                .orderStatus(EOrderStatus.IN_PROGRESS)
                .product(Arrays.asList(product, productUm))
                .build();
        purchaseOrderRepository.save(purchaseOrder);

        List<ProductDTO> listProductDTO = purchaseOrderService.showOrderProduct(purchaseOrder.getId());
        assertFalse(listProductDTO.isEmpty());
    }

}
