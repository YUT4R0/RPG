package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import authors.Bowman;
import authors.Warrior;
import authors.Wizzard;
import entities.Inventory;
import entities.Item;
import entities.Person;
import entities.Team;

public class TeamModel {
  private BufferedWriter writer;
  private BufferedReader reader;
  private Team team;
  private Person person;
  private File file;

  public TeamModel() {
    this.writer = null;
    this.reader = null;
    this.file = null;
  }

  private Person getPersonFromFile(String line, String[] cols) {
    String name = cols[0];
    String cat = cols[1];
    int damage = Integer.parseInt(cols[2]);
    int life = Integer.parseInt(cols[3]);
    int energy = Integer.parseInt(cols[4]);

    String teamName = cols[5];
    String inventoryStr = cols[6].replace("[", "").replace("]", "");
    String[] itemsNames = inventoryStr.split(",");
    Inventory inventory = new Inventory();
    for (String itemName : itemsNames) {
      String item = itemName.trim();
      if (item != "")
        inventory.add(new Item(item));
    }

    if (cat.equals("Warrior"))
      this.person = new Warrior(name, life, damage, energy, inventory, new Team(teamName));
    else if (cat.equals("Bowman"))
      this.person = new Bowman(name, life, damage, energy, inventory, new Team(teamName));
    else if (cat.equals("Wizzard"))
      this.person = new Wizzard(name, life, damage, energy, inventory, new Team(teamName));
    return person;
  }

  public ArrayList<Person> getMembersFromTeam(String fileName) {
    ArrayList<Person> persons = new ArrayList<Person>();
    file = new File(fileName);

    if (!file.exists()) {
      System.err.println("Arquivo de usuários não encontrado.");
      return persons;
    }

    try {
      reader = new BufferedReader(new FileReader(fileName));
      String line;
      while ((line = reader.readLine()) != null) {
        String[] cols = line.split(";");
        if (cols.length == 7) {
          person = getPersonFromFile(line, cols);
          persons.add(person);
        }
      }
    } catch (IOException e) {
      System.err.println("Erro ao ler file de usuários: " + e.getMessage());
    }

    return persons;
  }

  public Team getTeamByName(String teamName) {
    String fileName = teamName + ".txt";
    file = new File(fileName);

    if (file.exists() && !file.isDirectory()) {
      ArrayList<Person> members = getMembersFromTeam(fileName);
      team = new Team(teamName, members);
      return team;
    }

    return null;
  }

  public Person getPersonFromTeam(String personName, Team team) {
    for (Person p : team.getMembers()) {
      if (p.getName().equals(personName))
        return p;
    }
    return null;
  }

  public void clearTeam(Team team) {
    try {
      team.clear();
      String fileName = team.getName() + ".txt";
      writer = new BufferedWriter(new FileWriter(fileName, false));
      writer.write("");
      writer.close();
    } catch (IOException e) {
      System.err.println("Erro ao limpar file de usuários: " + e.getMessage());
    }
  }

  public void saveAll(List<Person> persons, Team team) {
    for (Person p : persons)
      addPersonToTeam(p, team);
  }

  public void removePersonFromTeam(Person person, Team team) {
    String fileName = team.getName() + ".txt";
    file = new File(fileName);

    if (file.exists() && !file.isDirectory()) {
      List<Person> persons = getMembersFromTeam(fileName);
      Iterator<Person> iterator = persons.iterator();
      while (iterator.hasNext()) {
        Person p = iterator.next();
        if (p.getName().equals(person.getName())) {
          iterator.remove();
          team.removePerson(p);
          break;
        }
      }
      clearTeam(team);
      saveAll(persons, team);
    } else {
      throw new Error("Esse time nao existe!");
    }
  }

  public ArrayList<Team> getTeams() {
    File rootDir = new File(".");
    ArrayList<Team> teams = new ArrayList<Team>();
    File[] files = rootDir.listFiles();
    
    if (files != null) {
      for (File f : files) {
        boolean validFormat = f.isFile() && f.getName().endsWith(".txt");
        if (validFormat && f.getName().length() > 6) {
          String teamName = f.getName().replace(".txt", "");
          Team team = getTeamByName(teamName);
          teams.add(team);
        }
      }
    }
    return teams;
  }

  public void create(String teamName) {
    String fileName = teamName + ".txt";
    file = new File(fileName);
    try {
      if (file.createNewFile()) {
        writer = new BufferedWriter(new FileWriter(fileName, true));
        writer.close();
        System.out.println("Team created!");
      }
    } catch (IOException e) {
      System.out.println("Failed to create file: " + e.getMessage());
    }
  }

  public void addPersonToTeam(Person person, Team team) {
    String fileName = team.getName() + ".txt";
    file = new File(fileName);

    if (!file.exists())
      throw new Error("file not found!");
    
    try {
      team.addPerson(person);
      writer = new BufferedWriter(new FileWriter(fileName, true));

      ArrayList<String> items = new ArrayList<String>();
      for (Item i : person.getInventory())
        items.add(i.getName());

      writer.write(
          person.getName() + ";" +
              person.getClass().getSimpleName() + ";" +
              person.getDamage() + ";" +
              person.getLife() + ";" +
              person.getEnergy() + ";" +
              person.getTeam().getName() + ";" +
              items);
      writer.newLine();
      writer.close();
    } catch (IOException e) {
      System.out.println("Failed to write to file: " + e.getMessage());
    }
  }

  public void deleteTeam(Team team) {
    String fileName = team.getName() + ".txt";
    file = new File(fileName);
    team.clear();

    if (file.exists() && !file.isDirectory()) {
      if (file.delete()) {
        System.out.println("Time elimiando com sucesso!");
      }
    }
  }

  public void updatePersonFromTeam(Person newPerson, Team team) {
    String fileName = team.getName() + ".txt";
    file = new File(fileName);
    if (file.exists() && !file.isDirectory()) {
      person = getPersonFromTeam(newPerson.getName(), team);
      removePersonFromTeam(person, team);
      addPersonToTeam(newPerson, team);
    }
  }
}
