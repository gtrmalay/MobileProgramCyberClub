package com.example.rpmproekt;

import com.example.tdsclub.models.User;
import org.junit.Test;
import static org.junit.Assert.*;

public class UserTest {

    @Test
    public void testGetFullName_ReturnsFullNameWhenSet() {
        User user = new User();
        user.setFullName("John FullName");

        assertEquals("John FullName", user.getFullName());
    }

    @Test
    public void testGetFullName_ReturnsNameWhenFullNameNotSet() {
        User user = new User();
        user.setName("John Name");

        assertEquals("John Name", user.getFullName());
    }

    @Test
    public void testGetFullName_ReturnsFioWhenFullNameAndNameNotSet() {
        User user = new User();
        user.setFio("John Fio");

        assertEquals("John Fio", user.getFullName());
    }

    @Test
    public void testGetFullName_ReturnsNullWhenNoNamesSet() {
        User user = new User();

        assertNull(user.getFullName());
    }

    @Test
    public void testSetAndGetEmail() {
        User user = new User();
        user.setEmail("john@example.com");

        assertEquals("john@example.com", user.getEmail());
    }

    @Test
    public void testToString() {
        User user = new User();
        user.setFullName("Full Name");
        user.setName("Name");
        user.setFio("Fio");
        user.setEmail("email@example.com");

        String expected = "User{fullName='Full Name', name='Name', fio='Fio', email='email@example.com'}";
        assertEquals(expected, user.toString());
    }
}
