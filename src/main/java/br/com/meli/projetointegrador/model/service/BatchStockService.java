package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.exception.BatchStockException;
import br.com.meli.projetointegrador.model.dto.AgentDTO;
import br.com.meli.projetointegrador.model.dto.BatchStockDTO;
import br.com.meli.projetointegrador.model.dto.ProductPurchaseOrderDTO;
import br.com.meli.projetointegrador.model.dto.SectionDTO;
import br.com.meli.projetointegrador.model.entity.Agent;
import br.com.meli.projetointegrador.model.entity.BatchStock;
import br.com.meli.projetointegrador.model.entity.Product;
import br.com.meli.projetointegrador.model.repository.BatchStockRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Jhony Zuim / Lucas Pereira / Edmilson Nobre / Rafael Vicente
 * @version 1.0.0
 * @since 15/10/2021
 * Camada service responsavel pela regra de negocio relacionada ao batchStock
 */

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

    public void postAll(List<BatchStock> listBatchStock, AgentDTO agentDTO, SectionDTO sectionDTO) {
        listBatchStock.forEach(b -> {
            Product product = productService.find(b.getProductId());
            if (productService.validProductSection(sectionDTO.getSectionCode()) &&
                sectionService.validSectionLength(product.getSection()))
                b.agent(agentService.find(agentDTO.getCpf()));
                b.section(sectionService.find(sectionDTO.getSectionCode()));
        });
        batchStockRepository.saveAll(listBatchStock);
    }

    public void putAll(List<BatchStock> listBatchStock, List<BatchStockDTO> listBatchStockDTO, AgentDTO agentDTO, SectionDTO sectionDTO) {
        for (int i = 0; i < listBatchStock.size(); i++) {
            for (int x = i; x < listBatchStockDTO.size(); x++) {
                if (productService.validProductSection(sectionDTO.getSectionCode()) &&
                        sectionService.validSectionLength(listBatchStock.get(i).getSection()))
                    listBatchStock.get(i).batchNumber(listBatchStockDTO.get(x).getBatchNumber());
                    listBatchStock.get(i).productId(listBatchStockDTO.get(x).getProductId());
                    listBatchStock.get(i).currentTemperature(listBatchStockDTO.get(x).getCurrentTemperature());
                    listBatchStock.get(i).minimumTemperature(listBatchStockDTO.get(x).getMinimumTemperature());
                    listBatchStock.get(i).initialQuantity(listBatchStockDTO.get(x).getInitialQuantity());
                    listBatchStock.get(i).currentQuantity(listBatchStockDTO.get(x).getCurrentQuantity());
                    listBatchStock.get(i).manufacturingDate(listBatchStockDTO.get(x).getManufacturingDate());
                    listBatchStock.get(i).manufacturingTime(listBatchStockDTO.get(x).getManufacturingTime());
                    listBatchStock.get(i).dueDate(listBatchStockDTO.get(x).getDueDate());
                    Agent agent = listBatchStock.get(i).getAgent();
                    agent.cpf(agentDTO.getCpf());
                    agent.name(agentDTO.getName());
                    listBatchStock.get(i).agent(agent);
                    x++;
            }
            batchStockRepository.save(listBatchStock.get(i));
        }
    }

    public void updateBatchStock(List<ProductPurchaseOrderDTO> productList) {
        productList.forEach(p -> {
            final List<BatchStock> listBatchStock = batchStockRepository.findAllByProductId(p.getProductId());
            if (listBatchStock.isEmpty()) {
                throw new BatchStockException("Nao foi encontrado estoque para esse produto!!!");
            }
            listBatchStock.forEach(b -> {
                b.setCurrentQuantity(b.getCurrentQuantity()-p.getQuantity());
                batchStockRepository.save(b);
            });
        });
    }
}
