package com.turf.adminportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.turf.adminportal.dto.Teams;

public interface TeamRepo extends JpaRepository<Teams, Integer> {

	
}
