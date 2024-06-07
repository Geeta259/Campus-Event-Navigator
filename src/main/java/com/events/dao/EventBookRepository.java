package com.events.dao;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.events.entities.BookingEvent;

import jakarta.transaction.Transactional;





public interface EventBookRepository extends JpaRepository<BookingEvent,Integer>{

	@Query("from BookingEvent as e where e.eventid =:eid and e.uid =:uid")
	public BookingEvent findEventById(@Param("eid")Integer eid,@Param("uid")Integer uid);
	
	@Query("SELECT DISTINCT b.uid FROM BookingEvent b WHERE b.eventid = :eventid")
    List<Integer> findDistinctUserIdsByEventId(@Param("eventid")Integer eventid);
	
	@Query("SELECT b FROM BookingEvent b WHERE b.uid IN :uids")
	List<BookingEvent> findRecordsByUids(@Param("uids") List<Integer> uids);
	
	 @Modifying
	    @Transactional
	@Query("DELETE FROM BookingEvent b WHERE b.eventid = :eventid AND b.uid = :uid")
	public  void deleteByEventIdAndUserId(@Param("eventid") Integer eventid, @Param("uid") Integer uid);
	
}
