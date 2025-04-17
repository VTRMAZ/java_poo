package com.booking.view;

import com.booking.controller.AccommodationController;
import com.booking.controller.BookingController;
import com.booking.model.Accommodation;
import com.booking.model.Booking;
import com.booking.model.User;
import com.booking.utils.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDashboardView extends JFrame implements ActionListener {

    private final User currentUser;
    private final AccommodationController accommodationController;
    private final BookingController bookingController;

    private JPanel resultsPanel;
    private JComboBox<String> ratingComboBox;

    private JTabbedPane tabbedPane;
    private JTable bookingsTable;
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
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel welcomeLabel = new JLabel("Welcome, " + currentUser.getFirstName() + " " + currentUser.getLastName());
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));

        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(this);

        headerPanel.add(welcomeLabel, BorderLayout.WEST);
        headerPanel.add(logoutButton, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();
        JPanel searchPanel = createSearchPanel();
        tabbedPane.addTab("Search Accommodations", searchPanel);
        JPanel bookingsPanel = createBookingsPanel();
        tabbedPane.addTab("My Bookings", bookingsPanel);

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel filtersPanel = new JPanel(new GridLayout(1, 5, 10, 0));
        filtersPanel.setBorder(BorderFactory.createTitledBorder("Search Filters"));

        JPanel cityPanel = new JPanel(new BorderLayout());
        cityPanel.add(new JLabel("City:"), BorderLayout.NORTH);
        cityField = new JTextField();
        cityPanel.add(cityField, BorderLayout.CENTER);

        JPanel typePanel = new JPanel(new BorderLayout());
        typePanel.add(new JLabel("Type:"), BorderLayout.NORTH);
        String[] accommodationTypes = {"All", "hotel", "apartment", "villa", "chalet", "cottage"};
        accommodationTypeComboBox = new JComboBox<>(accommodationTypes);
        typePanel.add(accommodationTypeComboBox, BorderLayout.CENTER);

        JPanel pricePanel = new JPanel(new BorderLayout());
        pricePanel.add(new JLabel("Max Price per Night:"), BorderLayout.NORTH);
        priceRangeSlider = new JSlider(JSlider.HORIZONTAL, 0, 1000, 500);
        priceRangeSlider.setMajorTickSpacing(250);
        priceRangeSlider.setMinorTickSpacing(50);
        priceRangeSlider.setPaintTicks(true);
        priceRangeSlider.setPaintLabels(true);
        priceRangeLabel = new JLabel("$500");
        priceRangeSlider.addChangeListener(e -> priceRangeLabel.setText("$" + priceRangeSlider.getValue()));
        pricePanel.add(priceRangeSlider, BorderLayout.CENTER);
        pricePanel.add(priceRangeLabel, BorderLayout.EAST);

        JPanel ratingPanel = new JPanel(new BorderLayout());
        ratingPanel.add(new JLabel("Min Rating:"), BorderLayout.NORTH);
        String[] ratingOptions = {"All", "5", "4", "3", "2", "1"};
        ratingComboBox = new JComboBox<>(ratingOptions);
        ratingPanel.add(ratingComboBox, BorderLayout.CENTER);

        searchButton = new JButton("Search");
        searchButton.addActionListener(this);

        filtersPanel.add(cityPanel);
        filtersPanel.add(typePanel);
        filtersPanel.add(pricePanel);
        filtersPanel.add(ratingPanel);
        filtersPanel.add(searchButton);

        searchPanel.add(filtersPanel, BorderLayout.NORTH);

        resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        resultsPanel.setBackground(Color.WHITE);

        JScrollPane resultsScrollPane = new JScrollPane(resultsPanel);
        resultsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        resultsScrollPane.getVerticalScrollBar().setUnitIncrement(16);

        searchPanel.add(resultsScrollPane, BorderLayout.CENTER);
        return searchPanel;
    }

    private JPanel createBookingsPanel() {
        JPanel bookingsPanel = new JPanel(new BorderLayout());
        bookingsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        bookingTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
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

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        cancelButton = new JButton("Cancel Booking");
        cancelButton.addActionListener(this);
        buttonsPanel.add(cancelButton);

        bookingsPanel.add(buttonsPanel, BorderLayout.SOUTH);
        return bookingsPanel;
    }

    private void loadAccommodations() {
        resultsPanel.removeAll();
        List<Accommodation> accommodations = accommodationController.getAllAccommodations();
        for (Accommodation acc : accommodations) {
            resultsPanel.add(Box.createVerticalStrut(10));
            resultsPanel.add(new AccommodationCard(acc));
        }
        resultsPanel.revalidate();
        resultsPanel.repaint();
    }

    private void searchAccommodations() {
        String city = cityField.getText().trim();
        String type = accommodationTypeComboBox.getSelectedItem().toString();
        double maxPrice = priceRangeSlider.getValue();
        int exactRating = 0;
        String selectedRating = ratingComboBox.getSelectedItem().toString();
        if (!selectedRating.equals("All")) {
            exactRating = Integer.parseInt(selectedRating);
        }

        List<Accommodation> accommodations = accommodationController.searchWithFilters(
                city.isEmpty() ? null : city,
                null,
                "All".equals(type) ? null : type,
                0,
                maxPrice,
                exactRating,
                0
        );

        resultsPanel.removeAll();
        for (Accommodation acc : accommodations) {
            resultsPanel.add(Box.createVerticalStrut(10));
            resultsPanel.add(new AccommodationCard(acc));
        }
        resultsPanel.revalidate();
        resultsPanel.repaint();
    }

    private void loadUserBookings() {
        bookingTableModel.setRowCount(0);
        List<Booking> bookings = bookingController.getBookingsByUserId(currentUser.getId());
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

    private void logout() {
        this.dispose();
        SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView();
            loginView.setVisible(true);
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchButton) {
            searchAccommodations();
        } else if (e.getSource() == cancelButton) {
            cancelBooking();
        } else if (e.getSource() == logoutButton) {
            logout();
        }
    }

    public void refreshBookings() {
        loadUserBookings();
        tabbedPane.setSelectedIndex(1);
    }

    private void cancelBooking() {
        int selectedRow = bookingsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a booking to cancel.", "Selection Required", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int bookingId = (int) bookingsTable.getValueAt(selectedRow, 0);
        String status = (String) bookingsTable.getValueAt(selectedRow, 6);

        if ("CANCELLED".equals(status) || "COMPLETED".equals(status)) {
            JOptionPane.showMessageDialog(this, "You cannot cancel a booking that is already " + status.toLowerCase() + ".", "Cancellation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to cancel this booking?", "Confirm Cancellation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean cancelled = bookingController.cancelBooking(bookingId);
            if (cancelled) {
                JOptionPane.showMessageDialog(this, "Booking cancelled successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadUserBookings();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to cancel booking. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class AccommodationCard extends JPanel {
        public AccommodationCard(Accommodation accommodation) {
            setLayout(new BorderLayout());
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));
            setBackground(Color.WHITE);
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));

            JLabel imageLabel;
            String imageUrl = getPrimaryImageUrl(accommodation.getId());

            if (imageUrl != null) {
                try {
                    ImageIcon originalIcon = new ImageIcon(getClass().getClassLoader().getResource("images/" + imageUrl));
                    Image scaledImage = originalIcon.getImage().getScaledInstance(120, 100, Image.SCALE_SMOOTH);
                    imageLabel = new JLabel(new ImageIcon(scaledImage));
                } catch (Exception e) {
                    imageLabel = new JLabel("Image not found");
                    imageLabel.setForeground(Color.GRAY);
                    imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
                }
            } else {
                imageLabel = new JLabel("No image");
                imageLabel.setForeground(Color.GRAY);
                imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            }

            imageLabel.setPreferredSize(new Dimension(120, 100));
            add(imageLabel, BorderLayout.WEST);


            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
            infoPanel.setBackground(Color.WHITE);

            JLabel nameLabel = new JLabel(accommodation.getName());
            nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
            JLabel cityLabel = new JLabel(accommodation.getCity() + ", " + accommodation.getCountry());
            cityLabel.setFont(new Font("Arial", Font.PLAIN, 13));
            JLabel priceLabel = new JLabel("Price: $" + String.format("%.2f", accommodation.getPricePerNight()) + " / night");
            priceLabel.setFont(new Font("Arial", Font.PLAIN, 13));
            priceLabel.setForeground(new Color(0, 128, 0));
            JLabel ratingLabel = new JLabel("Rating: " + accommodation.getRating());
            ratingLabel.setFont(new Font("Arial", Font.PLAIN, 13));

            infoPanel.add(nameLabel);
            infoPanel.add(cityLabel);
            infoPanel.add(priceLabel);
            infoPanel.add(ratingLabel);
            add(infoPanel, BorderLayout.CENTER);

            JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            actionPanel.setBackground(Color.WHITE);
            JButton viewButton = new JButton("View");
            JButton bookButton = new JButton("Book");

            viewButton.addActionListener(e -> {
                AccommodationDetailsView detailsView = new AccommodationDetailsView(accommodation, currentUser);
                detailsView.setVisible(true);
            });

            bookButton.addActionListener(e -> {
                BookingFormView bookingForm = new BookingFormView(accommodation, currentUser, UserDashboardView.this);
                bookingForm.setVisible(true);
            });

            actionPanel.add(viewButton);
            actionPanel.add(bookButton);
            add(actionPanel, BorderLayout.EAST);
        }
    }
    private String getPrimaryImageUrl(int accommodationId) {
        String imageUrl = null;
        String query = "SELECT thumbnail_image FROM accommodations WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, accommodationId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                imageUrl = rs.getString("thumbnail_image");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return imageUrl;
    }
}