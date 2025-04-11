package com.booking.view;

import com.booking.controller.UserController;
import com.booking.model.User;
import com.booking.utils.ValidationUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginView extends JFrame implements ActionListener {
    
    private final UserController userController;
    
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    
    public LoginView() {
        userController = new UserController();
        
        setTitle("Booking Application - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        
        initComponents();
    }
    
    private void initComponents() {
        // Set layout
        setLayout(new BorderLayout());
        
        // Create header panel
        JPanel headerPanel = new JPanel();
        JLabel titleLabel = new JLabel("Welcome to Booking Application");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);
        
        // Create input panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();
        
        inputPanel.add(usernameLabel);
        inputPanel.add(usernameField);
        inputPanel.add(passwordLabel);
        inputPanel.add(passwordField);
        inputPanel.add(new JLabel("")); // Empty label for spacing
        
        // Create button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        
        loginButton = new JButton("Login");
        registerButton = new JButton("Register");
        
        loginButton.addActionListener(this);
        registerButton.addActionListener(this);
        
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        
        inputPanel.add(buttonPanel);
        add(inputPanel, BorderLayout.CENTER);
        
        // Create footer panel
        JPanel footerPanel = new JPanel();
        JLabel copyrightLabel = new JLabel("© 2025 Booking Application");
        footerPanel.add(copyrightLabel);
        add(footerPanel, BorderLayout.SOUTH);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            handleLogin();
        } else if (e.getSource() == registerButton) {
            openRegisterView();
        }
    }
    
    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                                         "Please enter both username and password.", 
                                         "Login Error", 
                                         JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Authenticate user
        User user = userController.authenticateUser(username, password);
        
        if (user != null) {
            // Login successful
            this.dispose(); // Close login window
            
            if (user.isAdmin()) {
                // Open admin dashboard
                SwingUtilities.invokeLater(() -> {
                    AdminDashboardView adminDashboard = new AdminDashboardView(user);
                    adminDashboard.setVisible(true);
                });
            } else {
                // Open user dashboard
                SwingUtilities.invokeLater(() -> {
                    UserDashboardView userDashboard = new UserDashboardView(user);
                    userDashboard.setVisible(true);
                });
            }
        } else {
            // Login failed
            JOptionPane.showMessageDialog(this, 
                                         "Invalid username or password. Please try again.", 
                                         "Login Failed", 
                                         JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
        }
    }
    
    private void openRegisterView() {
        this.dispose(); // Close login window
        
        SwingUtilities.invokeLater(() -> {
            RegisterView registerView = new RegisterView();
            registerView.setVisible(true);
        });
    }
}