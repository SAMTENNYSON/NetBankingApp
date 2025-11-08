package com.example.netbankingapp.ui.login;

import static org.junit.Assert.*;

import org.junit.Test;

public class LoginValidatorTest {

    @Test
    public void testValidCif() {
        assertTrue(LoginValidator.isValidCif("12345678901")); // ✅ 11 digits
    }


    @Test
    public void testInvalidCif() {
        assertFalse(LoginValidator.isValidCif("12345")); // ❌ too short
    }

    @Test
    public void testValidPassword() {
        assertTrue(LoginValidator.isValidPassword("myPass123"));
    }

    @Test
    public void testInvalidPassword() {
        assertFalse(LoginValidator.isValidPassword("")); // ❌ empty
    }

}