package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.exception.BuyerException;
import br.com.meli.projetointegrador.model.entity.Buyer;
import br.com.meli.projetointegrador.model.repository.BuyerRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BuyerService {

    private final BuyerRepository buyerRepository;

    public BuyerService(BuyerRepository buyerRepository) {
        this.buyerRepository = buyerRepository;
    }

    public Buyer find(String buyerId) {
        final Optional<Buyer> buyer = buyerRepository.findById(buyerId);
        if (buyer.isEmpty()) {
            throw new BuyerException("Comprador nao existe!!!");
        }
        return buyer.get();
    }

}
