import java.util.*;

public class Player {
    private String name;
    private int coins = 3;
    private List<Building> buildings = new ArrayList<>();
    private Set<Landmark> landmarks = new HashSet<>();

    public Player(String name) {
        this.name = name;
        buildings.add(new Building("Weizenfeld", 1, 1, false, 0,"blue"));
        buildings.add(new Building("Bäckerei", 1, 2, true, 0,"green"));
    }

    public String getName() { return name; }
    public int getCoins() { return coins; }
    public void addCoins(int amount) { coins += amount; }
    public boolean spendCoins(int amount) {
        if (coins >= amount) {
            coins -= amount;
            return true;
        }
        return false;
    }

    public List<Building> getBuildings() { return buildings; }

    public Map<String, Integer> getBuildingCounts() {
        Map<String, Integer> counts = new LinkedHashMap<>();
        for (Building b : buildings) {
            counts.put(b.getName(), counts.getOrDefault(b.getName(), 0) + 1);
        }
        return counts;
    }
}
