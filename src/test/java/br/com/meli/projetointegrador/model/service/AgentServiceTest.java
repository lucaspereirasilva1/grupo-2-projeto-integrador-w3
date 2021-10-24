package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.exception.AgentException;
import br.com.meli.projetointegrador.model.entity.Agent;
import br.com.meli.projetointegrador.model.repository.AgentRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AgentServiceTest {

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
        Agent agent = new Agent()
                .name("lucas")
                .cpf("11122233344")
                .build();

        when(agentRepository.findByCpf(anyString()))
                .thenReturn(Optional.empty());

        AgentException agentException = assertThrows(AgentException.class, () ->
                agentService.find(agent.getCpf()));

        String expectedMessage = "Representante nao cadastrado na base!!! Por gentileza realizar o cadastro";

        assertTrue(expectedMessage.contains(agentException.getMessage()));
    }

}
