package com.booking.view;

import com.booking.model.Accommodation;
import com.booking.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class CityAccommodationView extends JFrame {

    private final User user; // 🟡 attribut pour l'utilisateur connecté

    public CityAccommodationView(String city, List<Accommodation> accommodations, User user) {
        this.user = user;

        setTitle("Logements à " + city);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Logements disponibles à " + city, SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(title, BorderLayout.NORTH);

        JPanel listPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        for (Accommodation acc : accommodations) {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            panel.setBackground(Color.WHITE);

            JLabel name = new JLabel(acc.getName() + " - " + acc.getPricePerNight() + "€/nuit (" + acc.getRating() + "★)", SwingConstants.LEFT);
            name.setFont(new Font("Arial", Font.BOLD, 16));

            JLabel details = new JLabel("Capacité : " + acc.getMaxGuests() + " pers · Type : " + acc.getAccommodationType(), SwingConstants.LEFT);
            details.setFont(new Font("Arial", Font.PLAIN, 14));

            JPanel textPanel = new JPanel(new GridLayout(2, 1));
            textPanel.add(name);
            textPanel.add(details);

            JButton btnDetails = new JButton("Voir détails");
            btnDetails.addActionListener((ActionEvent e) -> {
                new AccommodationDetailsView(acc, user).setVisible(true); // ✅ passe l'objet acc et l'utilisateur connecté
                dispose();
            });

            panel.add(textPanel, BorderLayout.CENTER);
            panel.add(btnDetails, BorderLayout.EAST);

            listPanel.add(panel);
        }

        add(scrollPane, BorderLayout.CENTER);

        JButton retourBtn = new JButton("⬅ Retour à l’accueil");
        retourBtn.addActionListener(e -> {
            retourBtn.addActionListener(es -> {
                if (user != null) {
                    new AccueilView(user); // 👍 utilisateur connecté → AccueilView
                } else {
                    new HomeView().setVisible(true);;// ✅ utilisateur non connecté → HomeView avec la page bleue stylée

                }
                dispose();
            });

        });

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.add(retourBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }
}
