package com.booking.controller;

import com.booking.dao.PaymentDAO;
import com.booking.dao.impl.PaymentDAOImpl;
import com.booking.model.Payment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class PaymentController {
    private final PaymentDAO paymentDAO;
    private final BookingController bookingController;
    
    public PaymentController() {
        this.paymentDAO = new PaymentDAOImpl();
        this.bookingController = new BookingController();
    }
    
    public Payment getPaymentById(int id) {
        return paymentDAO.getById(id);
    }
    
    public List<Payment> getAllPayments() {
        return paymentDAO.getAll();
    }
    
    public boolean addPayment(Payment payment) {
        return paymentDAO.add(payment);
    }
    
    public boolean updatePayment(Payment payment) {
        return paymentDAO.update(payment);
    }
    
    public boolean deletePayment(int id) {
        return paymentDAO.delete(id);
    }
    
    public List<Payment> getPaymentsByBookingId(int bookingId) {
        return paymentDAO.getByBookingId(bookingId);
    }
    
    public List<Payment> getPaymentsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return paymentDAO.getByDateRange(startDate, endDate);
    }
    
    public List<Payment> getPaymentsByStatus(String status) {
        return paymentDAO.getByStatus(status);
    }
    
    public double getTotalRevenue() {
        return paymentDAO.getTotalRevenue();
    }
    
    public double getTotalRevenueByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return paymentDAO.getTotalRevenueByDateRange(startDate, endDate);
    }
    
    public boolean processPayment(int bookingId, double amount, String paymentMethod) {
        // Create a new payment
        Payment payment = new Payment();
        payment.setBookingId(bookingId);
        payment.setAmount(amount);
        payment.setPaymentMethod(paymentMethod);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setStatus("PENDING");
        payment.setTransactionId(generateTransactionId());
        
        // Save payment to database
        boolean paymentAdded = paymentDAO.add(payment);
        if (!paymentAdded) {
            return false;
        }
        
        // Simulate payment processing
        boolean paymentSuccess = simulatePaymentProcessing();
        
        if (paymentSuccess) {
            // Update payment status to completed
            payment.setStatus("COMPLETED");
            paymentDAO.update(payment);
            
            // Confirm the booking
            bookingController.confirmBooking(bookingId);
            
            return true;
        } else {
            // Update payment status to failed
            payment.setStatus("FAILED");
            paymentDAO.update(payment);
            
            return false;
        }
    }
    
    private String generateTransactionId() {
        return "TRX-" + LocalDateTime.now().toString().substring(0, 10).replace("-", "") + "-" + UUID.randomUUID().toString().substring(0, 6);
    }
    
    private boolean simulatePaymentProcessing() {
        // Simulate payment processing with a 95% success rate
        return Math.random() < 0.95;
    }
    
    public boolean refundPayment(int paymentId) {
        Payment payment = paymentDAO.getById(paymentId);
        if (payment != null && "COMPLETED".equals(payment.getStatus())) {
            payment.setStatus("REFUNDED");
            return paymentDAO.update(payment);
        }
        return false;
    }
}