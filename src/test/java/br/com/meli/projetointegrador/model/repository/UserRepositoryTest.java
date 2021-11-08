package br.com.meli.projetointegrador.model.repository;

import br.com.meli.projetointegrador.model.entity.Role;
import br.com.meli.projetointegrador.model.entity.User;
import br.com.meli.projetointegrador.model.enums.ERole;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataMongoTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        roleRepository.deleteAll();

        Role role = new Role();
        role.setName(ERole.ROLE_ADMIN);

        roleRepository.save(role);

        Set<Role> roles = new HashSet<>();
        roles.add(role);

        User user = new User();
        user.setUsername("lucas");
        user.setPassword("12345678");
        user.setEmail("lucas@gmail.com");
        user.setRoles(roles);

        userRepository.save(user);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    void findByUsername() {
        assertTrue(userRepository.findByUsername("lucas").isPresent());
    }

    @Test
    void existsByUsername() {
        assertTrue(userRepository.existsByUsername("lucas"));
    }

    @Test
    void existsByEmail() {
        assertTrue(userRepository.existsByEmail("lucas@gmail.com"));
    }
}