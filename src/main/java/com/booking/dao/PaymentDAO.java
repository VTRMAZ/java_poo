package com.booking.dao;

import com.booking.model.Payment;
import java.time.LocalDateTime;
import java.util.List;

public interface PaymentDAO {
    Payment getById(int id);
    List<Payment> getAll();
    boolean add(Payment payment);
    boolean update(Payment payment);
    boolean delete(int id);
    List<Payment> getByBookingId(int bookingId);
    List<Payment> getByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    List<Payment> getByStatus(String status);
    double getTotalRevenue();
    double getTotalRevenueByDateRange(LocalDateTime startDate, LocalDateTime endDate);
}