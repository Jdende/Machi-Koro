public class Landmark {
    private String name;
    private int cost;
    private boolean built;

    public Landmark(String name, int cost, boolean built) {
        this.name = name;
        this.cost = cost;
        this.built = built;
    }

    public String getName() {
        return name;
    }

    public int getCost() {
        return cost;
    }

    public boolean isBuilt() {
        return built;
    }

    public void build() {
        this.built = true;
    }

    @Override
    public String toString() {
        return name + " (Kosten: " + cost + ", gebaut: " + built + ")";
    }

    public String toDisplayString() {
        return name + " " + (built ? "✅" : "❌");
    }
}
