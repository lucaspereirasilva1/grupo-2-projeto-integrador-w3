package br.com.meli.projetointegrador.model.repository;

import br.com.meli.projetointegrador.model.entity.Buyer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataMongoTest
class BuyerRepositoryTest {

    @Autowired
    private BuyerRepository buyerRepository;

    @BeforeEach
    void setUp() {
        buyerRepository.deleteAll();

        Buyer buyer = new Buyer()
                .name("lucas")
                .cpf("22233344411")
                .build();
        buyerRepository.save(buyer);
    }

    @AfterEach
    void tearDown() {
        buyerRepository.deleteAll();
    }

    @Test
    void findByCpf() {
        assertTrue(buyerRepository.findByCpf("22233344411").isPresent());
    }
}