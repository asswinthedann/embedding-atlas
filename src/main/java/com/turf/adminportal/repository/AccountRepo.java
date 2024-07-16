package com.turf.adminportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.turf.adminportal.dto.Account;

public interface AccountRepo extends JpaRepository<Account, Integer> {

}
