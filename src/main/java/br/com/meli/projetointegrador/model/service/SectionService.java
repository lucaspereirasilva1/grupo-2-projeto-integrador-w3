package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.exception.SectionException;
import br.com.meli.projetointegrador.exception.ValidInputException;
import br.com.meli.projetointegrador.model.entity.BatchStock;
import br.com.meli.projetointegrador.model.entity.Section;
import br.com.meli.projetointegrador.model.repository.BatchStockRepository;
import br.com.meli.projetointegrador.model.repository.SectionRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Optional;

/**
 * @author Jhony Zuim - Edemilson Nobre
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

    /**
     * @author Jhony Zuim
     * @param section, recebe um section para validar se ele existe
     * @return true ou exception personalizada
     */

    public boolean validSection(Section section) {
        Optional<Section> sectionOptional = sectionRepository.findBySectionCode(section.getSectionCode());
        if (sectionOptional.isPresent())
            return true;
        else
            throw new SectionException("Nao existe esse setor, por gentileza verificar o setor!");
    }

    /**
     * @author Edemilson Nobre
     * Teste para validar se uma section tem espaco livre
     */
    public Boolean validSectionLength(Section section) {
        if (batchStockRepository.countBySection(section) < section.getMaxLength())
            return true;
        else
            throw new SectionException("Nao tem espaco.");
    }

    /**
     * @author Edemilson Nobre
     * Teste para validar se uma section existe
     */
    public Boolean validSectionInformed(BatchStock batchStock) {
        if(!(batchStock.getSection()==null))
            return true;
        else
            throw new ValidInputException("Sessao nao informada!!! Reenviar com uma sessao  existente!");
    }
}
