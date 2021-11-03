package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.exception.BuyerException;
import br.com.meli.projetointegrador.model.entity.Buyer;
import br.com.meli.projetointegrador.model.repository.BuyerRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Jhony Zuim / Lucas Pereira / Edmilson Nobre / Rafael Vicente
 * @version 1.0.0
 * @since 15/10/2021
 * Camada service responsavel pela regra de negocio relacionada ao buyer
 */

@Service
public class BuyerService {

    private final BuyerRepository buyerRepository;

    public BuyerService(BuyerRepository buyerRepository) {
        this.buyerRepository = buyerRepository;
    }

    /**
     * @param buyerId recebe um Id buyer;
     * @return um Buyer ou exception.
     */
    public Buyer find(String buyerId) {
        final Optional<Buyer> buyer = buyerRepository.findById(buyerId);
        if (buyer.isEmpty()) {
            throw new BuyerException("Comprador nao existe!!!");
        }
        return buyer.get();
    }

}
