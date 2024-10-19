import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Location {
    private String name;
    private String description;
    private List<Item> items; // Each location has an inventory. This allows a more fluid interaction.
    private List<Creature> creatures; // Multiple creatures could be in an area, but currently only one is set.
    private Map<String, Location> connections;

    public Location(String name, String description) {
        // getter/setter for the class. These are all used in generation before the playloop is started.
        this.name = name;
        this.description = description;
        this.items = new ArrayList<>();
        this.creatures = new ArrayList<>();
        this.connections = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description; // used to introduce the area to the player.
    }

    public void addItem(Item item) {
        // quite simple. It adds an item to the location's item list. This is set up to be interactable in the playloop.
        items.add(item);
    }

    public void removeItem(Item item) {
        // This method delets items. It is used when an item is picked up.
        items.remove(item);
    }

    public Item pickUpItem(int index) {
        if (index < 0 || index >= items.size()) {
            // Is this check needed? I think it might be redundant. Unable to check at this time.
            System.out.println("Invalid choice. No item picked up.");
            return null;
        }
        return items.remove(index); // Remove item from location and return it
    }

    public void addCreature(Creature creature) {
        // Add a creature to the list of creatures in an area.
        creatures.add(creature);
    }

    public void removeCreature(Creature creature) {
        // remove a creature from the list of creatures in an area.
        creatures.remove(creature);
    }

    public void connectLocation(String direction, Location location) {
        // Used to link locations. This is fairly dynamic, as thet can have a number of connections.
        connections.put(direction, location);
    }

    public Location getConnectedLocation(String direction) {
        // This shows what locations are connected to the entered location.
        return connections.get(direction);
    }

    public List<Item> getItems() {
        // show the items in an area. Used in displayItems.
        return items;
    }

    public List<Creature> getCreatures() {
        // show the creatures in an area. Used in displayCreatures.
        return creatures;
    }

    public void displayItems() {
        // Is this check for no items redundant? I think that the current playGame loop already checks for the case where there are no items.
        System.out.println("Items in this location:");
        if (items.isEmpty()) {
            System.out.println(" None");
        } else {
            for (Item item : items) {
                System.out.println(item.getItemName() + " " + item.getItemType() + "");
            }
        }
    }

    public void displayCreatures() {
        System.out.println("Creatures in this location:");
        if (creatures.isEmpty()) {
            System.out.println("- None");// This is definately redundant. I will come back and see what happens if this if statement is removed.
        } else {
            for (Creature creature : creatures) {
                System.out.println(creature.getName());
            }
        }
    }

    // Display available movement options, each with a letter. The letter is how the user can use them.
    public void displayConnections() {
        System.out.println("You can move to:");
        for (Map.Entry<String, Location> entry : connections.entrySet()) {
            String direction = entry.getKey();
            String locationName = entry.getValue().getName();
            System.out.println(direction + ": " + locationName);
        }
    }

    // Handle player movement
    public Location movePlayer() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            displayConnections();
            System.out.println("Choose a direction (enter the corresponding letter):");
            String choice = scanner.nextLine().trim().toUpperCase();

            Location nextLocation = getConnectedLocation(choice);
            if (nextLocation != null) {
                return nextLocation;
            } else {
                System.out.println("Invalid direction. Please try again.");
            }
        }
    }

    // Additional method to interact with items
    public Item findItem(String itemName) {
        for (Item item : items) {
            if (item.getItemName().equalsIgnoreCase(itemName)) {
                return item;
            }
        }
        return null;
    }
}
