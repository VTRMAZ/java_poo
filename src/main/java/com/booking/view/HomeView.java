package com.booking.view;

import com.booking.controller.AccommodationController;
import com.booking.controller.UserController;
import com.booking.model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.border.LineBorder;
import java.io.File;
import java.awt.geom.RoundRectangle2D;

public class HomeView extends JFrame implements ActionListener {
    
    private final AccommodationController accommodationController;
    private final UserController userController;
    
    private JButton loginButton;
    private JButton registerButton;
    private JButton searchButton;
    
    private JTextField destinationField;
    private JSpinner checkInDateSpinner;
    private JSpinner checkOutDateSpinner;
    private JSpinner guestsSpinner;
    
    private JPanel popularDestinationsPanel;
    private JPanel specialOffersPanel;
    
    public HomeView() {
        this.accommodationController = new AccommodationController();
        this.userController = new UserController();
        
        setTitle("Booking.com - Find your perfect stay");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1024, 768);
        setLocationRelativeTo(null);
        
        initComponents();
    }
    
    private void initComponents() {
        // Main panel with BoxLayout (vertical)
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        
        // Header panel
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel);
        
        // Hero section with search
        JPanel heroPanel = createHeroPanel();
        mainPanel.add(heroPanel);
        
        // Popular destinations section
        JPanel destinationsPanel = createDestinationsPanel();
        mainPanel.add(destinationsPanel);
        
        // Special offers section
        JPanel offersPanel = createOffersPanel();
        mainPanel.add(offersPanel);
        
        // Footer section
        JPanel footerPanel = createFooterPanel();
        mainPanel.add(footerPanel);
        
        // Add main panel to frame
        add(mainPanel);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0, 53, 128)); // Booking.com blue
        headerPanel.setPreferredSize(new Dimension(getWidth(), 60));
        headerPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
        
        // Logo
        JLabel logoLabel = new JLabel("Booking.com");
        logoLabel.setFont(new Font("Arial", Font.BOLD, 24));
        logoLabel.setForeground(Color.WHITE);
        
        // Login/register buttons
        JPanel authPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        authPanel.setOpaque(false);
        
        loginButton = new JButton("Login");
        registerButton = new JButton("Register");
        
        stylePrimaryButton(loginButton);
        styleSecondaryButton(registerButton);
        
        loginButton.addActionListener(this);
        registerButton.addActionListener(this);
        
        authPanel.add(registerButton);
        authPanel.add(loginButton);
        
        headerPanel.add(logoLabel, BorderLayout.WEST);
        headerPanel.add(authPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createHeroPanel() {
        // Create a gradient panel using a custom JPanel
        JPanel heroPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                int w = getWidth();
                int h = getHeight();
                Color color1 = new Color(0, 53, 128);
                Color color2 = new Color(0, 93, 168);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
                
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
                g2d.dispose();
            }
        };
        
        heroPanel.setLayout(new BoxLayout(heroPanel, BoxLayout.Y_AXIS));
        heroPanel.setBorder(new EmptyBorder(40, 20, 60, 20));
        
        // Hero text with drop shadow
        JLabel heroTitle = new JLabel("Find your perfect stay");
        heroTitle.setFont(new Font("Arial", Font.BOLD, 36));
        heroTitle.setForeground(Color.WHITE);
        heroTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Apply drop shadow effect (we'd use a proper library in a real app)
        heroTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        
        JLabel heroSubtitle = new JLabel("Search deals on hotels, homes, and much more...");
        heroSubtitle.setFont(new Font("Arial", Font.PLAIN, 18));
        heroSubtitle.setForeground(new Color(220, 220, 255));
        heroSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Create a panel with white background and rounded corners for search box
        JPanel searchContainerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 15, 15));
                g2.dispose();
            }
        };
        searchContainerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 15));
        searchContainerPanel.setOpaque(false);
        searchContainerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        searchContainerPanel.setMaximumSize(new Dimension(900, 80));
        searchContainerPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        // Destination input - now using rounded text field
        destinationField = createRoundedTextField("Where are you going?");
        destinationField.setPreferredSize(new Dimension(220, 46));
        
        // Style the date spinners
        SpinnerDateModel checkInModel = new SpinnerDateModel();
        checkInDateSpinner = new JSpinner(checkInModel);
        JSpinner.DateEditor checkInEditor = new JSpinner.DateEditor(checkInDateSpinner, "MMM d, yyyy");
        checkInDateSpinner.setEditor(checkInEditor);
        checkInDateSpinner.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        checkInDateSpinner.setPreferredSize(new Dimension(150, 46));
        
        // Add a label above the spinner
        JLabel checkInLabel = new JLabel("Check-in");
        checkInLabel.setFont(new Font("Arial", Font.BOLD, 12));
        checkInLabel.setForeground(new Color(50, 50, 50));
        
        // Panel to hold label and spinner vertically
        JPanel checkInPanel = new JPanel();
        checkInPanel.setLayout(new BoxLayout(checkInPanel, BoxLayout.Y_AXIS));
        checkInPanel.setOpaque(false);
        checkInPanel.add(checkInLabel);
        checkInPanel.add(Box.createVerticalStrut(3));
        checkInPanel.add(checkInDateSpinner);
        
        // Check-out date with similar styling
        SpinnerDateModel checkOutModel = new SpinnerDateModel();
        checkOutDateSpinner = new JSpinner(checkOutModel);
        JSpinner.DateEditor checkOutEditor = new JSpinner.DateEditor(checkOutDateSpinner, "MMM d, yyyy");
        checkOutDateSpinner.setEditor(checkOutEditor);
        checkOutDateSpinner.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        checkOutDateSpinner.setPreferredSize(new Dimension(150, 46));
        
        JLabel checkOutLabel = new JLabel("Check-out");
        checkOutLabel.setFont(new Font("Arial", Font.BOLD, 12));
        checkOutLabel.setForeground(new Color(50, 50, 50));
        
        JPanel checkOutPanel = new JPanel();
        checkOutPanel.setLayout(new BoxLayout(checkOutPanel, BoxLayout.Y_AXIS));
        checkOutPanel.setOpaque(false);
        checkOutPanel.add(checkOutLabel);
        checkOutPanel.add(Box.createVerticalStrut(3));
        checkOutPanel.add(checkOutDateSpinner);
        
        // Guests with similar styling
        SpinnerNumberModel guestsModel = new SpinnerNumberModel(2, 1, 10, 1);
        guestsSpinner = new JSpinner(guestsModel);
        guestsSpinner.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        guestsSpinner.setPreferredSize(new Dimension(100, 46));
        
        JLabel guestsLabel = new JLabel("Guests");
        guestsLabel.setFont(new Font("Arial", Font.BOLD, 12));
        guestsLabel.setForeground(new Color(50, 50, 50));
        
        JPanel guestsPanel = new JPanel();
        guestsPanel.setLayout(new BoxLayout(guestsPanel, BoxLayout.Y_AXIS));
        guestsPanel.setOpaque(false);
        guestsPanel.add(guestsLabel);
        guestsPanel.add(Box.createVerticalStrut(3));
        guestsPanel.add(guestsSpinner);
        
        // Search button with updated styling
        searchButton = new JButton("Search");
        searchButton.setPreferredSize(new Dimension(120, 46));
        stylePrimaryButton(searchButton);
        searchButton.addActionListener(this);
        
        // Add the components to the search panel with proper padding
        searchContainerPanel.add(destinationField);
        searchContainerPanel.add(Box.createHorizontalStrut(5));
        searchContainerPanel.add(checkInPanel);
        searchContainerPanel.add(Box.createHorizontalStrut(5));
        searchContainerPanel.add(checkOutPanel);
        searchContainerPanel.add(Box.createHorizontalStrut(5));
        searchContainerPanel.add(guestsPanel);
        searchContainerPanel.add(Box.createHorizontalStrut(5));
        searchContainerPanel.add(searchButton);
        
        // Add everything to the hero panel with proper spacing
        heroPanel.add(Box.createVerticalStrut(30));
        heroPanel.add(heroTitle);
        heroPanel.add(Box.createVerticalStrut(15));
        heroPanel.add(heroSubtitle);
        heroPanel.add(Box.createVerticalStrut(40));
        heroPanel.add(searchContainerPanel);
        heroPanel.add(Box.createVerticalStrut(20));
        
        return heroPanel;
    }
    
    private JPanel createDestinationsPanel() {
        JPanel sectionPanel = new JPanel();
        sectionPanel.setLayout(new BoxLayout(sectionPanel, BoxLayout.Y_AXIS));
        sectionPanel.setBackground(Color.WHITE);
        sectionPanel.setBorder(new EmptyBorder(40, 30, 40, 30));
        
        // Section title with modern styling
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel sectionTitle = new JLabel("Popular destinations");
        sectionTitle.setFont(new Font("Arial", Font.BOLD, 28));
        sectionTitle.setForeground(new Color(30, 30, 30));
        
        JLabel viewAllLabel = new JLabel("View all");
        viewAllLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        viewAllLabel.setForeground(new Color(0, 113, 194));
        viewAllLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewAllLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        
        titlePanel.add(sectionTitle);
        titlePanel.add(viewAllLabel);
        
        // Destinations grid panel with improved layout
        popularDestinationsPanel = new JPanel(new GridLayout(2, 4, 20, 20));
        popularDestinationsPanel.setBackground(Color.WHITE);
        
        // Add destination cards with city-specific colors
        String[] popularCities = {"Paris", "Nice", "Chamonix", "Cannes", "Bordeaux", "Lyon", "Marseille", "Strasbourg"};
        Color[] cityColors = {
            new Color(65, 105, 225),  // Paris - Royal Blue
            new Color(30, 144, 255),  // Nice - Dodger Blue
            new Color(70, 130, 180),  // Chamonix - Steel Blue
            new Color(0, 191, 255),   // Cannes - Deep Sky Blue
            new Color(100, 149, 237), // Bordeaux - Cornflower Blue
            new Color(135, 206, 235), // Lyon - Sky Blue
            new Color(176, 196, 222), // Marseille - Light Steel Blue
            new Color(173, 216, 230)  // Strasbourg - Light Blue
        };
        
        for (int i = 0; i < popularCities.length; i++) {
            JPanel card = createDestinationCard(popularCities[i], cityColors[i]);
            popularDestinationsPanel.add(card);
        }
        
        sectionPanel.add(titlePanel);
        sectionPanel.add(Box.createVerticalStrut(25));
        sectionPanel.add(popularDestinationsPanel);
        
        return sectionPanel;
    }
    
    private JPanel createDestinationCard(String city, Color themeColor) {
        // Create a card panel with hover effect
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw rounded rectangle for the card
                g2.setColor(Color.WHITE);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth()-1, getHeight()-1, 12, 12));
                
                g2.dispose();
            }
        };
        
        card.setLayout(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(0, 0, 10, 0)
        ));
        
        // Create gradient panel that simulates an image
        JPanel imagePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                // Create a gradient background to simulate an image
                int w = getWidth();
                int h = getHeight();
                
                // Create a darker version of the theme color for the gradient
                Color darkColor = new Color(
                    Math.max(0, themeColor.getRed() - 50),
                    Math.max(0, themeColor.getGreen() - 50),
                    Math.max(0, themeColor.getBlue() - 50)
                );
                
                GradientPaint gp = new GradientPaint(0, 0, themeColor, 0, h, darkColor);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
                
                // Add a slight overlay at the bottom for text visibility
                GradientPaint overlay = new GradientPaint(
                    0, h-50, new Color(0, 0, 0, 0), 
                    0, h, new Color(0, 0, 0, 120)
                );
                g2d.setPaint(overlay);
                g2d.fillRect(0, h-50, w, 50);
                
                g2d.dispose();
            }
        };
        
        imagePanel.setPreferredSize(new Dimension(150, 150));
        imagePanel.setLayout(new BorderLayout());
        
        // Create city name that appears on the image
        JLabel cityImageLabel = new JLabel(city);
        cityImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        cityImageLabel.setVerticalAlignment(SwingConstants.BOTTOM);
        cityImageLabel.setForeground(Color.WHITE);
        cityImageLabel.setFont(new Font("Arial", Font.BOLD, 16));
        cityImageLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        imagePanel.add(cityImageLabel, BorderLayout.CENTER);
        
        // Icon to represent popular attractions (would use actual icons in a real app)
        JLabel attractionLabel = new JLabel("★ Popular");
        attractionLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        attractionLabel.setForeground(new Color(255, 255, 255));
        attractionLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 0));
        imagePanel.add(attractionLabel, BorderLayout.NORTH);
        
        // Information panel below the image
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(12, 15, 8, 15));
        
        JLabel nameLabel = new JLabel(city);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        nameLabel.setForeground(new Color(50, 50, 50));
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel infoLabel = new JLabel(getDestinationDescription(city));
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        infoLabel.setForeground(new Color(100, 100, 100));
        infoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        infoPanel.add(nameLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(infoLabel);
        
        card.add(imagePanel, BorderLayout.CENTER);
        card.add(infoPanel, BorderLayout.SOUTH);
        
        // Add hover effect
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(themeColor, 2),
                    BorderFactory.createEmptyBorder(0, 0, 10, 0)
                ));
                card.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                    BorderFactory.createEmptyBorder(0, 0, 10, 0)
                ));
            }
        });
        
        return card;
    }
    
    // Helper method to provide descriptions for destinations
    private String getDestinationDescription(String city) {
        switch (city) {
            case "Paris": return "325 properties";
            case "Nice": return "198 properties";
            case "Chamonix": return "156 properties";
            case "Cannes": return "212 properties";
            case "Bordeaux": return "178 properties";
            case "Lyon": return "201 properties";
            case "Marseille": return "185 properties";
            case "Strasbourg": return "163 properties";
            default: return "150+ properties";
        }
    }
    
    private JPanel createOffersPanel() {
        JPanel sectionPanel = new JPanel();
        sectionPanel.setLayout(new BoxLayout(sectionPanel, BoxLayout.Y_AXIS));
        sectionPanel.setBackground(Color.WHITE);
        sectionPanel.setBorder(new EmptyBorder(40, 30, 40, 30));
        
        // Section title with modern styling similar to destinations
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel sectionTitle = new JLabel("Special offers");
        sectionTitle.setFont(new Font("Arial", Font.BOLD, 28));
        sectionTitle.setForeground(new Color(30, 30, 30));
        
        JLabel viewAllLabel = new JLabel("View all offers");
        viewAllLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        viewAllLabel.setForeground(new Color(0, 113, 194));
        viewAllLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewAllLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        
        titlePanel.add(sectionTitle);
        titlePanel.add(viewAllLabel);
        
        // Offers grid panel with improved spacing
        specialOffersPanel = new JPanel(new GridLayout(1, 3, 25, 0));
        specialOffersPanel.setBackground(Color.WHITE);
        
        // Add offer cards with nice colors
        String[] offerTitles = {
            "Summer Deals: 15% off",
            "Last Minute Getaways",
            "Extended Stay Discounts"
        };
        
        String[] offerDescriptions = {
            "Enjoy sunny vacations with our special summer discounts on luxury hotels and beachfront properties. Valid until September 30th.",
            "Spontaneous trip? Find great last-minute deals with up to 25% off. Perfect for weekend escapes and city breaks.",
            "Save up to 30% when staying longer than a week. The longer you stay, the more you save. No hidden fees."
        };
        
        // Offer-specific colors
        Color[] offerColors = {
            new Color(255, 183, 77),   // Orange for summer
            new Color(77, 166, 255),   // Blue for last minute
            new Color(109, 174, 129)   // Green for extended stay
        };
        
        // Offer-specific CTAs
        String[] ctaTexts = {
            "Get Summer Deal",
            "Find Last Minute Deals",
            "See Long-Stay Offers"
        };
        
        for (int i = 0; i < offerTitles.length; i++) {
            JPanel card = createOfferCard(offerTitles[i], offerDescriptions[i], offerColors[i], ctaTexts[i]);
            specialOffersPanel.add(card);
        }
        
        sectionPanel.add(titlePanel);
        sectionPanel.add(Box.createVerticalStrut(25));
        sectionPanel.add(specialOffersPanel);
        
        return sectionPanel;
    }
    
    private JPanel createOfferCard(String title, String description, Color themeColor, String ctaText) {
        // Card with rounded corners
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw rounded rectangle for the card
                g2.setColor(Color.WHITE);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth()-1, getHeight()-1, 15, 15));
                
                g2.dispose();
            }
        };
        
        card.setLayout(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(230, 230, 230), 1, true),
            BorderFactory.createEmptyBorder(0, 0, 15, 0)
        ));
        
        // Create a header panel with a color theme
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                int w = getWidth();
                int h = getHeight();
                
                // Lighter version of theme color
                Color lightColor = new Color(
                    Math.min(255, themeColor.getRed() + 30),
                    Math.min(255, themeColor.getGreen() + 30),
                    Math.min(255, themeColor.getBlue() + 30)
                );
                
                // Create gradient
                GradientPaint gp = new GradientPaint(0, 0, lightColor, w, 0, themeColor);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
                
                // Add some visual interest with a simple pattern
                g2d.setColor(new Color(255, 255, 255, 30));
                for (int i = 0; i < w; i += 20) {
                    g2d.drawLine(i, 0, i + 10, h);
                }
                
                g2d.dispose();
            }
        };
        
        headerPanel.setPreferredSize(new Dimension(100, 120));
        headerPanel.setLayout(new BorderLayout());
        
        // Add a discount badge
        JPanel badgePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(220, 53, 69));
                g2d.fillOval(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        
        badgePanel.setOpaque(false);
        badgePanel.setPreferredSize(new Dimension(60, 60));
        badgePanel.setLayout(new BorderLayout());
        
        JLabel discountLabel = new JLabel(title.contains("%") ? title.replaceAll(".*?(\\d+%).*", "$1") : "Deal");
        discountLabel.setHorizontalAlignment(SwingConstants.CENTER);
        discountLabel.setForeground(Color.WHITE);
        discountLabel.setFont(new Font("Arial", Font.BOLD, 14));
        badgePanel.add(discountLabel, BorderLayout.CENTER);
        
        JPanel badgeContainerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        badgeContainerPanel.setOpaque(false);
        badgeContainerPanel.add(badgePanel);
        headerPanel.add(badgeContainerPanel, BorderLayout.NORTH);
        
        // Add a symbolic icon (would be real icons in a production app)
        JLabel iconLabel = new JLabel(title.contains("Summer") ? "☀️" : 
                                     title.contains("Minute") ? "⏱️" : "📅");
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setForeground(Color.WHITE);
        headerPanel.add(iconLabel, BorderLayout.CENTER);
        
        // Create info panel with clean layout
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(50, 50, 50));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel descLabel = new JLabel("<html><p style='width:250px'>" + description + "</p></html>");
        descLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        descLabel.setForeground(new Color(100, 100, 100));
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Create a custom styled button matching the offer theme
        JButton viewButton = new JButton(ctaText);
        viewButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        viewButton.setMargin(new Insets(8, 15, 8, 15));
        viewButton.setBackground(themeColor);
        viewButton.setForeground(Color.WHITE);
        viewButton.setFont(new Font("Arial", Font.BOLD, 14));
        viewButton.setFocusPainted(false);
        viewButton.setBorderPainted(false);
        viewButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect to button
        viewButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                viewButton.setBackground(new Color(
                    Math.max(0, themeColor.getRed() - 20),
                    Math.max(0, themeColor.getGreen() - 20),
                    Math.max(0, themeColor.getBlue() - 20)
                ));
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                viewButton.setBackground(themeColor);
            }
        });
        
        infoPanel.add(titleLabel);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(descLabel);
        infoPanel.add(Box.createVerticalStrut(15));
        infoPanel.add(viewButton);
        
        card.add(headerPanel, BorderLayout.NORTH);
        card.add(infoPanel, BorderLayout.CENTER);
        
        // Add hover effect to the entire card
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(themeColor, 2, true),
                    BorderFactory.createEmptyBorder(0, 0, 15, 0)
                ));
                card.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(new Color(230, 230, 230), 1, true),
                    BorderFactory.createEmptyBorder(0, 0, 15, 0)
                ));
            }
        });
        
        return card;
    }
    
    private JPanel createFooterPanel() {
        // Create a footer with subtle gradient background
        JPanel footerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                int w = getWidth();
                int h = getHeight();
                Color color1 = new Color(245, 245, 250);
                Color color2 = new Color(235, 235, 240);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
                
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
                g2d.dispose();
            }
        };
        
        footerPanel.setLayout(new BorderLayout());
        footerPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        footerPanel.setPreferredSize(new Dimension(getWidth(), 220));
        
        // Create a container for the top section
        JPanel topSection = new JPanel(new BorderLayout());
        topSection.setOpaque(false);
        topSection.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        // Create navigation columns
        JPanel navColumns = new JPanel(new GridLayout(1, 4, 30, 0));
        navColumns.setOpaque(false);
        
        // Column 1: About
        JPanel aboutColumn = createFooterColumn("About Booking.com", new String[]{
            "About us", "How we work", "Sustainability", "Press center", "Careers", "Investor relations"
        });
        
        // Column 2: Help
        JPanel helpColumn = createFooterColumn("Customer Service", new String[]{
            "Help center", "Safety information", "Cancellation options", "Support", "Report an issue", "FAQ"
        });
        
        // Column 3: Terms
        JPanel termsColumn = createFooterColumn("Legal", new String[]{
            "Terms & conditions", "Privacy policy", "Cookie policy", "Accommodation rankings", "MSA statement"
        });
        
        // Column 4: Contact
        JPanel contactColumn = createFooterColumn("Contact Us", new String[]{
            "Partner help", "Property owners", "Affiliate program", "Corporate contact"
        });
        
        navColumns.add(aboutColumn);
        navColumns.add(helpColumn);
        navColumns.add(termsColumn);
        navColumns.add(contactColumn);
        
        topSection.add(navColumns, BorderLayout.CENTER);
        
        // Create a bottom section with divider, social links and copyright
        JPanel bottomSection = new JPanel(new BorderLayout());
        bottomSection.setOpaque(false);
        
        // Add a separator line
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(220, 220, 220));
        bottomSection.add(separator, BorderLayout.NORTH);
        
        // Create social links and copyright container
        JPanel socialAndCopyright = new JPanel(new BorderLayout());
        socialAndCopyright.setOpaque(false);
        socialAndCopyright.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        // Social icons
        JPanel socialPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        socialPanel.setOpaque(false);
        
        String[] socialIcons = {"Facebook", "Twitter", "Instagram", "LinkedIn", "Pinterest"};
        for (String social : socialIcons) {
            JLabel socialLabel = new JLabel(social);
            socialLabel.setFont(new Font("Arial", Font.BOLD, 12));
            socialLabel.setForeground(new Color(80, 80, 80));
            socialLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            socialPanel.add(socialLabel);
        }
        
        // Copyright text with app links
        JPanel copyrightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        copyrightPanel.setOpaque(false);
        
        // Mobile app buttons (would be images in real app)
        JLabel appStoreLabel = new JLabel("App Store");
        appStoreLabel.setFont(new Font("Arial", Font.BOLD, 12));
        appStoreLabel.setForeground(new Color(0, 113, 194));
        appStoreLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JLabel playStoreLabel = new JLabel("Google Play");
        playStoreLabel.setFont(new Font("Arial", Font.BOLD, 12));
        playStoreLabel.setForeground(new Color(0, 113, 194));
        playStoreLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        copyrightPanel.add(appStoreLabel);
        copyrightPanel.add(playStoreLabel);
        
        socialAndCopyright.add(socialPanel, BorderLayout.WEST);
        socialAndCopyright.add(copyrightPanel, BorderLayout.EAST);
        
        bottomSection.add(socialAndCopyright, BorderLayout.CENTER);
        
        // Copyright notice
        JLabel copyrightLabel = new JLabel("© 2024 Booking.com™. All rights reserved.");
        copyrightLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        copyrightLabel.setForeground(new Color(100, 100, 100));
        copyrightLabel.setHorizontalAlignment(SwingConstants.CENTER);
        copyrightLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        bottomSection.add(copyrightLabel, BorderLayout.SOUTH);
        
        // Add both sections to the footer
        footerPanel.add(topSection, BorderLayout.NORTH);
        footerPanel.add(bottomSection, BorderLayout.CENTER);
        
        return footerPanel;
    }
    
    // Helper method to create a footer navigation column
    private JPanel createFooterColumn(String title, String[] links) {
        JPanel column = new JPanel();
        column.setLayout(new BoxLayout(column, BoxLayout.Y_AXIS));
        column.setOpaque(false);
        
        // Column title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(new Color(50, 50, 50));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        column.add(titleLabel);
        
        // Links
        for (String link : links) {
            JLabel linkLabel = new JLabel(link);
            linkLabel.setFont(new Font("Arial", Font.PLAIN, 13));
            linkLabel.setForeground(new Color(0, 113, 194));
            linkLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            linkLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            linkLabel.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 0));
            
            // Add hover effect
            linkLabel.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    linkLabel.setForeground(new Color(0, 83, 164));
                    linkLabel.setText("<html><u>" + link + "</u></html>");
                }
                
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    linkLabel.setForeground(new Color(0, 113, 194));
                    linkLabel.setText(link);
                }
            });
            
            column.add(linkLabel);
        }
        
        return column;
    }
    
    private void stylePrimaryButton(JButton button) {
        button.setBackground(new Color(0, 113, 194));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorderPainted(true);
        button.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(0, 83, 164), 1),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        
        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 93, 174));
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 113, 194));
            }
        });
    }
    
    private void styleSecondaryButton(JButton button) {
        button.setBackground(new Color(0, 53, 128));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setFocusPainted(false);
        button.setBorderPainted(true);
        button.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(0, 43, 108), 1),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        
        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 33, 108));
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 53, 128));
            }
        });
    }
    
    // New method to create a rounded JTextField with placeholder
    private JTextField createRoundedTextField(String placeholder) {
        JTextField textField = new JTextField(placeholder) {
            @Override
            protected void paintComponent(Graphics g) {
                if (!isOpaque() && getBorder() instanceof LineBorder) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(getBackground());
                    g2.fill(new RoundRectangle2D.Double(0, 0, getWidth()-1, getHeight()-1, 15, 15));
                    g2.dispose();
                }
                super.paintComponent(g);
            }
        };
        
        textField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1) {
                @Override
                public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(Color.LIGHT_GRAY);
                    g2.draw(new RoundRectangle2D.Double(x, y, width-1, height-1, 15, 15));
                    g2.dispose();
                }
            },
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        
        textField.setOpaque(false);
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setForeground(Color.GRAY);
        
        textField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setText(placeholder);
                    textField.setForeground(Color.GRAY);
                }
            }
        });
        
        return textField;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            // Open login view
            LoginView loginView = new LoginView();
            loginView.setVisible(true);
            this.dispose();
        } else if (e.getSource() == registerButton) {
            // Open register view
            RegisterView registerView = new RegisterView();
            registerView.setVisible(true);
            this.dispose();
        } else if (e.getSource() == searchButton) {
            // Get search parameters
            String destination = destinationField.getText();
            
            java.util.Date checkInDate = (java.util.Date) checkInDateSpinner.getValue();
            java.util.Date checkOutDate = (java.util.Date) checkOutDateSpinner.getValue();
            int guests = (int) guestsSpinner.getValue();
            
            // Perform search - in a real app we'd pass to a search results view
            JOptionPane.showMessageDialog(this, 
                "Searching for accommodations in " + destination + 
                " from " + checkInDate.toString() + 
                " to " + checkOutDate.toString() + 
                " for " + guests + " guests");
            
            // Open guest search results (as if user is not logged in)
            // In a real app, this would navigate to a search results view
            this.dispose();
            JOptionPane.showMessageDialog(this, "Please log in to view search results");
            LoginView loginView = new LoginView();
            loginView.setVisible(true);
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HomeView homeView = new HomeView();
            homeView.setVisible(true);
        });
    }
}