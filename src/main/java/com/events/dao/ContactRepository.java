package com.events.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.events.entities.Contact;
import com.events.entities.Coordinator;



public interface ContactRepository extends JpaRepository<Contact,Integer>{
	@Query("from Contact as c")
	public Page<Coordinator> findCoordinatorByUser(Pageable  pageable);
	
	 @Query("select count(u) from Contact u")
		public long countAll();
}
