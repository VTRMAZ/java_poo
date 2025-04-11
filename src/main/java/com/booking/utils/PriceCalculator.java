package com.booking.utils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class PriceCalculator {
    
    public static double calculateTotalPrice(double pricePerNight, LocalDate checkInDate, LocalDate checkOutDate, int numberOfGuests) {
        if (checkInDate == null || checkOutDate == null || checkInDate.isAfter(checkOutDate)) {
            return 0;
        }
        
        long numberOfNights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        return pricePerNight * numberOfNights * numberOfGuests;
    }
    
    public static double applyDiscount(double price, double discountPercentage) {
        if (discountPercentage <= 0 || discountPercentage > 100) {
            return price;
        }
        
        return price * (1 - (discountPercentage / 100));
    }
    
    public static double calculateTotalPriceWithDiscount(double pricePerNight, LocalDate checkInDate, LocalDate checkOutDate, 
                                                     int numberOfGuests, double discountPercentage) {
        double totalPrice = calculateTotalPrice(pricePerNight, checkInDate, checkOutDate, numberOfGuests);
        return applyDiscount(totalPrice, discountPercentage);
    }
    
    public static double calculateAverageNightlyRate(double totalPrice, LocalDate checkInDate, LocalDate checkOutDate) {
        if (checkInDate == null || checkOutDate == null || checkInDate.isAfter(checkOutDate)) {
            return 0;
        }
        
        long numberOfNights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        if (numberOfNights == 0) {
            return 0;
        }
        
        return totalPrice / numberOfNights;
    }
    
    public static double calculateTax(double price, double taxRate) {
        if (taxRate < 0) {
            return 0;
        }
        
        return price * (taxRate / 100);
    }
    
    public static double calculateTotalWithTax(double price, double taxRate) {
        return price + calculateTax(price, taxRate);
    }
    
    public static double roundToTwoDecimalPlaces(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}