import java.util.Random;

public class Dice {

    private int value;
    private Random r;
    private static final int NUMBER_SIDES = 6;

    public Dice() {
        r = new Random();
        value = 1;
    }

    public int getValue() {
        return value;
    }

    public void roll() {
        value = r.nextInt(NUMBER_SIDES) + 1;
    }

    @Override
    public String toString() {
        return "Dice{" +
                "value=" + value +
                '}';
    }
}
