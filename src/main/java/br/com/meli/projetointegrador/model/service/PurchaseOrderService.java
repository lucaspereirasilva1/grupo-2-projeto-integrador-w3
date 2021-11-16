package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.exception.PersistenceException;
import br.com.meli.projetointegrador.exception.PurchaseOrderException;
import br.com.meli.projetointegrador.model.dto.ProductDTO;
import br.com.meli.projetointegrador.model.dto.ProductPurchaseOrderDTO;
import br.com.meli.projetointegrador.model.dto.PurchaseOrderDTO;
import br.com.meli.projetointegrador.model.entity.Product;
import br.com.meli.projetointegrador.model.entity.PurchaseOrder;
import br.com.meli.projetointegrador.model.enums.EOrderStatus;
import br.com.meli.projetointegrador.model.repository.PurchaseOrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Jhony Zuim / Lucas Pereira / Edmilson Nobre / Rafael Vicente
 * @version 1.0.0
 * @since 15/10/2021
 * Camada service responsavel pela regra de negocio relacionada a PurchaseOrderService
 */

@Service
public class PurchaseOrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final ProductService productService;
    private final BuyerService buyerService;
    private final BatchStockService batchStockService;
    private BigDecimal total = new BigDecimal(0);
    private static final Logger logger = LoggerFactory.getLogger(PurchaseOrderService.class);

    public PurchaseOrderService(PurchaseOrderRepository purchaseOrderRepository
            , ProductService productService, BuyerService buyerService
            , BatchStockService batchStockService) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.productService = productService;
        this.buyerService = buyerService;
        this.batchStockService = batchStockService;
    }

    /**
     * @param id recebe um Id de produto;
     * @return adiciona o produto a uma lista ou exception.
     */
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
                    .productPrice(p.getProductPrice())
                    .dueDate(p.getDueDate())
                    .category(p.getCategory().getName());
            listProductDTO.add(productDTO);
        });
        return listProductDTO;
    }

    /**
     * @param purchaseOrderDTO recebe uma purchaseOrder;
     * @return se existente faz o update caso nao exista e salva uma nova.
     */
    public BigDecimal save(PurchaseOrderDTO purchaseOrderDTO){
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        total =  new BigDecimal(0);
        if (ObjectUtils.isEmpty(purchaseOrderDTO.getId())) {
            final List<Product> productListPost = calculeTotal(purchaseOrderDTO);
            purchaseOrder.productList(productListPost);
            purchaseOrder.date(LocalDate.now());
            purchaseOrder.buyer(buyerService.find(purchaseOrderDTO.getBuyerId()));
            purchaseOrder.orderStatus(EOrderStatus.ORDER_CHART);
            try {
                purchaseOrderRepository.save(purchaseOrder);
            }catch (DataAccessException e) {
                logger.error("Erro durante a persistencia no banco!!!", e);
                throw new PersistenceException("Erro durante a persistencia no banco!!!");
            }
            purchaseOrderRepository.save(purchaseOrder);
            batchStockService.updateBatchStock(purchaseOrderDTO.getListProductPurchaseOrderDTO());
        }else {
            final List<Product> productListPut = calculeTotal(purchaseOrderDTO);
            purchaseOrder.productList(productListPut);
            purchaseOrder.date(purchaseOrderDTO.getData());
            purchaseOrder.buyer(buyerService.find(purchaseOrderDTO.getBuyerId()));
            purchaseOrder.orderStatus(EOrderStatus.ORDER_CHART);
            purchaseOrder.id(purchaseOrderDTO.getId());
            try {
                purchaseOrderRepository.save(purchaseOrder);
            }catch (DataAccessException e) {
                logger.error("Erro durante a persistencia no banco!!!", e);
                throw new PersistenceException("Erro durante a persistencia no banco!!!");
            }
            batchStockService.updateBatchStock(purchaseOrderDTO.getListProductPurchaseOrderDTO());
        }
        return total;
    }

    /**
     * @param purchaseOrderDTO recebe uma purchaseOrderDTO;
     * @return adiciona o produto a lista ou retorna a exception.
     */
    private List<Product> calculeTotal(PurchaseOrderDTO purchaseOrderDTO) {
        List<Product> productList = new ArrayList<>();
        for (ProductPurchaseOrderDTO p : purchaseOrderDTO.getListProductPurchaseOrderDTO()) {
            Product product = productService.find(p.getProductId());
            Boolean dueDataProduct = batchStockService.dueDataProduct(product.getDueDate());
            if (Boolean.FALSE.equals(dueDataProduct)) {
                throw new PurchaseOrderException("Vencimento inferior a 3 semanas!!! Produto: " + product.getProductName() + " Vencimento: " + product.getDueDate());
            }
            if (p.getProductId().equals(product.getProductId())) {
                total = total.add(product.getProductPrice().multiply(new BigDecimal(p.getQuantity())));
            } else {
                throw new PurchaseOrderException("Produto nao encontrado");
            }
            productList.add(product);
        }
        return productList;
    }
}