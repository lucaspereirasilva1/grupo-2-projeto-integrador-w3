package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.exception.SectionException;
import br.com.meli.projetointegrador.model.entity.BatchStock;
import br.com.meli.projetointegrador.model.entity.Section;
import br.com.meli.projetointegrador.model.entity.Warehouse;
import br.com.meli.projetointegrador.model.repository.BatchStockRepository;
import br.com.meli.projetointegrador.model.repository.SectionRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Jhony Zuim
 * @version 1.0.0
 * @since 15/10/2021
 */

public class SectionServiceTest {

    private final SectionRepository mockSectionRepository = mock(SectionRepository.class);
    private final SectionService sectionService = new SectionService(mockSectionRepository);

    private final BatchStockRepository mockBatchStockRepository=  mock(BatchStockRepository.class);
    private final SectionService sectionService2 = new SectionService(mockBatchStockRepository);

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


    @Test
    void validSectionFullTest() {
        List<BatchStock> listBatchStock = new ArrayList<>();

        Warehouse warehouse = new Warehouse()
                .warehouseCode("RS")
                .warehouseName("POA")
                .build();

        Section section = new Section()
                .id("1")
                .sectionCode("LA")
                .sectionName("Laticionios")
                .maxLength(3)
                .warehouse(warehouse)
                .build();

        BatchStock batchStock1 = new BatchStock()
                .id("1")
                .batchNumber(1)
                .productId("QJ")
                .currentTemperature(10.0F)
                .minimumTemperature(5.0F)
                .initialQuantity(1)
                .currentQuantity(5)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalDateTime.now())
                .dueDate(LocalDate.now())
                .section(section)
                .build();

        BatchStock batchStock2 = new BatchStock()
                .id("2")
                .batchNumber(2)
                .productId("QJ")
                .currentTemperature(10.0F)
                .minimumTemperature(5.0F)
                .initialQuantity(1)
                .currentQuantity(5)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalDateTime.now())
                .dueDate(LocalDate.now())
                .section(section)
                .build();

        BatchStock batchStock3 = new BatchStock()
                .id("3")
                .batchNumber(3)
                .productId("QJ")
                .currentTemperature(10.0F)
                .minimumTemperature(5.0F)
                .initialQuantity(1)
                .currentQuantity(5)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalDateTime.now())
                .dueDate(LocalDate.now())
                .section(section)
                .build();

        listBatchStock.add(batchStock1);
        listBatchStock.add(batchStock2);
        listBatchStock.add(batchStock3);

        when(mockBatchStockRepository.countBySection(any()))
                .thenReturn((long) listBatchStock.size());

        SectionException sectionException = assertThrows
                (SectionException.class,() -> sectionService2.validSectionLength(section));

        String mensagemEsperada = "Nao tem espaco.";
        String mensagemRecebida = sectionException.getMessage();

        assertTrue(mensagemEsperada.contains(mensagemRecebida));
    }

    @Test
    void validSectionNoFullTest() {
        List<BatchStock> listBatchStock = new ArrayList<>();

        Warehouse warehouse = new Warehouse()
                .warehouseCode("RS")
                .warehouseName("POA")
                .build();

        Section section = new Section()
                .id("1")
                .sectionCode("LA")
                .sectionName("Laticionios")
                .maxLength(3)
                .warehouse(warehouse)
                .build();

        BatchStock batchStock1 = new BatchStock()
                .id("1")
                .batchNumber(1)
                .productId("QJ")
                .currentTemperature(10.0F)
                .minimumTemperature(5.0F)
                .initialQuantity(1)
                .currentQuantity(5)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalDateTime.now())
                .dueDate(LocalDate.now())
                .section(section)
                .build();

        BatchStock batchStock2 = new BatchStock()
                .id("2")
                .batchNumber(2)
                .productId("LA")
                .currentTemperature(10.0F)
                .minimumTemperature(5.0F)
                .initialQuantity(1)
                .currentQuantity(5)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalDateTime.now())
                .dueDate(LocalDate.now())
                .section(section)
                .build();

        listBatchStock.add(batchStock1);
        listBatchStock.add(batchStock2);

        when(mockBatchStockRepository.countBySection(any()))
                .thenReturn((long) listBatchStock.size());

        assertTrue(sectionService2.validSectionLength(section));
    }
}
