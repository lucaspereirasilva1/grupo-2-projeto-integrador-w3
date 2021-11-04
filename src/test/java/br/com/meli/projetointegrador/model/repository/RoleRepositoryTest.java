package br.com.meli.projetointegrador.model.repository;

import br.com.meli.projetointegrador.model.entity.Role;
import br.com.meli.projetointegrador.model.enums.ERole;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataMongoTest
class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;
    @BeforeEach
    void setUp() {
        roleRepository.deleteAll();

        Role role = new Role();
        role.setName(ERole.ROLE_ADMIN);

        roleRepository.save(role);
    }

    @AfterEach
    void tearDown() {
        roleRepository.deleteAll();
    }

    @Test
    void findByName() {
        assertTrue(roleRepository.findByName(ERole.ROLE_ADMIN).isPresent());
    }
}