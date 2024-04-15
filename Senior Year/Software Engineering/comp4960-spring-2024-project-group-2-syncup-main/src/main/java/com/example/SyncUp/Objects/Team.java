package com.example.SyncUp.Objects;

import java.util.ArrayList;

public class Team {
	private String teamName;
	private ArrayList<User> teamMembers = new ArrayList<>(0);
	
	public Team(User creator, String name) {
		this.teamName = name;
		addUser(creator);
	}
	
	public String getTeamName() {
		return teamName;
	}
	
	public ArrayList<User> getTeamMembers() {
		return teamMembers;
	}
	
	public void addUser(User u) {
		teamMembers.add(u);
		u.getTeams().add(this);
	}
	
	public void addUsers(User[] u) {
		for (int i=0; i<u.length; i++) {
			this.addUser(u[i]);
		}
	}
}
