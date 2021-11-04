package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.exception.SectionException;
import br.com.meli.projetointegrador.exception.ValidInputException;
import br.com.meli.projetointegrador.model.entity.BatchStock;
import br.com.meli.projetointegrador.model.entity.Section;
import br.com.meli.projetointegrador.model.repository.BatchStockRepository;
import br.com.meli.projetointegrador.model.repository.SectionRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Jhony Zuim / Lucas Pereira / Edmilson Nobre / Rafael Vicente
 * @version 1.0.0
 * @since 15/10/2021
 * Camada service responsavel pela regra de negocio relacionada ao section
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
     * @param sectionCode recebe um section para validar se ele existe
     * @return true ou exception personalizada
     */
    public Boolean validSection(String sectionCode) {
        if (sectionRepository.existsSectionBySectionCode(sectionCode)){
            return true;
        } else {
            throw new SectionException("Nao existe esse setor, por gentileza verificar o setor!");
        }
    }

    /**
     * @param section recebe uma section;
     * @return verdadeiro ou exception.
     */
    public Boolean validSectionLength(Section section) {
        if (batchStockRepository.countBySection(section) < section.getMaxLength())
            return true;
        else
            throw new SectionException("Nao tem espaco.");
    }

    /**
     * @param batchStock recebe um batchStock;
     * @return verdadeiro ou exception.
     */
    public Boolean validSectionInformed(BatchStock batchStock) {
        if(!(batchStock.getSection()==null))
            return true;
        else
            throw new ValidInputException("Sessao nao informada!!! Reenviar com uma sessao  existente!");
    }

    /**
     * @param sectionCode recebe um codigo de section;
     * @return section ou exception caso nao exista.
     */
    public Section find(String sectionCode) {
        Optional<Section> section = sectionRepository.findBySectionCode(sectionCode);
        if (section.isEmpty()) {
            throw new SectionException("Sessao nao existe!!! Reenviar com uma sessao valida");
        }
        return section.get();
    }
}