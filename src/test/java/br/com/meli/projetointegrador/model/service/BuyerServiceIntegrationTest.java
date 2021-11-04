package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.exception.BuyerException;
import br.com.meli.projetointegrador.model.entity.Buyer;
import br.com.meli.projetointegrador.model.repository.BuyerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ObjectUtils;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class BuyerServiceIntegrationTest {

    @Autowired
    private BuyerRepository buyerRepository;

    @Autowired
    private BuyerService buyerService;

    @Test
    void find() {
        Buyer buyer = new Buyer()
                .cpf("11122233311")
                .name("lucas")
                .build();
        buyerRepository.save(buyer);
        final Buyer buyerReturn = buyerService.find(buyer.getId());
        assertFalse(ObjectUtils.isEmpty(buyerReturn));
        assertEquals(buyer, buyerReturn);
    }

    @Test
    void findNotExist() {
        BuyerException buyerException = assertThrows
                (BuyerException.class,() ->
                        buyerService.find(""));
        String menssagemEsperada = "Comprador nao existe!!!";
        assertTrue(menssagemEsperada.contains(buyerException.getMessage()));
    }

}

