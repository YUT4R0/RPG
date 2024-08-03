package authors;

import java.util.Random;

import entities.Inventory;
import entities.Item;
import entities.Person;
import entities.Team;

public class Warrior extends Person {
  private final String NOT_ALLOWED = "VARINHAVARAARCOEFLECHA";
  
  public Warrior(String name, int life, int damage) {
    super(name, life, damage);
  }  
  
  public Warrior(String name, int life, int damage, int energy, Inventory inventory, Team team) {
    super(name, life, damage, energy, inventory, team);
  }  
  
  @Override
  public void attachItem(Item item) {
    if (!NOT_ALLOWED.contains(item.getName().toUpperCase()))
      super.attachItem(item);
    else
      throw new Error("Não é possível adicionar a espada ao Warrior");
  }

  @Override
  public void attack(Person target, String itemName) {
    Item item = useItem(itemName);
    System.out.println("The warrior " + getName() + " attacked " + target.getName() + " using a " + item.getName() + "!");
    if (getDamage() < target.getDamage() && getDamage() > 5) {
      System.out.println("... but he's geting weak due to the exaustion!");
      setDamage(getDamage() - 1);
    }
    Random r = new Random();
    double chance = r.nextDouble();
    if (chance < 0.1) {
      System.out.println("... and also he/she broke his " + item.getName() + "!");
      removeItem(item);
    }
  }
}