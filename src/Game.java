import java.util.*;

public class Game {
    private List<Player> players = new ArrayList<>();
    private int currentPlayerIndex = 0;
    private int lastRoll = 0;

    public Game() {
        players.add(new Player("Spieler 1"));
        players.add(new Player("Spieler 2"));
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
    public void rollDice() {
        lastRoll = Dice.roll();
        System.out.println(getCurrentPlayer().getName() + " würfelt: " + lastRoll);
        distributeIncome();
    }

    private void distributeIncome() {
        for (Player player : players) {
            for (Building b : player.getBuildings()) {
                if (b.getActivationNumber() == lastRoll) {
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
}
