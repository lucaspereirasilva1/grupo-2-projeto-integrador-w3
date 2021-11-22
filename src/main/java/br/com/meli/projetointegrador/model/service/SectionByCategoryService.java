package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.exception.ProductException;
import br.com.meli.projetointegrador.model.entity.Section;
import br.com.meli.projetointegrador.model.entity.SectionByCategory;
import br.com.meli.projetointegrador.model.entity.SectionCategory;
import br.com.meli.projetointegrador.model.repository.SectionByCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SectionByCategoryService {

    private final SectionByCategoryRepository sectionByCategoryRepository;

    public SectionByCategoryService(SectionByCategoryRepository sectionByCategoryRepository) {
        this.sectionByCategoryRepository = sectionByCategoryRepository;
    }

    public Boolean validProductSection(Section section, SectionCategory sectionCategory){
        Optional<SectionByCategory> existsProductBySection = sectionByCategoryRepository.findByCategory(sectionCategory);
        if (existsProductBySection.isPresent()){
            if (existsProductBySection.get().getSection().equals(section)) {
                return true;
            }else {
                throw new ProductException("Produto nao faz parte do setor, por favor verifique o setor correto!");
            }
        } else {
            throw new ProductException("Categoria e setor nao parametrizados!!! Por gentileza verificar o base");
        }
    }

}
