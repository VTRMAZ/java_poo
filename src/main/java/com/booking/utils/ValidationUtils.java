package com.booking.utils;

import java.util.regex.Pattern;

public class ValidationUtils {
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[0-9]{10,15}$");
    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MIN_USERNAME_LENGTH = 4;
    
    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }
    
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.isEmpty()) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone).matches();
    }
    
    public static boolean isValidPassword(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }
        
        // Check length
        if (password.length() < MIN_PASSWORD_LENGTH) {
            return false;
        }
        
        // Check for at least one digit
        if (!password.matches(".*\\d.*")) {
            return false;
        }
        
        // Check for at least one uppercase letter
        if (!password.matches(".*[A-Z].*")) {
            return false;
        }
        
        // Check for at least one lowercase letter
        if (!password.matches(".*[a-z].*")) {
            return false;
        }
        
        return true;
    }
    
    public static boolean isValidUsername(String username) {
        if (username == null || username.isEmpty()) {
            return false;
        }
        
        // Check length
        if (username.length() < MIN_USERNAME_LENGTH) {
            return false;
        }
        
        // Check for alphanumeric characters and underscore only
        return username.matches("^[a-zA-Z0-9_]+$");
    }
    
    public static boolean isPositiveNumber(double number) {
        return number > 0;
    }
    
    public static boolean isPositiveNumber(int number) {
        return number > 0;
    }
    
    public static boolean isValidRating(int rating) {
        return rating >= 1 && rating <= 5;
    }
    
    public static boolean isValidPercentage(double percentage) {
        return percentage >= 0 && percentage <= 100;
    }
    
    public static boolean isNotEmpty(String value) {
        return value != null && !value.isEmpty();
    }
    
    public static String sanitizeInput(String input) {
        if (input == null) {
            return "";
        }
        // Remove HTML tags
        input = input.replaceAll("<[^>]*>", "");
        // Remove SQL injection characters
        input = input.replaceAll("['\"\\\\;]", "");
        return input.trim();
    }
}