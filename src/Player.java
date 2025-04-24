import java.util.*;

public class Player {
    private String name;
    private int coins ;
    private List<Building> buildings;
    private List<Landmark> landmarks;

    public Player(String name) {
        this.name = name;
        this.coins = 3;
        this.buildings = new ArrayList<>();
        this.landmarks = new ArrayList<>();
    }

    public void addStartingBuildings() {
        buildings.add(new Building("Weizenfeld", 1, 0, 0, 1, "blue", 1));
        buildings.add(new Building("Bäckerei", 2, 3, 0, 1,"green", 1));
    }

    public void addStartingLandmarks() {
        landmarks.add(new Landmark("Rathaus", 0, true));
        landmarks.add(new Landmark("Bahnhof", 4, false));
        landmarks.add(new Landmark("Einkaufszentrum", 10, false));
        landmarks.add(new Landmark("Freizeitpark", 16, false));
        landmarks.add(new Landmark("Funkturm", 22, false));
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

    public List<Landmark> getUnbuiltLandmarks() {
        List<Landmark> result = new ArrayList<>();
        for (Landmark l : landmarks) {
            if (!l.isBuilt()) {
                result.add(l);
            }
        }
        return result;
    }

    public List<Landmark> getLandmarks() {
        return landmarks;
    }

    public boolean hasTrainStation() {
        return landmarks.stream()
                .anyMatch(l -> l.getName().equals("Bahnhof") && l.isBuilt());
    }

    public boolean hasBuiltAllLandmarks() {
        return landmarks.stream().allMatch(Landmark::isBuilt);
    }
}
