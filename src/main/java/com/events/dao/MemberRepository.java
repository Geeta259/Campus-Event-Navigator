package com.events.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import com.events.entities.Member;


public interface MemberRepository extends JpaRepository<Member,Integer>{
	 @Query("select u from Member u where u.email = :email")
		public Member getUserByName(@Param("email") String email);

	   @Query("select count(m) from Member m where m.role = :role")
	    long countByRole(@Param("role") String role);
	 
	 @Query("from Member as u where u.role = 'ROLE_USER'")
		public Page<Member> findUser(Pageable pageable);

}
