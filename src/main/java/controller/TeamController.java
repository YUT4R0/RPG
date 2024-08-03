package controller;

import java.util.List;

import entities.Person;
import entities.Team;
import model.TeamModel;

public class TeamController {
  private TeamModel model;

  public TeamController() {
    this.model = new TeamModel();
  }

  public void create(Team team) {
    model.create(team.getName());
  }

  public Team getTeamByName(String name) {
    return model.getTeamByName(name);
  }

  public void clearTeam(Team team) {
    model.clearTeam(team);
  }
  
  public void addPersonToTeam(Person person, Team team) {
    model.addPersonToTeam(person, team);
  }
  
  public List<Team> getTeams() {
    return model.getTeams();
  }

  public void deleteTeam(Team team) {
    model.deleteTeam(team);
  }

  public void removePersonFromTeam(Person person, Team team) {
    model.removePersonFromTeam(person, team);
  }

  public Person getPersonFromTeam(String personName, Team team) {
    return model.getPersonFromTeam(personName, team);
  }

  public void updatePersonFromTeam(Person person, Team team) {
    model.updatePersonFromTeam(person, team);
  }
}