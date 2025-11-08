package com.example.netbankingapp.ui.login;

public class LoginValidator {

    public static boolean isValidCif(String cif) {
        // CIF must be exactly 11 digits
        return cif != null && cif.matches("\\d{11}");
    }

    public static boolean isValidPassword(String password) {
        // Password must not be empty
        return password != null && !password.trim().isEmpty();
    }
}
