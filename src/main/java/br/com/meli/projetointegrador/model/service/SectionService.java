package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.exception.SectionExecption;
import br.com.meli.projetointegrador.exception.SectionException;
import br.com.meli.projetointegrador.model.entity.Section;
import br.com.meli.projetointegrador.model.repository.BatchStockRepository;
import br.com.meli.projetointegrador.model.repository.SectionRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Jhony Zuim
 * @version 1.0.0
 * @since 15/10/2021
 */

@Service
public class SectionService {

    private SectionRepository sectionRepository;
    private BatchStockRepository batchStockRepository;

    public SectionService(BatchStockRepository batchStockRepository) {
        this.batchStockRepository = batchStockRepository;
    }

    public SectionService(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    public Section find(String sectionCode) {
        Optional<Section> section = sectionRepository.findBySectionCode(sectionCode);
        if (section.isEmpty()) {
            throw new SectionExecption("Sessao nao existe!!! Reenviar com uma sessao valida");
        }
        return section.get();
     }

    /**
     * @author Jhony Zuim
     * @version 1.0.0
     * @param section, recebe um section para validar se ele existe
     * @return true ou exception personalizada
     */

    public boolean validSection(Section section) {
        Optional<Section> sectionOptional = sectionRepository.findById(section.getId());
        if (sectionOptional.isPresent()){
            return true;
        } else {
            throw new SectionException("Nao existe esse setor, por gentileza verificar o setor!");
        }
    }

    public Boolean validSectionLength(Section section) {
        if (batchStockRepository.countBySection(section) < section.getMaxLength())
            return true;
        else
            throw new SectionException("Nao tem espaco.");
    }
}
