package com.turf.adminportal.controller;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.turf.adminportal.dto.Account;
import com.turf.adminportal.dto.Membership;
import com.turf.adminportal.dto.Slots;
import com.turf.adminportal.dto.Teams;
import com.turf.adminportal.dto.Transaction;
import com.turf.adminportal.service.TurfService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class TurfController {
	@Autowired
	private TurfService service;

	@RequestMapping("/")
	public ModelAndView home(ModelAndView mv) {
		mv.setViewName("login");
		return mv;
	}

	@RequestMapping("/verify")
	public String verifyCredentials(@RequestParam("userName") String userName, @RequestParam("pass") String password) {

		if (service.verifyCred(userName, password)) {

			return "redirect:/bookingsPage";
		}
		return "Wrong Username and Password";
	}

//SLOT BOOKINGS PAGE CONTROLLERS

	@RequestMapping("/bookingsPage")
	public ModelAndView bookingsPage() {

		List<Slots> bookedSlots = getUpcommingBookings();
		System.out.println(bookedSlots);
		ModelAndView mv = new ModelAndView();
		mv.setViewName("bookings");
		mv.addObject("slots", bookedSlots);
		return mv;

	}

	@RequestMapping("/bookSlot")
	public ModelAndView bookSlotTest(HttpServletRequest req, Transaction trans, ModelAndView mv) {

		String name = req.getParameter("name");
		int id = Integer.parseInt(req.getParameter("teamId"));
		long number = Long.parseLong(req.getParameter("number"));
		String email = req.getParameter("email");
		Date date = service.stringToDate(req.getParameter("date"));
		Time in = service.stringToTime(req.getParameter("entryTime"));
		Time out = service.stringToTime(req.getParameter("exitTime"));

		List<Integer> booked = service.slotAvailablity(date, in, out);

		if (booked.isEmpty()) {
			service.addPoints(id, 200);
			service.slotBookingTrans(trans);
			service.updateAccBal(trans.getAccNum(), trans.getTransType(), trans.getAmount());
			Slots s = new Slots();
			s.setName(name);
			s.setTeamId(id);
			s.setNumber(number);
			s.setEmail(email);
			s.setDate(date);
			s.setEntryTime(in);
			s.setExitTime(out);
			s.setStatus("Booked");
			service.bookSlot(s);
		} else {
			mv.setViewName("bookings");
			mv.addObject("alreadyBooked", booked);
			return mv;

		}

		List<Slots> bookedSlots = getUpcommingBookings();
		mv.setViewName("bookings");
		mv.addObject("slots", bookedSlots);
		return mv;

	}

	public List<Slots> getUpcommingBookings() {
		service.updateBookingStatus();
		return service.getUpcommingBookings();
	}

	@GetMapping("/getBookingsByDate")
	public ModelAndView getSlotsByDate(HttpServletRequest req, ModelAndView mv) {
		Date date = service.stringToDate(req.getParameter("date"));
		List<Slots> slots = service.getSlotsByDate(date);
		if (!slots.isEmpty()) {
			mv.setViewName("bookings");
			mv.addObject("bookedSlots", slots);
			return mv;
		} else {
			return null;
		}

	}

	@RequestMapping("/delById")
	public ModelAndView deleteById(@RequestParam int id, ModelAndView mv) {
		service.deleteById(id);

		mv.setViewName("bookings");
		mv.addObject("deleted", "Deleted Successfuly");
		return mv;
	}

	@GetMapping("getAll")
	public ModelAndView getAllSlots(ModelAndView mv) {

		List<Slots> allSlots = service.getAllSlots();
		mv.setViewName("slotsDb");
		mv.addObject("slots", allSlots);
		return mv;
	}

//UNUSED CONTROLLERS - Slot Bookings

	@GetMapping("getById")
	public Slots fetchById(@RequestParam int id) {

		return service.fetchById(id);

	}

	@GetMapping("getSlotsIdByDate")
	public List<Integer> getBookingsIdByDate(@RequestParam String date) {
		Date sqlDate = service.stringToDate(date);
		return service.getBookingsIdByDate(sqlDate);
	}

//------------------------------------------------------------------------------------------------------------------------------
//TEAMS CONTROLLER

	@RequestMapping("/mems")
	public ModelAndView teamsMems(ModelAndView mv) {
		mv.setViewName("teamsMems");
		return mv;
	}

	@PostMapping("/regTeam")
	public ModelAndView regTeam(HttpServletRequest req, ModelAndView mv, Teams team) {

		String tName = req.getParameter("tname");
		String cap = req.getParameter("cap");
		String email = req.getParameter("email");
		String color = req.getParameter("color");
		int teamNum = Integer.parseInt(req.getParameter("num"));

		team.setTeamName(tName);
		team.setTeamLead(cap);
		team.setEmail(email);
		team.setColor(color);
		team.setTeamNumber(teamNum);
		team.settPoints(400);
		service.regTeam(team);

		mv.setViewName("teamsMems");
		mv.addObject("regMsg", "Team Registered Successfully");
		return mv;

	}

	@RequestMapping("/getAllTeams")
	public ModelAndView getAllTeams(ModelAndView mv) {

		List<Teams> teams = service.getAllTeams();
		mv.setViewName("teamsMems");
		mv.addObject("teams", teams);
		return mv;

	}

	@RequestMapping("/getTeamsByPoints")
	public ModelAndView getTeamsByPoints(ModelAndView mv) {
		List<Teams> teams = service.getTeamsByPoints();
		mv.setViewName("teamsMems");
		mv.addObject("teamsByPoints", teams);
		return mv;
	}

	@RequestMapping("/updatePoints")
	public ModelAndView addPoints(@RequestParam int id, @RequestParam int points, ModelAndView mv) {
		service.addPoints(id, points);
		List<Teams> teams = service.getAllTeams();
		mv.setViewName("teamsMems");
		mv.addObject("addedMsg", "Points Added SuccessFully");
		mv.addObject("teams", teams);
		return mv;
	}

//-------------------------
//Membership

	@RequestMapping("/regMems")
	public ModelAndView registerMember(HttpServletRequest req, ModelAndView mv, Membership mems) {

		String name = req.getParameter("name");
		Date dob = service.stringToDate(req.getParameter("dob"));
		int age = service.findAge(dob);
		String region = req.getParameter("region");
		String email = req.getParameter("email");
		long phone = Long.parseLong(req.getParameter("phone"));
		String address = req.getParameter("address");
		String type = req.getParameter("type");

		mems.setName(name);
		mems.setDateOfBirth(dob);
		mems.setAge(age);
		mems.setRegion(region);
		mems.setEmail(email);
		mems.setPhone(phone);
		mems.setAddress(address);
		mems.setMembershipType(type);
		System.out.println(type);
		if (type.equals("gold")) {
			mems.setPaymentReceived(300);
		}
		if (type.equals("silver")) {
			mems.setPaymentReceived(200);
		}
		if (type.equals("platinum")) {
			mems.setPaymentReceived(400);
		}
		if (type.equals("diamond")) {
			mems.setPaymentReceived(500);
		} else {
			mems.setPaymentReceived(0);
		}
		service.addMems(mems);
		mv.setViewName("teamsMems");
		mv.addObject("memMsg", "Member Added Successfully");
		return mv;

	}

	@RequestMapping("/showMems")
	public ModelAndView getAllMems(ModelAndView mv) {
		List<Membership> mems = service.getAllMems();
		mv.setViewName("teamsMems");
		mv.addObject("mems", mems);
		return mv;

	}

//-----------------------------------------------------------------------------------------------------------------------------------------------
//	ACCOUNTS PAGE

	@RequestMapping("/acc")
	public ModelAndView accountsPage(ModelAndView mv) {
		mv.setViewName("accounts");
		return mv;
	}

	@PostMapping("createAcc")
	public ModelAndView createAccount(HttpServletRequest req, ModelAndView mv, Account acc) {

		String name = req.getParameter("name");
		String email = req.getParameter("email");
		String pass = req.getParameter("pass");
		String desc = req.getParameter("desc");

		acc.setAccName(name);
		acc.setEmail(email);
		acc.setPass(pass);
		acc.setDescription(desc);
		acc.setAccBalance(0);

		service.createAccount(acc);
		mv.setViewName("accounts");
		mv.addObject("accMsg", "Account Created Successfully");
		return mv;
	}

	@RequestMapping("/transact")
	public ModelAndView transact(HttpServletRequest req, ModelAndView mv, Transaction trans) {

		int accNum = Integer.parseInt(req.getParameter("accNum"));
		String type = req.getParameter("type");
		String source = req.getParameter("src");
		double amount = Double.parseDouble(req.getParameter("amt"));
		String desc = req.getParameter("desc");

		trans.setAccNum(accNum);
		trans.setTransType(type);
		trans.setSource(source);
		trans.setAmount(amount);
		trans.setDescription(desc);
		trans.setDate(service.todayDate());
		service.updateAccBal(accNum, type, amount);
		service.transact(trans);
		mv.setViewName("accounts");
		mv.addObject("transMsg", "Transaction Done SuccessFully");
		return mv;
	}

	@RequestMapping("/getBal")
	public ModelAndView getBalance(@RequestParam("accNum") int accNum, ModelAndView mv) {

		double accBal = service.getAccBal(accNum);
		mv.setViewName("accounts");
		mv.addObject("balance", accBal);
		return mv;
	}

	@RequestMapping("/getAcc")
	public ModelAndView getAllAcc(ModelAndView mv) {

		List<Account> acc = service.getAllAcc();
		mv.setViewName("accounts");
		mv.addObject("account", acc);
		return mv;

	}

	@GetMapping("getTransByDate")
	public ModelAndView getTransByDate(@RequestParam String date, ModelAndView mv) {
		Date sqlDate = service.stringToDate(date);
		List<Transaction> trans = service.getTransByDate(sqlDate);
		mv.setViewName("accounts");
		mv.addObject("transactions", trans);
		return mv;
	}

	@RequestMapping("/getTodaysTrans")
	public ModelAndView getTodayTrans(ModelAndView mv) {

		Date date = service.todayDate();
		List<Transaction> trans = service.getTransByDate(date);
		mv.setViewName("accounts");
		mv.addObject("transactions", trans);
		return mv;
	}

}
