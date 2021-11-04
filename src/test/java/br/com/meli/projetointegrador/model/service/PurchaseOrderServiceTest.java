package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.model.dto.OrderStatusDTO;
import br.com.meli.projetointegrador.model.dto.ProductPurchaseOrderDTO;
import br.com.meli.projetointegrador.model.dto.PurchaseOrderDTO;
import br.com.meli.projetointegrador.model.entity.*;
import br.com.meli.projetointegrador.model.enums.EOrderStatus;
import br.com.meli.projetointegrador.model.enums.ESectionCategory;
import br.com.meli.projetointegrador.model.repository.PurchaseOrderRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author Jhony Zuim / Lucas Pereira / Edmilson Nobre / Rafael Vicente
 * @version 1.0.0
 * @since 15/10/2021
 * Camada de testes unitarios do service responsavel pela regra de negocio relacionada ao section
 */

public class PurchaseOrderServiceTest {

    private final PurchaseOrderRepository mockPurchaseOrderRepository = mock(PurchaseOrderRepository.class);
    private final ProductService mockProductService = mock(ProductService.class);
    private final BuyerService mockBuyerService = mock(BuyerService.class);
    private final BatchStockService mockBatchStockService = mock(BatchStockService.class);
    private final PurchaseOrderService purchaseOrderService = new PurchaseOrderService(
            mockPurchaseOrderRepository, mockProductService, mockBuyerService, mockBatchStockService);

    @Test
    void productPriceTest(){
        List<ProductPurchaseOrderDTO> listProductPurchaseOrderDTO = new ArrayList<>();
        ProductPurchaseOrderDTO productPurchaseOrderDTO1 = new ProductPurchaseOrderDTO()
                .productId("LE")
                .quantity(5)
                .build();
        ProductPurchaseOrderDTO productPurchaseOrderDTO2 = new ProductPurchaseOrderDTO()
                .productId("QJ")
                .quantity(3)
                .build();
        listProductPurchaseOrderDTO.add(productPurchaseOrderDTO1);
        listProductPurchaseOrderDTO.add(productPurchaseOrderDTO2);

        PurchaseOrderDTO purchaseOrderDTO = new PurchaseOrderDTO()
                .data(LocalDate.now())
                .buyerId("1")
                .orderStatus(new OrderStatusDTO().statusCode(EOrderStatus.IN_PROGRESS))
                .listProductPurchaseOrderDTO(listProductPurchaseOrderDTO);

        Warehouse warehouse = new Warehouse()
                .warehouseCode("SP")
                .warehouseName("Sao Paulo")
                .build();

        Section section = new Section()
                .sectionCode("LA")
                .sectionName("Laticionios")
                .maxLength(10)
                .warehouse(warehouse)
                .build();

        when(mockProductService.find(productPurchaseOrderDTO1.getProductId()))
                .thenReturn(new Product()
                        .productId("LE")
                        .productName("LEITE")
                        .productPrice(new BigDecimal(2))
                        .category(new SectionCategory().name(ESectionCategory.FF))
                        .section(section)
                        .dueDate(LocalDate.of(2021,11,30)));
        when(mockProductService.find(productPurchaseOrderDTO2.getProductId()))
                .thenReturn(new Product()
                        .productId("QJ")
                        .productName("QUEIJO")
                        .productPrice(new BigDecimal(3))
                        .category(new SectionCategory().name(ESectionCategory.FF))
                        .section(section)
                        .dueDate(LocalDate.of(2021,11,30)));
        when(mockBuyerService.find(anyString()))
                .thenReturn(new Buyer()
                        .name("lucas")
                        .cpf("22233344411")
                        .build());
        when(mockPurchaseOrderRepository.save(any(PurchaseOrder.class)))
                .thenReturn(new PurchaseOrder());

        BigDecimal total = purchaseOrderService.total(purchaseOrderDTO);
        assertEquals(new BigDecimal(19),total);
    }
}
