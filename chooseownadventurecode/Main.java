import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    // scanner is needed in java to be able to read user input.
    static Character player;
    // static Character player is calling the Character class
    static List<Location> placesVisited = new ArrayList<>();
    // While not currently used to do anything, placesVisited remembers where the player has been.
    // This could be expanded at some point to be called and display a different message the second or third time the player enters the area
    static Location currentLocation;
    // The line before this should call an instance of Location. These are used to build the map of the island the game is on.

    public static void main(String[] args) {
        // Make a new player, send the user through character creation, then once done clear the console.
        player = new Character();
        player.characterCreation();
        clearConsole(); // This is not a built in method for java. It is defined toward the end of this program.

        // Create locations
        Location startingBeach = new Location("Starting Beach", "Nothing is on this beach, but a broken board.");
        Location cliffPath = new Location("Cliff Path", "After walking for an hour, the beach keeps curving... You see a cliff going up the beach into the woods. It is short, so you can climb it. You can't see past it though.");
        Location debrisField = new Location("Debris Field", "You follow the beach to the right and notice some debris from the shipwreck. Most is useless. No sign of other survivors.");
        Location forest = new Location("Forest", "You venture deeper into the forest. Finally, you come to a clearing. It is at the top of a hill, with a river coming out of the side. Finally, fresh water!");
        Location emptyBeach = new Location("Empty Beach", "An isolated part of the beach with no signs of life. While you can see the beach curve ahead of you there is nothing else to be seen here. Except the forest, of course.");
        Location eastBank = new Location("East Bank", "You can see a river! It is hard to make out because brush follows it, but if you push through the brush, perhaps you can finally drink?");
        Location westBank = new Location("West Bank", "On the bank, you see down into a river! Finally, fresh water!");
        Location river = new Location("River", "A quick taste proves that the water is mixing with the ocean. Still, it isn't as salty as the sea. It must have a freshwater spring feeding it deeper in the woods.");
        // Locations need two things: a name and a description. For now the discription is displayed whenever the player enters an area.

        // Create and connect locations
        startingBeach.connectLocation("A", cliffPath);
        startingBeach.connectLocation("B", debrisField);
        startingBeach.connectLocation("C", forest);
        forest.connectLocation("A", startingBeach);
        forest.connectLocation("B", debrisField);
        forest.connectLocation("C", emptyBeach);
        forest.connectLocation("D", westBank);
        forest.connectLocation("E", cliffPath);
        forest.connectLocation("F", eastBank);
        debrisField.connectLocation("A", startingBeach);
        debrisField.connectLocation("B", forest);
        debrisField.connectLocation("C", emptyBeach);
        emptyBeach.connectLocation("A", eastBank);
        emptyBeach.connectLocation("B", forest);
        emptyBeach.connectLocation("C", debrisField);
        eastBank.connectLocation("A", river);
        eastBank.connectLocation("B", forest);
        eastBank.connectLocation("C", emptyBeach);
        westBank.connectLocation("A", river);
        westBank.connectLocation("B", forest);
        westBank.connectLocation("C", startingBeach);
        cliffPath.connectLocation("A", westBank);
        cliffPath.connectLocation("B", forest);
        cliffPath.connectLocation("C", startingBeach);
        river.connectLocation("A", eastBank);
        river.connectLocation("B", westBank);
        // connectLocation is meant to allow navagation for the user. Each location has to connect to somewhere or the player will get stuck.

        // Make a club, which is an item.
        Item club = new Item("club", "weapon", 1, 6);
        // For that club, put it on the starting beach.
        startingBeach.addItem(club);

        // Make the oar Item and then add it to debris field the same way as the club.
        Item oar = new Item("oar", "weapon", 1, 8);
        debrisField.addItem(oar);

        // Make a crab and put it in the river
        Creature crab = new Creature("crab", 10, 6);
        river.addCreature(crab);

        // currentLocation is where the player is located. This is used to call the prompts for where to go next and location discription.
        currentLocation = startingBeach;
        placesVisited.clear();
        // playgame is the workhorse of this program. It will be calling almost every other class at some point.
        playGame();
    }

    public static void playGame() {
        // play is basically checking if the player is alive still. Once they get killed, play is no longer true.
        boolean play = true;
    
        while (play) {
            // is this clearConsole redundant? No, it is the main one. It prevents the user -interface from displaying all the earlier notifications.
            clearConsole();
    
            // Print the current location and description to the console, so the player can see.
            System.out.println(currentLocation.getDescription());
    
            // If there is a creature in the area, display them. Otherwise, don't show anything. The if statement is a boolean
            if (!currentLocation.getCreatures().isEmpty()) {
                currentLocation.displayCreatures();
    
                // start combat wth the creature
                Combat combat = new Combat(player, currentLocation.getCreatures().get(0), currentLocation, scanner);
                combat.startCombat();
    
                // Check if the player is still alive after combat with the creature
                if (player.isDead()) {
                    System.out.println("You have been defeated. You watch the light fade, realizing that this is it.");
                    play = false;  // End the game if the player is dead
                    continue;  // Skip the rest of the loop after the game ends
                }
            }
    
            // Display movement, resting, and item interaction options
            boolean validAction = false;
            while (!validAction) {
                // Display the connected locations
                System.out.println("Where would you like to move next?");
                // this is where all movement options will be displayed. This is cluttered at the moment, but a solid start.
                currentLocation.displayConnections();
    
                // If there are items, show item interaction options. Otherwise don't show the user anything.
                if (!currentLocation.getItems().isEmpty()) {
                    System.out.println("There are items here. Type 'I' to interact with items.");
                }
    
                // Option to rest. This shouldn't do anything unless the player is on the hill in the forest and holding a food item, though that interaction hasn't been fleshed out.
                System.out.println("You could also rest. It is hot under the sun, and you have been out here for a long while.");
                System.out.println("Type 'R' to rest.");
                System.out.println("Enter a direction, 'I' for items, or 'R' to rest:");
                // Lines 119, 123-125 seem to be condensable. If time permits clean them up.
                String choice = scanner.nextLine().trim().toUpperCase();
    
                // Handle if the user tries to rest. 
                if (choice.equals("R")) {
                    // attempt to rest and say if it was successful or not.
                    boolean restSuccessful = player.rest(currentLocation);
                    if (restSuccessful) {
                        validAction = true;
                    } else {
                        System.out.println("You are unable to rest here.");
                    }
                }
    
                // Handle item interaction. Checks to make sure that the user has chosen to enter the object interaction menu,
                //- and that the the list of items for the location isn't empty.
                else if (choice.equals("I") && !currentLocation.getItems().isEmpty()) {
                    handleItemInteraction();
                    validAction = true;  // Assuming item interaction was successful
                }
    
                // Handle movement. 
                else {
                    Location nextLocation = currentLocation.getConnectedLocation(choice);
                    if (nextLocation != null) {
                        currentLocation = nextLocation;  // Move to the chosen location
                        placesVisited.add(currentLocation);  // Track visited locations by adding it to the earlier placesVisited list.
                        validAction = true;
                    } else {
                        System.out.println("Invalid direction or action. Please try again.");
                        // Should stress test this to make sure it covers all inputs. If implimented correctly this shouldn't allow crashes.
                    }
                }
            }
        }
    
        scanner.close(); // Not sure what a dataleak is in this context, but the program crashes if the scanner is not closed.
    }
    
    // Method to handle item interactions. This is called in playGame. This code seems like it might bellong better as a part of the item class but that would require a fair bit of restructuring from here.
    public static void handleItemInteraction() {
        boolean interacting = true;
    
        while (interacting) {
            // Show available items
            currentLocation.displayItems();
            System.out.println("Enter the number of the item to pick up or type 'E' to exit item interaction:");
            // To improve this I could make it not ask this if there is only one item. Also, I should retun at some point and make this menu close once all items are picked up.
    
            String itemChoice = scanner.nextLine().trim().toUpperCase(); // .trim().toUpperCase() is useful. This is how to make choices not case sensitive.
    
            if (itemChoice.equals("E")) {
                interacting = false;  // "E" is used to exit the item interaction menu. This is where I need to add code to auto-close this menu once all items are picked up.
            } else {
                try {
                    int index = Integer.parseInt(itemChoice) - 1;  // Convert to 0-based index
                    if (index >= 0 && index < currentLocation.getItems().size()) {
                        Item pickedItem = currentLocation.pickUpItem(index);  // Pick up the item
                        player.addItem(pickedItem);  // Add item to player’s inventory
                        System.out.println("You picked up a " + pickedItem.getItemName() + ".");
                    } else {
                        System.out.println("Invalid item choice.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number or 'E' to exit.");
                }
            }
        }
    }
      
    public static void clearConsole() {
        // This method came from chatgpt. Appearently Java has no universal clear console method that can be called.
        try {
            // For Windows
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                // For Unix-based systems (Linux, macOS)
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (Exception e) {
            // Handle exceptions (e.g., if the command fails)
            System.out.println("Could not clear the console.");
        }
    }
}
