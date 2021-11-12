package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.exception.AgentException;
import br.com.meli.projetointegrador.model.dto.AgentDTO;
import br.com.meli.projetointegrador.model.entity.Agent;
import br.com.meli.projetointegrador.model.repository.AgentRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Jhony Zuim / Lucas Pereira / Edmilson Nobre / Rafael Vicente
 * @version 1.0.0
 * @since 15/10/2021
 * Camada service responsavel pela regra de negocio relacionada ao agent
 */

@Service
public class AgentService {

    private final AgentRepository agentRepository;

    public AgentService(AgentRepository agentRepository) {
        this.agentRepository = agentRepository;
    }

    /**
     * @param cpf, recebe um CPF de um agente;
     * @return retorna o agente ou exception.
     */
    public Agent find(String cpf) {
        Optional<Agent> agent = agentRepository.findByCpf(cpf);
        if (agent.isEmpty()) {
            throw new AgentException("Representante nao cadastrado na base!!! Por gentileza realizar o cadastro");
        }
        return agent.get();
    }

    /**
     * @param cpf, recebe o cpf de um agente;
     * @return retorna o agente ou exception.
     */
    public AgentDTO findByCpf(String cpf) {
        final Agent agent = find(cpf);
        return new AgentDTO()
                .name(agent.getName())
                .cpf(agent.getCpf())
                .warehouseCode(agent.getWarehouse().getWarehouseCode())
                .build();
    }

}
