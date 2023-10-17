package com.mishanya.junit.service;

import com.mishanya.junit.dto.User;
import com.mishanya.junit.paramresolver.UserServiceParamResolver;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

/*
    TestInstance.Lifecycle.PER_CLASS - Создаём только 1 объект класса для всех тестов
    estInstance.Lifecycle.PER_METHOD
 */
@Tag("fast")
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestMethodOrder(MethodOrderer.DisplayName.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith({
        UserServiceParamResolver.class
})
public class UserServiceTest {

    private static final User IVAN = User.of(1, "Ivan", "123");
    private static final User PETR = User.of(2, "Petr", "111");
    private UserService userService;

    UserServiceTest(TestInfo testInfo) {
        System.out.println();
    }


    @BeforeAll
    void init(){
        System.out.println("Before all: " + this);

    }

    @BeforeEach
    void prepare(UserService userService) {
        System.out.println("Before each: " + this );
        this.userService = userService;
    }

    @Test
    @Order(1)
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
    @Order(2)
    void userSizeIfUserAdded(){
        System.out.println("Test 2: " + this );
        userService.add(IVAN);
        userService.add(PETR);

        var users = userService.getAll();

        assertThat(users).hasSize(2);
//        assertEquals(2, users.size());
    }

    @Test
    @DisplayName("Users converted to map by id")
    void usersConvertedToMapById() {
        userService.add(IVAN, PETR);

        Map<Integer, User> users = userService.getAllConvertedById();

        assertAll(
                () -> assertThat(users).containsKeys(IVAN.getId(), PETR.getId()),
                () -> assertThat(users).containsValues(IVAN, PETR)
        );
    }

    @AfterEach
    void deleteDataFromDatabase() {
        System.out.println("After each: " + this);
    }

    @AfterAll
    void closeConnectionPool() {
        System.out.println("After all: " + this);

    }
    @Nested
    @DisplayName("Test user login functionality")
    @Tag("login")
    class LoginTest {
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

        @ParameterizedTest
//        @ArgumentsSource()
//        @NullSource       // {
//        @EmptySource      //  Only 1 param
//        @ValueSource      //}
//        @NullAndEmptySource
//        @ValueSource(strings = {
//                "Ivan", "Petr"
//        })
        @MethodSource("com.mishanya.junit.service.UserServiceTest#getArgumentsForLoginTest")
//        @CsvFileSource(resources = "/login-test-data.csv", delimiter = ',', numLinesToSkip = 1)
//        @CsvSource({
//                "Ivan,123",
//                "Petr,111"
//        })
        void loginParametrizedTest(String username, String password, Optional<User> user) {
            userService.add(IVAN, PETR);

            var maybeUser = userService.login(username, password);
            assertThat(maybeUser).isEqualTo(user);
        }

    }
    static Stream<Arguments> getArgumentsForLoginTest() {
        return Stream.of(
                Arguments.of("Ivan", "123", Optional.of(IVAN)),
                Arguments.of("Petr", "111", Optional.of(PETR)),
                Arguments.of("Petr", "123", Optional.empty()),
                Arguments.of("Sara", "111", Optional.empty())
        );
    }
}
