package com.example.netbankingapp.ui.register;

import static org.junit.Assert.*;

import org.junit.Test;

public class RegisterValidatorTest {

    @Test
    public void validAccountNumber() {
        assertTrue(RegisterValidator.isValidAccountNumber("1234567890123456"));
    }

    @Test
    public void invalidAccountNumber_short() {
        assertFalse(RegisterValidator.isValidAccountNumber("12345"));
    }

    @Test
    public void validCifNumber() {
        assertTrue(RegisterValidator.isValidCifNumber("12345678901"));
    }

    @Test
    public void invalidCifNumber_letters() {
        assertFalse(RegisterValidator.isValidCifNumber("abcdef12345"));
    }

    @Test
    public void validTransactionPin() {
        assertTrue(RegisterValidator.isValidTransactionPin("1234"));
    }

    @Test
    public void invalidTransactionPin_not4Digits() {
        assertFalse(RegisterValidator.isValidTransactionPin("12"));
    }

}