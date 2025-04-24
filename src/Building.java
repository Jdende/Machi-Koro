public class Building {
    private String name;
    private int activationNumber1;
    private int activationNumber2;
    private int activationNumber3;
    private int income;
    private String color;
    private int cost;

    public Building(String name, int activationNumber1, int activationNumber2, int activationNumber3, int income, String color, int cost) {
        this.name = name;
        this.activationNumber1 = activationNumber1;
        this.activationNumber2 = activationNumber2;
        this.activationNumber3 = activationNumber3;
        this.income = income;
        this.cost = cost;
        this.color = color;
    }

    public String getName() { return name; }
    public int getActivationNumber1() { return activationNumber1; }
    public int getActivationNumber2() {
        return activationNumber2;
    }

    public int getActivationNumber3() {
        return activationNumber3;
    }
    public int getIncome() { return income; }
    public String getColor() { return color; }
    public int getCost() { return cost; }
}
