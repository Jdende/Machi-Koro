import javax.swing.*;

public class Launcher {
    public static void main(String[] args) {
        startGame();
    }

    public static void startGame() {
        javax.swing.SwingUtilities.invokeLater(() -> {
            String[] options = { "2", "3", "4" };
            String selected = (String) JOptionPane.showInputDialog(
                    null,
                    "Wie viele Spieler?",
                    "Spielstart",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            if (selected != null) {
                int playerCount = Integer.parseInt(selected);
                Game game = new Game(playerCount);
                new GameUI(game);
            }
        });
    }
}
