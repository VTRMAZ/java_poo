package com.booking.dao;

import com.booking.model.Accommodation;
import java.util.List;

public interface AccommodationDAO {
    Accommodation getById(int id);
    List<Accommodation> getAll();
    boolean add(Accommodation accommodation);
    boolean update(Accommodation accommodation);
    boolean delete(int id);
    List<Accommodation> searchByCity(String city);
    List<Accommodation> searchByCountry(String country);
    List<Accommodation> searchByType(String accommodationType);
    List<Accommodation> searchByPriceRange(double minPrice, double maxPrice);
    List<Accommodation> searchByRating(int minRating);
    List<Accommodation> searchWithFilters(String city, String country, String type, double minPrice, double maxPrice, int minRating, int maxGuests);
}