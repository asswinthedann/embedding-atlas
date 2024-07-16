package com.turf.adminportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.turf.adminportal.dto.Transaction;

public interface TransRepo extends JpaRepository<Transaction, Integer> {

}
