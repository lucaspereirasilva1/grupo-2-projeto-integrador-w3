package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.exception.AgentException;
import br.com.meli.projetointegrador.model.entity.Agent;
import br.com.meli.projetointegrador.model.entity.Warehouse;
import br.com.meli.projetointegrador.model.repository.AgentRepository;
import br.com.meli.projetointegrador.model.repository.WarehouseRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Jhony Zuim / Lucas Pereira / Edmilson Nobre / Rafael Vicente
 * @version 1.0.0
 * @since 15/10/2021
 * Camada de teste integrado do service responsavel pela regra de negocio relacionada ao agent
 */

@SpringBootTest
public class AgentServiceIntegrationTest {

    @Autowired
    private AgentService agentService;

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @BeforeEach
    void setUp() {
        Warehouse warehouse = new Warehouse()
                .warehouseCode("SP")
                .warehouseName("sao paulo")
                .build();
        warehouseRepository.save(warehouse);

        Agent agent = new Agent().
                cpf("11122233344").
                name("lucas").
                warehouse(warehouse).
                build();
        agentRepository.save(agent);
    }

    @AfterEach
    void cleanUpDatabase() {
        warehouseRepository.deleteAll();
        agentRepository.deleteAll();
    }

    @Test
    void findIntegrationTest() {
        Agent agent = agentService.find("11122233344");
        assertEquals("11122233344", agent.getCpf());
    }

    @Test
    void findNotExistIntegrationTest() {
        AgentException agentException = assertThrows(AgentException.class, () ->
                agentService.find(""));
        String expectedMessage = "Representante nao cadastrado na base!!! Por gentileza realizar o cadastro";
        assertTrue(expectedMessage.contains(agentException.getMessage()));
    }

}
