package com.turf.adminportal.service;

import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.turf.adminportal.dao.TurfDao;
import com.turf.adminportal.dto.Account;
import com.turf.adminportal.dto.Membership;
import com.turf.adminportal.dto.Slots;
import com.turf.adminportal.dto.Teams;
import com.turf.adminportal.dto.Transaction;

@Service
public class TurfService {

	@Autowired
	private TurfDao dao;

	public boolean verifyCred(String uName, String pass) {
		return dao.verifyCred(uName, pass);
	}

	public Date stringToDate(String sDate) {
		DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate date = LocalDate.parse(sDate, sdf);
		return Date.valueOf(date);
	}

	public Time stringToTime(String sTime) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			long ms = sdf.parse(sTime).getTime();
			return new Time(ms);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

//--------------------------------------------------------------------------------------------------------------------
// SLOTS BOOKING

	public Slots bookSlot(Slots s) {
		return dao.bookSlot(s);
	}

	public void slotBookingTrans(Transaction trans) {
		dao.slotBookingTransaction(trans);
	}
	
	public void updateBookingStatus() {
		dao.updateBookingStatus();
	}

	public Slots fetchById(int id) {

		return dao.fetchById(id);
	}

	public void deleteById(int id) {
		dao.deleteById(id);
	}

	public List<Slots> getAllSlots() {
		return dao.getAllSlots();
	}

	public List<Slots> getUpcommingBookings() {
		return dao.getUpcommingBookings();
	}

	public List<Integer> slotAvailablity(Date date, Time in, Time out) {
		return dao.slotAvailablity(date, in, out);
	}

	public List<Integer> getBookingsIdByDate(Date date) {

		return dao.getBookingsIdbyDate(date);
	}

	public List<Slots> getSlotsByDate(Date date) {
		return dao.getSlotsByDate(date);
	}

// SLOTS BOOKINGS
//-----------------------------------------------------------------------------------------------------------
// TEAMS

	public Teams regTeam(Teams t) {
		return dao.regTeam(t);
	}

	public List<Teams> getAllTeams() {
		return dao.getAllTeams();
	}

	public void addPoints(int id, int points) {
		dao.addPoints(id, points);
	}

	public List<Teams> getTeamsByPoints(){
		return dao.getTeamsByPoints();
	}
	
//---------------
//	MEMS

	public Membership addMems(Membership mems) {
		return dao.addMems(mems);
	}
	
	public int findAge(Date dob) {

		LocalDate birthDate = dob.toLocalDate();
		LocalDate currentDate = LocalDate.now();
		return Period.between(birthDate, currentDate).getYears();

	}
	
	public List<Membership> getAllMems(){
		return dao.getAllMeme();
	}
	
		
//--------------------------------------------------------------------------------------------------------------------------------
//	ACCOUNTS
	
	
	public Account createAccount(Account acc) {
		acc.setAccBalance(0);
		return dao.createAccount(acc);
	}

	public List<Account> getAllAcc() {
		return dao.getAllAcc();
	}
	
	public List<Integer> getAccNums() {
		return dao.getAccNums();
	}
	
	public Date todayDate() {
		return dao.nowDate();
	}
	
		
	public Transaction transact(Transaction trans) {
		return dao.transact(trans);
	}
	
	public double getAccBal(int accNum) {
		return dao.getAccBalance(accNum);
	}
	
	public void updateAccBal(int accNum, String transType, double amount) {
		dao.updateAccBal(accNum, transType, amount);
	}

	public List<Transaction> getTransByDate(Date date) {
		return dao.getTransByDate(date);
	}


}