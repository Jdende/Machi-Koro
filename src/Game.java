import javax.swing.*;
import java.util.*;
import java.util.List;

public class Game {
    private final List<Player> players = new ArrayList<>();
    private int currentPlayerIndex = 0;
    private int lastRoll = 0;
    private boolean townHallEffectApplies = false;
    private boolean rolledPair = false;

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
            int dice1 = Dice.roll();
            int dice2 = Dice.roll();
            lastRoll = dice1 + dice2;
            if (dice1 == dice2) {
                rolledPair = true;
            }
        } else {
            lastRoll = Dice.roll();
        }

        distributeIncome();
        return lastRoll;
    }


    private void distributeIncome() {
        for (Player player : players) {
            for (Building b : player.getBuildings()) {
                if (b.getActivationNumber1() == lastRoll || b.getActivationNumber2() == lastRoll ||
                        b.getActivationNumber3() == lastRoll) {
                    if (b.getColor().equals("red") && player != getCurrentPlayer() &&
                            getCurrentPlayer().getCoins() >= b.getIncome()) {
                        player.addCoins(b.getIncome());
                        getCurrentPlayer().spendCoins(b.getIncome());
                    } else if (b.getColor().equals("blue")) {
                        player.addCoins(b.getIncome());
                    } else if (b.getColor().equals("green") && player == getCurrentPlayer()) {
                        player.addCoins(b.getIncome());
                    }
                }
            }
        }

        //Rathaus Effekt
        if (getCurrentPlayer().getCoins() == 0) {
            getCurrentPlayer().addCoins(1);
            townHallEffectApplies = true;
        }
    }

    // Nächster Spieler ist dran
    public void nextTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    public boolean hasWinner() {
        return getCurrentPlayer().hasBuiltAllLandmarks();
    }

    public boolean townHallEffectApplies() {
        return townHallEffectApplies;
    }

    public void setTownHallEffectApplies() {
        townHallEffectApplies = false;
    }

    public boolean hasRolledPair() {
        return rolledPair;
    }

    public void setRolledPair() {
        rolledPair = false;
    }

    public List<Building> getAvailableBuildings() {
        List<Building> buildings = new ArrayList<>();
        buildings.add(new Building("Weizenfeld", 1, 0, 0, 1,  "blue", 1, 1));
        buildings.add(new Building("Bäckerei", 2, 3, 0, 1,  "green", 1, 0));
        buildings.add(new Building("Cafe", 3, 0, 0, 1, "red", 2, 2));
        // Optional: weitere Karten hier hinzufügen
        return buildings;
    }
}
