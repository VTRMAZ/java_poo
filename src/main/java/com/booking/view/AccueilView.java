//utilisateur connecté

package com.booking.view;

import com.booking.controller.AccommodationController;
import com.booking.model.Accommodation;
import com.booking.model.User;
import com.booking.utils.LoginManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.List;

public class AccueilView extends JFrame {

    private final AccommodationController accommodationController = new AccommodationController();
    private final User user;

    public AccueilView(User user) {
        this.user = user;

        setTitle("Accueil - Destinations populaires");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Destinations populaires", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 26));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);

        JPanel villesPanel = new JPanel(new GridLayout(2, 4, 15, 15));
        villesPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        List<String> villes = Arrays.asList("Paris", "Nice", "Chamonix", "Cannes", "Lyon", "Bordeaux", "Marseille", "Strasbourg");

        for (String ville : villes) {
            JPanel card = new JPanel(new BorderLayout());
            card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            card.setBackground(Color.WHITE);

            JLabel label = new JLabel(ville, SwingConstants.CENTER);
            label.setFont(new Font("Arial", Font.BOLD, 16));

            JButton voirBtn = new JButton("Voir les logements");
            voirBtn.addActionListener((ActionEvent e) -> {
                List<Accommodation> accommodations = accommodationController.getAccommodationsByCity(ville);
                new CityAccommodationView(ville, accommodations, user);
                dispose();
            });

            card.add(label, BorderLayout.CENTER);
            card.add(voirBtn, BorderLayout.SOUTH);

            villesPanel.add(card);
        }

        add(villesPanel, BorderLayout.CENTER);

        JButton loginBtn = new JButton("Se connecter");
        loginBtn.addActionListener(e -> {
            new LoginView(); // ou LoginView(user) si tu gères la session
            dispose();
        });

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(loginBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }
}
