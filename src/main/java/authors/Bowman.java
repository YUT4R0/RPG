package authors;

import java.util.Random;

import entities.Inventory;
import entities.Item;
import entities.Person;
import entities.Team;

public class Bowman extends Person {
  private final String NOT_ALLOWED = "ESPADAVARAVARINHAMACHADO";
  
  public Bowman(String name, int life, int damage) {
    super(name, life, damage);
  }

  public Bowman(String name, int life, int damage, int energy, Inventory inventory, Team team) {
    super(name, life, damage, energy, inventory, team);
  }

  @Override
  public void attachItem(Item item) {
    if (!NOT_ALLOWED.contains(item.getName().toUpperCase()))
      super.attachItem(item);
    else
      throw new Error("Cannot add this item to the archer");
  }

  @Override
  public void attack(Person target, String itemName) {
    Item item = useItem(itemName);
    System.out.println(getName() + " attacked " + target.getName() + " using a " + item.getName() + "in a distance of " + getLife() + getDamage());
    if (getLife() < getDamage() && getLife() < 10 && getEnergy() > 5) {
      System.out.println("...but he got really exausted!");
      setEnergy(getEnergy() - 1);
    }
    Random r = new Random();
    double chance = r.nextDouble();
    if (chance < 0.1) {
      System.out.println("... and also he/she broke his " + item.getName() + "!");
      removeItem(item);
    }
  }
}