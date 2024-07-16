package com.turf.adminportal.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Teams {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int teamId;
	private String teamName;
	private String teamLead;
	private String email;
	private String color;
	private int teamNumber;
	private int tPoints;

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getTeamLead() {
		return teamLead;
	}

	public void setTeamLead(String teamLead) {
		this.teamLead = teamLead;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getTeamNumber() {
		return teamNumber;
	}

	public void setTeamNumber(int teamNumber) {
		this.teamNumber = teamNumber;
	}

	public int gettPoints() {
		return tPoints;
	}

	public void settPoints(int tPoints) {
		this.tPoints = tPoints;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	@Override
	public String toString() {
		return "Teams [teamId=" + teamId + ", teamName=" + teamName + ", teamLead=" + teamLead + ", email=" + email
				+ ", color=" + color + ", teamNumber=" + teamNumber + ", tPoints=" + tPoints + "]";
	}

	
}
