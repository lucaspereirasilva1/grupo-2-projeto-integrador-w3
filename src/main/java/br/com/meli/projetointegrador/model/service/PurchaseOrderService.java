package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.exception.PurchaseOrderException;
import br.com.meli.projetointegrador.model.dto.ProductDTO;
import br.com.meli.projetointegrador.model.entity.PurchaseOrder;
import br.com.meli.projetointegrador.model.repository.PurchaseOrderRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PurchaseOrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final ProductService productService;

    public PurchaseOrderService(PurchaseOrderRepository purchaseOrderRepository
            , ProductService productService) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.productService = productService;
    }

    public List<ProductDTO> showOrderProduct(String id) {
        List<ProductDTO> listProductDTO = new ArrayList<>();
        final Optional<PurchaseOrder> purchaseOrder = purchaseOrderRepository.findById(id);
        if (purchaseOrder.isEmpty()) {
            throw new PurchaseOrderException("Ordem de compra nao encontrada!!!");
        }
        purchaseOrder.get().getProduct().forEach(p -> {
            ProductDTO productDTO = new ProductDTO()
                    .productId(p.getProductId())
                    .productName(p.getProductName())
                    .sectionName(p.getSection().getSectionName())
                    .category(p.getCategory().getName());
            listProductDTO.add(productDTO);
        });
        return listProductDTO;
    }

}
