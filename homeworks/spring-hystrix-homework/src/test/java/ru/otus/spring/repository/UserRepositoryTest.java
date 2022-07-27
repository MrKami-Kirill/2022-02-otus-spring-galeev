package ru.otus.spring.repository;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.spring.domain.*;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Data Mongo для работы с пользователями")
@DataMongoTest
public class UserRepositoryTest {

    private static final String FIRST_USER_NAME = "USER";

    @Autowired
    private UserRepository userRepository;

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @DisplayName("Должен сохранять нового пользователя")
    @Test
    void shouldSaveNewUser() {
        final String expectedNewUsername = "TEST";
        val actualNewUser = userRepository.save(new User(expectedNewUsername, "$2a$12$4JRoWnNV64pNQmcrU.yL3OfUzn.HIVDe2rBMF3/MsjuBE0vSxAiKS", "ROLE_TEST"));
        val expectedNewUser = userRepository.findByUsername(expectedNewUsername);
        assertThat(expectedNewUser).isPresent().get()
                .usingRecursiveComparison().isEqualTo(actualNewUser);
    }

    @DisplayName("Должен загружать информацию о нужном пользователе по id")
    @Test
    void shouldFindExpectedUserById() {
        val actualUser = userRepository.findByUsername(FIRST_USER_NAME);
        assertThat(actualUser).isPresent();
        assertThat(userRepository.findById(actualUser.get().getId())).isPresent();
    }

    @DisplayName("Должен загружать информацию о нужном пользовател по имени")
    @Test
    void shouldFindExpectedUserByUsername() {
        assertThat(userRepository.findByUsername(FIRST_USER_NAME)).isPresent();
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @DisplayName("Должен удалять пользователя по id")
    @Test
    void shouldDeleteUserById() {
        val deleteUser = userRepository.findByUsername(FIRST_USER_NAME);
        assertThat(deleteUser).isPresent();
        userRepository.deleteById(deleteUser.get().getId());
        val expectedUser = userRepository.findByUsername(FIRST_USER_NAME);
        assertThat(expectedUser).isEmpty();
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @DisplayName("Должен удалять пользователя по имени")
    @Test
    void shouldDeleteUserByUsername() {
        val deleteUser = userRepository.findByUsername(FIRST_USER_NAME);
        assertThat(deleteUser).isPresent();
        userRepository.deleteByUsername(FIRST_USER_NAME);
        val expectedUser = userRepository.findByUsername(FIRST_USER_NAME);
        assertThat(expectedUser).isEmpty();
    }
}
