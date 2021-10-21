package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.exception.SectionExecption;
import br.com.meli.projetointegrador.model.entity.Section;
import br.com.meli.projetointegrador.model.repository.SectionRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SectionService {

    private final SectionRepository sectionRepository;

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
}
