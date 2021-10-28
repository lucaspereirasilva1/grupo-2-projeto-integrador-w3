package br.com.meli.projetointegrador.model.repository;

import br.com.meli.projetointegrador.model.entity.*;
import br.com.meli.projetointegrador.util.SectionCategory;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
public class SaveDataRepositoryTest {

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Test
    void saveProduct() {
        productRepository.deleteAll();

        List<Product> listProduct = new ArrayList<>();
        final Optional<Section> section = sectionRepository.findBySectionCode("FR");

        Product product = new Product()
                .productId("LE")
                .productName("leite")
                .section(section.orElse(new Section()))
                .build();

        Product productUm = new Product()
                .productName("QJ")
                .productName("queijo")
                .section(section.orElse(new Section()))
                .build();

        listProduct.add(product);
        listProduct.add(productUm);

        productRepository.saveAll(listProduct);
    }

    @Test
    void saveTotal() {
        productRepository.deleteAll();
        warehouseRepository.deleteAll();
        sectionRepository.deleteAll();
        agentRepository.deleteAll();

        Warehouse warehouse = new Warehouse()
                .warehouseCode("SP")
                .warehouseName("sao paulo")
                .build();

        warehouseRepository.save(warehouse);

        Warehouse warehouseMG = new Warehouse()
                .warehouseCode("MG")
                .warehouseName("Minas Gerais")
                .build();

        warehouseRepository.save(warehouse);

        Section section = new Section()
                .sectionCode("LA")
                .sectionName("laticinios")
                .maxLength(10)
                .warehouse(warehouse)
                .build();

        sectionRepository.save(section);

        Section sectionCO = new Section()
                .sectionCode("CO")
                .sectionName("Congelados")
                .maxLength(10)
                .warehouse(warehouseMG)
                .build();

        sectionRepository.save(sectionCO);

        Product product = new Product()
                .productId("LE")
                .productName("leite")
                .section(section)
                .sectionCategory(SectionCategory.FF)
                .build();

        productRepository.save(product);

        Product productDois = new Product()
                .productId("DA")
                .productName("danone")
                .section(section)
                .sectionCategory(SectionCategory.FF)
                .build();

        productRepository.save(productDois);

        Agent agent = new Agent().
                cpf("11122233344").
                name("lucas").
                warehouse(warehouse).
                build();

        agentRepository.save(agent);

    }

    @Test
    void saveUser() {

    }

}
