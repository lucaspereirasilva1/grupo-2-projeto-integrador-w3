package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.exception.BuyerException;
import br.com.meli.projetointegrador.model.entity.Buyer;
import br.com.meli.projetointegrador.model.repository.BuyerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.util.ObjectUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BuyerServiceTest {

    private final BuyerRepository mockBuyerRepository = mock(BuyerRepository.class);
    private final BuyerService buyerService = new BuyerService(mockBuyerRepository);

    @Test
    void find() {
        Buyer buyer = new Buyer()
                .cpf("11122233311")
                .name("lucas")
                .build();
        when(mockBuyerRepository.findById(anyString()))
                .thenReturn(Optional.of(buyer));
        final Buyer buyerReturn = buyerService.find("1");
        assertFalse(ObjectUtils.isEmpty(buyerReturn));
        assertEquals(buyer, buyerReturn);
    }

    @Test
    void findNotExist() {
        when(mockBuyerRepository.findById(anyString()))
                .thenReturn(Optional.empty());
        BuyerException buyerException = assertThrows
                (BuyerException.class,() ->
                        buyerService.find("1"));
        String menssagemEsperada = "Comprador nao existe!!!";
        assertTrue(menssagemEsperada.contains(buyerException.getMessage()));
    }

}