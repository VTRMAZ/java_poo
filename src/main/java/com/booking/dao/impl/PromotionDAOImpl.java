package com.booking.dao.impl;

import com.booking.dao.PromotionDAO;
import com.booking.model.Promotion;
import com.booking.utils.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PromotionDAOImpl implements PromotionDAO {
    
    @Override
    public Promotion getById(int id) {
        String query = "SELECT * FROM promotions WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return extractPromotionFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public List<Promotion> getAll() {
        List<Promotion> promotions = new ArrayList<>();
        String query = "SELECT * FROM promotions";
        
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            
            while (resultSet.next()) {
                promotions.add(extractPromotionFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return promotions;
    }
    
    @Override
    public boolean add(Promotion promotion) {
        String query = "INSERT INTO promotions (accommodation_id, name, description, discount_percentage, " +
                       "start_date, end_date, promo_code) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            statement.setInt(1, promotion.getAccommodationId());
            statement.setString(2, promotion.getName());
            statement.setString(3, promotion.getDescription());
            statement.setDouble(4, promotion.getDiscountPercentage());
            statement.setDate(5, Date.valueOf(promotion.getStartDate()));
            statement.setDate(6, Date.valueOf(promotion.getEndDate()));
            statement.setString(7, promotion.getPromoCode());
            
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        promotion.setId(generatedKeys.getInt(1));
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
    public boolean update(Promotion promotion) {
        String query = "UPDATE promotions SET accommodation_id = ?, name = ?, description = ?, " +
                       "discount_percentage = ?, start_date = ?, end_date = ?, promo_code = ? WHERE id = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setInt(1, promotion.getAccommodationId());
            statement.setString(2, promotion.getName());
            statement.setString(3, promotion.getDescription());
            statement.setDouble(4, promotion.getDiscountPercentage());
            statement.setDate(5, Date.valueOf(promotion.getStartDate()));
            statement.setDate(6, Date.valueOf(promotion.getEndDate()));
            statement.setString(7, promotion.getPromoCode());
            statement.setInt(8, promotion.getId());
            
            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    @Override
    public boolean delete(int id) {
        String query = "DELETE FROM promotions WHERE id = ?";
        
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
    public List<Promotion> getByAccommodationId(int accommodationId) {
        List<Promotion> promotions = new ArrayList<>();
        String query = "SELECT * FROM promotions WHERE accommodation_id = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setInt(1, accommodationId);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    promotions.add(extractPromotionFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return promotions;
    }
    
    @Override
    public List<Promotion> getActivePromotions(LocalDate date) {
        List<Promotion> promotions = new ArrayList<>();
        String query = "SELECT * FROM promotions WHERE start_date <= ? AND end_date >= ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setDate(1, Date.valueOf(date));
            statement.setDate(2, Date.valueOf(date));
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    promotions.add(extractPromotionFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return promotions;
    }
    
    @Override
    public Promotion getByPromoCode(String promoCode) {
        String query = "SELECT * FROM promotions WHERE promo_code = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setString(1, promoCode);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return extractPromotionFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    private Promotion extractPromotionFromResultSet(ResultSet resultSet) throws SQLException {
        Promotion promotion = new Promotion();
        promotion.setId(resultSet.getInt("id"));
        promotion.setAccommodationId(resultSet.getInt("accommodation_id"));
        promotion.setName(resultSet.getString("name"));
        promotion.setDescription(resultSet.getString("description"));
        promotion.setDiscountPercentage(resultSet.getDouble("discount_percentage"));
        promotion.setStartDate(resultSet.getDate("start_date").toLocalDate());
        promotion.setEndDate(resultSet.getDate("end_date").toLocalDate());
        promotion.setPromoCode(resultSet.getString("promo_code"));
        
        return promotion;
    }
}