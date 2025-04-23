import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GameUI extends JFrame{
    private Game game;

    private JLabel playerLabel;
    private JLabel coinsLabel;
    private JLabel rollResultLabel;
    private JButton rollButton;
    private JButton nextTurnButton;

    private JTextArea buildingsArea;

    private JComboBox<String> buildingSelector;
    private JButton buyButton;

    private boolean hasRolled = false;

    private JComboBox<String> landmarkSelector;
    private JButton buildLandmarkButton;
    private JTextArea landmarkStatusArea;


    public GameUI(Game game) {
        this.game = game;
        setTitle("Machi Koro (Basis)");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1920, 1080);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Top Panel: Aktueller Spieler
        JPanel topPanel = new JPanel();
        playerLabel = new JLabel();
        coinsLabel = new JLabel();
        buildingsArea = new JTextArea(5, 30);
        buildingsArea.setEditable(false);
        buildingsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(buildingsArea);
        topPanel.add(scrollPane);
        topPanel.add(playerLabel);
        topPanel.add(coinsLabel);
        add(topPanel, BorderLayout.NORTH);
        landmarkStatusArea = new JTextArea(4, 30);
        landmarkStatusArea.setEditable(false);
        landmarkStatusArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane landmarkScrollPane = new JScrollPane(landmarkStatusArea);
        topPanel.add(landmarkScrollPane);

        // Center Panel: Würfelergebnis
        JPanel centerPanel = new JPanel();
        rollResultLabel = new JLabel("Noch nicht gewürfelt");
        rollResultLabel.setFont(new Font("Arial", Font.BOLD, 20));
        centerPanel.add(rollResultLabel);
        add(centerPanel, BorderLayout.CENTER);

        // Kauf-Panel
        JPanel buyPanel = new JPanel();
        buyPanel.setBorder(BorderFactory.createTitledBorder("Gebäude kaufen"));

        List<Building> available = game.getAvailableBuildings();
        String[] options = available.stream()
                .map(b -> b.getName() + " (" + b.getCost() + "💰)")
                .toArray(String[]::new);

        buildingSelector = new JComboBox<>(options);
        buyButton = new JButton("🛒 Kaufen");
        buyButton.setEnabled(false);

        buyButton.addActionListener(e -> {
            if (!hasRolled) {
                JOptionPane.showMessageDialog(this, "Du musst erst würfeln!");
                return;
            }

            int index = buildingSelector.getSelectedIndex();
            Building selected = game.getAvailableBuildings().get(index);
            Player player = game.getCurrentPlayer();

            if (player.spendCoins(selected.getCost())) {
                player.getBuildings().add(new Building(
                        selected.getName(), selected.getActivationNumber(),
                        selected.getIncome(), selected.isSelfRoll(), selected.getColor(), selected.getCost()
                ));
                updateUI();
            } else {
                JOptionPane.showMessageDialog(this, "Nicht genug Münzen!");
            }
        });

        buyPanel.add(buildingSelector);
        buyPanel.add(buyButton);
        add(buyPanel, BorderLayout.EAST);

        // Landmark-Panel
        JPanel landmarkPanel = new JPanel();
        landmarkPanel.setBorder(BorderFactory.createTitledBorder("Großprojekt bauen"));

        landmarkSelector = new JComboBox<>();
        buildLandmarkButton = new JButton("🏛️ Bauen");
        buildLandmarkButton.setEnabled(false);

        buildLandmarkButton.addActionListener(e -> {
            int index = landmarkSelector.getSelectedIndex();
            if (index < 0) return;

            Landmark selected = game.getCurrentPlayer().getUnbuiltLandmarks().get(index);
            Player player = game.getCurrentPlayer();

            if (player.spendCoins(selected.getCost())) {
                selected.build();
                updateUI();
            } else {
                JOptionPane.showMessageDialog(this, "Nicht genug Münzen!");
            }
        });

        landmarkPanel.add(landmarkSelector);
        landmarkPanel.add(buildLandmarkButton);
        add(landmarkPanel, BorderLayout.WEST);

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
            buyButton.setEnabled(true);
            buildLandmarkButton.setEnabled(true);
            hasRolled = true;
        });

        nextTurnButton.addActionListener(e -> {
            game.nextTurn();
            updateUI();
            rollButton.setEnabled(true);
            nextTurnButton.setEnabled(false);
            buyButton.setEnabled(false);
            buildLandmarkButton.setEnabled(false);
            hasRolled = false;
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

        // Gebäudeübersicht aktualisieren
        StringBuilder sb = new StringBuilder("📦 Gebäude:\n");
        for (var entry : current.getBuildingCounts().entrySet()) {
            sb.append("• ").append(entry.getKey())
                    .append(" x").append(entry.getValue()).append("\n");
        }
        buildingsArea.setText(sb.toString());

        // Landmark-Dropdown aktualisieren
        landmarkSelector.removeAllItems();
        for (Landmark l : game.getCurrentPlayer().getUnbuiltLandmarks()) {
            landmarkSelector.addItem(l.getName() + " (" + l.getCost() + "💰)");
        }
        buildLandmarkButton.setEnabled(hasRolled && landmarkSelector.getItemCount() > 0);

        // Landmarken anzeigen
        StringBuilder sbLandmarks = new StringBuilder("🏗️ Großprojekte:\n");
        for (Landmark l : game.getCurrentPlayer().getLandmarks()) {
            sbLandmarks.append("• ").append(l.toDisplayString())
                    .append("\n");
        }
        landmarkStatusArea.setText(sbLandmarks.toString());
    }
}
