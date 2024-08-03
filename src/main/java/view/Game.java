package view;

import java.io.Console;
import java.util.List;

import authors.Bowman;
import authors.Warrior;
import authors.Wizzard;
import controller.TeamController;
import entities.Item;
import entities.Person;
import entities.Team;

public class Game {
  private boolean running;
  private TeamController teamController;
  private Person person;
  private Team team;
  private Console sc;

  public Game() {
    this.person = null;
    this.team = null;
    this.sc = System.console();
    this.running = true;
    this.teamController = new TeamController();
  }

  private Person creatPerson() {
    String name = sc.readLine("> set player's name: ");
    if (name.length() < 3 || name.toUpperCase().equals("EXIT"))
      throw new Error("Invalid name format!");
    int life = Integer.parseInt(sc.readLine("> set " + name + "'s life: "));
    if (life <= 0)
      throw new Error("Life must be a positive number!");
    int damage = Integer.parseInt(sc.readLine("> set " + name + "'s force: "));
    if (damage <= 0)
      throw new Error("Force must be a positive number!");
    System.out.println("1 - Wizzard;" + "\n" + "2 - Warrior;" + "\n" + "3 - Bowman;");
    int cat = Integer.parseInt(sc.readLine("> set " + name + "'s category: "));
    switch (cat) {
      case 1:
        person = new Wizzard(name, life, damage);
        break;
      case 2:
        person = new Warrior(name, life, damage);
        break;
      case 3:
        person = new Bowman(name, life, damage);
        break;
      default:
        throw new Error("Invalid category!");
    }
    int op = Integer.parseInt(sc.readLine("> attach item to the player? (type 1 for 'yes'): "));
    if (op == 1) {
      String itemName = sc.readLine("> set item name: ");
      if (itemName.length() < 3)
        throw new Error("Item name must be at least 3 characters long!");
      person.attachItem(new Item(itemName));
    }
    return person;
  }

  private Team createTeam() {
    String name = sc.readLine("> set team name: ");
    if (name.length() < 3)
      throw new Error("Team name must have at least 3 characters!");
    if (teamController.getTeamByName(name) != null)
      throw new Error("Team already exists!");
    return new Team(name);
  }

  private void updateScenario(Team _team, Person _person, int discounted) {
    _person.setLife(discounted);
    teamController.updatePersonFromTeam(_person, _team);
    if (_person.getLife() <= 0) {
      System.out.println("The player " + _person.getName() + " died!");
      teamController.removePersonFromTeam(_person, _team);
      if (_team.getMembers().size() == 0)
        teamController.deleteTeam(_team);
    }
  }

  private void handleAttack(Team attackerTeam, Person attacker, Team targetTeam, Person target) {
    System.out.println("Choose attack type: ");
    System.out.println("1 - Normal attack");
    System.out.println("2 - Special attack");
    int attackType = Integer.parseInt(sc.readLine("> choose attack type: "));
    switch (attackType) {
      case 1:
        String itemName = sc.readLine("> choose item: ");
        if (itemName.length() < 3)
          throw new Error("Item name must be at least 3 characters long!");
        attacker.attack(target, itemName);
        updateScenario(targetTeam, target, target.getLife() - attacker.getDamage());
        break;
      case 2:
        attacker.useSpecialAttack(target);
        updateScenario(targetTeam, target, target.getLife() - (attacker.getDamage() * 2));
        break;
      default:
        throw new Error("Invalid attack type!");
    }
  }

  public void run() {
    System.out.println("<===== Well come to the RPG! =====>");
    while (running) {
      System.out.println("Set the your action: ");
      System.out.println("1 - Create a new team;");
      System.out.println("2 - Create a new player;");
      System.out.println("3 - Consult the list of players;");
      System.out.println("4 - Consult the list of teams;");
      System.out.println("5 - Add item to a player;");
      System.out.println("6 - Make an attack;");
      System.out.println("7 - Delete a team;");
      System.out.println("8 - Delete a player;");
      System.out.println("9 - Exit;");
      int cmd = Integer.parseInt(sc.readLine("> set command: "));

      switch (cmd) {
        case 1:
          System.out.println("% Creating a new team %");
          team = createTeam();
          teamController.create(team);
          break;
        case 2:
          if (teamController.getTeams().size() < 1) {
            System.out.println("You need at least 1 team to add a player");
            break;
          }
          System.out.println("% Creating a new player %");
          person = creatPerson();
          String teamName = sc.readLine("> set registered team's name: ");
          team = teamController.getTeamByName(teamName);
          if (teamController.getPersonFromTeam(person.getName(), team) != null)
            throw new Error("Person already registered in the team!");
          teamController.addPersonToTeam(person, team);
          System.out.println(person.getName() + " has benn added to " + team.getName() + "!");
          break;
        case 3:
          if (teamController.getTeams().size() < 1) {
            System.out.println("No teams = No players, my buddy");
            break;
          }
          System.out.println("<----- Players list ----->");
          List<Team> teams = teamController.getTeams();
          for (Team t : teams)
            if (t.getMembers().size() > 0)
              for (Person p : t.getMembers())
                p.show();
          break;
        case 4:
          if (teamController.getTeams().size() <= 1) {
            System.out.println("No teams registered yet!");
            break;
          }
          int op = Integer.parseInt(sc.readLine("> show teams members? (type 1 for 'yes'): "));
          boolean flag = (op == 1) ? true : false;
          System.out.println("<----- Teams list ----->");
          for (Team t : teamController.getTeams())
            t.show(flag);
          break;
        case 5:
          if (teamController.getTeams().size() < 1) {
            System.out.println("How dare you attach an item to a metaphorical player without even creating a team");
            break;
          }
          System.out.println("Adding an item to a player");
          teamName = sc.readLine("> set registered team's name: ");
          String playerName = sc.readLine("> set player's name: ");
          String itemName = sc.readLine("> set item name: ");
          team = teamController.getTeamByName(teamName);
          person = teamController.getPersonFromTeam(playerName, team);
          person.attachItem(new Item(itemName));
          teamController.updatePersonFromTeam(person, team);
          break;
        case 6:
          if (teamController.getTeams().size() < 1) {
            System.out.println("You need at least 2 teams to make an attack!");
            break;
          }
          System.out.println("% Attacking a player %");
          
          String attackerTeamName = sc.readLine("> set attacker's team name: ");
          Team attackerTeam = teamController.getTeamByName(attackerTeamName);
          if (attackerTeam == null)
            throw new Error("Attacker team not found!");
          
          String attackerName = sc.readLine("> set attacker's name: ");
          Person attacker = teamController.getPersonFromTeam(attackerName, attackerTeam);
          if (attacker == null)
            throw new Error("Attacker not found!");
          
          String targetTeamName = sc.readLine("> set target's team name: ");
          Team targetTeam = teamController.getTeamByName(targetTeamName);
          if (targetTeam == null)
            throw new Error("Defender team not found!");
          else if (targetTeamName.equals(attackerTeamName))
            throw new Error("Attacker and target must be different teams!");
          
          String targetName = sc.readLine("> set target's name: ");
          Person target = teamController.getPersonFromTeam(targetName, targetTeam);
          if (target == null)
            throw new Error("Defender not found!");
          
          handleAttack(attackerTeam, attacker, targetTeam, target);
          break;
        case 7:
          if (teamController.getTeams().size() < 1) {
            System.out.println("No teams to be deleted!");
            break;
          }
          System.out.println("Deleting a team");
          teamName = sc.readLine("> set team's name to delete: ");
          team = teamController.getTeamByName(teamName);
          if (team == null)
            throw new Error("Team not found!");
          teamController.deleteTeam(team);
          System.out.println("Team " + teamName + " has been deleted.");
          break;
        case 8:
          if (teamController.getTeams().size() < 1) {
            System.out.println("How can you delete a player if there's no team? It's not that dificult to use your brain");
            break;
          }
          System.out.println("Deleting a player");
          teamName = sc.readLine("> set player's team name: ");
          playerName = sc.readLine("> set player's name to delete: ");
          team = teamController.getTeamByName(teamName);
          person = teamController.getPersonFromTeam(playerName, team);
          if (person == null)
            throw new Error("Player not found!");
          teamController.removePersonFromTeam(person, team);
          System.out.println("Player " + playerName + " has been deleted from team " + teamName + ".");
          break;
        case 9:
          System.out.println("Exiting the game...");
          running = false;
          break;
        default:
          System.out.println("Invalid command");
          break;
      }
    }
  }
}
