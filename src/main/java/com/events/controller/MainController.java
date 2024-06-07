package com.events.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.events.entities.Category;
import com.events.entities.Contact;
import com.events.entities.Coordinator;
import com.events.entities.Events;
import com.events.entities.Sponsors;
import com.events.entities.Member;
import com.events.helper.Message;
import com.events.dao.CategoryRepository;
import com.events.dao.ContactRepository;
import com.events.dao.CoordinatorRepository;
import com.events.dao.EventRepository;
import com.events.dao.SponsorRepository;
import com.events.dao.MemberRepository;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;





@Controller
public class MainController {
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private MemberRepository memberRepository;
	

	@Autowired
	private SponsorRepository sponsorsRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private EventRepository eventRepository;
	
	@Autowired
	private CoordinatorRepository coordinatorRepository;
	
	@Autowired
	private ContactRepository contactRepository;
	

	@RequestMapping(value="/",method = RequestMethod.GET)
	public String home(Model model)
	{
		Pageable pageable = PageRequest.of(0, 6); 
		Page<Events> eventsPage = this.eventRepository.findAll(pageable);
		List<Events> events = eventsPage.getContent();

		
		model.addAttribute("events",events);
		
		Page<Category> categoryPage = this.categoryRepository.findAll(pageable);
		List<Category> category = categoryPage.getContent();
		model.addAttribute("category",category);
		
		pageable = PageRequest.of(0, 8);
		Page<Sponsors> sponsorPage = this.sponsorsRepository.findAll(pageable);
		List<Sponsors> sponsor = sponsorPage.getContent();
		model.addAttribute("sponsors",sponsor);
		

		Page<Coordinator> coordinatorPage = this.coordinatorRepository.findAll(pageable);
		List<Coordinator> coordinator = coordinatorPage.getContent();
		model.addAttribute("coordinator",coordinator);
		
		return "index";
	}
	@RequestMapping("/signup")
	public String signup(Model model)
	{
		return "signup";
	}
	//handler for registering user
		@PostMapping("/do_register")
		public String registerUser(@Valid @ModelAttribute("user") Member member,
				BindingResult result,
				@RequestParam(value="agreement",defaultValue="false")
		boolean agreement,Model model,HttpSession session)
		{
			try
			{
				if(!agreement)
				{
					System.out.println("You have not agreed the terms and conditions");
					throw new Exception("You have not agreed the terms and conditions");
				}
				
				
				member.setEnabled(false);
				member.setRole("ROLE_USER");
				member.setImage("default.jpeg");
				
				//user.setPassword(passwordEncoder.encode(user.getPassword()));
				
				System.out.println("Agreement " + agreement);
				System.out.println("USER " + member);
				
				this.memberRepository.save(member);
				
				model.addAttribute("user",new Member());
				session.setAttribute("message", new Message("Successfully registered wait for verification to login..!!","alert-success"));
				return "signup";
				
			}catch(Exception e)
			{
				e.printStackTrace();
				model.addAttribute("user",member);
				session.setAttribute("message", new Message("Agree the terms and conditions !!","alert-danger"));
				System.out.println(e.getMessage());
				return "signup";
			}
			
			
		}
		@RequestMapping("/signin")
		public String customLogin(Model model)
		{
			return "login";
		}
		
		@PostMapping("/process-contact")
		public String processContact(@ModelAttribute Contact contact,HttpSession session)
		{
			try
			{
				
				this.contactRepository.save(contact);
				//file uploading
				
			   System.out.println("DATA" + contact);
				System.out.println("Added to database");
				
				//message success sent
				
				session.setAttribute("message", new Message("Thankyou for contact us...","alert-success"));
				
			}catch(Exception e)
			{
				System.out.println("ERROR" + e.getMessage());
				//error message sent
				session.setAttribute("message", new Message("Something went wrong! Try Again...","alert-danger"));
			}
			return "redirect:/#contact";
		}
		
		@RequestMapping("/all_events")
		public String allEvents(Model model)
		{
			List<Events> events = this.eventRepository.findAll();
			model.addAttribute("events",events);
			return "all_events";
		}
		
		@RequestMapping("/all_category")
		public String allCategory(Model model)
		{
			List<Category> category = this.categoryRepository.findAll();
			model.addAttribute("category",category);
			return "all_category";
			
		}
		@RequestMapping("/all_sponsors")
		public String allSponsors(Model model)
		{
			List<Sponsors> sponsors = this.sponsorsRepository.findAll();
			model.addAttribute("sponsors",sponsors);
			return "all_sponsor";
			
		}
		@RequestMapping("/all_coordinators")
		public String allCoordinator(Model model)
		{
			List<Coordinator> coordinator = this.coordinatorRepository.findAll();
			model.addAttribute("coordinator",coordinator);
			
			return "all_coordinator";
			
		}
		
}

