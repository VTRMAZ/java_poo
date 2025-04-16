package com.booking.controller;

import com.booking.dao.UserDAO;
import com.booking.dao.impl.UserDAOImpl;
import com.booking.model.User;

import java.util.List;

public class UserController {
    private final UserDAO userDAO;
    
    public UserController() {
        this.userDAO = new UserDAOImpl();
    }
    
    public User getUserById(int id) {
        return userDAO.getById(id);
    }
    
    public List<User> getAllUsers() {
        return userDAO.getAll();
    }
    
    public boolean addUser(User user) {
        return userDAO.add(user);
    }
    
    public boolean updateUser(User user) {
        return userDAO.update(user);
    }
    
    public boolean deleteUser(int id) {
        return userDAO.delete(id);
    }
    
    public User getUserByUsername(String username) {
        return userDAO.getByUsername(username);
    }
    
    public User authenticateUser(String username, String password) {
        return userDAO.authenticate(username, password);
    }
    
    public boolean registerUser(String username, String password, String email, String firstName, String lastName, String phoneNumber,int newuser) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPhoneNumber(phoneNumber);
        user.setAdmin(false);
        user.setNewUsers(newuser);  // Ajout de cette ligne pour définir un utilisateur comme "nouveau"


        return userDAO.add(user);
    }
}