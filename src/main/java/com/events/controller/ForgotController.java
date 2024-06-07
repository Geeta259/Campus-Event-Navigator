package com.events.controller;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.events.dao.MemberRepository;
import com.events.entities.Member;
import com.events.helper.Message;
import com.events.services.EmailServices;

import jakarta.servlet.http.HttpSession;

@Controller
public class ForgotController {
	Random random;
	
	@Autowired
	private MemberRepository memberRepository;
	
	@Autowired
	private EmailServices emailServices;
	
	@Autowired
	private BCryptPasswordEncoder bcrypt;
	
	//email id form open handler
		@RequestMapping("/forgot")
		public String openEmailForm()
		{
			return "forgot";
		}
		
		@PostMapping("/send-otp")
		public String sendOTP(@RequestParam("email") String email,HttpSession session)
		{
			
			//verify  email first
			Member member = this.memberRepository.getUserByName(email);
			//System.out.println(user);
			if(member==null)
			{
				session.setAttribute("message", new Message("That email address is not registered.","alert-danger"));
				//System.out.println("error");
				return "forgot";
			}
			
			//valid registered email  send otp
			
			random  = new Random();
			System.out.println("Email " + email);
			
			//generating otp of 4 digit
			int otp  = 1000 + random.nextInt(9000);
			System.out.println("OTP " + otp);
		
			String subject = "OTP From NITRR Campus Event System for password reset";
			String message = ""
					+"<div style='border:1px solid #e2e2e2; padding:20px'>"
					+"<h1>"
					+"OTP  is "
					+"<b>"+otp
					+"</n>"
					+"</h1>"
					+"</div>";
			
		    String to = email;
		    
			boolean flag = this.emailServices.sendEmail(subject,message,to);
			
			if(flag)
			{
				//set otp and registered email in  session 
				session.setAttribute("myotp", otp);
				session.setAttribute("email", email);
				return "verify_otp";
			}
			else
			{
				session.setAttribute("message", new Message("Something went wrong! Try Again...","alert-danger"));
				return "forgot";
			}
			
		}
		
		@PostMapping("/verify-otp")
		public String verifyOtp(@RequestParam("otp") int otp,HttpSession session){
			int myotp = (int)session.getAttribute("myotp");
			//System.out.println("myotp");
			if(myotp==otp)
			{
				//if generated otp and entered otp are matched
				return  "password_change";
			}
			else {
				session.setAttribute("message", new Message("Entered wrong OTP!! Try again.","alert-danger"));
				return "forgot";
			}
		}
		
		@PostMapping("/change-password")
		public String changePassword(@RequestParam("newpassword") String newpassword,HttpSession session)
		{
			String email = (String)session.getAttribute("email");
			Member member = this.memberRepository.getUserByName(email);
			member.setPassword(this.bcrypt.encode(newpassword));
			this.memberRepository.save(member);
			return "redirect:/signin?changepass";
		}
}
