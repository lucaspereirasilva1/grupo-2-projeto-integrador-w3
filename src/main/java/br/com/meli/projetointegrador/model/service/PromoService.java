package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.exception.PersistenceException;
import br.com.meli.projetointegrador.exception.PromoException;
import br.com.meli.projetointegrador.model.dto.PromoFullRequestDTO;
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

/**
 * @author Lucas Pereira
 * @version 1.5.0
 * @since 25/11/2021
 * Service responsavel pelas regras de negocio referente a feature de promocoes
 */

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

    /**
     * @param productId, id do produto
     * @return valor final do produto com o desconto aplicado
     * @throws PersistenceException se ja existir no banco um objeto com mesmo id/index
     */
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

    /**
     * @param dueDate, date de vencimento do produto
     * @return porcentagem de desconto a ser aplicada
     * @throws PromoException caso o produto nao seja apto a promocao de vencimento
     */
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

    /**
     * @param promoRequestDTO, atualiza uma promocao existente
     * @return as informacoes da promocao atualizada
     * @throws PromoException se o valor de desconto for invalido
     * @throws PersistenceException se ja existir no banco um objeto com mesmo id/index
     */
    public PromoResponseDTO updatePromo(PromoRequestDTO promoRequestDTO) {
        if (promoRequestDTO.getPercentDiscount() != 0.05 &&
                promoRequestDTO.getPercentDiscount() != 0.15 &&
                promoRequestDTO.getPercentDiscount() != 0.25){
            throw new PromoException("Valor do desconto de vencimento deve ser 0.05, 0.15 ou 0.25!!!");
        }
        final Promo promo = find(promoRequestDTO.getProductId());
        final Product product = productService.find(promoRequestDTO.getProductId());
        BigDecimal finalValue = updatePercentDiscount(promo.getOriginalValue()
                ,promoRequestDTO.getPercentDiscount());
        promo.setFinalValue(finalValue);
        promo.setPercentDiscount(promoRequestDTO.getPercentDiscount());
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

    /**
     * @param finalValue valor final do produto
     * @param percentDiscount porcentagem de disconto
     * @return valor calculado baseado na porcetagem
     */
    private BigDecimal updatePercentDiscount(BigDecimal finalValue, Double percentDiscount) {
        return finalValue.subtract(finalValue.multiply(BigDecimal.valueOf(percentDiscount))).setScale(2, RoundingMode.HALF_EVEN);
    }

    /**
     * @param productId valor final do produto
     * @return uma promocao cadastrada
     * @throws PromoException se o codigo do produto for invalido
     */
    public Promo find(String productId) {
        final Optional<Promo> promo = promoRepository.findByProductId(productId);
        if (promo.isEmpty()) {
            throw new PromoException("Codigo do produto invalido!!!");
        }
        return promo.get();
    }

    /**
     * @return lista de promocoes cadastradas
    * @throws PromoException caso nao exista nenhuma promocao cadastrada
     */
    public List<PromoResponseDTO> listByDiscount() {
        final List<Promo> promoList = promoRepository.findAll();
        if (promoList.isEmpty()) {
            throw new PromoException("Nenhuma promocao de vencimento cadastrada!!!");
        }
        promoList.sort((Promo p1, Promo p2) -> p2.getPercentDiscount().compareTo(p1.getPercentDiscount()));
        return conveterListaDTO(promoList, PromoResponseDTO.class);
    }

    /**
     * @param entrada lista de entrada
     * @param saida lista de saida
     * @return uma lista convertida para um DTO
     */
    private <S, T> List<T> conveterListaDTO(List<S> entrada, Class<T> saida) {
        return entrada
                .stream()
                .map(element -> modelMapper.map(element, saida))
                .collect(Collectors.toList());
    }

    /**
     * @param promoFullRequestDTO request com id do produto, porcentagem de desconto e cpf
     * @return o valor do desconto aplicado no produto
     * @throws PersistenceException caso o objeto ja exista no banco com o mesmo id/index
     */
    public BigDecimal apllyPromoFull(PromoFullRequestDTO promoFullRequestDTO) {
        final BigDecimal finalValue;
        final Product product = productService.find(promoFullRequestDTO.getProductId());
        findUser(promoFullRequestDTO.getCpf());
        final Promo promo = findPromoIfExist(promoFullRequestDTO.getProductId());
        if (ObjectUtils.isEmpty(promo.getId())) {
            promo.setOriginalValue(product.getProductPrice());
            finalValue = updatePercentDiscount(product.getProductPrice(), promoFullRequestDTO.getPercent());
        } else {
            promo.id(promo.getId());
            promo.setOriginalValue(promo.getOriginalValue());
            finalValue = updatePercentDiscount(promo.getOriginalValue(), promoFullRequestDTO.getPercent());
        }
        promo.setFinalValue(finalValue);
        promo.setProductId(promoFullRequestDTO.getProductId());
        promo.setProductDueDate(product.getDueDate());
        promo.setPercentDiscount(promoFullRequestDTO.getPercent());
        product.setProductPrice(finalValue);
        productService.save(product);
        try {
            promoRepository.save(promo);
        }catch (DataAccessException e) {
            logger.error(ConstantsUtil.PERSISTENCE_ERROR, e);
            throw new PersistenceException(ConstantsUtil.PERSISTENCE_ERROR);
        }
        return finalValue;
    }

    /**
     * @param cpf cpf referente ao usuario logado
     * @throws PromoException caso o cpf nao seja encontrado
     */
    private void findUser(String cpf) {
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
    }

    /**
     * @param productId id do produto
     * @return a promocao referente ao id informado caso exista
     */
    private Promo findPromoIfExist(String productId) {
        final Optional<Promo> promo = promoRepository.findByProductId(productId);
        return promo.orElse(new Promo());
    }

}
