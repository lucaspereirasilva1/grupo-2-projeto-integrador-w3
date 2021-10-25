package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.exception.AgentException;
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

    public Agent find(String cpf) {
        Optional<Agent> agent = agentRepository.findByCpf(cpf);
        if (agent.isEmpty()) {
            throw new AgentException("Representante nao cadastrado na base!!! Por gentileza realizar o cadastro");
        }
        return agent.get();
    }

    public Boolean findAgentWarehouse(String warehouseCode) {
        if (agentRepository.findByWarehouse_WarehouseCode(warehouseCode).isPresent()) {
            return true;
        }else {
            throw new AgentException("Representante nao e do armazem!!! Por gentileza verificar o cadastro");
        }
    }
}
