package com.booking.dao;

import com.booking.model.Booking;
import java.time.LocalDate;
import java.util.List;

public interface BookingDAO {
    Booking getById(int id);
    List<Booking> getAll();
    boolean add(Booking booking);
    boolean update(Booking booking);
    boolean delete(int id);
    List<Booking> getByUserId(int userId);
    List<Booking> getByAccommodationId(int accommodationId);
    List<Booking> getByDateRange(LocalDate startDate, LocalDate endDate);
    List<Booking> getByStatus(String status);
    boolean isAccommodationAvailable(int accommodationId, LocalDate checkInDate, LocalDate checkOutDate);
}