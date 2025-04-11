package com.booking.dao.impl;

import com.booking.dao.BookingDAO;
import com.booking.model.Booking;
import com.booking.utils.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookingDAOImpl implements BookingDAO {
    
    @Override
    public Booking getById(int id) {
        String query = "SELECT * FROM bookings WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return extractBookingFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public List<Booking> getAll() {
        List<Booking> bookings = new ArrayList<>();
        String query = "SELECT * FROM bookings";
        
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            
            while (resultSet.next()) {
                bookings.add(extractBookingFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return bookings;
    }
    
    @Override
    public boolean add(Booking booking) {
        String query = "INSERT INTO bookings (user_id, accommodation_id, check_in_date, check_out_date, " +
                       "number_of_guests, total_price, status, booking_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            statement.setInt(1, booking.getUserId());
            statement.setInt(2, booking.getAccommodationId());
            statement.setDate(3, Date.valueOf(booking.getCheckInDate()));
            statement.setDate(4, Date.valueOf(booking.getCheckOutDate()));
            statement.setInt(5, booking.getNumberOfGuests());
            statement.setDouble(6, booking.getTotalPrice());
            statement.setString(7, booking.getStatus());
            statement.setDate(8, Date.valueOf(booking.getBookingDate()));
            
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        booking.setId(generatedKeys.getInt(1));
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
    public boolean update(Booking booking) {
        String query = "UPDATE bookings SET user_id = ?, accommodation_id = ?, check_in_date = ?, " +
                       "check_out_date = ?, number_of_guests = ?, total_price = ?, status = ?, booking_date = ? " +
                       "WHERE id = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setInt(1, booking.getUserId());
            statement.setInt(2, booking.getAccommodationId());
            statement.setDate(3, Date.valueOf(booking.getCheckInDate()));
            statement.setDate(4, Date.valueOf(booking.getCheckOutDate()));
            statement.setInt(5, booking.getNumberOfGuests());
            statement.setDouble(6, booking.getTotalPrice());
            statement.setString(7, booking.getStatus());
            statement.setDate(8, Date.valueOf(booking.getBookingDate()));
            statement.setInt(9, booking.getId());
            
            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    @Override
    public boolean delete(int id) {
        String query = "DELETE FROM bookings WHERE id = ?";
        
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
    public List<Booking> getByUserId(int userId) {
        List<Booking> bookings = new ArrayList<>();
        String query = "SELECT * FROM bookings WHERE user_id = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setInt(1, userId);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    bookings.add(extractBookingFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return bookings;
    }
    
    @Override
    public List<Booking> getByAccommodationId(int accommodationId) {
        List<Booking> bookings = new ArrayList<>();
        String query = "SELECT * FROM bookings WHERE accommodation_id = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setInt(1, accommodationId);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    bookings.add(extractBookingFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return bookings;
    }
    
    @Override
    public List<Booking> getByDateRange(LocalDate startDate, LocalDate endDate) {
        List<Booking> bookings = new ArrayList<>();
        String query = "SELECT * FROM bookings WHERE check_in_date >= ? AND check_out_date <= ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setDate(1, Date.valueOf(startDate));
            statement.setDate(2, Date.valueOf(endDate));
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    bookings.add(extractBookingFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return bookings;
    }
    
    @Override
    public List<Booking> getByStatus(String status) {
        List<Booking> bookings = new ArrayList<>();
        String query = "SELECT * FROM bookings WHERE status = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setString(1, status);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    bookings.add(extractBookingFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return bookings;
    }
    
    @Override
    public boolean isAccommodationAvailable(int accommodationId, LocalDate checkInDate, LocalDate checkOutDate) {
        String query = "SELECT COUNT(*) FROM bookings WHERE accommodation_id = ? AND status != 'CANCELLED' " +
                       "AND ((check_in_date <= ? AND check_out_date >= ?) " +
                       "OR (check_in_date <= ? AND check_out_date >= ?) " +
                       "OR (check_in_date >= ? AND check_out_date <= ?))";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setInt(1, accommodationId);
            statement.setDate(2, Date.valueOf(checkInDate));
            statement.setDate(3, Date.valueOf(checkInDate));
            statement.setDate(4, Date.valueOf(checkOutDate));
            statement.setDate(5, Date.valueOf(checkOutDate));
            statement.setDate(6, Date.valueOf(checkInDate));
            statement.setDate(7, Date.valueOf(checkOutDate));
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) == 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    private Booking extractBookingFromResultSet(ResultSet resultSet) throws SQLException {
        Booking booking = new Booking();
        booking.setId(resultSet.getInt("id"));
        booking.setUserId(resultSet.getInt("user_id"));
        booking.setAccommodationId(resultSet.getInt("accommodation_id"));
        booking.setCheckInDate(resultSet.getDate("check_in_date").toLocalDate());
        booking.setCheckOutDate(resultSet.getDate("check_out_date").toLocalDate());
        booking.setNumberOfGuests(resultSet.getInt("number_of_guests"));
        booking.setTotalPrice(resultSet.getDouble("total_price"));
        booking.setStatus(resultSet.getString("status"));
        booking.setBookingDate(resultSet.getDate("booking_date").toLocalDate());
        
        return booking;
    }
}