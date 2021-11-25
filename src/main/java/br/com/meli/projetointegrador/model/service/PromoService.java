package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.exception.PersistenceException;
import br.com.meli.projetointegrador.exception.PromoException;
import br.com.meli.projetointegrador.model.dto.PromoRequestDTO;
import br.com.meli.projetointegrador.model.dto.PromoResponseDTO;
import br.com.meli.projetointegrador.model.entity.Product;
import br.com.meli.projetointegrador.model.entity.Promo;
import br.com.meli.projetointegrador.model.entity.Role;
import br.com.meli.projetointegrador.model.entity.User;
import br.com.meli.projetointegrador.model.enums.ERole;
import br.com.meli.projetointegrador.model.repository.PromoRepository;
import br.com.meli.projetointegrador.model.repository.UserRepository;
import br.com.meli.projetointegrador.utils.ConstantsUtil;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PromoService {

    private final ProductService productService;
    private final PromoRepository promoRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper = new ModelMapper();
    private static final Logger logger = LoggerFactory.getLogger(PromoService.class);

    public PromoService(ProductService productService, PromoRepository promoRepository, UserRepository userRepository) {
        this.productService = productService;
        this.promoRepository = promoRepository;
        this.userRepository = userRepository;
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
        final BigDecimal finalValue = updatePercentDiscount(product.getProductPrice(), discount);
        product.setProductPrice(finalValue);
        promo.setFinalValue(finalValue);
        productService.save(product);
        try {
            promoRepository.save(promo);
        }catch (DataAccessException e) {
            logger.error(ConstantsUtil.PERSISTENCE_ERROR, e);
            throw new PersistenceException(ConstantsUtil.PERSISTENCE_ERROR);
        }
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

    public PromoResponseDTO updatePromo(PromoRequestDTO promoRequestDTO) {
        if (promoRequestDTO.getPercentDiscount() != 0.05 &&
                promoRequestDTO.getPercentDiscount() != 0.15 &&
                promoRequestDTO.getPercentDiscount() != 0.25){
            throw new PromoException("Valor do desconto de vencimento deve ser 0.05, 0.15 ou 0.25!!!");
        }
        final Promo promo = find(promoRequestDTO.getProductId());
        final Product product = productService.find(promoRequestDTO.getProductId());
        BigDecimal finalValue = updatePercentDiscount(promo.getOriginalValue()
                ,promoRequestDTO.getPercentDiscount()).setScale(2, RoundingMode.HALF_EVEN);
        promo.setFinalValue(finalValue);
        product.setProductPrice(finalValue);
        productService.save(product);
        try {
            promoRepository.save(promo);
        }catch (DataAccessException e) {
            logger.error(ConstantsUtil.PERSISTENCE_ERROR, e);
            throw new PersistenceException(ConstantsUtil.PERSISTENCE_ERROR);
        }
        return new PromoResponseDTO()
                .productId(promoRequestDTO.getProductId())
                .productDueDate(promo.getProductDueDate())
                .originalValue(promo.getOriginalValue())
                .percentDiscount(promoRequestDTO.getPercentDiscount())
                .finalValue(finalValue)
                .build();
    }

    private BigDecimal updatePercentDiscount(BigDecimal finalValue, Double percentDiscount) {
        return finalValue.subtract(finalValue.multiply(BigDecimal.valueOf(percentDiscount))).setScale(2, RoundingMode.HALF_EVEN);
    }

    public Promo find(String productId) {
        final Optional<Promo> promo = promoRepository.findByProductId(productId);
        if (promo.isEmpty()) {
            throw new PromoException("Codigo do produto invalido!!!");
        }
        return promo.get();
    }

    public List<PromoResponseDTO> listByDiscount() {
        final List<Promo> promoList = promoRepository.findAll();
        if (promoList.isEmpty()) {
            throw new PromoException("Nenhuma promocao de vencimento cadastrada!!!");
        }
        promoList.sort((Promo p1, Promo p2) -> p2.getPercentDiscount().compareTo(p1.getPercentDiscount()));
        return conveterListaDTO(promoList, PromoResponseDTO.class);
    }

    private <S, T> List<T> conveterListaDTO(List<S> entrada, Class<T> saida) {
        return entrada
                .stream()
                .map(element -> modelMapper.map(element, saida))
                .collect(Collectors.toList());
    }

    public BigDecimal apllyPromoFull(String productId, Double percent, String cpf) {
        final Product product = productService.find(productId);
        final Optional<User> user = userRepository.findByCpf(cpf);
        if (user.isEmpty()) {
            throw new PromoException("Cpf nao encontrado!!!");
        }else {
            final Set<Role> roles = user.get().getRoles().stream()
                    .filter(u -> u.getName().equals(ERole.ROLE_ADMIN))
                    .collect(Collectors.toSet());
            if (ObjectUtils.isEmpty(roles)) {
                throw new PromoException("Usuario sem permissao de administrador!!!");
            }
        }
        BigDecimal finalValue = updatePercentDiscount(product.getProductPrice(), percent);
        product.setProductPrice(finalValue);
        final Promo promo = new Promo()
                .productId(productId)
                .productDueDate(product.getDueDate())
                .originalValue(product.getProductPrice())
                .percentDiscount(percent)
                .finalValue(finalValue)
                .build();
        productService.save(product);
        try {
            promoRepository.save(promo);
        }catch (DataAccessException e) {
            logger.error(ConstantsUtil.PERSISTENCE_ERROR, e);
            throw new PersistenceException(ConstantsUtil.PERSISTENCE_ERROR);
        }
        return finalValue;
    }

}
