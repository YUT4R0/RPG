package authors;

import java.util.Random;

import entities.Inventory;
import entities.Item;
import entities.Person;
import entities.Team;

public class Wizzard extends Person {
  private final String NOT_ALLOWED = "ESPADAMACHADOARCOEFLECHA";
  
  public Wizzard(String name, int life, int damage) {
    super(name, life, damage);
  }

  public Wizzard(String name, int life, int damage, int energy, Inventory inventory, Team team) {
    super(name, life, damage, energy, inventory, team);
  }
  
  @Override
  public void attachItem(Item item) {
    if (!NOT_ALLOWED.contains(item.getName().toUpperCase()))
      super.attachItem(item);
    else
      throw new Error("Não é possível adicionar a espada ao Wizzard");
  }

  @Override
  public void attack(Person target, String itemName) {
    Item item = useItem(itemName);
    System.out.println("The wizzard " + getName() + " attacked " + target.getName() + " using a " + item.getName() + "!");
    if (getLife() > 5 && getDamage() > getLife()) {
      System.out.println("...but it costed him to defeat the enemy!");
      setLife(getLife() - 1);
    }
    Random r = new Random();
    double chance = r.nextDouble();
    if (chance < 0.1) {
      System.out.println("... and also he/she broke his " + item.getName() + "!");
      removeItem(item);
    }
  }
}