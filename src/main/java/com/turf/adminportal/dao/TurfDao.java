package com.turf.adminportal.dao;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.turf.adminportal.dto.Account;
import com.turf.adminportal.dto.Membership;
import com.turf.adminportal.dto.Slots;
import com.turf.adminportal.dto.Teams;
import com.turf.adminportal.dto.Transaction;
import com.turf.adminportal.repository.AccountRepo;
import com.turf.adminportal.repository.MembershipRepo;
import com.turf.adminportal.repository.TeamRepo;
import com.turf.adminportal.repository.TransRepo;
import com.turf.adminportal.repository.TurfRepo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

@Repository
public class TurfDao {

	@Autowired
	TurfRepo sRepo;
	@Autowired
	TeamRepo tRepo;
	@Autowired
	AccountRepo accRepo;
	@Autowired
	TransRepo transRepo;
	@Autowired
	MembershipRepo memsRepo;
	@Autowired
	private EntityManagerFactory emf;

	public boolean verifyCred(String userName, String pass) {

		EntityManager em = emf.createEntityManager();
		EntityTransaction et = em.getTransaction();
		try {

			et.begin();
			String sql = "SELECT COUNT(*) FROM credentials WHERE user_name = :username AND password = :pass ";
			Query query = em.createNativeQuery(sql);
			query.setParameter("username", userName);
			query.setParameter("pass", pass);

			Number res = (Number) query.getSingleResult();
			et.commit();
			return res.intValue() == 1;
		} catch (Exception e) {
			e.printStackTrace();
			if (em.getTransaction().isActive()) {
				et.rollback();
			}
			return false;
		} finally {
			em.close();
		}

	}

//--------------------------------------------------------------------
// SLOTS BOOKINGS

	public Slots bookSlot(Slots s) {
		return sRepo.save(s);
	}

	public Slots fetchById(int id) {

		Optional<Slots> slot = sRepo.findById(id);

		if (slot.isPresent()) {
			return slot.get();
		} else
			return null;

	}

	public Teams fetchTeams(int id) {

		Teams team = tRepo.findById(id).get();
		return team;

	}

	public void deleteById(int id) {

		sRepo.deleteById(id);

	}

	public List<Slots> getAllSlots() {

		return sRepo.findAll();
	}

	public List<Integer> slotAvailablity(Date date, Time in, Time out) {

		EntityManager em = emf.createEntityManager();

		String jpql = "SELECT s.sNo FROM Slots s WHERE s.date= :date AND (:in <= s.entryTime AND :out >= s.exitTime )";
		TypedQuery<Integer> query = em.createQuery(jpql, Integer.class);
		query.setParameter("date", date);
		query.setParameter("in", in);
		query.setParameter("out", out);

		List<Integer> teamNames = query.getResultList();
		return teamNames;
	}

	public List<Integer> getBookingsIdbyDate(Date date) {

		EntityManager em = emf.createEntityManager();

		String jpql = "SELECT s.teamId FROM Slots s WHERE s.date = :date";
		TypedQuery<Integer> query = em.createQuery(jpql, Integer.class);
		query.setParameter("date", date);
		List<Integer> teamnames = query.getResultList();
		System.out.println(teamnames);
		return teamnames;
	}

	public void updateBookingStatus() {
		EntityManager em = emf.createEntityManager();
		EntityTransaction et = em.getTransaction();
		LocalDate locDate = LocalDate.now();
		Date today = Date.valueOf(locDate);

		String jpql = "UPDATE Slots s SET s.status = 'Expired' WHERE s.date < :todaysDate ";
		et.begin();
		Query query = em.createQuery(jpql);
		query.setParameter("todaysDate", today);
		query.executeUpdate();
		et.commit();
	}

	public List<Slots> getUpcommingBookings() {

		EntityManager em = emf.createEntityManager();
		String jpql = "SELECT s FROM Slots s WHERE s.status != 'Expired' ";

		TypedQuery<Slots> query = em.createQuery(jpql, Slots.class);
		List<Slots> bookedSlots = query.getResultList();
		return bookedSlots;

	}

	public void slotBookingTransaction(Transaction trans) {
		trans.setAccNum(121);
		trans.setTransType("Cr");
		trans.setSource("Slot Booking");
		trans.setDescription("transaction auto updated for booking slots");
		trans.setDate(nowDate());
		trans.setAmount(500);
		transRepo.save(trans);
	}

	public List<Slots> getSlotsByDate(Date date) {

		List<Slots> bookedSlots = sRepo.findByDate(date);
		return bookedSlots;
	}

//BOOKINGS SLOTS
//---------------------------------------------------------------------------------------------------------------------
//TEAMS	

	public Teams regTeam(Teams t) {
		return tRepo.save(t);
	}

	public List<Teams> getAllTeams() {
		return tRepo.findAll();
	}
	
	public void addPoints(int id, int points) {

		EntityManager em = emf.createEntityManager();
		EntityTransaction et = em.getTransaction();

		String jpql = "UPDATE Teams t SET t.tPoints = t.tPoints + :points WHERE t.teamId = :id ";
		et.begin();
		em.createQuery(jpql).setParameter("points", points).setParameter("id", id).executeUpdate();
		et.commit();

	}
	
	public List<Teams> getTeamsByPoints(){
		EntityManager em = emf.createEntityManager();
		
		String jpql = "SELECT t FROM Teams t ORDER BY t.tPoints DESC";
		TypedQuery<Teams> query = em.createQuery(jpql, Teams.class);
		List<Teams> allTeams = query.getResultList();
		return allTeams;
		
	}
	
	
//--------------------------------------------------------------------------------------
//  MEMS
	
	public Membership addMems(Membership mems) {
		return memsRepo.save(mems);
	}
	
	public List<Membership> getAllMeme() {
		return memsRepo.findAll();
	}
	
	
	
// MEMS
//---------------------------------------------------------------------------------------------------------
// ACCOUNTS
	
	

	public Account createAccount(Account acc) {
		return accRepo.save(acc);
	}
	

	public List<Account> getAllAcc() {
		return accRepo.findAll();
	}
	
	public Transaction transact(Transaction trans) {
		return transRepo.save(trans);
	}

	public List<Integer> getAccNums() {
	
		EntityManager em = emf.createEntityManager();

		String jpql = "SELECT a.accNum FROM Account a";
		TypedQuery<Integer> query = em.createQuery(jpql,Integer.class);
		List<Integer> accNums = query.getResultList();
		return accNums;
 	}
	
	public double getAccBalance(int accNum) {

		EntityManager em = emf.createEntityManager();

		Account acc = em.find(Account.class, accNum);
		double balance = acc.getAccBalance();
		return balance;

	}

	public Date nowDate() {
		LocalDate locDate = LocalDate.now();
		Date sqlDate = Date.valueOf(locDate);
		return sqlDate;
	}

	public void updateAccBal(int accNum, String transType, double amount) {

		EntityManager em = emf.createEntityManager();
		EntityTransaction et = em.getTransaction();
		et.begin();
		Account acc = em.find(Account.class, accNum);
		double lastBal = acc.getAccBalance();
		if (transType.equals("Cr")) {
			double currentBal = lastBal + amount;
			acc.setAccBalance(currentBal);
			em.merge(acc);
			et.commit();
		}
		if (transType.equals("Dr")) {
			double currentBal = lastBal - amount;
			acc.setAccBalance(currentBal);
			em.merge(acc);
			et.commit();
		}

	}



	public List<Transaction> getTransByDate(Date date) {

		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		String jpql = "SELECT t FROM Transaction t WHERE t.date = :date ";
		TypedQuery<Transaction> query = em.createQuery(jpql, Transaction.class);
		query.setParameter("date", date);
		List<Transaction> transList = query.getResultList();
		
		em.getTransaction().commit();
		return transList;
	}




}