package com.works;

import com.works.util.Action;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MainAppTest {

    static Action action;

    @BeforeAll
    static void setUpClass() {
        System.out.println("MainAppTest.setUpClass");
        action = new Action();
    }

    @BeforeEach
    void setUp() {
        System.out.println("MainAppTest.setUp");
    }

    @Test
    @Order(1)
    @DisplayName("Add < 70")
    void main() {
        int add = action.add();
        assertTrue(add < 70);
    }

    @Test
    @Order(2)
    @DisplayName("Update = 80")
    void contextLoads() {
        int update = action.update();
        assertEquals(update, 80);
    }

    @Test
    @Order(3)
    @DisplayName("Delete < 90")
    void test() {
        int delete = action.delete();
        assertFalse(delete > 90);
    }

    @Test
    @Order(4)
    @DisplayName("Email Validation")
    void emailValidation() {
        boolean emailValidation = action.emailValidation("ali@");
        assertTrue(emailValidation);
    }

    @AfterEach
    void tearDown() {
        System.out.println("MainAppTest.tearDown");
    }

    @AfterAll
    static void tearDownClass() {
        System.out.println("MainAppTest.tearDownClass");
    }

}
