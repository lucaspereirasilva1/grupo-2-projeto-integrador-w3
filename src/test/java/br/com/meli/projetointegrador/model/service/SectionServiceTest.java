package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.model.entity.Section;
import br.com.meli.projetointegrador.model.entity.Warehouse;
import br.com.meli.projetointegrador.model.repository.SectionRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SectionServiceTest {

    private SectionRepository mockSectionRepository = mock(SectionRepository.class);
    private SectionService sectionService = new SectionService(mockSectionRepository);

    @Test
    void validSectionExistTest(){

        Warehouse warehouse = new Warehouse()
                .warehouseCode("MG")
                .warehouseName("Sao Paulo")
                .build();

        Section section = new Section()
                .id("1")
                .sectionCode("LA")
                .sectionName("Laticionios")
                .maxLength(10)
                .warehouse(warehouse)
                .build();

        when(mockSectionRepository.findById(any()))
                .thenReturn(Optional.of(new Section()
                        .id("1")
                        .sectionCode("LA")
                        .sectionName("Laticionios")
                        .maxLength(10)
                        .warehouse(warehouse)
                        .build()));

        assertTrue(sectionService.validSection(section));
    }

}
