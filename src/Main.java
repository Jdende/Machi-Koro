public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            Game game = new Game();
            new GameUI(game);
        });

    }
}
