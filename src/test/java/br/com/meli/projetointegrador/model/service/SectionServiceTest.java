package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.exception.SectionException;
import br.com.meli.projetointegrador.model.entity.Section;
import br.com.meli.projetointegrador.model.entity.Warehouse;
import br.com.meli.projetointegrador.model.repository.SectionRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Jhony Zuim
 * @version 1.0.0
 * @since 15/10/2021
 */

public class SectionServiceTest {

    private SectionRepository mockSectionRepository = mock(SectionRepository.class);
    private SectionService sectionService = new SectionService(mockSectionRepository);

    /**
     * @author Jhony Zuim
     * @version 1.0.0
     *  Teste para validar se uma section existe
     */

    @Test
    void validSectionExistTest(){

        Warehouse warehouse = new Warehouse()
                .warehouseCode("SP")
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

    /**
     * @author Jhony Zuim
     * @version 1.0.0
     *  Teste para validar se uma section nao existe
     */

    @Test
    void validSectionNotExistTest(){

        Warehouse warehouse = new Warehouse()
                .warehouseCode("SP")
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
                .thenReturn(Optional.empty());

        SectionException sectionException = assertThrows(SectionException.class, () ->
                sectionService.validSection(section));

        String expectedMessage = "Nao existe esse setor, por gentileza verificar o setor!";

        assertTrue(expectedMessage.contains(sectionException.getMessage()));
    }
}
