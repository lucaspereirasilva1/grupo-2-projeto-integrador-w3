package br.com.meli.projetointegrador;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ProjetoIntegradorApplicationTests {

    @Test
    void contextLoads() {
        assertEquals(1,Integer.valueOf(1));
    }

}
