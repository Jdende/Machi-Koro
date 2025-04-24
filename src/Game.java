import java.util.*;

public class Game {
    private List<Player> players = new ArrayList<>();
    private int currentPlayerIndex = 0;
    private int lastRoll = 0;

    public Game(int playerCount) {
        for (int i = 1; i <= playerCount; i++) {
            Player player = new Player("Spieler " + i);
            player.addStartingBuildings();
            player.addStartingLandmarks();
            players.add(player);
        }
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public Player getOtherPlayer() {
        return players.get((currentPlayerIndex + 1) % players.size());
    }

    public List<Player> getPlayers() {
        return players;
    }

    public int getLastRoll() {
        return lastRoll;
    }

    // Würfeln und Einkommen verteilen
    public int rollDice(int diceCount) {
        if (diceCount == 2) {
            lastRoll = Dice.roll() + Dice.roll();
        } else {
            lastRoll = Dice.roll();
        }

        distributeIncome();
        return lastRoll;
    }


    private void distributeIncome() {
        for (Player player : players) {
            for (Building b : player.getBuildings()) {
                if (b.getActivationNumber1() == lastRoll) {
                    if (b.getColor().equals("blue")) {
                        player.addCoins(b.getIncome());
                    } else if (b.getColor().equals("green") && player == getCurrentPlayer()) {
                        player.addCoins(b.getIncome());
                    }
                    // Rote Karten (Gegenspieler-Einkommen) lassen wir erstmal weg
                }
            }
        }
    }

    // Nächster Spieler ist dran
    public void nextTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    public boolean hasWinner() {
        return getCurrentPlayer().hasBuiltAllLandmarks();
    }

    public List<Building> getAvailableBuildings() {
        List<Building> buildings = new ArrayList<>();
        buildings.add(new Building("Weizenfeld", 1, 0, 0, 1,  "blue", 1));
        buildings.add(new Building("Bäckerei", 2, 3, 0, 1,  "green", 1));
        // Optional: weitere Karten hier hinzufügen
        return buildings;
    }
}
