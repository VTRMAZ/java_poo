package com.booking.view;

import com.booking.controller.*;
import com.booking.model.*;
import com.booking.utils.DateUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

public class AdminDashboardView extends JFrame implements ActionListener {
    
    private final User currentUser;
    private final UserController userController;
    private final AccommodationController accommodationController;
    private final BookingController bookingController;
    private final PromotionController promotionController;
    private final PaymentController paymentController;
    private JTable accommodationTable;

    private JTabbedPane tabbedPane;
    
    // Tables and their models
    private JTable usersTable;
    private JTable accommodationsTable;
    private JTable bookingsTable;
    private JTable promotionsTable;
    private DefaultTableModel userTableModel;
    private DefaultTableModel accommodationTableModel;
    private DefaultTableModel bookingTableModel;
    private DefaultTableModel promotionTableModel;

    // Buttons
    private JButton logoutButton;
    private JButton addUserButton;
    private JButton editUserButton;
    private JButton deleteUserButton;
    private JButton addAccommodationButton;
    private JButton editAccommodationButton;
    private JButton deleteAccommodationButton;
    private JButton updateBookingStatusButton;
    private JButton addPromotionButton;
    private JButton editPromotionButton;
    private JButton deletePromotionButton;
    private JButton refreshStatsButton;


    public AdminDashboardView(User user) {
        this.currentUser = user;
        this.userController = new UserController();
        this.accommodationController = new AccommodationController();
        this.bookingController = new BookingController();
        this.promotionController = new PromotionController();
        this.paymentController = new PaymentController();
        
        setTitle("Booking Application - Admin Dashboard");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        
        initComponents();
        loadAllData();
    }
    
    private void initComponents() {
        // Set layout
        setLayout(new BorderLayout());
        
        // Create header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel welcomeLabel = new JLabel("Admin Dashboard - Welcome, " + currentUser.getFirstName() + " " + currentUser.getLastName());
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(this);
        
        headerPanel.add(welcomeLabel, BorderLayout.WEST);
        headerPanel.add(logoutButton, BorderLayout.EAST);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Create tabbed pane
        tabbedPane = new JTabbedPane();
        
        // Create Users tab
        JPanel usersPanel = createUsersPanel();
        tabbedPane.addTab("Users", usersPanel);
        
        // Create Accommodations tab
        JPanel accommodationsPanel = createAccommodationsPanel();
        tabbedPane.addTab("Accommodations", accommodationsPanel);
        
        // Create Bookings tab
        JPanel bookingsPanel = createBookingsPanel();
        tabbedPane.addTab("Bookings", bookingsPanel);
        
        // Create Promotions tab
        JPanel promotionsPanel = createPromotionsPanel();
        tabbedPane.addTab("Promotions", promotionsPanel);
        
        // Create Statistics tab
        JPanel statisticsPanel = createStatisticsPanel();
        tabbedPane.addTab("Statistics", statisticsPanel);
        
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    private JPanel createUsersPanel() {
        JPanel usersPanel = new JPanel(new BorderLayout());
        usersPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create users table
        userTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
        };
        
        userTableModel.addColumn("ID");
        userTableModel.addColumn("Username");
        userTableModel.addColumn("Email");
        userTableModel.addColumn("First Name");
        userTableModel.addColumn("Last Name");
        userTableModel.addColumn("Phone Number");
        userTableModel.addColumn("Admin");
        
        usersTable = new JTable(userTableModel);
        usersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        usersTable.getColumnModel().getColumn(0).setMaxWidth(50); // ID column
        usersTable.getColumnModel().getColumn(6).setMaxWidth(70); // Admin column
        
        JScrollPane scrollPane = new JScrollPane(usersTable);
        usersPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Create buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        addUserButton = new JButton("Add User");
        editUserButton = new JButton("Edit User");
        deleteUserButton = new JButton("Delete User");
        
        addUserButton.addActionListener(this);
        editUserButton.addActionListener(this);
        deleteUserButton.addActionListener(this);

        buttonsPanel.add(addUserButton);
        buttonsPanel.add(editUserButton);
        buttonsPanel.add(deleteUserButton);
        
        usersPanel.add(buttonsPanel, BorderLayout.SOUTH);
        
        return usersPanel;
    }
    
    private JPanel createAccommodationsPanel() {
        JPanel accommodationsPanel = new JPanel(new BorderLayout());
        accommodationsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
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
        accommodationTableModel.addColumn("Max Guests");
        accommodationTableModel.addColumn("Rating");
        
        accommodationsTable = new JTable(accommodationTableModel);
        accommodationsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        accommodationsTable.getColumnModel().getColumn(0).setMaxWidth(50); // ID column
        accommodationsTable.getColumnModel().getColumn(7).setMaxWidth(70); // Rating column
        
        JScrollPane scrollPane = new JScrollPane(accommodationsTable);
        accommodationsPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Create buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        addAccommodationButton = new JButton("Add Accommodation");
        editAccommodationButton = new JButton("Refresh");
        deleteAccommodationButton = new JButton("Delete Accommodation");
        // ensuite tu ajoutes ton bouton
        accommodationTable = new JTable();
        loadAccommodations();  // Pour charger les données à l'ouverture

        JButton addAccommodationButton = new JButton("add a accommodation");
        addAccommodationButton.addActionListener(e -> openAddAccommodationDialog());

        editAccommodationButton.addActionListener(e -> loadAccommodations());

        JPanel accommodationPanel = new JPanel(new BorderLayout());

// Panel du haut avec les boutons
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.add(addAccommodationButton);

// Ajout dans le panel principal
        accommodationPanel.add(topPanel, BorderLayout.NORTH);
        accommodationPanel.add(new JScrollPane(accommodationTable), BorderLayout.CENTER);
        loadAccommodations();
// Ajout à l'onglet "Hébergements"



        addAccommodationButton.addActionListener(this);
        editAccommodationButton.addActionListener(this);
        deleteAccommodationButton.addActionListener(this);
        
        buttonsPanel.add(addAccommodationButton);
        buttonsPanel.add(editAccommodationButton);
        buttonsPanel.add(deleteAccommodationButton);
        
        accommodationsPanel.add(buttonsPanel, BorderLayout.SOUTH);
        
        return accommodationsPanel;
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
        bookingTableModel.addColumn("User");
        bookingTableModel.addColumn("Accommodation");
        bookingTableModel.addColumn("Check-in");
        bookingTableModel.addColumn("Check-out");
        bookingTableModel.addColumn("Guests");
        bookingTableModel.addColumn("Total Price");
        bookingTableModel.addColumn("Status");
        bookingTableModel.addColumn("Booking Date");
        
        bookingsTable = new JTable(bookingTableModel);
        bookingsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(bookingsTable);
        bookingsPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Create buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        updateBookingStatusButton = new JButton("Update Status");
        updateBookingStatusButton.addActionListener(this);
        
        buttonsPanel.add(updateBookingStatusButton);
        
        bookingsPanel.add(buttonsPanel, BorderLayout.SOUTH);
        
        return bookingsPanel;
    }
    
    private JPanel createPromotionsPanel() {
        JPanel promotionsPanel = new JPanel(new BorderLayout());
        promotionsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create promotions table
        promotionTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
        };
        
        promotionTableModel.addColumn("ID");
        promotionTableModel.addColumn("Accommodation");
        promotionTableModel.addColumn("Name");
        promotionTableModel.addColumn("Discount %");
        promotionTableModel.addColumn("Start Date");
        promotionTableModel.addColumn("End Date");
        promotionTableModel.addColumn("Promo Code");
        promotionTableModel.addColumn("Status");
        
        promotionsTable = new JTable(promotionTableModel);
        promotionsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(promotionsTable);
        promotionsPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Create buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        addPromotionButton = new JButton("Add Promotion");
        editPromotionButton = new JButton("Edit Promotion");
        deletePromotionButton = new JButton("Delete Promotion");
        
        addPromotionButton.addActionListener(this);
        editPromotionButton.addActionListener(this);
        deletePromotionButton.addActionListener(this);
        
        buttonsPanel.add(addPromotionButton);
        buttonsPanel.add(editPromotionButton);
        buttonsPanel.add(deletePromotionButton);

        promotionsPanel.add(buttonsPanel, BorderLayout.SOUTH);

        return promotionsPanel;
    }
    
    private JPanel createStatisticsPanel() {
        JPanel statisticsPanel = new JPanel(new BorderLayout());
        statisticsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create a panel to hold charts
        JPanel chartsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        
        // Revenue chart
        ChartPanel revenueChartPanel = createRevenueChart();
        revenueChartPanel.setBorder(BorderFactory.createTitledBorder("Monthly Revenue"));
        
        // Bookings by status chart
        ChartPanel bookingStatusChartPanel = createBookingStatusChart();
        bookingStatusChartPanel.setBorder(BorderFactory.createTitledBorder("Bookings by Status"));
        
        // Bookings by accommodation type chart
        ChartPanel accommodationTypeChartPanel = createAccommodationTypeChart();
        accommodationTypeChartPanel.setBorder(BorderFactory.createTitledBorder("Accommodation Types"));
        
        // Summary panel
        JPanel summaryPanel = createSummaryPanel();
        summaryPanel.setBorder(BorderFactory.createTitledBorder("Summary"));
        
        // Add charts to panel
        chartsPanel.add(revenueChartPanel);
        chartsPanel.add(bookingStatusChartPanel);
        chartsPanel.add(accommodationTypeChartPanel);
        chartsPanel.add(summaryPanel);
        
        statisticsPanel.add(chartsPanel, BorderLayout.CENTER);
        
        // Add refresh button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        refreshStatsButton = new JButton("Refresh Statistics");
        refreshStatsButton.addActionListener(this);
        buttonPanel.add(refreshStatsButton);
        
        statisticsPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        return statisticsPanel;
    }
    
    private ChartPanel createRevenueChart() {
        // Create dataset for monthly revenue
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        // Get current year and last 6 months
        int currentYear = LocalDate.now().getYear();
        int currentMonth = LocalDate.now().getMonthValue();
        
        for (int i = 5; i >= 0; i--) {
            int month = currentMonth - i;
            int year = currentYear;
            
            if (month <= 0) {
                month += 12;
                year--;
            }
            
            YearMonth yearMonth = YearMonth.of(year, month);
            LocalDateTime startDate = yearMonth.atDay(1).atStartOfDay();
            LocalDateTime endDate = yearMonth.atEndOfMonth().atTime(23, 59, 59);
            
            double revenue = paymentController.getTotalRevenueByDateRange(startDate, endDate);
            dataset.addValue(revenue, "Revenue", yearMonth.getMonth().toString());
        }
        
        // Create chart
        JFreeChart chart = ChartFactory.createBarChart(
            null,                // chart title
            "Month",             // domain axis label
            "Revenue ($)",       // range axis label
            dataset,             // data
            PlotOrientation.VERTICAL,
            false,               // include legend
            true,                // tooltips
            false                // URLs
        );
        
        return new ChartPanel(chart);
    }
    
    private ChartPanel createBookingStatusChart() {
        // Create dataset for booking status
        DefaultPieDataset dataset = new DefaultPieDataset();
        
        // Count bookings by status
        List<Booking> allBookings = bookingController.getAllBookings();
        Map<String, Long> statusCounts = allBookings.stream()
            .collect(Collectors.groupingBy(Booking::getStatus, Collectors.counting()));
        
        // Add data to dataset
        statusCounts.forEach((status, count) -> dataset.setValue(status, count));
        
        // Create chart
        JFreeChart chart = ChartFactory.createPieChart(
            null,       // chart title
            dataset,    // data
            true,       // include legend
            true,       // tooltips
            false       // URLs
        );
        
        return new ChartPanel(chart);
    }
    
    private ChartPanel createAccommodationTypeChart() {
        // Create dataset for accommodation types
        DefaultPieDataset dataset = new DefaultPieDataset();
        
        // Count accommodations by type
        List<Accommodation> allAccommodations = accommodationController.getAllAccommodations();
        Map<String, Long> typeCounts = allAccommodations.stream()
            .collect(Collectors.groupingBy(Accommodation::getAccommodationType, Collectors.counting()));
        
        // Add data to dataset
        typeCounts.forEach((type, count) -> dataset.setValue(type, count));
        
        // Create chart
        JFreeChart chart = ChartFactory.createPieChart(
            null,       // chart title
            dataset,    // data
            true,       // include legend
            true,       // tooltips
            false       // URLs
        );
        
        return new ChartPanel(chart);
    }
    
    private JPanel createSummaryPanel() {
        JPanel summaryPanel = new JPanel(new GridLayout(6, 2, 5, 10));
        
        int totalUsers = userController.getAllUsers().size();
        int totalAccommodations = accommodationController.getAllAccommodations().size();
        int totalBookings = bookingController.getAllBookings().size();
        double totalRevenue = paymentController.getTotalRevenue();
        
        // Calculate occupancy rate (confirmed and completed bookings in the future)
        List<Booking> futureBookings = bookingController.getAllBookings().stream()
            .filter(b -> b.getCheckInDate().isAfter(LocalDate.now()) 
                    && (b.getStatus().equals("CONFIRMED") || b.getStatus().equals("COMPLETED")))
            .collect(Collectors.toList());
        
        double occupancyRate = 0;
        if (totalAccommodations > 0) {
            // Simple occupancy rate calculation (can be more sophisticated)
            occupancyRate = (double) futureBookings.size() / totalAccommodations * 100;
        }
        
        // Calculate most popular accommodation type
        Map<String, Long> typeBookings = bookingController.getAllBookings().stream()
            .map(b -> accommodationController.getAccommodationById(b.getAccommodationId()))
            .filter(a -> a != null)
            .collect(Collectors.groupingBy(Accommodation::getAccommodationType, Collectors.counting()));
        
        String popularType = typeBookings.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse("N/A");
        
        // Add summary information
        summaryPanel.add(new JLabel("Total Users:"));
        summaryPanel.add(new JLabel(String.valueOf(totalUsers)));
        
        summaryPanel.add(new JLabel("Total Accommodations:"));
        summaryPanel.add(new JLabel(String.valueOf(totalAccommodations)));
        
        summaryPanel.add(new JLabel("Total Bookings:"));
        summaryPanel.add(new JLabel(String.valueOf(totalBookings)));
        
        summaryPanel.add(new JLabel("Total Revenue:"));
        summaryPanel.add(new JLabel(String.format("$%.2f", totalRevenue)));
        
        summaryPanel.add(new JLabel("Occupancy Rate:"));
        summaryPanel.add(new JLabel(String.format("%.1f%%", occupancyRate)));
        
        summaryPanel.add(new JLabel("Most Popular Type:"));
        summaryPanel.add(new JLabel(popularType));
        
        return summaryPanel;
    }
    
    private void loadAllData() {
        loadUsers();
        loadAccommodations();
        loadBookings();
        loadPromotions();
    }
    
    private void loadUsers() {
        // Clear table
        userTableModel.setRowCount(0);
        
        // Get all users
        List<User> users = userController.getAllUsers();
        
        // Add rows to table
        for (User user : users) {
            userTableModel.addRow(new Object[]{
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhoneNumber(),
                user.isAdmin() ? "Yes" : "No"
            });
        }
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
                accommodation.getMaxGuests(),
                accommodation.getRating()
            });
        }
    }
    
    private void loadBookings() {
        // Clear table
        bookingTableModel.setRowCount(0);
        
        // Get all bookings
        List<Booking> bookings = bookingController.getAllBookings();
        
        // Add rows to table
        for (Booking booking : bookings) {
            User user = userController.getUserById(booking.getUserId());
            Accommodation accommodation = accommodationController.getAccommodationById(booking.getAccommodationId());
            
            bookingTableModel.addRow(new Object[]{
                booking.getId(),
                user != null ? user.getUsername() : "Unknown",
                accommodation != null ? accommodation.getName() : "Unknown",
                booking.getCheckInDate(),
                booking.getCheckOutDate(),
                booking.getNumberOfGuests(),
                String.format("$%.2f", booking.getTotalPrice()),
                booking.getStatus(),
                booking.getBookingDate()
            });
        }
    }
    
    private void loadPromotions() {
        // Clear table
        promotionTableModel.setRowCount(0);
        
        // Get all promotions
        List<Promotion> promotions = promotionController.getAllPromotions();
        
        // Add rows to table
        for (Promotion promotion : promotions) {
            Accommodation accommodation = accommodationController.getAccommodationById(promotion.getAccommodationId());
            
            // Determine promotion status
            String status;
            LocalDate today = LocalDate.now();
            if (today.isBefore(promotion.getStartDate())) {
                status = "Upcoming";
            } else if (today.isAfter(promotion.getEndDate())) {
                status = "Expired";
            } else {
                status = "Active";
            }
            
            promotionTableModel.addRow(new Object[]{
                promotion.getId(),
                accommodation != null ? accommodation.getName() : "Unknown",
                promotion.getName(),
                String.format("%.2f%%", promotion.getDiscountPercentage()),
                promotion.getStartDate(),
                promotion.getEndDate(),
                promotion.getPromoCode(),
                status
            });
        }
    }
    
    private void refreshStatistics() {
        // Remove old statistics tab
        tabbedPane.removeTabAt(4);
        
        // Create a new statistics panel
        JPanel statisticsPanel = createStatisticsPanel();
        tabbedPane.addTab("Statistics", statisticsPanel);
        
        // Select the statistics tab
        tabbedPane.setSelectedIndex(4);
    }
    
    private void updateBookingStatus() {
        int selectedRow = bookingsTable.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                                         "Please select a booking to update.", 
                                         "Selection Required", 
                                         JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int bookingId = (int) bookingsTable.getValueAt(selectedRow, 0);
        String currentStatus = (String) bookingsTable.getValueAt(selectedRow, 7);
        
        // Create status options
        String[] statusOptions = {"PENDING", "CONFIRMED", "CANCELLED", "COMPLETED"};
        
        // Show status selection dialog
        String newStatus = (String) JOptionPane.showInputDialog(
            this,
            "Select new booking status:",
            "Update Booking Status",
            JOptionPane.QUESTION_MESSAGE,
            null,
            statusOptions,
            currentStatus
        );
        
        if (newStatus != null && !newStatus.equals(currentStatus)) {
            boolean updated = false;
            
            // Update status based on selection
            switch (newStatus) {
                case "CONFIRMED":
                    updated = bookingController.confirmBooking(bookingId);
                    break;
                case "CANCELLED":
                    updated = bookingController.cancelBooking(bookingId);
                    break;
                case "COMPLETED":
                    updated = bookingController.completeBooking(bookingId);
                    break;
                default:
                    // Handle other status changes
                    Booking booking = bookingController.getBookingById(bookingId);
                    if (booking != null) {
                        booking.setStatus(newStatus);
                        updated = bookingController.updateBooking(booking);
                    }
                    break;
            }
            
            if (updated) {
                JOptionPane.showMessageDialog(this, 
                                             "Booking status updated successfully.", 
                                             "Update Successful", 
                                             JOptionPane.INFORMATION_MESSAGE);
                loadBookings(); // Refresh bookings table
            } else {
                JOptionPane.showMessageDialog(this, 
                                             "Failed to update booking status.", 
                                             "Update Error", 
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


    private void openAddAccommodationDialog() {
        JTextField nameField = new JTextField();
        JTextField addressField = new JTextField();
        JTextField cityField = new JTextField();
        JTextField countryField = new JTextField();
        JTextField descriptionField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField guestsField = new JTextField();
        JTextField typeField = new JTextField();
        JCheckBox wifiCheck = new JCheckBox("WiFi");
        JCheckBox poolCheck = new JCheckBox("Piscine");
        JCheckBox parkingCheck = new JCheckBox("Parking");
        JTextField ratingField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Nom :")); panel.add(nameField);
        panel.add(new JLabel("Adresse :")); panel.add(addressField);
        panel.add(new JLabel("Ville :")); panel.add(cityField);
        panel.add(new JLabel("Pays :")); panel.add(countryField);
        panel.add(new JLabel("Description :")); panel.add(descriptionField);
        panel.add(new JLabel("Prix par nuit :")); panel.add(priceField);
        panel.add(new JLabel("Nombre max de personnes :")); panel.add(guestsField);
        panel.add(new JLabel("Type :")); panel.add(typeField);
        panel.add(wifiCheck); panel.add(poolCheck); panel.add(parkingCheck);
        panel.add(new JLabel("Note (1 à 5) :")); panel.add(ratingField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Ajouter un hébergement",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            Accommodation acc = new Accommodation();
            acc.setName(nameField.getText());
            acc.setAddress(addressField.getText());
            acc.setCity(cityField.getText());
            acc.setCountry(countryField.getText());
            acc.setDescription(descriptionField.getText());
            acc.setPricePerNight(Double.parseDouble(priceField.getText()));
            acc.setMaxGuests(Integer.parseInt(guestsField.getText()));
            acc.setAccommodationType(typeField.getText());
            acc.setHasWifi(wifiCheck.isSelected());
            acc.setHasPool(poolCheck.isSelected());
            acc.setHasParkingSpace(parkingCheck.isSelected());
            acc.setRating(Integer.parseInt(ratingField.getText()));

            boolean success = accommodationController.addAccommodation(acc);
            if (success) {
                JOptionPane.showMessageDialog(this, "Hébergement ajouté !");
            } else {
                JOptionPane.showMessageDialog(this, "Échec de l'ajout.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == logoutButton) {
            logout();
        } else if (e.getSource() == addUserButton) {
            // Handle add user
            // For now, we'll use a simple message
            JOptionPane.showMessageDialog(this, 
                                          "Add User functionality would be implemented here.", 
                                          "Admin Function", 
                                          JOptionPane.INFORMATION_MESSAGE);
        } else if (e.getSource() == editUserButton) {
            // Handle edit user
            JOptionPane.showMessageDialog(this, 
                                          "Edit User functionality would be implemented here.", 
                                          "Admin Function", 
                                          JOptionPane.INFORMATION_MESSAGE);
        } else if (e.getSource() == deleteUserButton) {
            // Handle delete user
            JOptionPane.showMessageDialog(this, 
                                          "Delete User functionality would be implemented here.", 
                                          "Admin Function", 
                                          JOptionPane.INFORMATION_MESSAGE);
        } else if (e.getSource() == addAccommodationButton) {
            // Handle add accommodation
            JOptionPane.showMessageDialog(this, 
                                          "Add Accommodation functionality would be implemented here.", 
                                          "Admin Function", 
                                          JOptionPane.INFORMATION_MESSAGE);
        } else if (e.getSource() == deleteAccommodationButton) {
            // Handle delete accommodation
            JOptionPane.showMessageDialog(this, 
                                          "Delete Accommodation functionality would be implemented here.", 
                                          "Admin Function", 
                                          JOptionPane.INFORMATION_MESSAGE);
        } else if (e.getSource() == updateBookingStatusButton) {
            updateBookingStatus();
        } else if (e.getSource() == addPromotionButton) {
            // Handle add promotion
            JOptionPane.showMessageDialog(this, 
                                          "Add Promotion functionality would be implemented here.", 
                                          "Admin Function", 
                                          JOptionPane.INFORMATION_MESSAGE);
        } else if (e.getSource() == editPromotionButton) {
            // Handle edit promotion
            JOptionPane.showMessageDialog(this, 
                                          "Edit Promotion functionality would be implemented here.", 
                                          "Admin Function", 
                                          JOptionPane.INFORMATION_MESSAGE);
        } else if (e.getSource() == deletePromotionButton) {
            // Handle delete promotion
            JOptionPane.showMessageDialog(this, 
                                          "Delete Promotion functionality would be implemented here.", 
                                          "Admin Function", 
                                          JOptionPane.INFORMATION_MESSAGE);
        } else if (e.getSource() == refreshStatsButton) {
            refreshStatistics();
        }
    }
}