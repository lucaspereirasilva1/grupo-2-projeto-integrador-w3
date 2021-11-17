package br.com.meli.projetointegrador.model.service;

import br.com.meli.projetointegrador.exception.SectionException;
import br.com.meli.projetointegrador.exception.ValidInputException;
import br.com.meli.projetointegrador.model.entity.BatchStock;
import br.com.meli.projetointegrador.model.entity.Section;
import br.com.meli.projetointegrador.model.entity.Warehouse;
import br.com.meli.projetointegrador.model.repository.BatchStockRepository;
import br.com.meli.projetointegrador.model.repository.SectionRepository;
import br.com.meli.projetointegrador.model.repository.WarehouseRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Jhony Zuim / Lucas Pereira / Edmilson Nobre / Rafael Vicente
 * @version 1.0.0
 * @since 15/10/2021
 * Camada de teste integrado do service responsavel pela regra de negocio relacionada ao section
 */

@SpringBootTest
class SectionServiceIntegrationTest {

    @Autowired
    private SectionService sectionService;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private BatchStockRepository batchStockRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    /**
     * @author Edemilson Nobre
     * Povoa o banco
     */
    @BeforeEach
    void setUp(){
        Warehouse warehouse = new Warehouse()
                .warehouseCode("RS")
                .warehouseName("POA")
                .build();
        warehouseRepository.save(warehouse);

        Section section = new Section()
                .sectionCode("LA")
                .sectionName("Laticionios")
                .maxLength(3)
                .warehouse(warehouse)
                .build();
        sectionRepository.save(section);

        BatchStock batchStock1 = new BatchStock()
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
        batchStockRepository.save(batchStock1);

        BatchStock batchStock2 = new BatchStock()
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
        batchStockRepository.save(batchStock2);
    }

    /**
     * @author Edemilson Nobre
     * Chama a rotina pra linpar o banco
     */
    @AfterEach
    void cleanUpDataBase(){
        clearBase();
    }

    /**
     * @author Edemilson Nobre
     * Teste para validar se uma section existe
     */
    @Test
    void validSectionExist(){
        assertTrue(sectionRepository.findBySectionCode("LA").isPresent());
    }

    /**
     * @author Edemilson Nobre
     * Teste para validar se uma section NAO existe
     */
    @Test
    void validSectionNotExist(){
        SectionException sectionException = assertThrows(SectionException.class, () ->
                sectionService.validSection("L5A"));
        String expectedMessage = "Nao existe esse setor, por gentileza verificar o setor!";
        assertTrue(expectedMessage.contains(sectionException.getMessage()));
    }

    /**
     * @author Edemilson Nobre
     * Teste para validar se uma section NAO esta cheia
     */
    @Test
    void validSectionNotFull() {
        Optional<Section> sectionOptional = sectionRepository.findBySectionCode("LA");
        assertTrue(sectionService.validSectionLength(sectionOptional.orElse(new Section())));
    }

    /**
     * @author Edemilson Nobre
     * Teste para validar se uma section esta cheia
     */
    @Test
    void validSectionFull() {
        Optional<Section> sectionOptional = sectionRepository.findBySectionCode("LA");
        BatchStock batchStock = new BatchStock()
                .batchNumber(10)
                .productId("QJ")
                .currentTemperature(10.0F)
                .minimumTemperature(5.0F)
                .initialQuantity(1)
                .currentQuantity(5)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalDateTime.now())
                .dueDate(LocalDate.now())
                .section(sectionOptional.orElse(new Section()))
                .build();
        batchStockRepository.save(batchStock);

        Section section = sectionOptional.orElse(new Section());
        SectionException sectionException = assertThrows
                (SectionException.class,() -> sectionService.validSectionLength(section));

        String mensagemEsperada = "Nao tem espaco.";
        String mensagemRecebida = sectionException.getMessage();

        assertTrue(mensagemEsperada.contains(mensagemRecebida));
    }

    /**
     * @author Edemilson Nobre
     * Teste quando a section nao passada no batchStock
     */
    @Test
    void validSectionInformed(){
        Section section = new Section()
                .sectionCode("LA")
                .sectionName("Laticionios")
                .maxLength(3)
                .build();

        BatchStock batchStock = new BatchStock()
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

        assertTrue(sectionService.validSectionInformed(batchStock));
    }

    /**
     * @author Edemilson Nobre
     * Teste quando a section nao passada no batchStock
     */
    @Test
    void validSectionNotInformed(){
        BatchStock batchStock = new BatchStock()
                .batchNumber(1)
                .productId("QJ")
                .currentTemperature(10.0F)
                .minimumTemperature(5.0F)
                .initialQuantity(1)
                .currentQuantity(5)
                .manufacturingDate(LocalDate.now())
                .manufacturingTime(LocalDateTime.now())
                .dueDate(LocalDate.now())
                .build();

        ValidInputException validInputException = assertThrows
                (ValidInputException.class,() -> sectionService.validSectionInformed(batchStock));

        String mensagemEsperada = "Sessao nao informada!!! Reenviar com uma sessao  existente!";
        String mensagemRecebida = validInputException.getMessage();

        assertTrue(mensagemEsperada.contains(mensagemRecebida));
    }

    @Test
    void findIntegrationTest() {
        final Section section = sectionService.find("LA");
        assertEquals("LA", section.getSectionCode());
    }

    @Test
    void findNotExistIntegrationTest() {
        SectionException sectionException = assertThrows
                (SectionException.class,() -> sectionService.find("XX"));

        String mensagemEsperada = "Sessao nao existe!!! Reenviar com uma sessao valida";
        String mensagemRecebida = sectionException.getMessage();

        assertTrue(mensagemEsperada.contains(mensagemRecebida));
    }

    /**
     * @author Edemilson Nobre
     * deixa vo Banco linpo
     */
    void clearBase() {
        warehouseRepository.deleteAll();
        sectionRepository.deleteAll();
        batchStockRepository.deleteAll();
    }
}
