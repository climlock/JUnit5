package com.mishanya.junit.service;

import com.mishanya.junit.dto.User;
import org.junit.jupiter.api.*;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

/*
    TestInstance.Lifecycle.PER_CLASS - Создаём только 1 объект класса для всех тестов
    estInstance.Lifecycle.PER_METHOD
 */

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserServiceTest {

    private static final User IVAN = User.of(1, "Ivan", "123");
    private static final User PETR = User.of(2, "Petr", "111");
    private UserService userService;


    @BeforeAll
    void init(){
        System.out.println("Before all: " + this);

    }

    @BeforeEach
    void prepare() {
        System.out.println("Before each: " + this );
        userService = new UserService();
    }

    @Test
    void userEmptyIfUserAdded(){
        System.out.println("Test 1: " + this );
        var users = userService.getAll();

        // () -> "List should be empty" - Description
        /*
        Also can be used:
        assertTrue
        assertFalse
         */

        assertTrue(users.isEmpty(), () -> "List should be empty");
    }

    @Test
    void userSizeIfUserAdded(){
        System.out.println("Test 2: " + this );
        userService.add(IVAN);
        userService.add(PETR);

        var users = userService.getAll();

        assertThat(users).hasSize(2);
//        assertEquals(2, users.size());
    }

    @Test
    void usersConvertedToMapById() {
        userService.add(IVAN, PETR);

        Map<Integer, User> users = userService.getAllConvertedById();

        assertAll(
                () -> assertThat(users).containsKeys(IVAN.getId(), PETR.getId()),
                () -> assertThat(users).containsValues(IVAN, PETR)
        );
    }

    @Test
    void loginSuccessIfUserExists(){
        userService.add(IVAN);

        Optional<User> maybeUser = userService.login(IVAN.getUsername(), IVAN.getPassword());

        assertThat(maybeUser).isPresent();
//        assertTrue(maybeUser.isPresent());

        maybeUser.ifPresent(user -> assertThat(user).isEqualTo(IVAN));
//        maybeUser.ifPresent(user -> assertEquals(IVAN, user));
    }

    @Test
    void throwExceptionIfUserNameOrPasswordIsNull() {
//        try{
//            userService.login(null, "222");
//            Assertions.fail("Login should throws exception on null username");
//
//        } catch (IllegalArgumentException e) {
//            assertTrue(true);
//        }

//  Более удобный аналог
//        assertThrows(IllegalArgumentException.class, () ->  userService.login(null, "222"));

        assertAll(
                () ->assertThrows(IllegalArgumentException.class, () ->  userService.login(null, "222")),
                () -> assertThrows(IllegalArgumentException.class, () ->  userService.login("222", null))
        );
    }

    @Test
    void loginFailIfLoginPasswordInNotCorrect(){
        userService.add(IVAN);

        var maybeUser = userService.login(IVAN.getUsername(), "dummy");

        assertTrue(maybeUser.isEmpty());
    }
    @Test
    void loginFailIfUserDoesNotExist(){
        userService.add(IVAN);

        var maybeUser = userService.login("dummy", IVAN.getPassword());

        assertTrue(maybeUser.isEmpty());
    }

    @AfterEach
    void deleteDataFromDatabase() {
        System.out.println("After each: " + this);
    }

    @AfterAll
    void closeConnectionPool() {
        System.out.println("After all: " + this);

    }
}
