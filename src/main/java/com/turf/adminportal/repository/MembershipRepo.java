package com.turf.adminportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.turf.adminportal.dto.Membership;

public interface MembershipRepo extends JpaRepository<Membership, Integer> {

}
