package com.booking.dao;

import com.booking.model.User;
import java.util.List;

public interface UserDAO {
    User getById(int id);
    List<User> getAll();
    boolean add(User user);
    boolean update(User user);
    boolean delete(int id);
    User getByUsername(String username);
    User authenticate(String username, String password);
}