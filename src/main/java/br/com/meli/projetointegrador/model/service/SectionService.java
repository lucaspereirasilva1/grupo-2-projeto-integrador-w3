package br.com.meli.projetointegrador.model.service;

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

    private final SectionRepository sectionRepository;
    private final BatchStockRepository batchStockRepository;

    public SectionService(BatchStockRepository batchStockRepository,
                          SectionRepository sectionRepository) {
        this.batchStockRepository = batchStockRepository;
        this.sectionRepository = sectionRepository;
    }

    public Section find(String sectionCode) {
        Optional<Section> section = sectionRepository.findBySectionCode(sectionCode);
        if (section.isEmpty()) {
            throw new SectionException("Sessao nao existe!!! Reenviar com uma sessao valida");
        }
        return section.get();
     }

    /**
     * @author Jhony Zuim
     * @param section, recebe um section para validar se ele existe
     * @return true ou exception personalizada
     */

    public boolean validSection(String sectionCode) {
        Optional<Section> sectionOptional = sectionRepository.findBySectionCode(sectionCode);
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
