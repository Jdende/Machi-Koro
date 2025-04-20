import javax.swing.*;
import java.awt.*;

public class GameUI extends JFrame{
    private Game game;

    private JLabel playerLabel;
    private JLabel coinsLabel;
    private JLabel rollResultLabel;
    private JButton rollButton;
    private JButton nextTurnButton;

    public GameUI(Game game) {
        this.game = game;
        setTitle("Machi Koro (Basis)");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Top Panel: Aktueller Spieler
        JPanel topPanel = new JPanel();
        playerLabel = new JLabel();
        coinsLabel = new JLabel();
        topPanel.add(playerLabel);
        topPanel.add(coinsLabel);
        add(topPanel, BorderLayout.NORTH);

        // Center Panel: Würfelergebnis
        JPanel centerPanel = new JPanel();
        rollResultLabel = new JLabel("Noch nicht gewürfelt");
        rollResultLabel.setFont(new Font("Arial", Font.BOLD, 20));
        centerPanel.add(rollResultLabel);
        add(centerPanel, BorderLayout.CENTER);

        // Bottom Panel: Buttons
        JPanel buttonPanel = new JPanel();
        rollButton = new JButton("🎲 Würfeln");
        nextTurnButton = new JButton("➡️ Nächster Spieler");
        nextTurnButton.setEnabled(false);

        rollButton.addActionListener(e -> {
            game.rollDice();
            updateUI();
            rollButton.setEnabled(false);
            nextTurnButton.setEnabled(true);
        });

        nextTurnButton.addActionListener(e -> {
            game.nextTurn();
            updateUI();
            rollButton.setEnabled(true);
            nextTurnButton.setEnabled(false);
        });

        buttonPanel.add(rollButton);
        buttonPanel.add(nextTurnButton);
        add(buttonPanel, BorderLayout.SOUTH);

        updateUI();
        setVisible(true);
    }

    private void updateUI() {
        Player current = game.getCurrentPlayer();
        playerLabel.setText("Am Zug: " + current.getName());
        coinsLabel.setText("💰 Münzen: " + current.getCoins());
        int roll = game.getLastRoll();
        rollResultLabel.setText(roll > 0 ? "🎲 Gewürfelt: " + roll : "Noch nicht gewürfelt");
    }
}
