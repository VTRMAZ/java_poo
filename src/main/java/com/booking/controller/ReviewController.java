package com.booking.controller;

import com.booking.dao.ReviewDAO;
import com.booking.dao.UserDAO;
import com.booking.dao.impl.ReviewDAOImpl;
import com.booking.dao.impl.UserDAOImpl;
import com.booking.model.Review;
import com.booking.model.User;

import java.time.LocalDate;
import java.util.List;

public class ReviewController {
    private final ReviewDAO reviewDAO;
    private final UserDAO userDAO;
    
    public ReviewController() {
        this.reviewDAO = new ReviewDAOImpl();
        this.userDAO = new UserDAOImpl();
    }
    
    public Review getReviewById(int id) {
        Review review = reviewDAO.getById(id);
        if (review != null) {
            // Enrich with user data
            enrichReviewWithUser(review);
        }
        return review;
    }
    
    public List<Review> getAllReviews() {
        List<Review> reviews = reviewDAO.getAll();
        // Enrich all reviews with user data
        enrichReviewsWithUsers(reviews);
        return reviews;
    }
    
    public boolean addReview(Review review) {
        return reviewDAO.add(review);
    }
    
    public boolean updateReview(Review review) {
        return reviewDAO.update(review);
    }
    
    public boolean deleteReview(int id) {
        return reviewDAO.delete(id);
    }
    
    public List<Review> getReviewsByUserId(int userId) {
        List<Review> reviews = reviewDAO.getByUserId(userId);
        // Enrich all reviews with user data
        enrichReviewsWithUsers(reviews);
        return reviews;
    }
    
    public List<Review> getReviewsByAccommodationId(int accommodationId) {
        List<Review> reviews = reviewDAO.getByAccommodationId(accommodationId);
        // Enrich all reviews with user data
        enrichReviewsWithUsers(reviews);
        return reviews;
    }
    
    public List<Review> getReviewsByBookingId(int bookingId) {
        List<Review> reviews = reviewDAO.getByBookingId(bookingId);
        // Enrich all reviews with user data
        enrichReviewsWithUsers(reviews);
        return reviews;
    }
    
    public double getAverageRatingForAccommodation(int accommodationId) {
        return reviewDAO.getAverageRatingForAccommodation(accommodationId);
    }
    
    public boolean createReview(int userId, int accommodationId, int bookingId, int rating, String comment) {
        // Validate rating
        if (rating < 1 || rating > 5) {
            return false;
        }
        
        Review review = new Review();
        review.setUserId(userId);
        review.setAccommodationId(accommodationId);
        review.setBookingId(bookingId);
        review.setRating(rating);
        review.setComment(comment);
        review.setReviewDate(LocalDate.now());
        review.setHelpfulVotes(0); // Initialize helpful votes to 0
        
        return reviewDAO.add(review);
    }
    
    public boolean createDetailedReview(int userId, int accommodationId, int bookingId, 
                                      int rating, int cleanlinessRating, int comfortRating, 
                                      int locationRating, int valueRating, String comment) {
        // Validate ratings
        if (rating < 1 || rating > 5 || 
            cleanlinessRating < 1 || cleanlinessRating > 5 ||
            comfortRating < 1 || comfortRating > 5 ||
            locationRating < 1 || locationRating > 5 ||
            valueRating < 1 || valueRating > 5) {
            return false;
        }
        
        Review review = new Review();
        review.setUserId(userId);
        review.setAccommodationId(accommodationId);
        review.setBookingId(bookingId);
        review.setRating(rating);
        review.setCleanlinessRating(cleanlinessRating);
        review.setComfortRating(comfortRating);
        review.setLocationRating(locationRating);
        review.setValueRating(valueRating);
        review.setComment(comment);
        review.setReviewDate(LocalDate.now());
        review.setHelpfulVotes(0);
        
        return reviewDAO.add(review);
    }
    
    public boolean addHelpfulVote(int reviewId) {
        Review review = reviewDAO.getById(reviewId);
        if (review != null) {
            review.setHelpfulVotes(review.getHelpfulVotes() + 1);
            return reviewDAO.update(review);
        }
        return false;
    }
    
    // Helper methods to enrich reviews with user data
    private void enrichReviewWithUser(Review review) {
        if (review != null) {
            User user = userDAO.getById(review.getUserId());
            review.setUser(user);
        }
    }
    
    private void enrichReviewsWithUsers(List<Review> reviews) {
        if (reviews != null) {
            for (Review review : reviews) {
                enrichReviewWithUser(review);
            }
        }
    }
}