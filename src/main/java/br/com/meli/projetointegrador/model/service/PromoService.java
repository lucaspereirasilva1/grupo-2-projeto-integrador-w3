package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.exception.PromoException;
import br.com.meli.projetointegrador.model.entity.Product;
import br.com.meli.projetointegrador.model.entity.Promo;
import br.com.meli.projetointegrador.model.repository.PromoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class PromoService {

    private final ProductService productService;
    private final PromoRepository promoRepository;

    public PromoService(ProductService productService, PromoRepository promoRepository) {
        this.productService = productService;
        this.promoRepository = promoRepository;
    }

    public BigDecimal apllyPromo(String productId) {
        final Product product = productService.find(productId);
        final Double discount = percentDiscount(product.getDueDate());
        final Promo promo = new Promo()
                .productId(productId)
                .productDueDate(product.getDueDate())
                .originalValue(product.getProductPrice())
                .percentDiscount(discount)
                .build();
        final BigDecimal finalValue = product.getProductPrice()
                .subtract(product.getProductPrice()
                        .multiply(BigDecimal.valueOf(discount)));
        product.setProductPrice(finalValue);
        promo.setFinalValue(finalValue);
        promoRepository.save(promo);
        return finalValue;
    }

    private Double percentDiscount(LocalDate dueDate) {
        if (dueDate.equals(LocalDate.now().plusWeeks(1))) {
            return 0.05;
        } else {
            if (dueDate.equals(LocalDate.now().plusDays(5))) {
                return 0.15;
            } else {
                if (dueDate.equals(LocalDate.now().plusDays(3))) {
                    return 0.25;
                } else {
                    throw new PromoException("Produto nao apto a promocao de vencimento!!!");
                }
            }
        }
    }

}
