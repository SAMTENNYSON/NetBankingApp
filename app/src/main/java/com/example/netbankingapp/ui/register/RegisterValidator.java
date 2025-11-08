package com.example.netbankingapp.ui.register;

public class RegisterValidator {
    public static boolean isValidAccountNumber(String acc) {
        return acc != null && acc.matches("\\d{16}");
    }

    public static boolean isValidCifNumber(String cif) {
        return cif != null && cif.matches("\\d{11}");
    }

    public static boolean isValidBranchCode(String branch) {
        return branch != null && branch.matches("\\d{5}");
    }

    public static boolean isValidTransactionPin(String tpin) {
        return tpin != null && tpin.matches("\\d{4}");
    }
}
