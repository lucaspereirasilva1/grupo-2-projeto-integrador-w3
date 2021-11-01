package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.exception.ProductException;
import br.com.meli.projetointegrador.exception.PurchaseOrderException;
import br.com.meli.projetointegrador.model.dto.ProductDTO;
import br.com.meli.projetointegrador.model.dto.ProductPurchaseOrderDTO;
import br.com.meli.projetointegrador.model.dto.PurchaseOrderDTO;
import br.com.meli.projetointegrador.model.entity.Product;
import br.com.meli.projetointegrador.model.entity.PurchaseOrder;
import br.com.meli.projetointegrador.model.enums.EOrderStatus;
import br.com.meli.projetointegrador.model.repository.PurchaseOrderRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PurchaseOrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final ProductService productService;
    private final BuyerService buyerService;
    private final BatchStockService batchStockService;

    public PurchaseOrderService(PurchaseOrderRepository purchaseOrderRepository
            , ProductService productService, BuyerService buyerService
            , BatchStockService batchStockService) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.productService = productService;
        this.buyerService = buyerService;
        this.batchStockService = batchStockService;
    }

    public List<ProductDTO> showOrderProduct(String id) {
        List<ProductDTO> listProductDTO = new ArrayList<>();
        final Optional<PurchaseOrder> purchaseOrder = purchaseOrderRepository.findById(id);
        if (purchaseOrder.isEmpty()) {
            throw new PurchaseOrderException("Ordem de compra nao encontrada!!!");
        }
        purchaseOrder.get().getProductList().forEach(p -> {
            ProductDTO productDTO = new ProductDTO()
                    .productId(p.getProductId())
                    .productName(p.getProductName())
                    .sectionName(p.getSection().getSectionName())
                    .category(p.getCategory().getName());
            listProductDTO.add(productDTO);
        });
        return listProductDTO;
    }

    public BigDecimal total(PurchaseOrderDTO purchaseOrderDTO){
        BigDecimal total= new BigDecimal(0);
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        List<Product> listProduct = new ArrayList<>();
        for (ProductPurchaseOrderDTO p:purchaseOrderDTO.getListProductPurchaseOrderDTO()) {
            Product product = productService.find(p.getProductId());
            productService.doeDataProduct(product.getDueDate());
            if (p.getProductId().equals(product.getProductId())){
                total=total.add(product.getProductPrice().multiply(new BigDecimal(p.getQuantity())));
            }else{
                throw new ProductException("Produto nao encontrado");
            }
            listProduct.add(product);
        }
        purchaseOrder.productList(listProduct);
        purchaseOrder.date(LocalDate.now());
        purchaseOrder.buyer(buyerService.find(purchaseOrderDTO.getBuyerId()));
        purchaseOrder.orderStatus(EOrderStatus.ORDER_CHART);
        purchaseOrderRepository.save(purchaseOrder);
        batchStockService.updateBatchStock(purchaseOrderDTO.getListProductPurchaseOrderDTO());
        return total;
    }

}
