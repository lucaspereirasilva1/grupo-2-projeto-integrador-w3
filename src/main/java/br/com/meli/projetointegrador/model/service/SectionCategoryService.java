package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.exception.SectionCategoryException;
import br.com.meli.projetointegrador.model.entity.SectionCategory;
import br.com.meli.projetointegrador.model.enums.ESectionCategory;
import br.com.meli.projetointegrador.model.repository.SectionCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SectionCategoryService {

    private final SectionCategoryRepository sectionCategoryRepository;

    public SectionCategoryService(SectionCategoryRepository sectionCategoryRepository) {
        this.sectionCategoryRepository = sectionCategoryRepository;
    }

    public SectionCategory find(ESectionCategory eSectionCategory) {
        final Optional<SectionCategory> sectionCategory = sectionCategoryRepository.findByName(eSectionCategory);
        if (sectionCategory.isEmpty()) {
            throw new SectionCategoryException("Categoria " + eSectionCategory.toString() + " nao encontrada!!!");
        }
        return sectionCategory.get();
    }

}
