package entities;

import java.util.ArrayList;

import interfaces.ISpecialAttack;

public abstract class Person implements ISpecialAttack {
  private final double DESC = 0.20;
  private String name;
  private int life, damage, energy;
  private Inventory inventory;
  private Team team;

  public Person(String name, int life, int damage) {
    this.name = name;
    this.life = life;
    this.damage = damage;
    this.energy = life;
    this.inventory = new Inventory();
    this.team = new Team("");
  }

  public Person(String name, int life, int damage, int energy, Inventory inventory, Team team) {
    this.name = name;
    this.life = life;
    this.damage = damage;
    this.energy = energy;
    this.inventory = inventory;
    this.team = team;
  }

  abstract public void attack(Person target, String itemName);

  public void setTeam(Team team) {
    this.team = team;
  }

  public Team getTeam() {
    return this.team;
  }

  public void removeFromTeam() {
    this.team.removePerson(this);
  }

  public void attachItem(Item item) {
    inventory.add(item);
  }

  public Item useItem(String itemName) {
    Item found = null;
    for (Item i : inventory.getItems()) {
      if (i.getName().equals(itemName)) {
        found = i;
        break;
      }
    }
    if (found == null)
      throw new Error("Item not found on inventory!");
    return found;
  }

  public void removeItem(Item item) {
    inventory.remove(item);
  }

  public ArrayList<Item> getInventory() {
    return inventory.getItems();
  }

  public void useSpecialAttack(Person target) {
    if (this.energy >= 0) {
      System.out.println(getName() + " used a special attack against " + target.getName() + "!");
      this.energy -= this.energy * DESC;
      if (this.energy < 0)
        this.energy = 0;
    } else {
      System.out.println("Insuficient energy!");
    }
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getLife() {
    return life;
  }

  public void setLife(int life) {
    this.life = life;
  }

  public int getDamage() {
    return damage;
  }

  public void setDamage(int damage) {
    this.damage = damage;
  }

  public int getEnergy() {
    return energy;
  }

  public void setEnergy(int energy) {
    this.energy = energy;
  }

  public void printPerson() {
    System.out.println("Name: " + this.name);
    System.out.println("Category: " + this.getClass().getSimpleName());
    System.out.println("Life: " + this.life);
    System.out.println("Damage: " + this.damage);
    System.out.println("Energy: " + this.energy);
    inventory.printInventory();
  }

  public void show() {
    System.out
        .println("- " + getName() + "(" + getClass().getSimpleName() + ")" + "[" + getDamage() + "]" + "{" + getLife()
            + "}" + "<" + getEnergy() + ">" + "[" + getInventory().size() + " items]=(" + getTeam().getName() + ")");
  }
}