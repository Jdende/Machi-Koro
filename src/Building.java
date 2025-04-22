public class Building {
    private String name;
    private int activationNumber;
    private int income;
    private String color;
    private boolean selfRoll;
    private int cost;

    public Building(String name, int activationNumber, int income, boolean selfRoll, int cost, String color) {
        this.name = name;
        this.activationNumber = activationNumber;
        this.income = income;
        this.selfRoll = selfRoll;
        this.cost = cost;
        this.color = color;
    }

    public String getName() { return name; }
    public int getActivationNumber() { return activationNumber; }
    public int getIncome() { return income; }
    public String getColor() { return color; }
    public int getCost() { return cost; }
    public boolean isSelfRoll() { return selfRoll; }
}
