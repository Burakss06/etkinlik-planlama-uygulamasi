package com.works.repository;

import com.works.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import org.springframework.context.annotation.Import;
import com.works.CacheTestConfig;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Import(CacheTestConfig.class)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindByEmailIgnoreCase() {
        User user = new User();
        user.setName("Burak");
        user.setSurname("Senol");
        user.setEmail("burak@outlook.com");
        user.setEnabled(true);
        user.setPassword("123456");
        userRepository.saveAndFlush(user);

        System.out.println("ALL USERS IN DB: " + userRepository.findAll());

        Optional<User> found = userRepository.findByEmailIgnoreCase("BURAK@OUTLOOK.COM");
        assertTrue(found.isPresent());
        assertEquals("Burak", found.get().getName());
    }

    @Test
    public void testFindByEnabledTrueAndEmailIgnoreCase() {
        User user = new User();
        user.setName("Burak");
        user.setSurname("Senol");
        user.setEmail("burak@outlook.com");
        user.setEnabled(true);
        user.setPassword("123456");
        userRepository.saveAndFlush(user);

        Optional<User> found = userRepository.findByEnabledTrueAndEmailIgnoreCase("BURAK@OUTLOOK.COM");
        assertTrue(found.isPresent());
        assertEquals("Burak", found.get().getName());
    }
}
