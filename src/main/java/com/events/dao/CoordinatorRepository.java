package com.events.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.events.entities.Coordinator;



public interface CoordinatorRepository extends JpaRepository<Coordinator,Integer>{
	@Query("from Coordinator as c where c.member.id =:id")
	public Page<Coordinator> findCoordinatorByUser(@Param("id")int id,Pageable  pageable);
	
	@Query("select count(c) from Coordinator c where c.member.id =:id")
	public long countAll(@Param("id")int id);
	
}
