package com.booking.controller;

import com.booking.dao.AccommodationDAO;
import com.booking.dao.ReviewDAO;
import com.booking.dao.impl.AccommodationDAOImpl;
import com.booking.dao.impl.ReviewDAOImpl;
import com.booking.model.Accommodation;

import java.util.List;

public class AccommodationController {
    private final AccommodationDAO accommodationDAO;
    private final ReviewDAO reviewDAO;
    
    public AccommodationController() {
        this.accommodationDAO = new AccommodationDAOImpl();
        this.reviewDAO = new ReviewDAOImpl();
    }
    
    public Accommodation getAccommodationById(int id) {
        return accommodationDAO.getById(id);
    }
    
    public List<Accommodation> getAllAccommodations() {
        return accommodationDAO.getAll();
    }
    
    public boolean addAccommodation(Accommodation accommodation) {
        return accommodationDAO.add(accommodation);
    }
    
    public boolean updateAccommodation(Accommodation accommodation) {
        return accommodationDAO.update(accommodation);
    }
    
    public boolean deleteAccommodation(int id) {
        return accommodationDAO.delete(id);
    }
    
    public List<Accommodation> searchByCity(String city) {
        return accommodationDAO.searchByCity(city);
    }
    
    public List<Accommodation> searchByCountry(String country) {
        return accommodationDAO.searchByCountry(country);
    }
    
    public List<Accommodation> searchByType(String accommodationType) {
        return accommodationDAO.searchByType(accommodationType);
    }
    
    public List<Accommodation> searchByPriceRange(double minPrice, double maxPrice) {
        return accommodationDAO.searchByPriceRange(minPrice, maxPrice);
    }
    
    public List<Accommodation> searchByRating(int minRating) {
        return accommodationDAO.searchByRating(minRating);
    }
    
    public List<Accommodation> searchWithFilters(String city, String country, String type, 
                                              double minPrice, double maxPrice, 
                                              int minRating, int maxGuests) {
        return accommodationDAO.searchWithFilters(city, country, type, minPrice, maxPrice, minRating, maxGuests);
    }
    
    public double getAverageRating(int accommodationId) {
        return reviewDAO.getAverageRatingForAccommodation(accommodationId);
    }
    
    public boolean createAccommodation(String name, String address, String city, String country, 
                                    String description, double pricePerNight, int maxGuests, 
                                    String accommodationType, boolean hasWifi, boolean hasPool, 
                                    boolean hasParkingSpace) {
        Accommodation accommodation = new Accommodation();
        accommodation.setName(name);
        accommodation.setAddress(address);
        accommodation.setCity(city);
        accommodation.setCountry(country);
        accommodation.setDescription(description);
        accommodation.setPricePerNight(pricePerNight);
        accommodation.setMaxGuests(maxGuests);
        accommodation.setAccommodationType(accommodationType);
        accommodation.setHasWifi(hasWifi);
        accommodation.setHasPool(hasPool);
        accommodation.setHasParkingSpace(hasParkingSpace);
        accommodation.setRating(0); // Initial rating
        
        return accommodationDAO.add(accommodation);
    }
}