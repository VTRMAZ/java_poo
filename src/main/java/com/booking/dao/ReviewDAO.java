package com.booking.dao;

import com.booking.model.Review;
import java.util.List;

public interface ReviewDAO {
    Review getById(int id);
    List<Review> getAll();
    boolean add(Review review);
    boolean update(Review review);
    boolean delete(int id);
    List<Review> getByUserId(int userId);
    List<Review> getByAccommodationId(int accommodationId);
    List<Review> getByBookingId(int bookingId);
    double getAverageRatingForAccommodation(int accommodationId);
}