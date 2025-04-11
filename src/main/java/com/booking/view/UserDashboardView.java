package com.booking.view;

import com.booking.controller.AccommodationController;
import com.booking.controller.BookingController;
import com.booking.model.Accommodation;
import com.booking.model.Booking;
import com.booking.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class UserDashboardView extends JFrame implements ActionListener {
    
    private final User currentUser;
    private final AccommodationController accommodationController;
    private final BookingController bookingController;
    
    private JTabbedPane tabbedPane;
    private JTable accommodationsTable;
    private JTable bookingsTable;
    private DefaultTableModel accommodationTableModel;
    private DefaultTableModel bookingTableModel;
    private JButton searchButton;
    private JButton bookButton;
    private JButton cancelButton;
    private JButton viewDetailsButton;
    private JButton logoutButton;
    private JTextField cityField;
    private JComboBox<String> accommodationTypeComboBox;
    private JSlider priceRangeSlider;
    private JLabel priceRangeLabel;
    
    public UserDashboardView(User user) {
        this.currentUser = user;
        this.accommodationController = new AccommodationController();
        this.bookingController = new BookingController();
        
        setTitle("Booking Application - User Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        initComponents();
        loadAccommodations();
        loadUserBookings();
    }
    
    private void initComponents() {
        // Set layout
        setLayout(new BorderLayout());
        
        // Create header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel welcomeLabel = new JLabel("Welcome, " + currentUser.getFirstName() + " " + currentUser.getLastName());
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(this);
        
        headerPanel.add(welcomeLabel, BorderLayout.WEST);
        headerPanel.add(logoutButton, BorderLayout.EAST);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Create tabbed pane
        tabbedPane = new JTabbedPane();
        
        // Create search accommodations panel
        JPanel searchPanel = createSearchPanel();
        tabbedPane.addTab("Search Accommodations", searchPanel);
        
        // Create my bookings panel
        JPanel bookingsPanel = createBookingsPanel();
        tabbedPane.addTab("My Bookings", bookingsPanel);
        
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create search filters panel
        JPanel filtersPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        filtersPanel.setBorder(BorderFactory.createTitledBorder("Search Filters"));
        
        // City filter
        JPanel cityPanel = new JPanel(new BorderLayout());
        cityPanel.add(new JLabel("City:"), BorderLayout.NORTH);
        cityField = new JTextField();
        cityPanel.add(cityField, BorderLayout.CENTER);
        
        // Accommodation type filter
        JPanel typePanel = new JPanel(new BorderLayout());
        typePanel.add(new JLabel("Type:"), BorderLayout.NORTH);
        String[] accommodationTypes = {"All", "hotel", "apartment", "villa", "chalet", "cottage"};
        accommodationTypeComboBox = new JComboBox<>(accommodationTypes);
        typePanel.add(accommodationTypeComboBox, BorderLayout.CENTER);
        
        // Price range filter
        JPanel pricePanel = new JPanel(new BorderLayout());
        pricePanel.add(new JLabel("Max Price per Night:"), BorderLayout.NORTH);
        priceRangeSlider = new JSlider(JSlider.HORIZONTAL, 0, 1000, 500);
        priceRangeSlider.setMajorTickSpacing(250);
        priceRangeSlider.setMinorTickSpacing(50);
        priceRangeSlider.setPaintTicks(true);
        priceRangeSlider.setPaintLabels(true);
        priceRangeLabel = new JLabel("$500");
        priceRangeSlider.addChangeListener(e -> {
            int value = priceRangeSlider.getValue();
            priceRangeLabel.setText("$" + value);
        });
        pricePanel.add(priceRangeSlider, BorderLayout.CENTER);
        pricePanel.add(priceRangeLabel, BorderLayout.EAST);
        
        // Search button
        searchButton = new JButton("Search");
        searchButton.addActionListener(this);
        
        filtersPanel.add(cityPanel);
        filtersPanel.add(typePanel);
        filtersPanel.add(pricePanel);
        filtersPanel.add(searchButton);
        
        searchPanel.add(filtersPanel, BorderLayout.NORTH);
        
        // Create accommodations table
        accommodationTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
        };
        
        accommodationTableModel.addColumn("ID");
        accommodationTableModel.addColumn("Name");
        accommodationTableModel.addColumn("City");
        accommodationTableModel.addColumn("Country");
        accommodationTableModel.addColumn("Type");
        accommodationTableModel.addColumn("Price/Night");
        accommodationTableModel.addColumn("Rating");
        
        accommodationsTable = new JTable(accommodationTableModel);
        accommodationsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        accommodationsTable.getColumnModel().getColumn(0).setMaxWidth(50); // ID column
        accommodationsTable.getColumnModel().getColumn(6).setMaxWidth(70); // Rating column
        
        JScrollPane scrollPane = new JScrollPane(accommodationsTable);
        searchPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Create buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        viewDetailsButton = new JButton("View Details");
        bookButton = new JButton("Book Now");
        
        viewDetailsButton.addActionListener(this);
        bookButton.addActionListener(this);
        
        buttonsPanel.add(viewDetailsButton);
        buttonsPanel.add(bookButton);
        
        searchPanel.add(buttonsPanel, BorderLayout.SOUTH);
        
        return searchPanel;
    }
    
    private JPanel createBookingsPanel() {
        JPanel bookingsPanel = new JPanel(new BorderLayout());
        bookingsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create bookings table
        bookingTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
        };
        
        bookingTableModel.addColumn("ID");
        bookingTableModel.addColumn("Accommodation");
        bookingTableModel.addColumn("Check-in");
        bookingTableModel.addColumn("Check-out");
        bookingTableModel.addColumn("Guests");
        bookingTableModel.addColumn("Total Price");
        bookingTableModel.addColumn("Status");
        
        bookingsTable = new JTable(bookingTableModel);
        bookingsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(bookingsTable);
        bookingsPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Create buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        cancelButton = new JButton("Cancel Booking");
        cancelButton.addActionListener(this);
        
        buttonsPanel.add(cancelButton);
        
        bookingsPanel.add(buttonsPanel, BorderLayout.SOUTH);
        
        return bookingsPanel;
    }
    
    private void loadAccommodations() {
        // Clear table
        accommodationTableModel.setRowCount(0);
        
        // Get all accommodations
        List<Accommodation> accommodations = accommodationController.getAllAccommodations();
        
        // Add rows to table
        for (Accommodation accommodation : accommodations) {
            accommodationTableModel.addRow(new Object[]{
                accommodation.getId(),
                accommodation.getName(),
                accommodation.getCity(),
                accommodation.getCountry(),
                accommodation.getAccommodationType(),
                String.format("$%.2f", accommodation.getPricePerNight()),
                accommodation.getRating()
            });
        }
    }
    
    private void loadUserBookings() {
        // Clear table
        bookingTableModel.setRowCount(0);
        
        // Get user's bookings
        List<Booking> bookings = bookingController.getBookingsByUserId(currentUser.getId());
        
        // Add rows to table
        for (Booking booking : bookings) {
            Accommodation accommodation = accommodationController.getAccommodationById(booking.getAccommodationId());
            
            bookingTableModel.addRow(new Object[]{
                booking.getId(),
                accommodation != null ? accommodation.getName() : "Unknown",
                booking.getCheckInDate(),
                booking.getCheckOutDate(),
                booking.getNumberOfGuests(),
                String.format("$%.2f", booking.getTotalPrice()),
                booking.getStatus()
            });
        }
    }
    
    private void searchAccommodations() {
        // Get filter values
        String city = cityField.getText().trim();
        String type = accommodationTypeComboBox.getSelectedItem().toString();
        double maxPrice = priceRangeSlider.getValue();
        
        // Clear table
        accommodationTableModel.setRowCount(0);
        
        // Get filtered accommodations
        List<Accommodation> accommodations;
        
        if ("All".equals(type)) {
            accommodations = accommodationController.searchWithFilters(
                city.isEmpty() ? null : city,
                null, // country
                null, // type (all)
                0, // minPrice
                maxPrice, // maxPrice
                0, // minRating
                0  // maxGuests
            );
        } else {
            accommodations = accommodationController.searchWithFilters(
                city.isEmpty() ? null : city,
                null, // country
                type, // specific type
                0, // minPrice
                maxPrice, // maxPrice
                0, // minRating
                0  // maxGuests
            );
        }
        
        // Add rows to table
        for (Accommodation accommodation : accommodations) {
            accommodationTableModel.addRow(new Object[]{
                accommodation.getId(),
                accommodation.getName(),
                accommodation.getCity(),
                accommodation.getCountry(),
                accommodation.getAccommodationType(),
                String.format("$%.2f", accommodation.getPricePerNight()),
                accommodation.getRating()
            });
        }
    }
    
    private void viewAccommodationDetails() {
        int selectedRow = accommodationsTable.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                                         "Please select an accommodation to view.", 
                                         "Selection Required", 
                                         JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int accommodationId = (int) accommodationsTable.getValueAt(selectedRow, 0);
        Accommodation accommodation = accommodationController.getAccommodationById(accommodationId);
        
        if (accommodation != null) {
            AccommodationDetailsView detailsView = new AccommodationDetailsView(accommodation, currentUser);
            detailsView.setVisible(true);
        }
    }
    
    private void bookAccommodation() {
        int selectedRow = accommodationsTable.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                                         "Please select an accommodation to book.", 
                                         "Selection Required", 
                                         JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int accommodationId = (int) accommodationsTable.getValueAt(selectedRow, 0);
        Accommodation accommodation = accommodationController.getAccommodationById(accommodationId);
        
        if (accommodation != null) {
            BookingFormView bookingForm = new BookingFormView(accommodation, currentUser, this);
            bookingForm.setVisible(true);
        }
    }
    
    private void cancelBooking() {
        int selectedRow = bookingsTable.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                                         "Please select a booking to cancel.", 
                                         "Selection Required", 
                                         JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int bookingId = (int) bookingsTable.getValueAt(selectedRow, 0);
        String status = (String) bookingsTable.getValueAt(selectedRow, 6);
        
        if ("CANCELLED".equals(status) || "COMPLETED".equals(status)) {
            JOptionPane.showMessageDialog(this, 
                                         "You cannot cancel a booking that is already " + status.toLowerCase() + ".", 
                                         "Cancellation Error", 
                                         JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
                                                  "Are you sure you want to cancel this booking?", 
                                                  "Confirm Cancellation", 
                                                  JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            boolean cancelled = bookingController.cancelBooking(bookingId);
            
            if (cancelled) {
                JOptionPane.showMessageDialog(this, 
                                             "Booking cancelled successfully.", 
                                             "Cancellation Successful", 
                                             JOptionPane.INFORMATION_MESSAGE);
                loadUserBookings(); // Refresh bookings table
            } else {
                JOptionPane.showMessageDialog(this, 
                                             "Failed to cancel booking. Please try again.", 
                                             "Cancellation Error", 
                                             JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void logout() {
        this.dispose(); // Close dashboard
        
        SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView();
            loginView.setVisible(true);
        });
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchButton) {
            searchAccommodations();
        } else if (e.getSource() == viewDetailsButton) {
            viewAccommodationDetails();
        } else if (e.getSource() == bookButton) {
            bookAccommodation();
        } else if (e.getSource() == cancelButton) {
            cancelBooking();
        } else if (e.getSource() == logoutButton) {
            logout();
        }
    }
    
    // Method to refresh bookings data (called after successful booking)
    public void refreshBookings() {
        loadUserBookings();
        tabbedPane.setSelectedIndex(1); // Switch to My Bookings tab
    }
}