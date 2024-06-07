package com.events.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import com.events.entities.Sponsors;



public interface SponsorRepository extends JpaRepository<Sponsors,Integer>{
	@Query("select s from Sponsors s")
	public List<Sponsors> findAll();
	
	@Query("from Sponsors as s where s.member.id =:id")
	public Page<Sponsors> findSponsorsByUser(@Param("id")int id,Pageable  pageable);
	
	@Query("select count(s) from Sponsors s where s.member.id =:id")
	public long countAll(@Param("id")int id);

}
