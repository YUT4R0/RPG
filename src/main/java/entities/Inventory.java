package entities;

import java.util.ArrayList;

public class Inventory {
  private ArrayList<Item> inventory;

  public Inventory() {
    this.inventory = new ArrayList<Item>();
  }

  public void add(Item item) {
    if (!inventory.contains(item))
      inventory.add(item);
    else
      throw new Error("Item already exists on inventory!");
  }

  public void remove(Item item) {
    try {
      for (Item i : inventory) {
        if (!i.getName().equals(item.getName())) {
          inventory.remove(item);
          break;
        }
      }
    } catch (Exception e) {
      System.out.println("Error while removing item" + e.getMessage());
    }
  }

  public ArrayList<Item> getItems() {
    return inventory;
  }

  public void printInventory() {
    System.out.println("<---[ INVENTORY (lenght: " + inventory.size() + ")]--->");
    for (Item i : inventory)
      System.out.println("- " + i.getName());
  }
}