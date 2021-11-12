package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.exception.AgentException;
import br.com.meli.projetointegrador.model.dto.AgentDTO;
import br.com.meli.projetointegrador.model.entity.Agent;
import br.com.meli.projetointegrador.model.entity.Warehouse;
import br.com.meli.projetointegrador.model.repository.AgentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.util.ObjectUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Jhony Zuim / Lucas Pereira / Edmilson Nobre / Rafael Vicente
 * @version 1.0.0
 * @since 15/10/2021
 * Camada de testes unitarios do service responsavel pela regra de negocio relacionada ao agent
 */

class AgentServiceTest {

    private final AgentRepository agentRepository = mock(AgentRepository.class);
    private final AgentService agentService = new AgentService(agentRepository);

    @Test
    void findTest() {
        Agent agent = new Agent()
                .name("lucas")
                .cpf("11122233344")
                .build();

        when(agentRepository.findByCpf(anyString()))
                .thenReturn(Optional.of(agent));

        Agent agentReturn = agentService.find(agent.getCpf());
        assertEquals(agentReturn, agent);
    }

    @Test
    void findNotExistTest() {
        when(agentRepository.findByCpf(anyString()))
                .thenReturn(Optional.empty());
        AgentException agentException = assertThrows(AgentException.class, () ->
                agentService.find("11122233344"));

        String expectedMessage = "Representante nao cadastrado na base!!! Por gentileza realizar o cadastro";

        assertTrue(expectedMessage.contains(agentException.getMessage()));
    }


    @Test
    void findByCpf() {
        Warehouse warehouse = new Warehouse()
                .warehouseCode("SP")
                .warehouseName("sao paulo")
                .build();
        Agent agent = new Agent()
                .name("lucas")
                .cpf("11122233344")
                .warehouse(warehouse)
                .build();
        when(agentRepository.findByCpf(anyString()))
                .thenReturn(Optional.of(agent));
        final AgentDTO agentDTO = agentService.findByCpf("11122233344");
        assertFalse(ObjectUtils.isEmpty(agentDTO));
        assertEquals("11122233344", agentDTO.getCpf());
        assertEquals("lucas", agentDTO.getName());
        assertEquals("SP", agentDTO.getWarehouseCode());
    }
}
