public class Item {
    private String itemName;
    private String itemType; // for now the only types are weapons and food
    private int minDamage; // should only apply to weapons
    private int maxDamage; // should only apply to weapons
    private boolean canRest; // If the item can be used for resting

    // Constructor for item.
    public Item(String itemName, String itemType, int minDamage, int maxDamage) {
        this.itemName = itemName;
        this.itemType = itemType;
        this.minDamage = minDamage;
        this.maxDamage = maxDamage;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemType() {
        return itemType;
    }

    public int getMinDamage() {
        return minDamage;
    }

    public int getMaxDamage() {
        return maxDamage;
    }

    public boolean canRest() {
        return canRest;
    }

    public void useItem() {
        // Implement how the item can be used. Not really expanded or used yet. Leftover from when the food items were consumables.
        // kept because it can be used to expand the program at a future date.
        System.out.println("You used the " + itemName + ".");
    }
}