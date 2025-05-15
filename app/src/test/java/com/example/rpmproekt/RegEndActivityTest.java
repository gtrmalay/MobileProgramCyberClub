package com.example.rpmproekt;

import com.example.tdsclub.models.RegisterResponse;
import org.junit.Test;

public class RegEndActivityTest {

    @Test
    public void validateFields_EmptyName_ReturnsFalse() {

        String name = "";
        String email = "test@example.com";
        String password = "password123";
        String confirmPassword = "password123";


        boolean isValid = isValidInput(name, email, password, confirmPassword);


        if (isValid) {
            throw new AssertionError("Validation should fail with empty name");
        }
    }

    @Test
    public void validateFields_EmptyEmail_ReturnsFalse() {

        String name = "John Doe";
        String email = "";
        String password = "password123";
        String confirmPassword = "password123";


        boolean isValid = isValidInput(name, email, password, confirmPassword);


        if (isValid) {
            throw new AssertionError("Validation should fail with empty email");
        }
    }

    @Test
    public void validateFields_ShortPassword_ReturnsFalse() {
        // Arrange
        String name = "John Doe";
        String email = "test@example.com";
        String password = "pass";
        String confirmPassword = "pass";

        // Act
        boolean isValid = isValidInput(name, email, password, confirmPassword);

        // Assert
        if (isValid) {
            throw new AssertionError("Validation should fail with short password");
        }
    }

    @Test
    public void validateFields_MismatchedPasswords_ReturnsFalse() {

        String name = "John Doe";
        String email = "test@example.com";
        String password = "password123";
        String confirmPassword = "different123";


        boolean isValid = isValidInput(name, email, password, confirmPassword);

        if (isValid) {
            throw new AssertionError("Validation should fail with mismatched passwords");
        }
    }

    @Test
    public void validateFields_ValidInput_ReturnsTrue() {

        String name = "John Doe";
        String email = "test@example.com";
        String password = "password123";
        String confirmPassword = "password123";


        boolean isValid = isValidInput(name, email, password, confirmPassword);


        if (!isValid) {
            throw new AssertionError("Validation should pass with valid input");
        }
    }

    @Test
    public void handleSuccessfulResponse_ReturnsExpectedToken() {

        RegisterResponse response = new RegisterResponse();
        response.setToken("mock-token");


        String token = handleResponse(response);


        if (!"mock-token".equals(token)) {
            throw new AssertionError("handleResponse should return token 'mock-token', but returned '" + token + "'");
        }
    }

    @Test
    public void handleNullResponse_ReturnsEmptyToken() {

        RegisterResponse response = null;


        String token = handleResponse(response);


        if (!"".equals(token)) {
            throw new AssertionError("handleResponse should return empty token for null response, but returned '" + token + "'");
        }
    }


    private boolean isValidInput(String name, String email, String password, String confirmPassword) {
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            return false;
        }
        if (password.length() < 6) {
            return false;
        }
        return password.equals(confirmPassword);
    }


    private String handleResponse(RegisterResponse response) {
        if (response != null && response.getToken() != null) {
            return response.getToken();
        }
        return "";
    }
}