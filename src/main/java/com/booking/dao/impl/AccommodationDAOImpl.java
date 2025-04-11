package com.booking.dao.impl;

import com.booking.dao.AccommodationDAO;
import com.booking.model.Accommodation;
import com.booking.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccommodationDAOImpl implements AccommodationDAO {
    
    @Override
    public Accommodation getById(int id) {
        String query = "SELECT * FROM accommodations WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return extractAccommodationFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public List<Accommodation> getAll() {
        List<Accommodation> accommodations = new ArrayList<>();
        String query = "SELECT * FROM accommodations";
        
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            
            while (resultSet.next()) {
                accommodations.add(extractAccommodationFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return accommodations;
    }
    
    @Override
    public boolean add(Accommodation accommodation) {
        String query = "INSERT INTO accommodations (name, address, city, country, description, price_per_night, max_guests, " +
                       "accommodation_type, has_wifi, has_pool, has_parking_space, rating) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            statement.setString(1, accommodation.getName());
            statement.setString(2, accommodation.getAddress());
            statement.setString(3, accommodation.getCity());
            statement.setString(4, accommodation.getCountry());
            statement.setString(5, accommodation.getDescription());
            statement.setDouble(6, accommodation.getPricePerNight());
            statement.setInt(7, accommodation.getMaxGuests());
            statement.setString(8, accommodation.getAccommodationType());
            statement.setBoolean(9, accommodation.isHasWifi());
            statement.setBoolean(10, accommodation.isHasPool());
            statement.setBoolean(11, accommodation.isHasParkingSpace());
            statement.setInt(12, accommodation.getRating());
            
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        accommodation.setId(generatedKeys.getInt(1));
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    @Override
    public boolean update(Accommodation accommodation) {
        String query = "UPDATE accommodations SET name = ?, address = ?, city = ?, country = ?, description = ?, " +
                       "price_per_night = ?, max_guests = ?, accommodation_type = ?, has_wifi = ?, has_pool = ?, " +
                       "has_parking_space = ?, rating = ? WHERE id = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setString(1, accommodation.getName());
            statement.setString(2, accommodation.getAddress());
            statement.setString(3, accommodation.getCity());
            statement.setString(4, accommodation.getCountry());
            statement.setString(5, accommodation.getDescription());
            statement.setDouble(6, accommodation.getPricePerNight());
            statement.setInt(7, accommodation.getMaxGuests());
            statement.setString(8, accommodation.getAccommodationType());
            statement.setBoolean(9, accommodation.isHasWifi());
            statement.setBoolean(10, accommodation.isHasPool());
            statement.setBoolean(11, accommodation.isHasParkingSpace());
            statement.setInt(12, accommodation.getRating());
            statement.setInt(13, accommodation.getId());
            
            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    @Override
    public boolean delete(int id) {
        String query = "DELETE FROM accommodations WHERE id = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setInt(1, id);
            
            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    @Override
    public List<Accommodation> searchByCity(String city) {
        List<Accommodation> accommodations = new ArrayList<>();
        String query = "SELECT * FROM accommodations WHERE city LIKE ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setString(1, "%" + city + "%");
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    accommodations.add(extractAccommodationFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return accommodations;
    }
    
    @Override
    public List<Accommodation> searchByCountry(String country) {
        List<Accommodation> accommodations = new ArrayList<>();
        String query = "SELECT * FROM accommodations WHERE country LIKE ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setString(1, "%" + country + "%");
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    accommodations.add(extractAccommodationFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return accommodations;
    }
    
    @Override
    public List<Accommodation> searchByType(String accommodationType) {
        List<Accommodation> accommodations = new ArrayList<>();
        String query = "SELECT * FROM accommodations WHERE accommodation_type = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setString(1, accommodationType);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    accommodations.add(extractAccommodationFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return accommodations;
    }
    
    @Override
    public List<Accommodation> searchByPriceRange(double minPrice, double maxPrice) {
        List<Accommodation> accommodations = new ArrayList<>();
        String query = "SELECT * FROM accommodations WHERE price_per_night BETWEEN ? AND ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setDouble(1, minPrice);
            statement.setDouble(2, maxPrice);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    accommodations.add(extractAccommodationFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return accommodations;
    }
    
    @Override
    public List<Accommodation> searchByRating(int minRating) {
        List<Accommodation> accommodations = new ArrayList<>();
        String query = "SELECT * FROM accommodations WHERE rating >= ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setInt(1, minRating);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    accommodations.add(extractAccommodationFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return accommodations;
    }
    
    @Override
    public List<Accommodation> searchWithFilters(String city, String country, String type, double minPrice, double maxPrice, int minRating, int maxGuests) {
        List<Accommodation> accommodations = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM accommodations WHERE 1=1");
        List<Object> parameters = new ArrayList<>();
        
        if (city != null && !city.isEmpty()) {
            queryBuilder.append(" AND city LIKE ?");
            parameters.add("%" + city + "%");
        }
        
        if (country != null && !country.isEmpty()) {
            queryBuilder.append(" AND country LIKE ?");
            parameters.add("%" + country + "%");
        }
        
        if (type != null && !type.isEmpty()) {
            queryBuilder.append(" AND accommodation_type = ?");
            parameters.add(type);
        }
        
        if (minPrice >= 0) {
            queryBuilder.append(" AND price_per_night >= ?");
            parameters.add(minPrice);
        }
        
        if (maxPrice > 0) {
            queryBuilder.append(" AND price_per_night <= ?");
            parameters.add(maxPrice);
        }
        
        if (minRating > 0) {
            queryBuilder.append(" AND rating >= ?");
            parameters.add(minRating);
        }
        
        if (maxGuests > 0) {
            queryBuilder.append(" AND max_guests >= ?");
            parameters.add(maxGuests);
        }
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(queryBuilder.toString())) {
            
            // Set parameters
            for (int i = 0; i < parameters.size(); i++) {
                Object param = parameters.get(i);
                if (param instanceof String) {
                    statement.setString(i + 1, (String) param);
                } else if (param instanceof Integer) {
                    statement.setInt(i + 1, (Integer) param);
                } else if (param instanceof Double) {
                    statement.setDouble(i + 1, (Double) param);
                }
            }
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    accommodations.add(extractAccommodationFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return accommodations;
    }
    
    private Accommodation extractAccommodationFromResultSet(ResultSet resultSet) throws SQLException {
        Accommodation accommodation = new Accommodation();
        accommodation.setId(resultSet.getInt("id"));
        accommodation.setName(resultSet.getString("name"));
        accommodation.setAddress(resultSet.getString("address"));
        accommodation.setCity(resultSet.getString("city"));
        accommodation.setCountry(resultSet.getString("country"));
        accommodation.setDescription(resultSet.getString("description"));
        accommodation.setPricePerNight(resultSet.getDouble("price_per_night"));
        accommodation.setMaxGuests(resultSet.getInt("max_guests"));
        accommodation.setAccommodationType(resultSet.getString("accommodation_type"));
        accommodation.setHasWifi(resultSet.getBoolean("has_wifi"));
        accommodation.setHasPool(resultSet.getBoolean("has_pool"));
        accommodation.setHasParkingSpace(resultSet.getBoolean("has_parking_space"));
        accommodation.setRating(resultSet.getInt("rating"));
        
        return accommodation;
    }
}