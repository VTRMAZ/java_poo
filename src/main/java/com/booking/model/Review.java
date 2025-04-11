package com.booking.model;

import java.time.LocalDate;

public class Review {
    private int id;
    private int userId;
    private int accommodationId;
    private int bookingId;
    private int rating; // 1-5 stars
    private String comment;
    private LocalDate reviewDate;
    
    // New properties
    private int cleanlinessRating;
    private int comfortRating;
    private int locationRating;
    private int valueRating;
    private int helpfulVotes;
    private User user; // Reference to the User object
    
    public Review() {}
    
    public Review(int id, int userId, int accommodationId, int bookingId, int rating, String comment, LocalDate reviewDate) {
        this.id = id;
        this.userId = userId;
        this.accommodationId = accommodationId;
        this.bookingId = bookingId;
        this.rating = rating;
        this.comment = comment;
        this.reviewDate = reviewDate;
        this.helpfulVotes = 0; // Default value
    }
    
    // Full constructor with new properties
    public Review(int id, int userId, int accommodationId, int bookingId, int rating, 
                  int cleanlinessRating, int comfortRating, int locationRating, int valueRating,
                  String comment, LocalDate reviewDate, int helpfulVotes) {
        this.id = id;
        this.userId = userId;
        this.accommodationId = accommodationId;
        this.bookingId = bookingId;
        this.rating = rating;
        this.cleanlinessRating = cleanlinessRating;
        this.comfortRating = comfortRating;
        this.locationRating = locationRating;
        this.valueRating = valueRating;
        this.comment = comment;
        this.reviewDate = reviewDate;
        this.helpfulVotes = helpfulVotes;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public int getAccommodationId() {
        return accommodationId;
    }
    
    public void setAccommodationId(int accommodationId) {
        this.accommodationId = accommodationId;
    }
    
    public int getBookingId() {
        return bookingId;
    }
    
    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }
    
    public int getRating() {
        return rating;
    }
    
    public void setRating(int rating) {
        this.rating = rating;
    }
    
    public String getComment() {
        return comment;
    }
    
    public void setComment(String comment) {
        this.comment = comment;
    }
    
    public LocalDate getReviewDate() {
        return reviewDate;
    }
    
    public void setReviewDate(LocalDate reviewDate) {
        this.reviewDate = reviewDate;
    }
    
    // Getters and setters for new properties
    public int getCleanlinessRating() {
        return cleanlinessRating;
    }
    
    public void setCleanlinessRating(int cleanlinessRating) {
        this.cleanlinessRating = cleanlinessRating;
    }
    
    public int getComfortRating() {
        return comfortRating;
    }
    
    public void setComfortRating(int comfortRating) {
        this.comfortRating = comfortRating;
    }
    
    public int getLocationRating() {
        return locationRating;
    }
    
    public void setLocationRating(int locationRating) {
        this.locationRating = locationRating;
    }
    
    public int getValueRating() {
        return valueRating;
    }
    
    public void setValueRating(int valueRating) {
        this.valueRating = valueRating;
    }
    
    public int getHelpfulVotes() {
        return helpfulVotes;
    }
    
    public void setHelpfulVotes(int helpfulVotes) {
        this.helpfulVotes = helpfulVotes;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", userId=" + userId +
                ", accommodationId=" + accommodationId +
                ", bookingId=" + bookingId +
                ", rating=" + rating +
                ", comment='" + comment + '\'' +
                ", reviewDate=" + reviewDate +
                '}';
    }
}