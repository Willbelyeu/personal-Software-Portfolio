import java.util.ArrayList; // gets arrays, which will be used here
import java.util.List;      // lists are also used here.
import java.util.Scanner;   // user input reader. Mainly used in charactor creation.

public class Character {
    // Misspelled.
    Scanner scanner = new Scanner(System.in);
    int lv; // not fully implemented, but interesting enough that I left it in.
    int constitution; // Used with lv to get max hitpoints.
    int maxHitPoints;
    int currentHitPoints; // If the player is hurt their hitpoints change. Having currentHitPoints seperate allows healing to be cleaner.
    int str; // So far used only for attacking.
    int endurance; // Used when moving or attacking.
    int exhaustion; // Basically the same thing curentHitPoints is to maxHitPoints.
    List<Item> inventory; // Player's inventory. At some point should be modified to have a limit to what can be carried. The original plan was only one item of type weapon, as there is only one hand to carry it in.

    
    public Character() {
        inventory = new ArrayList<>();  // Initialize inventory here
        exhaustion = 0;  // Initialize exhaustion to 0
    }

    public void characterCreation() {
        while (true) {
            System.out.println("In what order do you prioritize these three stats:");
            System.out.println("1. Hitpoints: Being able to take damage.");
            System.out.println("2. Strength: Increased carrying ability and attack.");
            System.out.println("3. Endurance: Being able to go longer between rests.");
            System.out.println("Enter the order of importance as 123, 132, 213, 231, 312, or 321.");
            
            String chosenStat = scanner.nextLine();
            
            // Define base values for constitution, strength, and endurance
            int[][] statValues = {
                {14, 10, 8},   // 123
                {14, 8, 10},   // 132
                {12, 14, 8},   // 213
                {10, 14, 10},  // 231
                {12, 10, 10},  // 312
                {12, 8, 10}    // 321
            };
            
            // Map input to corresponding stat values
            switch (chosenStat) {
                case "123":
                    constitution = statValues[0][0];
                    str = statValues[0][1];
                    endurance = statValues[0][2];
                    break;
                case "132":
                    constitution = statValues[1][0];
                    str = statValues[1][1];
                    endurance = statValues[1][2];
                    break;
                case "213":
                    constitution = statValues[2][0];
                    str = statValues[2][1];
                    endurance = statValues[2][2];
                    break;
                case "231":
                    constitution = statValues[3][0];
                    str = statValues[3][1];
                    endurance = statValues[3][2];
                    break;
                case "312":
                    constitution = statValues[4][0];
                    str = statValues[4][1];
                    endurance = statValues[4][2];
                    break;
                case "321":
                    constitution = statValues[5][0];
                    str = statValues[5][1];
                    endurance = statValues[5][2];
                    break;
                default:
                    System.out.println("Invalid input. Please enter a combination of the numbers 1, 2, and 3.");
                    continue;  // Go back to the start of the loop for valid input
            }
    
            // Set HP after assigning constitution
            getHpOnLevelup(1);  // Assuming the player starts at level 1
            return;  // Exit the loop after a successful selection
        }
    }
    
    // Add item to inventory
    public void addItem(Item item) {// this should be called in locations, allowing an item to disapear from there and appear in the charactor inventory.
        inventory.add(item);
        System.out.println(item.getItemName() + " has been added to your inventory.");
    }

    // Display the player's inventory
    public void showInventory() {
        if (inventory.isEmpty()) {// if that itemlist is empty, show following message.
            System.out.println("Your inventory is empty.");
        } else {
            System.out.println("You are carrying:");
            for (Item item : inventory) {// for each item in list:
                System.out.println("- " + item.getItemName() + " (" + item.getItemType() + ")");
            }
        }
    }

    // Remove an item from the inventory, aka drop. Not curently used, though was meant to be if the limit of one weapon was enforced.
    // This hasn't been implimented yet because I would also need to call and add this item to the location. Of course, that could also be done in inventory management.
    public void removeItemFromInventory(Item item) {
        inventory.remove(item);
        System.out.println(item.getItemName() + " has been removed from your inventory.");
    }

    // Check if the player has food. If yes then boolean = true.
    public boolean hasFood() {
        return inventory.stream().anyMatch(item -> item.getItemType().equals("food"));
    }

    public void displayCurrentStats() { // show the player their hp and endurance.
        System.out.println(currentHitPoints + " hitpoints remaining");
        System.out.println((endurance - exhaustion) + " endurance left");
    }

    // Handle taking damage
    public boolean takeDamage(int damage) {
        currentHitPoints -= damage;
        return !isDead();  // Return false if dead, true if still alive
    }

    // Calculate attack damage based on equipped weapon or strength
    public int attack() {
        // Base attack value from strength
        int attack = (str <= 11) ? 1 : (str <= 12) ? 2 : (str <= 14) ? 3 : 0;
        int extraDamage = 0;
        // Iterate through inventory to find weapons and calculate their random damage. Only meant so far to work with one weapon, so if two are carried it will add both's damage at once. 
        // This could be fixed here by making it select the higher maxDamage, but would be better to impliment a limit on weapons in the inventory.
        for (Item item : inventory) {// foreach item in list
            if (item.getItemType().equals("weapon")) {
                // Add random damage based on the weapon's min/max damage
                extraDamage = item.getMinDamage() + (int) (Math.random() * (item.getMaxDamage() - item.getMinDamage() + 1));
            }
        }
        attack += extraDamage;// Total attack damage is base strength + extra weapon damage
        return attack;
    }
    
    // Use endurance for actions like combat or travel
    public boolean useEndurance() {
        exhaustion += 1;
        return exhaustion < endurance;  // Return false if exhausted, true if can keep going
    }

    public boolean isDead() {
        return currentHitPoints <= 0;  // Dead if hitpoints are 0 or less
    }

    // Calculate hit points on level up, which is a method not added yet. for now, lv is 1.
    public void getHpOnLevelup(int level) {
        int conBonus = (constitution == 12) ? 1 : (constitution >= 11 && constitution <= 12) ? 2 : (constitution >= 13 && constitution <= 14) ? 3 : 0; // each ammount of constitution gives a different hp.
        maxHitPoints = (5 * level + conBonus * level); // If level was 1 and constitution was 12, should give a total of 6.
        currentHitPoints = maxHitPoints; // on levelup the player fully heals.
    }

    // Rest and recover health if player has food and water
    public boolean rest(Location currentLocation) {
        boolean hasFood = inventory.stream().anyMatch(item -> item.getItemType().equals("food")); // Check if player has food. This is an item type, not an item name.
    
        // Returns true if the player is in the forest. This is the only location with fresh drinkable water, and the player doesn't have access to anything to carry water in.
        boolean inForest = currentLocation.getDescription().toLowerCase().contains("forest");
    
        if (inForest && hasFood) { // if both are true, run following code.
            currentHitPoints = maxHitPoints;  // Restore health to max.
            exhaustion=0; // clear exhaustion.
            System.out.println("You make a fire and eat the crab, then lay down to sleep.");
            System.out.println("You wake feeling rested. Your scraps have closed up, and you are ready to go walking again.");
            return true;
        } else {
            System.out.println("You can't rest until you find some water. Food also feels important.");
            return false;
        }
    }
}
