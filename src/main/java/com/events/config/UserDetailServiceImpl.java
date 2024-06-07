package com.events.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.events.dao.MemberRepository;
import com.events.entities.Member;



public class UserDetailServiceImpl implements  UserDetailsService{

	@Autowired
	private MemberRepository memberRepository;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		//fetching user from database
		Member member = memberRepository.getUserByName(username);
		if(member==null)
		{
			throw new UsernameNotFoundException("Could not found user !!");
		}
		CustomUserDetails customUserDetails = new CustomUserDetails(member);
		return customUserDetails;
	}

}
