package com.booking;

import com.booking.view.HomeView;
import com.booking.utils.DatabaseConnection;

import javax.swing.*;
import java.awt.*;

public class BookingApp {
    
    public static void main(String[] args) {
        try {
            // Set the look and feel to the system's default
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            // Set better font rendering for Swing components
            System.setProperty("awt.useSystemAAFontSettings", "on");
            System.setProperty("swing.aatext", "true");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Initialize database connection
        try {
            DatabaseConnection.getConnection();
            System.out.println("Database connection established successfully.");
        } catch (Exception e) {
            System.err.println("Failed to connect to the database:");
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                                         "Failed to connect to the database. Please check your database settings.", 
                                         "Database Error", 
                                         JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Launch the application on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            // Create and display the home view (booking.com style)
            HomeView homeView = new HomeView();
            homeView.setVisible(true);
        });
        
        // Add shutdown hook to close database connection
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            DatabaseConnection.closeConnection();
            System.out.println("Database connection closed.");
        }));
    }
}