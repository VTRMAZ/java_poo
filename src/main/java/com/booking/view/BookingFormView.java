package com.booking.view;

import com.booking.controller.BookingController;
import com.booking.controller.PaymentController;
import com.booking.controller.PromotionController;
import com.booking.model.Accommodation;
import com.booking.model.User;
import com.booking.utils.DateUtils;
import com.booking.utils.LoginManager;
import com.booking.utils.PriceCalculator;
import com.booking.utils.ValidationUtils;
import com.booking.utils.DatabaseConnection;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class BookingFormView extends JFrame implements ActionListener {

    private final Accommodation accommodation;
    private final User currentUser;
    private final UserDashboardView parentDashboard;
    private final BookingController bookingController;
    private final PromotionController promotionController;
    private final PaymentController paymentController;

    private JSpinner checkInDateSpinner;
    private JSpinner checkOutDateSpinner;
    private JSpinner guestsSpinner;
    private JTextField promoCodeField;
    private JButton applyPromoButton;
    private JButton checkAvailabilityButton;
    private JButton bookButton;
    private JButton cancelButton;
    private JLabel totalPriceLabel;
    private JLabel availabilityLabel;
    private JLabel discountLabel;
    private JLabel nightsLabel;
    private JLabel bookingSummaryLabel;
    private JComboBox<String> paymentMethodComboBox;
    private JPanel cardPanel;
    private JTextField cardNumberField;
    private JTextField cardHolderField;
    private JTextField expiryDateField;
    private JTextField cvvField;

    private double discountPercentage = 0;
    private boolean isAvailable = false;
    private double totalPrice = 0;
    private int numberOfNights = 0;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public BookingFormView(Accommodation accommodation, User currentUser, UserDashboardView parentDashboard) {
        this.accommodation = accommodation;
        this.currentUser = currentUser;
        this.parentDashboard = parentDashboard;
        this.bookingController = new BookingController();
        this.promotionController = new PromotionController();
        this.paymentController = new PaymentController();

        setTitle("Complete your booking - " + accommodation.getName());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1300, 900);
        setLocationRelativeTo(null);
        setResizable(false);

        if (this.currentUser == null) {
            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Vous devez être connecté pour effectuer une réservation.\nSouhaitez-vous vous connecter maintenant ?",
                    "Connexion requise",
                    JOptionPane.YES_NO_OPTION
            );
            if (choice == JOptionPane.YES_OPTION) {
                new LoginView(() -> SwingUtilities.invokeLater(() -> {
                    User user = LoginManager.getCurrentUser();
                    if (user != null) {
                        new BookingFormView(accommodation, user, null).setVisible(true);
                    }
                })).setVisible(true);
                this.dispose();
                return;
            } else {
                this.dispose();
                return;
            }
        }

        initComponents();
        calculateInitialPrice();
    }

    private void initComponents() {
        // Main panel with vertical BoxLayout
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Add components to main panel
        mainPanel.add(createHeaderPanel());
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(createBookingDetailsPanel());
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(createPaymentPanel());
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(createButtonsPanel());

        // Add scroll pane
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Add to frame
        setContentPane(scrollPane);
    }

    private String capitalizeFirstLetter(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }

    private String getPrimaryImageUrl(int accommodationId) {
        String imageUrl = null;
        String query = "SELECT image_url FROM accommodation_images WHERE accommodation_id = ? AND is_primary = TRUE LIMIT 1";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, accommodationId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                imageUrl = rs.getString("image_url");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return imageUrl;
    }


    // 🔜 Partie 2 : createHeaderPanel
    // Suivie de createBookingDetailsPanel, createPaymentPanel, createButtonsPanel, calculateInitialPrice, calculatePrice, etc.


    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout(20, 0));
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setMaximumSize(new Dimension(Short.MAX_VALUE, 100));

        // Info hébergement à gauche
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);

        JLabel nameLabel = new JLabel(accommodation.getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 24));
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel addressLabel = new JLabel(accommodation.getAddress() + ", " + accommodation.getCity() + ", " + accommodation.getCountry());
        addressLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        addressLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel typeLabel = new JLabel(capitalizeFirstLetter(accommodation.getAccommodationType()) + " • Max " + accommodation.getMaxGuests() + " guests");
        typeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        typeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        infoPanel.add(nameLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(addressLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(typeLabel);

        // Image à droite
        JPanel imagePanel = new JPanel(new BorderLayout()) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(150, 100);
            }
        };
        imagePanel.setBackground(new Color(230, 230, 250));

        String imageUrl = getPrimaryImageUrl(accommodation.getId());

        if (imageUrl != null) {
            try {
                ImageIcon originalIcon = new ImageIcon(getClass().getClassLoader().getResource("images/" + imageUrl));
                Image scaledImage = originalIcon.getImage().getScaledInstance(150, 100, Image.SCALE_SMOOTH);
                JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
                imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
                imagePanel.add(imageLabel, BorderLayout.CENTER);
            } catch (Exception e) {
                JLabel errorLabel = new JLabel("Image not found");
                errorLabel.setForeground(Color.GRAY);
                imagePanel.add(errorLabel, BorderLayout.CENTER);
            }
        } else {
            JLabel noImageLabel = new JLabel("No image");
            noImageLabel.setForeground(Color.GRAY);
            imagePanel.add(noImageLabel, BorderLayout.CENTER);
        }

        headerPanel.add(infoPanel, BorderLayout.CENTER);
        headerPanel.add(imagePanel, BorderLayout.EAST);
        return headerPanel;
    }
    private JPanel createBookingDetailsPanel() {
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.X_AXIS));
        detailsPanel.setBackground(Color.WHITE);
        detailsPanel.setMaximumSize(new Dimension(Short.MAX_VALUE, 300));

        // 🎯 Formulaire de gauche
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(230, 230, 230), 1),
                new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel formTitle = new JLabel("Your booking details");
        formTitle.setFont(new Font("Arial", Font.BOLD, 18));
        formTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        // 📅 Dates
        JPanel datesPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        datesPanel.setBackground(Color.WHITE);
        datesPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel checkInLabel = new JLabel("Check-in date:");
        checkInLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        Calendar tomorrow = Calendar.getInstance();
        tomorrow.add(Calendar.DAY_OF_MONTH, 1);
        SpinnerDateModel checkInModel = new SpinnerDateModel(tomorrow.getTime(), null, null, Calendar.DAY_OF_MONTH);
        checkInDateSpinner = new JSpinner(checkInModel);
        JSpinner.DateEditor checkInEditor = new JSpinner.DateEditor(checkInDateSpinner, "MMM d, yyyy");
        checkInDateSpinner.setEditor(checkInEditor);
        checkInDateSpinner.addChangeListener(e -> calculatePrice());

        JLabel checkOutLabel = new JLabel("Check-out date:");
        checkOutLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        Calendar checkout = Calendar.getInstance();
        checkout.add(Calendar.DAY_OF_MONTH, 3);
        SpinnerDateModel checkOutModel = new SpinnerDateModel(checkout.getTime(), null, null, Calendar.DAY_OF_MONTH);
        checkOutDateSpinner = new JSpinner(checkOutModel);
        JSpinner.DateEditor checkOutEditor = new JSpinner.DateEditor(checkOutDateSpinner, "MMM d, yyyy");
        checkOutDateSpinner.setEditor(checkOutEditor);
        checkOutDateSpinner.addChangeListener(e -> calculatePrice());

        datesPanel.add(checkInLabel);
        datesPanel.add(checkInDateSpinner);
        datesPanel.add(checkOutLabel);
        datesPanel.add(checkOutDateSpinner);

        // 👥 Invités
        JPanel guestsPanel = new JPanel(new BorderLayout(10, 0));
        guestsPanel.setBackground(Color.WHITE);
        guestsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel guestsLabel = new JLabel("Number of guests:");
        guestsLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        SpinnerNumberModel guestsModel = new SpinnerNumberModel(2, 1, accommodation.getMaxGuests(), 1);
        guestsSpinner = new JSpinner(guestsModel);
        guestsSpinner.addChangeListener(e -> calculatePrice());

        guestsPanel.add(guestsLabel, BorderLayout.WEST);
        guestsPanel.add(guestsSpinner, BorderLayout.EAST);

        // 🎁 Code promo conditionnel
        JPanel promoPanel = new JPanel(new BorderLayout(10, 0));
        promoPanel.setBackground(Color.WHITE);
        promoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel promoLabel = new JLabel("Promo code:");
        promoLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        JPanel promoInputPanel = new JPanel(new BorderLayout(5, 0));
        promoInputPanel.setBackground(Color.WHITE);

        promoCodeField = new JTextField();
        applyPromoButton = new JButton("Apply");
        applyPromoButton.addActionListener(this);

        promoInputPanel.add(promoCodeField, BorderLayout.CENTER);
        promoInputPanel.add(applyPromoButton, BorderLayout.EAST);

        promoPanel.add(promoLabel, BorderLayout.WEST);
        promoPanel.add(promoInputPanel, BorderLayout.CENTER);

        // 🔒 Contrôle affichage promo
        boolean isEligibleForPromo = checkNewUserStatus(currentUser.getId());
        promoPanel.setVisible(isEligibleForPromo);

        // 📆 Disponibilité
        JPanel availabilityPanel = new JPanel(new BorderLayout(10, 0));
        availabilityPanel.setBackground(Color.WHITE);
        availabilityPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        availabilityLabel = new JLabel("Availability will be checked when you book");
        availabilityLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        availabilityLabel.setForeground(Color.GRAY);

        checkAvailabilityButton = new JButton("Check Availability");
        checkAvailabilityButton.addActionListener(this);

        availabilityPanel.add(availabilityLabel, BorderLayout.CENTER);
        availabilityPanel.add(checkAvailabilityButton, BorderLayout.EAST);

        // ✅ Ajout au panel gauche
        formPanel.add(formTitle);
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(datesPanel);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(guestsPanel);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(promoPanel);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(availabilityPanel);

        // 📊 Panel résumé à droite
        JPanel summaryPanel = new JPanel();
        summaryPanel.setLayout(new BoxLayout(summaryPanel, BoxLayout.Y_AXIS));
        summaryPanel.setBackground(Color.WHITE);
        summaryPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(230, 230, 230), 1),
                new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel summaryTitle = new JLabel("Price summary");
        summaryTitle.setFont(new Font("Arial", Font.BOLD, 18));
        summaryTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        bookingSummaryLabel = new JLabel("Your stay details will appear here");
        bookingSummaryLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        bookingSummaryLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel priceBreakdownPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        priceBreakdownPanel.setBackground(Color.WHITE);
        priceBreakdownPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel baseRateLabel = new JLabel("Base rate:");
        baseRateLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel baseRateValueLabel = new JLabel("€" + String.format("%.2f", accommodation.getPricePerNight()) + " per night");
        baseRateValueLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel nightsTextLabel = new JLabel("Number of nights:");
        nightsTextLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        nightsLabel = new JLabel("0");
        nightsLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel discountTextLabel = new JLabel("Discount:");
        discountTextLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        discountLabel = new JLabel("0%");
        discountLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel totalTextLabel = new JLabel("Total price:");
        totalTextLabel.setFont(new Font("Arial", Font.BOLD, 16));

        totalPriceLabel = new JLabel("€0.00");
        totalPriceLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalPriceLabel.setForeground(new Color(0, 113, 194));

        priceBreakdownPanel.add(baseRateLabel);
        priceBreakdownPanel.add(baseRateValueLabel);
        priceBreakdownPanel.add(nightsTextLabel);
        priceBreakdownPanel.add(nightsLabel);
        priceBreakdownPanel.add(discountTextLabel);
        priceBreakdownPanel.add(discountLabel);
        priceBreakdownPanel.add(totalTextLabel);
        priceBreakdownPanel.add(totalPriceLabel);

        summaryPanel.add(summaryTitle);
        summaryPanel.add(Box.createVerticalStrut(10));
        summaryPanel.add(bookingSummaryLabel);
        summaryPanel.add(Box.createVerticalStrut(20));
        summaryPanel.add(priceBreakdownPanel);

        // 📦 Fusion des deux panels
        detailsPanel.add(formPanel);
        detailsPanel.add(Box.createHorizontalStrut(20));
        detailsPanel.add(summaryPanel);

        return detailsPanel;
    }
    private JPanel createPaymentPanel() {
        JPanel paymentPanel = new JPanel();
        paymentPanel.setLayout(new BoxLayout(paymentPanel, BoxLayout.Y_AXIS));
        paymentPanel.setBackground(Color.WHITE);
        paymentPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(230, 230, 230), 1),
                new EmptyBorder(20, 20, 20, 20)
        ));
        paymentPanel.setMaximumSize(new Dimension(Short.MAX_VALUE, 250));

        JLabel paymentTitle = new JLabel("Payment details");
        paymentTitle.setFont(new Font("Arial", Font.BOLD, 18));
        paymentTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        // 🔄 Choix du mode de paiement
        JPanel methodPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        methodPanel.setBackground(Color.WHITE);
        methodPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel methodLabel = new JLabel("Payment method:");
        methodLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        String[] methods = {"Credit Card", "PayPal", "Bank Transfer"};
        paymentMethodComboBox = new JComboBox<>(methods);
        paymentMethodComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        paymentMethodComboBox.addActionListener(e -> updatePaymentForm());

        methodPanel.add(methodLabel);
        methodPanel.add(paymentMethodComboBox);

        // 🧾 Zone dynamique selon le mode
        cardPanel = new JPanel(new CardLayout());
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // 💳 Carte bancaire
        JPanel creditCardForm = new JPanel(new GridLayout(4, 2, 15, 10));
        creditCardForm.setBackground(Color.WHITE);

        JLabel cardNumberLabel = new JLabel("Card number:");
        cardNumberLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        cardNumberField = new JTextField();

        JLabel cardHolderLabel = new JLabel("Card holder name:");
        cardHolderLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        cardHolderField = new JTextField();

        JLabel expiryDateLabel = new JLabel("Expiry date (MM/YY):");
        expiryDateLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        expiryDateField = new JTextField();

        JLabel cvvLabel = new JLabel("CVV:");
        cvvLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        cvvField = new JTextField();

        creditCardForm.add(cardNumberLabel);
        creditCardForm.add(cardNumberField);
        creditCardForm.add(cardHolderLabel);
        creditCardForm.add(cardHolderField);
        creditCardForm.add(expiryDateLabel);
        creditCardForm.add(expiryDateField);
        creditCardForm.add(cvvLabel);
        creditCardForm.add(cvvField);

        // 🅿 PayPal
        JPanel paypalForm = new JPanel(new BorderLayout());
        paypalForm.setBackground(Color.WHITE);
        JLabel paypalLabel = new JLabel("You will be redirected to PayPal to complete your payment after booking.");
        paypalLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        paypalForm.add(paypalLabel, BorderLayout.CENTER);

        // 🏦 Virement bancaire
        JPanel bankTransferForm = new JPanel(new BorderLayout());
        bankTransferForm.setBackground(Color.WHITE);
        JLabel bankLabel = new JLabel("Bank account details will be provided after booking.");
        bankLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        bankTransferForm.add(bankLabel, BorderLayout.CENTER);

        // ➕ Ajout dans le CardLayout
        cardPanel.add(creditCardForm, "Credit Card");
        cardPanel.add(paypalForm, "PayPal");
        cardPanel.add(bankTransferForm, "Bank Transfer");

        // 🧩 Ajout au panneau principal
        paymentPanel.add(paymentTitle);
        paymentPanel.add(Box.createVerticalStrut(15));
        paymentPanel.add(methodPanel);
        paymentPanel.add(Box.createVerticalStrut(15));
        paymentPanel.add(cardPanel);

        return paymentPanel;
    }

    private JPanel createButtonsPanel() {
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonsPanel.setBackground(Color.WHITE);
        buttonsPanel.setMaximumSize(new Dimension(Short.MAX_VALUE, 50));

        // 🔴 Bouton Annuler
        cancelButton = new JButton("Cancel");
        cancelButton.setBackground(new Color(153, 0, 0)); // Rouge foncé
        cancelButton.setForeground(Color.WHITE); // Texte blanc
        cancelButton.setFont(new Font("Arial", Font.BOLD, 14));
        cancelButton.setOpaque(true);
        cancelButton.setContentAreaFilled(true);
        cancelButton.setFocusPainted(false);
        cancelButton.addActionListener(this);

        // 🟢 Bouton Réserver
        bookButton = new JButton("Complete Booking");
        bookButton.setBackground(new Color(0, 128, 0)); // Vert
        bookButton.setForeground(Color.WHITE); // Texte blanc
        bookButton.setFont(new Font("Arial", Font.BOLD, 14));
        bookButton.setOpaque(true);
        bookButton.setContentAreaFilled(true);
        bookButton.setFocusPainted(false);
        bookButton.addActionListener(this);

        // ➕ Ajout à la droite
        buttonsPanel.add(cancelButton);
        buttonsPanel.add(bookButton);

        return buttonsPanel;
    }

    private void updatePaymentForm() {
        CardLayout cl = (CardLayout) cardPanel.getLayout();
        cl.show(cardPanel, (String) paymentMethodComboBox.getSelectedItem());
    }


    private void calculateInitialPrice() {
        try {
            // Get dates from spinners
            Date checkInDate = (Date) checkInDateSpinner.getValue();
            Date checkOutDate = (Date) checkOutDateSpinner.getValue();

            // Convert to LocalDate
            LocalDate checkIn = DateUtils.convertDateToLocalDate(checkInDate);
            LocalDate checkOut = DateUtils.convertDateToLocalDate(checkOutDate);

            // Calculate nights
            numberOfNights = DateUtils.calculateDaysBetween(checkIn, checkOut);
            nightsLabel.setText(String.valueOf(numberOfNights));

            // Calculate total price
            totalPrice = accommodation.getPricePerNight() * numberOfNights;
            totalPriceLabel.setText(String.format("€%.2f", totalPrice));

            // Update summary
            bookingSummaryLabel.setText("<html>" + numberOfNights + " nights at " + accommodation.getName() +
                    "<br>" + DateUtils.formatLocalDate(checkIn) + " to " +
                    DateUtils.formatLocalDate(checkOut) + "</html>");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void calculatePrice() {
        try {
            // Get dates from spinners
            Date checkInDate = (Date) checkInDateSpinner.getValue();
            Date checkOutDate = (Date) checkOutDateSpinner.getValue();

            // Convert to LocalDate
            LocalDate checkIn = DateUtils.convertDateToLocalDate(checkInDate);
            LocalDate checkOut = DateUtils.convertDateToLocalDate(checkOutDate);

            // Calculate number of nights
            numberOfNights = DateUtils.calculateDaysBetween(checkIn, checkOut);

            // 🛑 Validation: Check-out must be after check-in
            if (numberOfNights <= 0) {
                JOptionPane.showMessageDialog(this,
                        "Check-out date must be after check-in date.",
                        "Date Error",
                        JOptionPane.ERROR_MESSAGE);

                // Auto-fix: set checkout = checkin + 1 day
                Calendar newCheckout = Calendar.getInstance();
                newCheckout.setTime(checkInDate);
                newCheckout.add(Calendar.DAY_OF_MONTH, 1);
                checkOutDateSpinner.setValue(newCheckout.getTime());

                // Recalculate
                checkOutDate = (Date) checkOutDateSpinner.getValue();
                checkOut = DateUtils.convertDateToLocalDate(checkOutDate);
                numberOfNights = DateUtils.calculateDaysBetween(checkIn, checkOut);
            }

            nightsLabel.setText(String.valueOf(numberOfNights));

            // Base price
            double basePrice = accommodation.getPricePerNight() * numberOfNights;

            // Discount application
            if (discountPercentage > 0) {
                totalPrice = basePrice * (1 - (discountPercentage / 100));
            } else {
                totalPrice = basePrice;
            }

            totalPriceLabel.setText(String.format("€%.2f", totalPrice));

            // Update summary
            bookingSummaryLabel.setText("<html>" + numberOfNights + " nights at " + accommodation.getName() +
                    "<br>" + DateUtils.formatLocalDate(checkIn) + " to " +
                    DateUtils.formatLocalDate(checkOut) + "</html>");

            // Reset availability
            isAvailable = false;
            availabilityLabel.setText("Availability will be checked when you book");
            availabilityLabel.setForeground(Color.GRAY);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void checkAvailability() {
        try {
            Date checkInDate = (Date) checkInDateSpinner.getValue();
            Date checkOutDate = (Date) checkOutDateSpinner.getValue();

            LocalDate checkIn = DateUtils.convertDateToLocalDate(checkInDate);
            LocalDate checkOut = DateUtils.convertDateToLocalDate(checkOutDate);

            int numberOfGuests = (int) guestsSpinner.getValue();

            if (numberOfGuests > accommodation.getMaxGuests()) {
                JOptionPane.showMessageDialog(this,
                        "Maximum number of guests allowed is " + accommodation.getMaxGuests() + ".",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            isAvailable = bookingController.isAccommodationAvailable(accommodation.getId(), checkIn, checkOut);

            if (isAvailable) {
                availabilityLabel.setText("Available");
                availabilityLabel.setForeground(new Color(0, 128, 0));

                JOptionPane.showMessageDialog(this,
                        "The accommodation is available for the selected dates!",
                        "Availability Check",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                availabilityLabel.setText("Not Available");
                availabilityLabel.setForeground(Color.RED);

                JOptionPane.showMessageDialog(this,
                        "The accommodation is not available for the selected dates.",
                        "Availability Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error checking availability. Please try again.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    private void applyPromoCode() {
        String promoCode = promoCodeField.getText().trim();

        if (promoCode.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a promo code.",
                    "Promo Code Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Double discount = promotionController.getPromotionDiscount(promoCode);

        if (discount > 0) {
            discountPercentage = discount;
            discountLabel.setText(String.format("%.2f%%", discountPercentage));
            JOptionPane.showMessageDialog(this,
                    "Promo code applied successfully! " +
                            String.format("%.2f%% discount will be applied to your booking.", discountPercentage),
                    "Promo Code Success",
                    JOptionPane.INFORMATION_MESSAGE);

            calculatePrice();
        } else {
            discountPercentage = 0;
            discountLabel.setText("0%");
            JOptionPane.showMessageDialog(this,
                    "Invalid or expired promo code.",
                    "Promo Code Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    private void processBooking() {
        if (currentUser == null) {
            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Vous devez être connecté pour effectuer une réservation.\nSouhaitez-vous vous connecter maintenant ?",
                    "Connexion requise",
                    JOptionPane.YES_NO_OPTION
            );

            if (choice == JOptionPane.YES_OPTION) {
                new LoginView(() -> SwingUtilities.invokeLater(() -> {
                    User user = LoginManager.getCurrentUser();
                    if (user != null) {
                        new BookingFormView(accommodation, user, parentDashboard).setVisible(true);
                    }
                })).setVisible(true);
                this.dispose();
            }

            return;
        }

        if (!isAvailable) {
            checkAvailability();
            if (!isAvailable) return;
        }

        try {
            Date checkInDate = (Date) checkInDateSpinner.getValue();
            Date checkOutDate = (Date) checkOutDateSpinner.getValue();

            LocalDate checkIn = DateUtils.convertDateToLocalDate(checkInDate);
            LocalDate checkOut = DateUtils.convertDateToLocalDate(checkOutDate);

            int numberOfGuests = (int) guestsSpinner.getValue();
            String paymentMethod = (String) paymentMethodComboBox.getSelectedItem();

            if ("Credit Card".equals(paymentMethod)) {
                if (cardNumberField.getText().trim().isEmpty() ||
                        cardHolderField.getText().trim().isEmpty() ||
                        expiryDateField.getText().trim().isEmpty() ||
                        cvvField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "Please fill in all credit card details.",
                            "Payment Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            String paymentMethodCode;
            switch (paymentMethod) {
                case "Credit Card":
                    paymentMethodCode = "CREDIT_CARD";
                    break;
                case "PayPal":
                    paymentMethodCode = "PAYPAL";
                    break;
                case "Bank Transfer":
                    paymentMethodCode = "BANK_TRANSFER";
                    break;
                default:
                    paymentMethodCode = "CREDIT_CARD";
                    break;
            }


            boolean bookingSuccess = bookingController.createBooking(
                    currentUser.getId(),
                    accommodation.getId(),
                    checkIn,
                    checkOut,
                    numberOfGuests,
                    accommodation.getPricePerNight(),
                    discountPercentage
            );

            if (!bookingSuccess) {
                JOptionPane.showMessageDialog(this,
                        "Failed to create booking. Please try again.",
                        "Booking Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            int bookingId = bookingController.getBookingsByUserId(currentUser.getId())
                    .stream()
                    .mapToInt(b -> b.getId())
                    .max()
                    .orElse(0);

            if (bookingId == 0) {
                JOptionPane.showMessageDialog(this,
                        "Failed to retrieve booking details. Please contact support.",
                        "Booking Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean paymentSuccess = paymentController.processPayment(bookingId, totalPrice, paymentMethodCode);

            if (paymentSuccess) {
                JOptionPane.showMessageDialog(this,
                        "Booking and payment completed successfully!\n" +
                                "Your booking ID is: " + bookingId + "\n\n" +
                                "A confirmation email has been sent to your email address.",
                        "Booking Successful",
                        JOptionPane.INFORMATION_MESSAGE);

                if (parentDashboard != null) {
                    parentDashboard.refreshBookings();
                }

                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Payment failed. Please try again or use a different payment method.",
                        "Payment Error",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "An error occurred during booking. Please try again.",
                    "Booking Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }



    private boolean checkNewUserStatus(int userId) {
        boolean isEligible = false;
        String query = "SELECT new_users FROM users WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                isEligible = rs.getInt("new_users") == 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isEligible;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == checkAvailabilityButton) {
            checkAvailability();
        } else if (source == applyPromoButton) {
            applyPromoCode();
        } else if (source == bookButton) {
            processBooking();
        } else if (source == cancelButton) {
            this.dispose();
        }
    }



}
