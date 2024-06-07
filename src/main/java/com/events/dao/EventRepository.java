package com.events.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.events.entities.Events;




public interface EventRepository extends JpaRepository<Events,Integer>{
	//get all contacts of current user through jpa query
		//@Query("select e from Events e where e.user.id =:userId")
		//public Page<Events> findEventsByUser(@Param("userId")int userId,Pageable pageable);
		
		@Query("from Events as e where e.member.id =:id")
		public Page<Events> findEventsByUser(@Param("id")int id,Pageable  pageable);
		
		/*
		 * A page is a sublist of a list of object.
		 * pageable object has two information currentpage and no of contact per page
		 * */
		
		//get all contact which contained given name and user
		//jpa query used here
		@Query("select count(e) from Events e where e.member.id =:id")
		public long countAll(@Param("id")int id);
		
		@Query("from Events as e where e.title =:title")
		public Events findEventByName(@Param("title")String name);
		
		
		@Query("from Events as e where e.member.id =:id")
		public List<Events> findEventsByUser(@Param("id")int id);
		
		
		 @Query("from Events as e where e.event_id =:id")
		public Optional<Events> findEventByEventId(@Param("id")int id);
	
}
