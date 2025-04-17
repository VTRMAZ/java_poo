package com.booking.view;

import com.booking.controller.AccommodationController;
import com.booking.controller.ReviewController;
import com.booking.model.Accommodation;
import com.booking.model.Review;
import com.booking.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.booking.utils.DatabaseConnection;  // Adapte le chemin si nécessaire

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.List;

public class AccommodationDetailsView extends JFrame implements ActionListener {
    
    private final Accommodation accommodation;
    private final User currentUser;
    private final AccommodationController accommodationController;
    private final ReviewController reviewController;
    
    private JButton bookNowButton;
    private JButton backButton;
    private JButton saveToWishlistButton;
    private JPanel reviewsPanel;
    private JPanel amenitiesPanel;
    private JPanel imagesPanel;
    
    public AccommodationDetailsView(Accommodation accommodation, User currentUser) {
        this.accommodation = accommodation;
        this.currentUser = currentUser;
        this.accommodationController = new AccommodationController();
        this.reviewController = new ReviewController();
        
        setTitle(accommodation.getName() + " - Booking");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1024, 768);
        setLocationRelativeTo(null);
        
        initComponents();
        loadReviews();
        loadAmenities();
        loadImages(accommodation.getId());
    }
    
    private void initComponents() {
        // Main scrollable panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        // Header panel
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel);
        
        // Main content panel (images, info, booking form)
        JPanel contentPanel = new JPanel(new BorderLayout(20, 0));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
        
        // Left side (images and details)
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(Color.WHITE);

// Images panel
// Images panel
        imagesPanel = new JPanel(new BorderLayout()) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(600, 400); // Taille fixe pour le conteneur
            }
        };
        imagesPanel.setBackground(new Color(230, 230, 250));
        imagesPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

// Charger les images
        loadImages(accommodation.getId());

        leftPanel.add(imagesPanel);
// Charger les images (cela remplira le imagesPanel)
        loadImages(accommodation.getId());

        leftPanel.add(imagesPanel);
// Récupérer l'image principale de la BDD
        String imageUrl = getPrimaryImageUrl(1); // 1 = accommodation_id

        JPanel imageContainer = new JPanel();
        imageContainer.setBackground(new Color(230, 230, 250));
        imageContainer.setPreferredSize(new Dimension(600, 400));

        if (imageUrl != null) {
            try {
                // L'image doit être placée dans src/main/resources/images/
                ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("images/" + imageUrl));
                Image image = icon.getImage().getScaledInstance(600, 400, Image.SCALE_SMOOTH);
                JLabel imageLabel = new JLabel(new ImageIcon(image));
                imageContainer.add(imageLabel);
            } catch (Exception e) {
                imageContainer.add(new JLabel("Image not found: " + imageUrl));
            }
        } else {
            imageContainer.add(new JLabel("No image available for this accommodation"));
        }

        imagesPanel.add(imageContainer, BorderLayout.CENTER);

        // Description panel
        JPanel descriptionPanel = createDescriptionPanel();
        
        // Amenities panel
        amenitiesPanel = new JPanel();
        amenitiesPanel.setLayout(new BoxLayout(amenitiesPanel, BoxLayout.Y_AXIS));
        amenitiesPanel.setBackground(Color.WHITE);
        amenitiesPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(230, 230, 230), 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel amenitiesTitle = new JLabel("Amenities");
        amenitiesTitle.setFont(new Font("Arial", Font.BOLD, 18));
        amenitiesPanel.add(amenitiesTitle);
        amenitiesPanel.add(Box.createVerticalStrut(10));
        
        // Location panel
        JPanel locationPanel = createLocationPanel();
        
        // Reviews panel
        JPanel reviewsContainer = new JPanel();
        reviewsContainer.setLayout(new BoxLayout(reviewsContainer, BoxLayout.Y_AXIS));
        reviewsContainer.setBackground(Color.WHITE);
        reviewsContainer.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(230, 230, 230), 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel reviewsTitle = new JLabel("Guest Reviews");
        reviewsTitle.setFont(new Font("Arial", Font.BOLD, 18));
        
        reviewsPanel = new JPanel();
        reviewsPanel.setLayout(new BoxLayout(reviewsPanel, BoxLayout.Y_AXIS));
        reviewsPanel.setBackground(Color.WHITE);
        
        reviewsContainer.add(reviewsTitle);
        reviewsContainer.add(Box.createVerticalStrut(15));
        reviewsContainer.add(reviewsPanel);
        
        leftPanel.add(imagesPanel);
        leftPanel.add(descriptionPanel);
        leftPanel.add(Box.createVerticalStrut(20));
        leftPanel.add(amenitiesPanel);
        leftPanel.add(Box.createVerticalStrut(20));
        leftPanel.add(locationPanel);
        leftPanel.add(Box.createVerticalStrut(20));
        leftPanel.add(reviewsContainer);
        
        // Right side (booking form)
        JPanel rightPanel = createBookingPanel();
        
        contentPanel.add(leftPanel, BorderLayout.CENTER);
        contentPanel.add(rightPanel, BorderLayout.EAST);
        
        mainPanel.add(contentPanel);
        
        // Add to frame
        add(scrollPane);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0, 53, 128)); // Booking.com blue
        headerPanel.setPreferredSize(new Dimension(getWidth(), 60));
        headerPanel.setBorder(new EmptyBorder(10, 40, 10, 40));
        
        // Back button
        backButton = new JButton("< Back to search");
        backButton.setFont(new Font("Arial", Font.PLAIN, 14));
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(new Color(0, 53, 128));
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(this);
        
        // Accommodation name
        JLabel nameLabel = new JLabel(accommodation.getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 20));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);

        headerPanel.add(backButton, BorderLayout.WEST);
        headerPanel.add(nameLabel, BorderLayout.CENTER);
        
        return headerPanel;
    }
    
    private JPanel createDescriptionPanel() {
        JPanel descPanel = new JPanel();
        descPanel.setLayout(new BoxLayout(descPanel, BoxLayout.Y_AXIS));
        descPanel.setBackground(Color.WHITE);
        descPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(230, 230, 230), 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        // Title and rating
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);
        
        JLabel nameLabel = new JLabel(accommodation.getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 24));
        
        JPanel ratingPanel = createRatingPanel(accommodation.getRating());
        
        titlePanel.add(nameLabel, BorderLayout.WEST);
        titlePanel.add(ratingPanel, BorderLayout.EAST);
        
        // Address
        JLabel addressLabel = new JLabel(accommodation.getAddress() + ", " + 
                                         accommodation.getCity() + ", " + 
                                         accommodation.getCountry());
        addressLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Type and capacity
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        infoPanel.setBackground(Color.WHITE);
        
        JLabel typeLabel = new JLabel("Type: " + capitalizeFirstLetter(accommodation.getAccommodationType()));
        typeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JLabel guestsLabel = new JLabel("Max guests: " + accommodation.getMaxGuests());
        guestsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        infoPanel.add(typeLabel);
        infoPanel.add(guestsLabel);
        
        // Description
        JTextArea descriptionArea = new JTextArea(accommodation.getDescription());
        descriptionArea.setFont(new Font("Arial", Font.PLAIN, 14));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setEditable(false);
        descriptionArea.setBackground(Color.WHITE);
        descriptionArea.setBorder(null);
        
        // Add components
        descPanel.add(titlePanel);
        descPanel.add(Box.createVerticalStrut(5));
        descPanel.add(addressLabel);
        descPanel.add(Box.createVerticalStrut(10));
        descPanel.add(infoPanel);
        descPanel.add(Box.createVerticalStrut(15));
        descPanel.add(descriptionArea);
        
        return descPanel;
    }
    
    private JPanel createLocationPanel() {
        JPanel locationPanel = new JPanel();
        locationPanel.setLayout(new BoxLayout(locationPanel, BoxLayout.Y_AXIS));
        locationPanel.setBackground(Color.WHITE);
        locationPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(230, 230, 230), 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel locationTitle = new JLabel("Location");
        locationTitle.setFont(new Font("Arial", Font.BOLD, 18));
        
        // Address
        JLabel addressLabel = new JLabel(accommodation.getAddress());
        addressLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JLabel cityCountryLabel = new JLabel(accommodation.getCity() + ", " + accommodation.getCountry());
        cityCountryLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Map placeholder (in a real app, this would be an actual map)
        JPanel mapPlaceholder = new JPanel();
        mapPlaceholder.setBackground(new Color(240, 240, 240));
        mapPlaceholder.setPreferredSize(new Dimension(600, 250));
        mapPlaceholder.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        
        JLabel mapLabel = new JLabel("Map would be displayed here");
        mapPlaceholder.add(mapLabel);
        
        locationPanel.add(locationTitle);
        locationPanel.add(Box.createVerticalStrut(10));
        locationPanel.add(addressLabel);
        locationPanel.add(cityCountryLabel);
        locationPanel.add(Box.createVerticalStrut(10));
        locationPanel.add(mapPlaceholder);
        
        return locationPanel;
    }
    
    private JPanel createBookingPanel() {
        JPanel bookingPanel = new JPanel();
        bookingPanel.setLayout(new BoxLayout(bookingPanel, BoxLayout.Y_AXIS));
        bookingPanel.setBackground(Color.WHITE);
        bookingPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(230, 230, 230), 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        bookingPanel.setPreferredSize(new Dimension(300, 400));
        
        // Price info
        DecimalFormat df = new DecimalFormat("#,##0.00");
        JLabel priceLabel = new JLabel("€" + df.format(accommodation.getPricePerNight()) + " per night");
        priceLabel.setFont(new Font("Arial", Font.BOLD, 24));
        priceLabel.setForeground(new Color(0, 113, 194));
        
        // Rating summary
        JPanel ratingPanel = createRatingPanel(accommodation.getRating());
        
        // Date selection
        JLabel checkInLabel = new JLabel("Check-in Date:");
        JSpinner checkInDateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor checkInEditor = new JSpinner.DateEditor(checkInDateSpinner, "MMM d, yyyy");
        checkInDateSpinner.setEditor(checkInEditor);
        
        JLabel checkOutLabel = new JLabel("Check-out Date:");
        JSpinner checkOutDateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor checkOutEditor = new JSpinner.DateEditor(checkOutDateSpinner, "MMM d, yyyy");
        checkOutDateSpinner.setEditor(checkOutEditor);
        
        // Guests
        JLabel guestsLabel = new JLabel("Guests:");
        SpinnerNumberModel guestsModel = new SpinnerNumberModel(2, 1, accommodation.getMaxGuests(), 1);
        JSpinner guestsSpinner = new JSpinner(guestsModel);
        
        // Total calculation
        JPanel totalPanel = new JPanel(new BorderLayout());
        totalPanel.setBackground(new Color(242, 248, 255));
        totalPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JLabel totalLabel = new JLabel("Total price:");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        JLabel totalAmountLabel = new JLabel("€" + df.format(accommodation.getPricePerNight() * 3));
        totalAmountLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalAmountLabel.setForeground(new Color(0, 113, 194));
        
        totalPanel.add(totalLabel, BorderLayout.WEST);
        totalPanel.add(totalAmountLabel, BorderLayout.EAST);
        
        // Booking button
        bookNowButton = new JButton("Book Now");
        bookNowButton.setBackground(new Color(0, 128, 0));
        bookNowButton.setForeground(Color.WHITE);
        bookNowButton.setFont(new Font("Arial", Font.BOLD, 16));
        bookNowButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        bookNowButton.setFocusPainted(false);
        bookNowButton.addActionListener(this);
        
        // Wishlist button
        saveToWishlistButton = new JButton("Save to Wishlist");
        saveToWishlistButton.setBackground(new Color(240, 240, 240));
        saveToWishlistButton.setFont(new Font("Arial", Font.PLAIN, 14));
        saveToWishlistButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        saveToWishlistButton.setFocusPainted(false);
        saveToWishlistButton.addActionListener(this);
        
        // Cancellation policy
        String policy = "STANDARD"; // Default
        try {
            if (accommodation.getCancellationPolicy() != null) {
                policy = accommodation.getCancellationPolicy();
            }
        } catch (Exception e) {
            // Ignore if method doesn't exist
        }
        
        JLabel policyLabel = new JLabel("<html><body>Cancellation policy: <b>" + policy + "</b></body></html>");
        policyLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        policyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Add components with proper spacing
        bookingPanel.add(priceLabel);
        bookingPanel.add(Box.createVerticalStrut(10));
        bookingPanel.add(ratingPanel);
        bookingPanel.add(Box.createVerticalStrut(20));
        bookingPanel.add(checkInLabel);
        bookingPanel.add(Box.createVerticalStrut(5));
        bookingPanel.add(checkInDateSpinner);
        bookingPanel.add(Box.createVerticalStrut(10));
        bookingPanel.add(checkOutLabel);
        bookingPanel.add(Box.createVerticalStrut(5));
        bookingPanel.add(checkOutDateSpinner);
        bookingPanel.add(Box.createVerticalStrut(10));
        bookingPanel.add(guestsLabel);
        bookingPanel.add(Box.createVerticalStrut(5));
        bookingPanel.add(guestsSpinner);
        bookingPanel.add(Box.createVerticalStrut(20));
        bookingPanel.add(totalPanel);
        bookingPanel.add(Box.createVerticalStrut(20));
        bookingPanel.add(bookNowButton);
        bookingPanel.add(Box.createVerticalStrut(10));
        bookingPanel.add(saveToWishlistButton);
        bookingPanel.add(Box.createVerticalStrut(15));
        bookingPanel.add(policyLabel);
        
        return bookingPanel;
    }
    
    private JPanel createRatingPanel(double rating) {
        JPanel ratingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        ratingPanel.setBackground(Color.WHITE);
        
        // Rating box
        JPanel ratingBox = new JPanel();
        ratingBox.setBackground(new Color(0, 53, 128));
        ratingBox.setPreferredSize(new Dimension(36, 36));
        
        DecimalFormat df = new DecimalFormat("#.#");
        JLabel ratingLabel = new JLabel(df.format(rating));
        ratingLabel.setFont(new Font("Arial", Font.BOLD, 16));
        ratingLabel.setForeground(Color.WHITE);
        
        ratingBox.add(ratingLabel);
        
        // Rating description
        String ratingDesc = getRatingDescription(rating);
        JLabel descLabel = new JLabel(ratingDesc);
        descLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        ratingPanel.add(ratingBox);
        ratingPanel.add(descLabel);
        
        return ratingPanel;
    }
    
    private void loadReviews() {
        reviewsPanel.removeAll();
        List<Review> reviews = reviewController.getReviewsByAccommodationId(accommodation.getId());
        
        if (reviews.isEmpty()) {
            JLabel noReviewsLabel = new JLabel("No reviews yet");
            noReviewsLabel.setFont(new Font("Arial", Font.ITALIC, 14));
            reviewsPanel.add(noReviewsLabel);
        } else {
            for (Review review : reviews) {
                JPanel reviewCard = createReviewCard(review);
                reviewsPanel.add(reviewCard);
                reviewsPanel.add(Box.createVerticalStrut(10));
            }
        }
        
        revalidate();
        repaint();
    }
    
    private JPanel createReviewCard(Review review) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(230, 230, 230), 1),
            new EmptyBorder(10, 10, 10, 10)
        ));
        
        // Header with user info and rating
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        
        User reviewer = review.getUser();
        String userName = reviewer != null ? 
                          reviewer.getFirstName() + " " + reviewer.getLastName().charAt(0) + "." : 
                          "Anonymous";
        
        JLabel userLabel = new JLabel(userName);
        userLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        JPanel miniRatingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        miniRatingPanel.setBackground(Color.WHITE);
        
        JPanel ratingBox = new JPanel();
        ratingBox.setBackground(new Color(0, 53, 128));
        ratingBox.setPreferredSize(new Dimension(30, 30));
        
        JLabel ratingLabel = new JLabel(String.valueOf(review.getRating()));
        ratingLabel.setFont(new Font("Arial", Font.BOLD, 14));
        ratingLabel.setForeground(Color.WHITE);
        
        ratingBox.add(ratingLabel);
        miniRatingPanel.add(ratingBox);
        
        headerPanel.add(userLabel, BorderLayout.WEST);
        headerPanel.add(miniRatingPanel, BorderLayout.EAST);
        
        // Review comment
        JTextArea commentArea = new JTextArea(review.getComment());
        commentArea.setFont(new Font("Arial", Font.PLAIN, 14));
        commentArea.setLineWrap(true);
        commentArea.setWrapStyleWord(true);
        commentArea.setEditable(false);
        commentArea.setBackground(Color.WHITE);
        commentArea.setBorder(null);
        
        // Footer with date and helpful button
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(Color.WHITE);
        
        JLabel dateLabel = new JLabel("Reviewed on: " + review.getReviewDate());
        dateLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        
        JPanel helpfulPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        helpfulPanel.setBackground(Color.WHITE);
        
        int helpfulVotes = 0;
        try {
            helpfulVotes = review.getHelpfulVotes();
        } catch (Exception e) {
            // If helpful_votes field is not available yet, default to 0
        }
        
        JButton helpfulButton = new JButton("Helpful (" + helpfulVotes + ")");
        helpfulButton.setFont(new Font("Arial", Font.PLAIN, 12));
        helpfulButton.setFocusPainted(false);
        
        helpfulPanel.add(helpfulButton);
        
        footerPanel.add(dateLabel, BorderLayout.WEST);
        footerPanel.add(helpfulPanel, BorderLayout.EAST);
        
        // Add components to card
        card.add(headerPanel, BorderLayout.NORTH);
        card.add(commentArea, BorderLayout.CENTER);
        card.add(footerPanel, BorderLayout.SOUTH);
        
        return card;
    }
    
    private void loadAmenities() {
        amenitiesPanel.removeAll();
        
        JLabel amenitiesTitle = new JLabel("Amenities");
        amenitiesTitle.setFont(new Font("Arial", Font.BOLD, 18));
        amenitiesPanel.add(amenitiesTitle);
        amenitiesPanel.add(Box.createVerticalStrut(15));
        
        // Grid for amenities
        JPanel amenitiesGrid = new JPanel(new GridLayout(0, 2, 10, 10));
        amenitiesGrid.setBackground(Color.WHITE);
        
        // Add amenities
        addAmenity(amenitiesGrid, "WiFi", accommodation.isHasWifi());
        addAmenity(amenitiesGrid, "Swimming Pool", accommodation.isHasPool());
        addAmenity(amenitiesGrid, "Parking", accommodation.isHasParking());
        
        // Check if newer properties are available
        try {
            addAmenity(amenitiesGrid, "Restaurant", accommodation.isHasRestaurant());
            addAmenity(amenitiesGrid, "Air Conditioning", accommodation.isHasAirConditioning());
            addAmenity(amenitiesGrid, "Pet Friendly", accommodation.isHasPetFriendly());
            addAmenity(amenitiesGrid, "Gym", accommodation.isHasGym());
        } catch (Exception e) {
            // If newer properties are not available, just continue
        }
        
        amenitiesPanel.add(amenitiesGrid);
        
        revalidate();
        repaint();
    }
    
    private void addAmenity(JPanel container, String name, boolean available) {
        JPanel amenityPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        amenityPanel.setBackground(Color.WHITE);
        
        // Icon (check or x)
        JLabel iconLabel = new JLabel(available ? "✓" : "✗");
        iconLabel.setFont(new Font("Arial", Font.BOLD, 16));
        iconLabel.setForeground(available ? new Color(0, 128, 0) : Color.RED);
        
        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        nameLabel.setForeground(available ? Color.BLACK : Color.GRAY);
        
        amenityPanel.add(iconLabel);
        amenityPanel.add(nameLabel);
        
        container.add(amenityPanel);
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

    private void loadImages(int accommodationId) {
        imagesPanel.removeAll();
        imagesPanel.setLayout(new BorderLayout());

        // Récupérer l'URL de l'image principale
        String imageUrl = getPrimaryImageUrl(accommodationId);

        if (imageUrl != null) {
            try {
                // Charger l'image depuis les ressources
                ImageIcon originalIcon = new ImageIcon(getClass().getClassLoader().getResource("images/" + imageUrl));
                Image originalImage = originalIcon.getImage();

                // Créer un JLabel pour afficher l'image avec redimensionnement intelligent
                JLabel imageLabel = new JLabel() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);

                        // Redimensionnement proportionnel pour s'adapter au panel
                        int panelWidth = getWidth();
                        int panelHeight = getHeight();

                        // Calcul des dimensions en conservant le ratio
                        double imageRatio = (double) originalIcon.getIconWidth() / originalIcon.getIconHeight();
                        double panelRatio = (double) panelWidth / panelHeight;

                        int drawWidth, drawHeight;

                        if (panelRatio > imageRatio) {
                            // Le panel est plus large que l'image (basé sur le ratio)
                            drawHeight = panelHeight;
                            drawWidth = (int) (drawHeight * imageRatio);
                        } else {
                            // Le panel est plus étroit que l'image
                            drawWidth = panelWidth;
                            drawHeight = (int) (drawWidth / imageRatio);
                        }

                        // Centrer l'image
                        int x = (panelWidth - drawWidth) / 2;
                        int y = (panelHeight - drawHeight) / 2;

                        // Dessiner l'image redimensionnée
                        g.drawImage(originalImage, x, y, drawWidth, drawHeight, this);
                    }
                };

                imageLabel.setPreferredSize(new Dimension(600, 400));
                imagesPanel.add(imageLabel, BorderLayout.CENTER);

            } catch (Exception e) {
                JLabel errorLabel = new JLabel("Image non trouvée: " + imageUrl);
                imagesPanel.add(errorLabel, BorderLayout.CENTER);
            }
        } else {
            JLabel noImageLabel = new JLabel("Aucune image disponible");
            imagesPanel.add(noImageLabel, BorderLayout.CENTER);
        }

        imagesPanel.revalidate();
        imagesPanel.repaint();
    }
    private String getRatingDescription(double rating) {
        if (rating >= 4.5) return "Excellent";
        if (rating >= 4.0) return "Very Good";
        if (rating >= 3.5) return "Good";
        if (rating >= 3.0) return "Average";
        return "Fair";
    }
    
    private String capitalizeFirstLetter(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButton) {
            dispose();
        } else if (e.getSource() == bookNowButton) {
            BookingFormView bookingForm = new BookingFormView(accommodation, currentUser, null);
            bookingForm.setVisible(true);
        } else if (e.getSource() == saveToWishlistButton) {
            JOptionPane.showMessageDialog(this, 
                "Added to wishlist!", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
            saveToWishlistButton.setText("Saved to Wishlist ✓");
            saveToWishlistButton.setEnabled(false);
        }
    }
}