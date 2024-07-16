package com.turf.adminportal.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Account {

	@Id
	private int accNum;
	private String accName;
	private String email;
	private String pass;
	private String description;
	private double accBalance;

	public int getAccNum() {
		return accNum;
	}

	public void setAccNum(int accNum) {
		this.accNum = accNum;
	}

	public String getAccName() {
		return accName;
	}

	public void setAccName(String accName) {
		this.accName = accName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPass() {
		return pass;

	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public double getAccBalance() {
		return accBalance;
	}

	public void setAccBalance(double accBalance) {
		this.accBalance = accBalance;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Account [accNum=" + accNum + ", accName=" + accName + ", email=" + email + ", pass=" + pass + ", desc="
				+ description + ", accBalance=" + accBalance + "]";
	}

}
