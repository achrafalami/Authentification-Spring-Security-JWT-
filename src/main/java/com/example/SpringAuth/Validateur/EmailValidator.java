package com.example.SpringAuth.Validateur;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class EmailValidator {

    private static final String EMPLOYEE_EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@sorec\\.com$";
    private static final String PRESTATAIRE_EMAIL_REGEX = "^(?!.*@sorec\\.com)\\b[A-Za-z0-9+_.-]+@[A-Za-z0-9-]+\\.[A-Za-z]{2,3}\\b$";
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,3}$";


    private static final Pattern employeePattern = Pattern.compile(EMPLOYEE_EMAIL_REGEX);
    private static final Pattern EmailPattern = Pattern.compile(EMAIL_REGEX);

    private static final Pattern prestatairePattern = Pattern.compile(PRESTATAIRE_EMAIL_REGEX);
    public static boolean isValidEmail(String email) {
        Matcher matcher = EmailPattern.matcher(email);
        return matcher.matches();
    }
    public static boolean isValidEmployeeSorecEmail(String email) {
        Matcher matcher = employeePattern.matcher(email);
        return matcher.matches();
    }


    public static boolean isValidEmployePrestataireEmail(String email) {
        Matcher matcher = prestatairePattern.matcher(email);
        return matcher.matches();
    }
}
