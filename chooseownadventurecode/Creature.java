import java.util.List;

public class Creature {
    int hp;
    int str;
    int attack;
    String name;
    // While the original creature was a copy of the player, it has been modified a lot. No need for the charactor creation method, for example. 
    // for now no need for the creature to be able to carry items or have limited endurance.
    // Adding a current hp and max hp could be interesting, but would mainly be to display how injured the creature is. This style of game doesn't make sense for the player to know the creature's hp numbers.

    // setter for the creature.
    public Creature(String name, int cHitpoints, int str) {
        this.name = name;
        this.hp = cHitpoints;
        this.str = str;
    }

    public boolean takeDamage(int damage) {
        // mainly to reduce the hp. Also calls the isDead method.
        hp -= damage;
        return !isDead();  // Return false if dead, true if still alive
    }

    public boolean isDead() {
        return hp <= 0;  // Dead if hitpoints are 0 or less
    }

    public String getName() {
        return name; // Get the creature's name
    }

    public int getHealth() {
        return hp; // Get the creature's name
    }

    public int attack() {
        // Calculate and return attack damage based on strength
        int attack=0;
        if (str<=10){attack=1;}
        if (str==12){attack=2;}
        if (str==14){attack=3;}
        int extraDamage = 0; //a leftover feature from charactor in case this is expanded to allow tools.
        attack+=extraDamage;
        return attack; // It occurs to me I could have used inheritance for both creature and character, reducing the lines of code. If time permits that might be a good use of time.
    }

    // Method to handle defeat. This should turn the creature into a usable item of food. For now no way to make more food than the charactor can carry, but that could be set up with an int size added to creature.
    public void onDefeat(Location location) {
        Item Food = new Item(name + " Meat", "food", 0, 0); // min and max damage aren't actually used. If this was restructured inheritance would be the way to go so that food isn't trying to do damage.
        location.addItem(Food); // Add the new food item to the location the creature died in.
        System.out.println(name + " is dead. You can pick it up and use it as food. ");
        location.removeCreature(this); // Removing the creature from the location, as it is now an item.
    }
}
