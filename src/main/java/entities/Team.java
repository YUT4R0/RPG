package entities;

import java.util.ArrayList;

public class Team {
  private String name;
  private ArrayList<Person> members;

  public Team(String name) {
    this.name = name;
    this.members = new ArrayList<Person>();
  }
  
  public Team(String name, ArrayList<Person> members) {
    this.name = name;
    this.members = new ArrayList<Person>();
    for (Person p : members)
      this.members.add(p);
  }

  public Team(ArrayList<Person> persons) {
    this.members = persons;
  }

  public void addPerson(Person person) {
    this.members.add(person);
    person.setTeam(this);
  }

  public Person getPerson(Person person) {
    Person target = null;
    try {
      for (Person p : members) {
        if (p == person) {
          target = p;
          break;
        }
      }
    } catch (Exception e) {
      System.out.println("Erro ao buscar personagem: " + e.getMessage());
    }
    return target;
  }

  public void removePerson(Person person) {
    this.members.remove(person);
    person.setTeam(null);
  }

  public void clear() {
    for (Person p : members) {
      p.setTeam(null);
    }
    this.members = new ArrayList<Person>();
  }
  
  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public ArrayList<Person> getMembers() {
    return this.members;
  }

  public void setMembers(ArrayList<Person> persons) {
    this.members = persons;
  }

  public void show(boolean showMembers) {
    System.out.println("[ " + getName() + " ]" + " (" + getMembers().size() + " members)");
    if (showMembers)
      for (Person p : getMembers())
        p.show();
  }
}