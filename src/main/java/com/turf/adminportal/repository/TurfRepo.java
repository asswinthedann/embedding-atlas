package com.turf.adminportal.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.turf.adminportal.dto.Slots;

public interface TurfRepo extends JpaRepository<Slots, Integer> {

	//Save(Slots s);
	//FindById(int id);
	//FindAll();
	//DeleteById(int id);

	List<Slots> findByDate(Date date);

	
}
