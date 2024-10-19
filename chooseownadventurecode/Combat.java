import java.util.Scanner;

public class Combat {
    // To work properly, Combat ends up using the location and both the player and the creature.
    // the scanner allows user input. This allows combat to be more interactable. 
    private Location location;
    private Character player;
    private Creature creature;
    private Scanner scanner;

    public Combat(Character player, Creature creature, Location location, Scanner scanner) {
        // The getter for Combat. This allows the class to recieve data to set it up. Currently combat can only handle one creature.
        this.player = player;
        this.creature = creature;
        this.location = location;
        this.scanner = scanner; // Assign the passed scanner
    }

    public void startCombat() {
        // startCombat does what the name says. It also runs combat, which might not be efficient. Could modify later, make combat its own method that startCombat calls.
        //-not more line efficient, but better following the rules of programing.
        System.out.println("A " + creature.getName() + " attacks you!");

        while (!player.isDead() && !creature.isDead()) {
            // As long as neither the player or creature is dead, this fight will continue.
            // If time permits later I should add code to call and display the player's remaining hit points and endurance.
            //-Maybe also the creatures hp, but I feel that should mostly be hidden from the player. Maybe just a message when it is first injured and another once it is near death? Those would be in the creature class as a method.
            System.out.println("What would you like to do?");
            System.out.println("1. Attack");
            System.out.println("2. Flee");

            String choice = scanner.nextLine().trim();

            if (choice.equals("1")) {
                // Player's choice is attack. This will get his attack from the Charactor Player and apply it to the creature's hp by using the creature.takeDamage method.
                int damageToCreature = player.attack();
                creature.takeDamage(damageToCreature);
                System.out.println("You attack the " + creature.getName() + " for " + damageToCreature + " damage.");

                // Check if creature is dead. If so tell the user and end combat. Return will break the loop.
                if (creature.isDead()) {
                    System.out.println("You defeated the " + creature.getName() + "!");
                    creature.onDefeat(location); // This method turns the creature into an item. Only food is available right now.
                    return; 
                }

                // Creature's turn to attack. It will call its attack method and apply it to the player's hp through the takeDamage method. The reason this is simular is because 
                //-creature was originally copied from character. It has been heavily modified of course.
                int damageToPlayer = creature.attack(); 
                player.takeDamage(damageToPlayer);
                System.out.println("The " + creature.getName() + " attacks you for " + damageToPlayer + " damage."); //I feel that I should add a timer at some point to make this display more pleasant.

                // If the player is killed play this section of code. This is a death banner and an end to combat.
                if (player.isDead()) {
                    System.out.println("You have been defeated by the " + creature.getName() + "!");
                    return; 
                }
            } 
            // If the player chooses to run, run this next code section.
            else if (choice.equals("2")) {
                if (flee()) {
                    // this is a true/false boolean because there is a random failure chance. 
                    // At some point this should be expanded to trigger a movement taking the player out of the location he entered combat in, but that will take some work I don't currently have time for.
                    System.out.println("You successfully fled from the " + creature.getName() + "!");
                    return; // End combat after fleeing
                } else {
                    System.out.println("You failed to flee!");
                }
            } else {
                // a catcher. Should prevent crashes.
                System.out.println("Invalid choice. Please enter 1 to attack or 2 to flee.");
            }
        }
    }
    private boolean flee() {
        // Implement the logic for fleeing
        // Returns a random success/failure. If time permits later success should be modified to link to the endurance remaining and/or the creature should have an added speed.
        return Math.random() > 0.5; // For now, 50% chance to flee successfully
    }
}