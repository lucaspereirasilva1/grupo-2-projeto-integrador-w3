package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.model.dto.AgentDTO;
import br.com.meli.projetointegrador.model.dto.BatchStockDTO;
import br.com.meli.projetointegrador.model.dto.SectionDTO;
import br.com.meli.projetointegrador.model.entity.Agent;
import br.com.meli.projetointegrador.model.entity.BatchStock;
import br.com.meli.projetointegrador.model.entity.Product;
import br.com.meli.projetointegrador.model.entity.Section;
import br.com.meli.projetointegrador.model.repository.BatchStockRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BatchStockService {

    private final BatchStockRepository batchStockRepository;
    private final SectionService sectionService;
    private final AgentService agentService;
    private final ProductService productService;

    public BatchStockService(BatchStockRepository batchStockRepository,
                             SectionService sectionService,
                             AgentService agentService, ProductService productService) {
        this.batchStockRepository = batchStockRepository;
        this.sectionService = sectionService;
        this.agentService = agentService;
        this.productService = productService;
    }

    public void put(BatchStock batchStock, Section section, Agent agent) {
        Section s = sectionService.find(section.getSectionCode());
        Agent a = agentService.find(agent.getCpf());
        batchStock.agent(a);
        batchStock.section(s);
        batchStockRepository.save(batchStock);
    }

    public void postAll(List<BatchStock> listBatchStock, AgentDTO agentDTO, SectionDTO sectionDTO) {
        listBatchStock.forEach(b -> {
            Product product = productService.find(b.getProductId());
            if (productService.validProductSection(product) &&
                sectionService.validSectionLength(b.getSection()))
                b.agent(agentService.find(agentDTO.getCpf()));
                b.section(sectionService.find(sectionDTO.getSectionCode()));
        });
        batchStockRepository.saveAll(listBatchStock);
    }

    public void putAll(List<BatchStock> listBatchStock, List<BatchStockDTO> listBatchStockDTO, AgentDTO agentDTO) {
        for (int i = 0; i < listBatchStock.size(); i++) {
            for (int x = i; x < listBatchStockDTO.size(); x++) {
                Product product = productService.find(listBatchStock.get(i).getProductId());
                if (productService.validProductSection(product) &&
                        sectionService.validSectionLength(listBatchStock.get(i).getSection()))
                    listBatchStock.get(i).batchNumber(listBatchStockDTO.get(x).getBatchNumber());
                    listBatchStock.get(i).productId(listBatchStockDTO.get(x).getProductId());
                    listBatchStock.get(i).currentTemperature(listBatchStockDTO.get(x).getCurrentTemperature());
                    listBatchStock.get(i).minimumTemperature(listBatchStockDTO.get(x).getMinimumTemperature());
                    listBatchStock.get(i).initialQuantity(listBatchStockDTO.get(x).getInitialQuantity());
                    listBatchStock.get(i).manufacturingDate(listBatchStockDTO.get(x).getManufacturingDate());
                    listBatchStock.get(i).manufacturingTime(listBatchStockDTO.get(x).getManufacturingTime());
                    listBatchStock.get(i).dueDate(listBatchStockDTO.get(x).getDueDate());
                    Agent agent = listBatchStock.get(i).getAgent();
                    agent.cpf(agentDTO.getCpf());
                    agent.name(agentDTO.getName());
                    listBatchStock.get(i).agent(agent);
                    x++;
            }
            batchStockRepository.saveAll(listBatchStock);
        }
    }

}
