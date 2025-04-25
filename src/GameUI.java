import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GameUI extends JFrame{
    private final Game game;

    private final JLabel playerLabel;
    private final JLabel coinsLabel;
    private final JLabel rollResultLabel;
    private final JButton rollButton;
    private final JButton nextTurnButton;

    private final JTextArea buildingsArea;

    private final JComboBox<String> buildingSelector;
    private final JButton buyButton;

    private final JComboBox<String> landmarkSelector;
    private final JButton buildLandmarkButton;
    private final JTextArea landmarkStatusArea;

    private boolean oneTurn = true;
    private boolean secondTurn = false;
    private boolean skipTurn = false;

    public GameUI(int playerCount) {
        this.game = new Game(playerCount);
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

        //Buttons
        buyButton = new JButton("🛒 Kaufen");
        buyButton.setEnabled(false);

        buildLandmarkButton = new JButton("🏛️ Bauen");
        buildLandmarkButton.setEnabled(false);

        rollButton = new JButton("🎲 Würfeln");

        nextTurnButton = new JButton("➡️ Nächster Spieler");
        nextTurnButton.setEnabled(false);

        buyButton.addActionListener(e -> {
            int index = buildingSelector.getSelectedIndex();
            Building selected = game.getAvailableBuildings().get(index);
            Player player = game.getCurrentPlayer();

            if (player.spendCoins(selected.getCost())) {
                player.getBuildings().add(new Building(
                        selected.getName(), selected.getActivationNumber1(), selected.getActivationNumber2(),
                        selected.getActivationNumber3(), selected.getIncome(), selected.getColor(), selected.getCost(),
                        selected.getSymbole()
                ));
                updateUI();
                buildLandmarkButton.setEnabled(false);
                buyButton.setEnabled(false);
                oneTurn = true;
                secondTurn = true;
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

        buildLandmarkButton.addActionListener(e -> {
            int index = landmarkSelector.getSelectedIndex();
            if (index < 0) return;

            Landmark selected = game.getCurrentPlayer().getUnbuiltLandmarks().get(index);
            Player player = game.getCurrentPlayer();

            //Großprojekt bauen und Nachricht dazu
            if (player.spendCoins(selected.getCost())) {
                selected.build();
                updateUI();
                if (selected.getName().equals("Bahnhof")) {
                    JOptionPane.showMessageDialog(this, "Du darfst ab jetzt mit zwei Würfeln würfeln!");
                }
                if (selected.getName().equals("Einkaufszentrum")) {
                    player.activateShoppingMall();
                    JOptionPane.showMessageDialog(this,
                            "Du erhältst ab jetzt eine Münze mehr für alle deine Geschäfts und Gastronomie Unternehmen!");
                }
                if (selected.getName().equals("Freizeitpark")) {
                    JOptionPane.showMessageDialog(this, "Wenn du einen Pasch würfelst, hast du einen weiteren Zug!");
                }
                buildLandmarkButton.setEnabled(false);
                buyButton.setEnabled(false);
                oneTurn = true;
                secondTurn = true;
            } else {
                JOptionPane.showMessageDialog(this, "Nicht genug Münzen!");
            }
        });

        landmarkPanel.add(landmarkSelector);
        landmarkPanel.add(buildLandmarkButton);
        add(landmarkPanel, BorderLayout.WEST);

        // Bottom Panel
        JPanel buttonPanel = new JPanel();

        rollButton.addActionListener(e -> {
            Player current = game.getCurrentPlayer();

            if (game.hasRolledPair() && current.hasAmusementPark() && !oneTurn) {
                int option = JOptionPane.showOptionDialog(
                        this,
                        "Möchtest du wirklich ohne etwas zu bauen erneut würfeln",
                        "Zweiter Zug",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        new String[]{"Ja","Nein"},
                        "Ja"
                );

                switch (option) {
                    case 0 -> {
                        oneTurn = true;
                        secondTurn = true;
                    }
                    case 1 -> {return;}
                }
            }

            game.setRolledPair();

            int dice = 1;
            if (current.hasTrainStation()) {
                int choice = JOptionPane.showConfirmDialog(
                        this,
                        "Möchtest du mit 2 Würfeln werfen?",
                        "Bahnhof aktiviert",
                        JOptionPane.YES_NO_OPTION
                );
                if (choice == JOptionPane.YES_OPTION) {
                    dice = 2;
                }
            }

            int roll = game.rollDice(dice);
            if (game.hasRolledPair() && !secondTurn) {
                JOptionPane.showMessageDialog(this, "🎲 Du hast einen " + roll/2 + "er Pasch (" + roll + ") gewürfelt!");
                if (current.hasAmusementPark()) {
                    JOptionPane.showMessageDialog(this, "Du hast einen weiteren Zug!");
                    oneTurn = false;
                    skipTurn = true;
                }
            } else {
                JOptionPane.showMessageDialog(this, "🎲 Du hast eine " + roll + " gewürfelt!");
            }

            if(game.townHallEffectApplies()) {
                JOptionPane.showMessageDialog(this, " Du erhältst eine Münze vom Rathaus!");
                game.setTownHallEffectApplies();
            }

            updateUI();
            if (oneTurn) {
                rollButton.setEnabled(false);
            }
            nextTurnButton.setEnabled(true);
            buyButton.setEnabled(true);
            buildLandmarkButton.setEnabled(true);
        });

        nextTurnButton.addActionListener(e -> {
            Player current = game.getCurrentPlayer();

            if (game.hasRolledPair() && current.hasAmusementPark() && skipTurn) {
                int option = JOptionPane.showOptionDialog(
                        this,
                        "Möchtest du wirklich deinen zweiten Zug abgeben",
                        "Zug abgegeben",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        new String[]{"Ja","Nein"},
                        "Ja"
                );

                switch (option) {
                    case 0 -> {
                        oneTurn = true;
                        secondTurn = false;
                        skipTurn = false;
                        game.nextTurn();
                        updateUI();
                        rollButton.setEnabled(true);
                        nextTurnButton.setEnabled(false);
                        buyButton.setEnabled(false);
                        buildLandmarkButton.setEnabled(false);
                        return;
                    }
                    case 1 -> {return;}
                }
            }

            secondTurn = false;
            skipTurn = false;
            game.nextTurn();
            updateUI();
            rollButton.setEnabled(true);
            nextTurnButton.setEnabled(false);
            buyButton.setEnabled(false);
            buildLandmarkButton.setEnabled(false);
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

        // Landmarken anzeigen
        StringBuilder sbLandmarks = new StringBuilder("🏗️ Großprojekte:\n");
        for (Landmark l : game.getCurrentPlayer().getLandmarks()) {
            sbLandmarks.append("• ").append(l.toDisplayString())
                    .append("\n");
        }
        landmarkStatusArea.setText(sbLandmarks.toString());

        if (game.hasWinner()) {
            Player winner = game.getCurrentPlayer();
            int option = JOptionPane.showOptionDialog(
                    this,
                    "🎉 " + winner.getName() + " hat alle Großprojekte gebaut und gewinnt das Spiel!\n" +
                            "Was möchtest du als Nächstes tun?",
                    "🏆 Spiel gewonnen",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    new String[]{"🔁 Zurück zum Hauptmenü","🚪 Beenden"},
                    "🔁 Neustart"
            );

            switch (option) {
                case 0 -> restartGame();
                case 1 -> System.exit(0);
            }
        }
    }

    private void restartGame() {
        new MainMenu().setVisible(true);
        dispose();
    }
}
