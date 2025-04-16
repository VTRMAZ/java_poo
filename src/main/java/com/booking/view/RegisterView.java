package com.booking.view;

import com.booking.controller.UserController;
import com.booking.utils.ValidationUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterView extends JFrame implements ActionListener {
    
    private final UserController userController;
    
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField emailField;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField phoneField;
    private JButton registerButton;
    private JButton backButton;
    private int newusers;
    
    public RegisterView() {
        userController = new UserController();
        
        setTitle("Booking Application - Register");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);
        
        initComponents();
    }
    
    private void initComponents() {
        // Set layout
        setLayout(new BorderLayout());
        
        // Create header panel
        JPanel headerPanel = new JPanel();
        JLabel titleLabel = new JLabel("Create New Account");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);
        
        // Create input panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(8, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordField = new JPasswordField();
        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField();
        JLabel firstNameLabel = new JLabel("First Name:");
        firstNameField = new JTextField();
        JLabel lastNameLabel = new JLabel("Last Name:");
        lastNameField = new JTextField();
        JLabel phoneLabel = new JLabel("Phone Number:");
        phoneField = new JTextField();
        newusers=1;
        
        inputPanel.add(usernameLabel);
        inputPanel.add(usernameField);
        inputPanel.add(passwordLabel);
        inputPanel.add(passwordField);
        inputPanel.add(confirmPasswordLabel);
        inputPanel.add(confirmPasswordField);
        inputPanel.add(emailLabel);
        inputPanel.add(emailField);
        inputPanel.add(firstNameLabel);
        inputPanel.add(firstNameField);
        inputPanel.add(lastNameLabel);
        inputPanel.add(lastNameField);
        inputPanel.add(phoneLabel);
        inputPanel.add(phoneField);
        
        // Create button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        
        registerButton = new JButton("Register");
        backButton = new JButton("Back to Login");
        
        registerButton.addActionListener(this);
        backButton.addActionListener(this);
        
        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);
        
        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == registerButton) {
            handleRegistration();
        } else if (e.getSource() == backButton) {
            backToLogin();
        }
    }
    
    private void handleRegistration() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        String email = emailField.getText();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String phone = phoneField.getText();
        
        // Validate input
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || 
            email.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                                         "All fields except phone number are required.", 
                                         "Registration Error", 
                                         JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!ValidationUtils.isValidUsername(username)) {
            JOptionPane.showMessageDialog(this, 
                                         "Username must be at least 4 characters long and contain only letters, numbers and underscore.", 
                                         "Registration Error", 
                                         JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!ValidationUtils.isValidPassword(password)) {
            JOptionPane.showMessageDialog(this, 
                                         "Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, and one digit.", 
                                         "Registration Error", 
                                         JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, 
                                         "Passwords do not match.", 
                                         "Registration Error", 
                                         JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!ValidationUtils.isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, 
                                         "Please enter a valid email address.", 
                                         "Registration Error", 
                                         JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!phone.isEmpty() && !ValidationUtils.isValidPhone(phone)) {
            JOptionPane.showMessageDialog(this, 
                                         "Please enter a valid phone number.", 
                                         "Registration Error", 
                                         JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Check if username already exists
        if (userController.getUserByUsername(username) != null) {
            JOptionPane.showMessageDialog(this, 
                                         "Username already exists. Please choose a different username.", 
                                         "Registration Error", 
                                         JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Register the user
        boolean registered = userController.registerUser(username, password, email, firstName, lastName, phone,1);
        
        if (registered) {
            // Registration successful
            JOptionPane.showMessageDialog(this, 
                                         "Registration successful! You can now login with your credentials.", 
                                         "Registration Successful", 
                                         JOptionPane.INFORMATION_MESSAGE);
            
            // Go back to login screen
            backToLogin();
        } else {
            // Registration failed
            JOptionPane.showMessageDialog(this, 
                                         "Registration failed. Please try again later.", 
                                         "Registration Error", 
                                         JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void backToLogin() {
        this.dispose(); // Close registration window
        
        SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView();
            loginView.setVisible(true);
        });
    }
}