package com.example.SyncUp.Objects;

import java.util.ArrayList;

public class User {
	private String firstName;
	private String lastName;

	private String solidIDP;
	private String solidWebID;
	private String solidClientID;
	private String solidClientSecret;
	private String authFlow;
	private ArrayList<String> unavailability;
	private ArrayList<Team> teams;

	public User(String idp, String webID, String clientID, String clientSecret, String authFlow) {
		this.solidIDP = idp;
		this.solidWebID = webID;
		this.solidClientID = clientID;
		this.solidClientSecret = clientSecret;
		this.authFlow = authFlow;
		this.unavailability = new ArrayList<>(0);
		this.teams = new ArrayList<>(0);

	}

	public String getName() {
		return (firstName + " " + lastName);
	}

	public void setName(String first, String last) {
		this.firstName = first;
		this.lastName = last;
	}

	public String getIDP() {
		return solidIDP;
	}
	public String getWebID() {
		return solidWebID;
	}
	public String getClientID() {
		return solidClientID;
	}
	public String getClientSecret() {
		return solidClientSecret;
	}
	public String getAuthFlow() {
		return authFlow;
	}


	public ArrayList<String> getUnavailability() {
		return this.unavailability;
	}

	public void setAvailability(String unavailable) {
		this.unavailability.add(unavailable);
	}

	public User getUser() {
		return this;
	}

	public ArrayList<Team> getTeams(){
		return this.teams;
	}

}