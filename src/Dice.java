import java.util.Random;

public class Dice {
    private static final Random rand = new Random();

    public static int roll() {
        return rand.nextInt(6) + 1;
    }
}
