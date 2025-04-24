import javax.swing.*;
import java.awt.*;

public class MainMenu extends JFrame {

    public MainMenu() {
        setTitle("Machi Koro – Hauptmenü");
        setSize(300, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("🎲 Machi Koro", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        String[] options = {"2 Spieler", "3 Spieler", "4 Spieler"};
        JComboBox<String> playerCountBox = new JComboBox<>(options);
        playerCountBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(playerCountBox);

        JButton startButton = new JButton("🎮 Spiel starten");
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(startButton);

        JButton exitButton = new JButton("🚪 Beenden");
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(exitButton);

        add(centerPanel, BorderLayout.CENTER);

        // Button Aktionen
        startButton.addActionListener(e -> {
            int playerCount = playerCountBox.getSelectedIndex() + 2;
            new GameUI(playerCount);  // Starte Spiel
            dispose();  // Schließe Hauptmenü
        });

        exitButton.addActionListener(e -> System.exit(0));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainMenu().setVisible(true);
        });
    }
}
