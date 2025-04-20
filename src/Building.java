public class Building {
    private String name;
    private int activationNumber;
    private int income;
    private String color; // z.B. blue, green, red

    public Building(String name, int activationNumber, int income, String color) {
        this.name = name;
        this.activationNumber = activationNumber;
        this.income = income;
        this.color = color;
    }

    public String getName() { return name; }
    public int getActivationNumber() { return activationNumber; }
    public int getIncome() { return income; }
    public String getColor() { return color; }
}
