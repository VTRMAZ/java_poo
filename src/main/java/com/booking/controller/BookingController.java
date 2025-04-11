package com.booking.controller;

import com.booking.dao.BookingDAO;
import com.booking.dao.impl.BookingDAOImpl;
import com.booking.model.Booking;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class BookingController {
    private final BookingDAO bookingDAO;
    
    public BookingController() {
        this.bookingDAO = new BookingDAOImpl();
    }
    
    public Booking getBookingById(int id) {
        return bookingDAO.getById(id);
    }
    
    public List<Booking> getAllBookings() {
        return bookingDAO.getAll();
    }
    
    public boolean addBooking(Booking booking) {
        return bookingDAO.add(booking);
    }
    
    public boolean updateBooking(Booking booking) {
        return bookingDAO.update(booking);
    }
    
    public boolean deleteBooking(int id) {
        return bookingDAO.delete(id);
    }
    
    public List<Booking> getBookingsByUserId(int userId) {
        return bookingDAO.getByUserId(userId);
    }
    
    public List<Booking> getBookingsByAccommodationId(int accommodationId) {
        return bookingDAO.getByAccommodationId(accommodationId);
    }
    
    public List<Booking> getBookingsByDateRange(LocalDate startDate, LocalDate endDate) {
        return bookingDAO.getByDateRange(startDate, endDate);
    }
    
    public List<Booking> getBookingsByStatus(String status) {
        return bookingDAO.getByStatus(status);
    }
    
    public boolean isAccommodationAvailable(int accommodationId, LocalDate checkInDate, LocalDate checkOutDate) {
        return bookingDAO.isAccommodationAvailable(accommodationId, checkInDate, checkOutDate);
    }
    
    public boolean createBooking(int userId, int accommodationId, LocalDate checkInDate, LocalDate checkOutDate, 
                              int numberOfGuests, double pricePerNight, double discountPercentage) {
        // Check if accommodation is available for the selected dates
        if (!isAccommodationAvailable(accommodationId, checkInDate, checkOutDate)) {
            return false;
        }
        
        // Calculate number of nights
        long numberOfNights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        
        // Calculate total price with discount
        double totalPrice = pricePerNight * numberOfNights * numberOfGuests;
        if (discountPercentage > 0) {
            totalPrice = totalPrice * (1 - (discountPercentage / 100));
        }
        
        // Create new booking
        Booking booking = new Booking();
        booking.setUserId(userId);
        booking.setAccommodationId(accommodationId);
        booking.setCheckInDate(checkInDate);
        booking.setCheckOutDate(checkOutDate);
        booking.setNumberOfGuests(numberOfGuests);
        booking.setTotalPrice(totalPrice);
        booking.setStatus("PENDING"); // Initial status
        booking.setBookingDate(LocalDate.now());
        
        return bookingDAO.add(booking);
    }
    
    public boolean confirmBooking(int bookingId) {
        Booking booking = bookingDAO.getById(bookingId);
        if (booking != null) {
            booking.setStatus("CONFIRMED");
            return bookingDAO.update(booking);
        }
        return false;
    }
    
    public boolean cancelBooking(int bookingId) {
        Booking booking = bookingDAO.getById(bookingId);
        if (booking != null) {
            booking.setStatus("CANCELLED");
            return bookingDAO.update(booking);
        }
        return false;
    }
    
    public boolean completeBooking(int bookingId) {
        Booking booking = bookingDAO.getById(bookingId);
        if (booking != null) {
            booking.setStatus("COMPLETED");
            return bookingDAO.update(booking);
        }
        return false;
    }
}