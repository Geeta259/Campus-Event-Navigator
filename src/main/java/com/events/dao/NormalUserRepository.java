package com.events.dao;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.events.entities.BookingEvent;
import com.events.entities.NormalUser;

public interface NormalUserRepository extends JpaRepository<NormalUser,Integer> {
	@Query("SELECT u FROM NormalUser u WHERE u.email = :email")
		public NormalUser getUserByEmail(@Param("email") String email);
	
	@Query("SELECT e from BookingEvent e where e.uid =:id")
	public List<BookingEvent> findEventsByUserId(@Param("id")int id);
	
	@Query("SELECT u FROM NormalUser u WHERE u.id = :id")
	public NormalUser getUserById(@Param("id")int id);
	
}
