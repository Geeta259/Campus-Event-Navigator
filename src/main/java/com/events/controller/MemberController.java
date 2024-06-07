package com.events.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.events.dao.CategoryRepository;
import com.events.dao.ContactRepository;
import com.events.dao.CoordinatorRepository;
import com.events.dao.EventBookRepository;
import com.events.dao.EventRepository;
import com.events.dao.FeedbackRepository;
import com.events.dao.SponsorRepository;
import com.events.dao.MemberRepository;
import com.events.dao.NormalUserRepository;
import com.events.entities.BookingEvent;
import com.events.entities.Category;
import com.events.entities.Contact;
import com.events.entities.Coordinator;
import com.events.entities.Events;
import com.events.entities.Sponsors;
import com.events.entities.UserFeedback;
import com.events.entities.Member;
import com.events.entities.NormalUser;
import com.events.helper.Message;
import com.events.services.EmailServices;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/member")
public class MemberController {

	@Autowired
	private MemberRepository memberRepository;
	

	@Autowired
	private SponsorRepository sponsorsRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private EventRepository eventRepository;
	
	
	@Autowired
	private  CoordinatorRepository coordinatorRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private ContactRepository contactRepository;
	
	@Autowired
	private EventBookRepository bookRepository;
	
	@Autowired
	private NormalUserRepository normalUserRepository;
	
	@Autowired
	private FeedbackRepository feedbackRepository;
	

	@Autowired
	private EmailServices emailServices;
	
	Random random;
	
	@ModelAttribute
	public void addCommonData(Model model,Principal principal,HttpSession session)
	{
		//principal class used to get  current active user name
		try
		{
			String userName = principal.getName();
			System.out.println("USERNAME" + userName);
			
			//get login user details from username
			Member member = memberRepository.getUserByName(userName);
			System.out.println("USER " + member);
			
			//add this user into model so that can use in template
			model.addAttribute("user",member);
			
			if(userName!=null)
			{
				session.setAttribute("login_member", member);
				session.removeAttribute("login_user");
			}	
			
		}catch(Exception e)
		{
			System.out.println(e);
		}			
	}
	
	
	@RequestMapping("/index")
	  public String dashboard(Model model,Principal principal,HttpSession session)
	  {
		
			String userName = principal.getName();
			//get user details from user name
			Member member = this.memberRepository.getUserByName(userName);
			long catcount =0,sponcount=0,eventscount=0,coordcount=0;
			if(member.getRole().equals("ROLE_ADMIN"))
			{
				catcount = this.categoryRepository.count();
				
				sponcount = this.sponsorsRepository.count();
				
				eventscount = this.eventRepository.count();
			
				coordcount = this.coordinatorRepository.count();
				
				long membercount = this.memberRepository.countByRole("ROLE_MEMBER");
				long usercount = this.memberRepository.countByRole("ROLE_USER");
				long feedcount = this.feedbackRepository.count();
				long contactcount = this.contactRepository.count();
				
				model.addAttribute("membercount",membercount);
				model.addAttribute("usercount",usercount);
				//System.out.println(membercount + " "+usercount);
				
				model.addAttribute("feedcount",feedcount);
				model.addAttribute("contactcount",contactcount);
			
			}
			else {
				catcount = this.categoryRepository.countAll(member.getId());
				sponcount = this.sponsorsRepository.countAll(member.getId());
				eventscount = this.eventRepository.countAll(member.getId());
				coordcount = this.coordinatorRepository.countAll(member.getId());
			}
		
			
			model.addAttribute("title","Member Dashboard");
			model.addAttribute("eventcount",eventscount);
			model.addAttribute("catcount",catcount);
			model.addAttribute("sponcount",sponcount);
			model.addAttribute("coordcount",coordcount);
			
			
			session.setAttribute("login_member", member);
			session.removeAttribute("login_user");
			 return "member/dashboard";
			 
	  }
	
	
	
	@GetMapping("/sponsor")
	public String openAddSponsorForm(Model model)
	{
		model.addAttribute("title","Add New Sponsor");
		model.addAttribute("sponsor",new Sponsors());
		return "member/addsponsor";
	}
	
	@PostMapping("/process-sponsor")
	public String processSponsor(@ModelAttribute Sponsors sponsors,Principal principal,@RequestParam("sponsor_image") MultipartFile file,HttpSession session)
	{
		try
		{
			String name = principal.getName();
			
			Member member = this.memberRepository.getUserByName(name);
			sponsors.setMember(member);
			
			//processing and uploading file..
			if(file.isEmpty())
			{
				//if the file is empty then try message
				System.out.println("file is empty");
				//default image set
				sponsors.setImage("banner.jpg");
			}
			else
			{
				//file add into folder and update name to contact
				//get file name from form and set into contact class
				sponsors.setImage(file.getOriginalFilename());
				
				//get location where you have to save the file.
				//classpathresources first automatically get path upto static
				File saveFile = new ClassPathResource("static/image/sponsor").getFile();
				
				//get absolute path of above file add separator and add file name
				Path path =  Paths.get(saveFile.getAbsolutePath()+ File.separator+ file.getOriginalFilename());
				
				//copy that file get input stream byte data copy from path 
				Files.copy(file.getInputStream(),path,StandardCopyOption.REPLACE_EXISTING);
					
			    System.out.println("file uploaded");
			}
				
			
			this.sponsorsRepository.save(sponsors);
			//file uploading
			
		  // System.out.println("DATA" + sponsors);
		//	System.out.println("Added to database");
			
			//message success sent
			
			session.setAttribute("message", new Message("New sponsor add successfully!! Add More..","success"));
			
		}catch(Exception e)
		{
			System.out.println("ERROR" + e.getMessage());
			//error message sent
			session.setAttribute("message", new Message("Something went wrong! Try Again...","danger"));
		}
		return "member/addsponsor";
	}
	
	@GetMapping("/show-sponsor/{page}")
	public String showSponsor(@PathVariable("page") Integer page,Model m,Principal principal,HttpSession session)
	{
		try
		{
			m.addAttribute("title","View Sponsor Record");
			
			//contact list send
			//get current active user name
			String userName = principal.getName();
			//get user details from user name
			Member member = this.memberRepository.getUserByName(userName);
			
			//create an object of pageable with current page no and no of data per page
			Pageable  pageable =  PageRequest.of(page, 5);
			
			Page<Sponsors> sponsors;
			
			if(member.getRole().equals("ROLE_ADMIN"))
			{
				sponsors = this.sponsorsRepository.findAll(pageable);
			}
			else
			{
				sponsors = this.sponsorsRepository.findSponsorsByUser(member.getId(),pageable);
			}
			//get contacts details by user id
			
			//List<Events> events = this.eventRepository.findEventsByUser(user.getId());
			//add all contacts into model
			m.addAttribute("sponsor",sponsors);
			m.addAttribute("currentPage",page);
			m.addAttribute("totalPages",sponsors.getTotalPages());
			
			//return this view
			return "member/view_sponsor";
			
		}catch(Exception e)
		{
			System.out.println("ERROR" + e.getMessage());
			return "redirect:/member/index";
		}	
	}
	
	@GetMapping("/delete-sponsor/{sponsor_id}")
	public String deleteSponsor(@PathVariable("sponsor_id")Integer sponsor_id,Principal principal,HttpSession session)
	{
		try
		{
			Sponsors sponsor = this.sponsorsRepository.findById(sponsor_id).get();
			//get current login user name
			String userName = principal.getName();

			//from username get user details
			Member member = this.memberRepository.getUserByName(userName);
		
			if(member.getRole().equals("ROLE_MEMBER") && member.getId()!= sponsor.getMember().getId())
			{
				session.setAttribute("message", new Message("You are not authorized to delete this sponsor record","danger"));
				return "redirect:/member/show-sponsor/0";
				
			}
			//if login user equals contact user id 	then contact is valid
			
				this.sponsorsRepository.deleteById(sponsor.getSponsor_id());
				//set message into session through Message class object
				session.setAttribute("message", new Message("Sponsor record deleted successfully","success"));

		  }catch(Exception e)
		{
			//if any exception occur
			System.out.println(e);
			//set error message on session
			session.setAttribute("message", new Message("something went wrong..try again!!","danger"));
			
		}
		//redirect url to show_contact template when this url fired.
		
		return "redirect:/member/show-sponsor/0";
	}
	
	//open update form handler
			@PostMapping("/update-sponsor/{sid}")
			public String updateSponsorForm(@PathVariable("sid") Integer sid,Model m)
			{
				m.addAttribute("title","Update Sponsor");
				Sponsors sponsor = this.sponsorsRepository.findById(sid).get();
				m.addAttribute("spon",sponsor);
				return "member/updatesponsor";
			}
			
			//update contact action handler
			@PostMapping("/process-sponsor-update")
			public String updateSponsorHandler(@ModelAttribute Sponsors sponsor,@RequestParam("sponsor_image") MultipartFile file ,Model m,HttpSession session,Principal principal)
			{
				try {
					
					//old contact details through id
					Sponsors oldSponsor = this.sponsorsRepository.findById(sponsor.getSponsor_id()).get();
					
					if(!file.isEmpty())
					{
						//if new file is not empty
						/*
						//delete old photo
						//get path of file
						File deleteFile = new ClassPathResource("static/image/sponsor").getFile();
						//get old image of contact 
						File file1 = new File(deleteFile,oldSponsor.getImage());
						//remove that file
						file1.delete();
						*/
						//update new photo
						//get location where you have to save the file.
						//classpathresources first automatically get path upto static
						
						File saveFile = new ClassPathResource("static/image/sponsor").getFile();
						
						//get absolute path of above file add separator and add file name
						Path path =  Paths.get(saveFile.getAbsolutePath()+ File.separator+ file.getOriginalFilename());
				

						//copy that file get input stream byte data copy from path 
						Files.copy(file.getInputStream(),path,StandardCopyOption.REPLACE_EXISTING);
					
						//set new image into contact
						sponsor.setImage(file.getOriginalFilename());
						
					}
					else
					{
						//set old image 
						sponsor.setImage(oldSponsor.getImage());
					}
					
					//get current user
					Member member = this.memberRepository.getUserByName(principal.getName());
					//set active user for this contact 
					sponsor.setMember(member);
					//update contact
					this.sponsorsRepository.save(sponsor);
					
					//set message on session
					session.setAttribute("message", new Message("Sponsor record updated successfully!!","success"));
				}catch(Exception e)
				{
					//set error message on session
					session.setAttribute("message", new Message("Something went wrong..","danger"));

					e.printStackTrace();
				}
				//reidrect into contact details  page
				return "redirect:/member/show-sponsor/0";
			}

		
			
			
	@GetMapping("/category")
	public String openAddCategoryForm(Model model)
	{
		model.addAttribute("title","Add New Category");
		model.addAttribute("category",new Category());
		return "member/addcategory";
	}
	
	@PostMapping("/process-category")
	public String processCategory(@ModelAttribute Category category,Principal principal,@RequestParam("cat_name") String cat_name,@RequestParam("category_image") MultipartFile file,HttpSession session)
	{
		try
		{
			category.setCategory(cat_name);
			String name = principal.getName();
		
			Member member = this.memberRepository.getUserByName(name);
			category.setMember(member);
			
			//processing and uploading file..
			if(file.isEmpty())
			{
				//if the file is empty then try message
				System.out.println("file is empty");
				//default image set
				category.setImage("banner.jpg");
			}
			else
			{
				//file add into folder and update name to contact
				//get file name from form and set into contact class
				
				category.setImage(file.getOriginalFilename());
				
				//get location where you have to save the file.
				//classpathresources first automatically get path upto static
				File saveFile = new ClassPathResource("static/image/category").getFile();
				
				//get absolute path of above file add separator and add file name
				Path path =  Paths.get(saveFile.getAbsolutePath()+ File.separator+ file.getOriginalFilename());
				
				//copy that file get input stream byte data copy from path 
				Files.copy(file.getInputStream(),path,StandardCopyOption.REPLACE_EXISTING);
					
			    System.out.println("file uploaded");
			}
				
			
			this.categoryRepository.save(category);
			//file uploading
			
		   //System.out.println("DATA" + category);
			//System.out.println("Added to database");
			
			//message success sent
			
			session.setAttribute("message", new Message("New category record add successfully ! Add More..","success"));
			
		}catch(Exception e)
		{
			System.out.println("ERROR" + e.getMessage());
			//error message sent
			session.setAttribute("message", new Message("Something went wrong! Try Again...","danger"));
		}
		return "member/addcategory";
	}
	
	@GetMapping("/show-category/{page}")
	public String showCategory(@PathVariable("page") Integer page,Model m,Principal principal,HttpSession session)
	{
		try {
			m.addAttribute("title","View Category Record");
			
			//contact list send
			//get current active user name
			String userName = principal.getName();
			//get user details from user name
			Member member = this.memberRepository.getUserByName(userName);
			
			//create an object of pageable with current page no and no of data per page
			Pageable  pageable =  PageRequest.of(page, 5);
			
			Page<Category> category;
			//get contacts details by user id
			if(member.getRole().equals("ROLE_ADMIN"))
			{
				category = this.categoryRepository.findAll(pageable);
			}
			else
			{
				category = this.categoryRepository.findCategoryByUser(member.getId(),pageable);
			}
			 
			
			//List<Events> events = this.eventRepository.findEventsByUser(user.getId());
			//add all contacts into model
			m.addAttribute("category",category);
			m.addAttribute("currentPage",page);
			m.addAttribute("totalPages",category.getTotalPages());
			return "member/view_category";
			
		}catch(Exception e)
		{
			System.out.println(e);
			return "redirect:/member/index";
		}
		
	}
	
	@GetMapping("/delete-category/{cat_id}")
	public String deleteCategory(@PathVariable("cat_id")Integer cat_id,Principal principal,HttpSession session)
	{
		try
		{
			
			Category category = this.categoryRepository.findById(cat_id).get();
			
			//check valid contact or not
			
			//get current login user name
			String userName = principal.getName();

			//from username get user details
			Member member = this.memberRepository.getUserByName(userName);
		
			if(member.getRole().equals("ROLE_MEMBER") && member.getId()!= category.getMember().getId())
			{
				session.setAttribute("message", new Message("You are not authorized to delete this category record","danger"));
				return "redirect:/member/show-category/0";
				
			}
				this.categoryRepository.deleteById(category.getCat_id());
				
				//set message into session through Message class object
				session.setAttribute("message", new Message("Category record deleted successfully","success"));

		  }catch(Exception e)
		{
			//if any exception occur
			System.out.println(e);
			//set error message on session
			session.setAttribute("message", new Message("Something went wrong..","danger"));
			
		}		
		return "redirect:/member/show-category/0";
	}
	
	//open update form handler
		@PostMapping("/update-category/{cid}")
		public String updateCategoryForm(@PathVariable("cid") Integer cid,Model m)
		{
			m.addAttribute("title","Update Category");
			Category category = this.categoryRepository.findById(cid).get();
			m.addAttribute("cat",category);
			return "member/updatecategory";
		}
		
		//update contact action handler
		@PostMapping("/process-category-update")
		public String updateCategoryHandler(@ModelAttribute Category category,@RequestParam("category_image") MultipartFile file ,@RequestParam("cat_name") String cat_name,Model m,HttpSession session,Principal principal)
		{
			try {
				
				//old contact details through id
				Category oldCategory = this.categoryRepository.findById(category.getCat_id()).get();
				category.setCategory(cat_name);
				
				if(!file.isEmpty())
				{
					//if new file is not empty
					
					//delete old photo
					/*
					//get path of file
					File deleteFile = new ClassPathResource("static/image/category").getFile();
					//get old image of contact 
					File file1 = new File(deleteFile,oldCategory.getImage());
					//remove that file
					file1.delete();
					*/
					
					//update new photo
					//get location where you have to save the file.
					//classpathresources first automatically get path upto static
					
					File saveFile = new ClassPathResource("static/image/category").getFile();
					
					//get absolute path of above file add separator and add file name
					Path path =  Paths.get(saveFile.getAbsolutePath()+ File.separator+ file.getOriginalFilename());
			

					//copy that file get input stream byte data copy from path 
					Files.copy(file.getInputStream(),path,StandardCopyOption.REPLACE_EXISTING);
				
					//set new image into contact
					category.setImage(file.getOriginalFilename());
					
				}
				else
				{
					//set old image 
					category.setImage(oldCategory.getImage());
				}
				
				//get current user
				Member member = this.memberRepository.getUserByName(principal.getName());
				//set active user for this contact 
				category.setMember(member);
				//update contact
				this.categoryRepository.save(category);
				
				//set message on session
				session.setAttribute("message", new Message("Category record updated successfully!!","success"));
			}catch(Exception e)
			{
				//set error message on session
				session.setAttribute("message", new Message("Something went wrong..","danger"));

				e.printStackTrace();
			}
			//reidrect into contact details  page
			return "redirect:/member/show-category/0";
		}

	
	@GetMapping("/add-event")
	public String openAddEventForm(Model model)
	{
		model.addAttribute("title","Create New Event");
		model.addAttribute("event",new Events());
		
		//get all category
		List<Category> category = this.categoryRepository.findAll();
		
		 List<String> cat_options = new ArrayList<String>();
		 
		 for(int i=0;i<category.size();i++)
		 cat_options.add(category.get(i).getCategory());
		 
		    
		 model.addAttribute("cat_options", cat_options);
		 
		//get all sponsors
			List<Sponsors> sponsors = this.sponsorsRepository.findAll();
			
			 List<String> spon_options = new ArrayList<String>();
			 
			 for(int i=0;i<sponsors.size();i++)
			 spon_options.add(sponsors.get(i).getSponsor());
			 
			    
			 model.addAttribute("spon_options", spon_options);
		
		
		return "member/create_event";
	}
	
	@PostMapping("/process-event")
	public String processEvent(@ModelAttribute Events event,@RequestParam("event_image") MultipartFile file,Principal principal,HttpSession session)
	{
		try
		{
			//save contact details into database
			String name = principal.getName();
			Member member = this.memberRepository.getUserByName(name);
			
			//set current active user in contact table
			event.setMember(member);			//set updated contacts add into user
			
			//processing and uploading file..
			if(file.isEmpty())
			{
				//if the file is empty then try message
				System.out.println("file is empty");
				//default image set
				event.setImage("banner.jpg");
			}
			else
			{
				//file add into folder and update name to contact
				//get file name from form and set into contact class
				event.setImage(file.getOriginalFilename());
				
				//get location where you have to save the file.
				//classpathresources first automatically get path upto static
				File saveFile = new ClassPathResource("static/image/events").getFile();
				
				//get absolute path of above file add separator and add file name
				Path path =  Paths.get(saveFile.getAbsolutePath()+ File.separator+ file.getOriginalFilename());
				
				//copy that file get input stream byte data copy from path 
				Files.copy(file.getInputStream(),path,StandardCopyOption.REPLACE_EXISTING);
					
			    System.out.println("file uploaded");
			}
			member.getEvents().add(event);
			
			
			//file uploading
			
			this.memberRepository.save(member);
			
		 //  System.out.println("DATA" + event);
			//System.out.println("Added to database");
			
			//message success sent
			
			session.setAttribute("message", new Message("New Event created successfully!! Add More..","success"));
			
		}catch(Exception e)
		{
			System.out.println("ERROR" + e.getMessage());
			//error message sent
			session.setAttribute("message", new Message("Something went wrong! Try Again...","danger"));
		}
		return "redirect:/member/add-event";
	}
	
	
	@GetMapping("/show-events/{page}")
	public String showEvents(@PathVariable("page") Integer page,Model m,Principal principal,HttpSession session)
	{
		try
		{
			m.addAttribute("title","View Events Record");
			
			//contact list send
			//get current active user name
			String userName = principal.getName();
			//get user details from user name
			Member member = this.memberRepository.getUserByName(userName);
			
			//create an object of pageable with current page no and no of data per page
			Pageable  pageable =  PageRequest.of(page, 5);
			
			Page<Events> events;
			if(member.getRole().equals("ROLE_ADMIN"))
			{
				events = this.eventRepository.findAll(pageable);
			}
			else
			{
				events = this.eventRepository.findEventsByUser(member.getId(),pageable);
			}
			
			//get contacts details by user id
		
			
			//List<Events> events = this.eventRepository.findEventsByUser(user.getId());
			//add all contacts into model
			m.addAttribute("events",events);
			m.addAttribute("currentPage",page);
			m.addAttribute("totalPages",events.getTotalPages());
			
		//	System.out.println(events.getTotalPages());
			
			//return this view
			return "member/view_events";
		}catch(Exception e)
		{
			System.out.println(e);
			return "redirect:/member/index";
		}	
	}
	

	@GetMapping("/delete-event/{event_id}")
	public String deleteEvent(@PathVariable("event_id")Integer event_id,Principal principal,HttpSession session)
	{
		try
		{
			
			Events event = this.eventRepository.findById(event_id).get();
			
			//check valid contact or not
			
			//get current login user name
			String userName = principal.getName();

			//from username get user details
			Member member = this.memberRepository.getUserByName(userName);
		
			if(member.getRole().equals("ROLE_MEMBER") && member.getId()!= event.getMember().getId())
			{
				session.setAttribute("message", new Message("You are not authorized to delete this event record","danger"));
				return "redirect:/member/show-events/0";
				
			}
			
			if(member.getRole().equals("ROLE_ADMIN"))
			{
				member = event.getMember();
			}
				//get contacts of active and remove current selected contact
				member.getEvents().remove(event);
				
				this.bookRepository.deleteById(event_id);
				
				//then save user after remove its contact
				this.memberRepository.save(member);
				
				//set message into session through Message class object
				session.setAttribute("message", new Message("Event record deleted successfully","success"));

		  }catch(Exception e)
		{
			//if any exception occur
			System.out.println(e);
			//set error message on session
			session.setAttribute("message", new Message("something went wrong..","danger"));
			
		}
			
		return "redirect:/member/show-events/0";
	}
	
	//open update form handler
	@PostMapping("/update-event/{eid}")
	public String updateEventForm(@PathVariable("eid") Integer eid,Model m)
	{
		m.addAttribute("title","Update Event");
		Events  event = this.eventRepository.findById(eid).get();
		//get all category
		List<Category> category = this.categoryRepository.findAll();
		
		 List<String> cat_options = new ArrayList<String>();
		 
		 for(int i=0;i<category.size();i++)
		 cat_options.add(category.get(i).getCategory());
		 
		    
		 m.addAttribute("cat_options", cat_options);
		 
		//get all sponsors
			List<Sponsors> sponsors = this.sponsorsRepository.findAll();
			
			 List<String> spon_options = new ArrayList<String>();
			 
			 for(int i=0;i<sponsors.size();i++)
			 spon_options.add(sponsors.get(i).getSponsor());
			 
			    
			 m.addAttribute("spon_options", spon_options);
		
		m.addAttribute("event",event);
		return "member/updateevent";
	}
	
	//update contact action handler
	@PostMapping("/process-event-update")
	public String updateEventHandler(@ModelAttribute Events event,@RequestParam("event_image") MultipartFile file ,Model m,HttpSession session,Principal principal)
	{
		try {
			
			//old event details through id
			Events oldEvent = this.eventRepository.findById(event.getEvent_id()).get();
			
			if(!file.isEmpty())
			{
				//if new file is not empty
				
				//delete old photo
				/*
				//get path of file
				File deleteFile = new ClassPathResource("static/image/events").getFile();
				//get old image of contact 
				File file1 = new File(deleteFile,oldEvent.getImage());
				//remove that file
				file1.delete();
				*/
				//update new photo
				//get location where you have to save the file.
				//classpathresources first automatically get path upto static
				
				File saveFile = new ClassPathResource("static/image/events").getFile();
				
				//get absolute path of above file add separator and add file name
				Path path =  Paths.get(saveFile.getAbsolutePath()+ File.separator+ file.getOriginalFilename());
		

				//copy that file get input stream byte data copy from path 
				Files.copy(file.getInputStream(),path,StandardCopyOption.REPLACE_EXISTING);
			
				//set new image into contact
				event.setImage(file.getOriginalFilename());
				
			}
			else
			{
				//set old image 
				event.setImage(oldEvent.getImage());
			}
			
			//get current user
			Member member = this.memberRepository.getUserByName(principal.getName());
			//set active user for this contact 
			event.setMember(member);
			//update contact
			this.eventRepository.save(event);
			
			//set message on session
			session.setAttribute("message", new Message("Event record updated successfully!!","success"));
		}catch(Exception e)
		{
			//set error message on session
			session.setAttribute("message", new Message("Something went wrong..","danger"));

			e.printStackTrace();
		}
		//reidrect into contact details  page
		return "redirect:/member/show-events/0";
	}

	
	@GetMapping("/show-booking/{eid}")
	public String showBooking(@PathVariable("eid") Integer eid,Model m,Principal principal,HttpSession session)
	{
		m.addAttribute("title","View Event Booking");
		
		try
		{
			//get current login user name
			String userName = principal.getName();

			//from username get user details
			Member member = this.memberRepository.getUserByName(userName);
			Events event = this.eventRepository.findById(eid).get();
			
			if(member.getRole().equals("ROLE_MEMBER") && member.getId()!= event.getMember().getId())
			{
				session.setAttribute("message", new Message("You are not authorized to show this event booking","danger"));
				return "redirect:/member/show-events/0";
				
			}
			
				//get contacts details by user id
				List<Integer> bookuid = this.bookRepository.findDistinctUserIdsByEventId(eid);
				   
				if(bookuid.size()==0)
				{
					session.setAttribute("message", new Message("No booking exist of this event","danger"));
					return "redirect:/member/show-events/0";
				}
				
				List<BookingEvent> bookevt =  this.bookRepository.findRecordsByUids(bookuid);
				List<NormalUser> bookuser = new ArrayList<>();
				for(Integer uid : bookuid)
				{
					NormalUser user = this.normalUserRepository.getUserById(uid);
					bookuser.add(user);
				}
				m.addAttribute("event",event);
				m.addAttribute("bookuser",bookuser);
				m.addAttribute("bookevt",bookevt);
				
				//return this view
				return "member/view_booking";
			
		}catch(Exception e)
		{
			System.out.println(e);
			return "redirect:/member/show-events/0";
		}
				
	}
	
	
	@GetMapping("/approved-booking/{eid}/{uid}")
	public String approvedBooking(@PathVariable("eid") Integer eid,@PathVariable("uid") Integer uid,Model m,Principal principal,HttpSession session)
	{
		try
		{	
			BookingEvent bookevent = this.bookRepository.findEventById(eid, uid);
			bookevent.setStatus("Approved");
			this.bookRepository.save(bookevent);
			
			session.setAttribute("message", new Message("Booking request approved successfully!!","success"));
			return "redirect:/member/show-booking/{eid}";			
			
		}catch(Exception e)
		{
			System.out.println(e);
			return "redirect:/member/show-events/0";
		}
				
	}
	
	@GetMapping("/reject-booking/{eid}/{uid}")
	public String rejectBooking(@PathVariable("eid") Integer eid,@PathVariable("uid") Integer uid,Model m,Principal principal,HttpSession session)
	{
		try
		{	
			BookingEvent bookevent = this.bookRepository.findEventById(eid, uid);
			bookevent.setStatus("Rejected");
			this.bookRepository.save(bookevent);
			
			session.setAttribute("message", new Message("Booking rejected successfully you can delete now if you want!!","success"));
			return "redirect:/member/show-booking/{eid}";			
			
		}catch(Exception e)
		{
			System.out.println(e);
			return "redirect:/member/show-events/0";
		}
				
	}
	
	
	@GetMapping("/delete-booking/{eid}/{uid}")
	public String deleteBooking(@PathVariable("eid") Integer eid,@PathVariable("uid") Integer uid,Model m,Principal principal,HttpSession session)
	{
		try
		{	
			this.bookRepository.deleteByEventIdAndUserId(eid, uid);
			session.setAttribute("message", new Message("Booking record deleted successfully!!","success"));	        
			return "redirect:/member/show-events/0";			
			
		}catch(Exception e)
		{
			System.out.println(e);
			return "redirect:/member/show-events/0";
		}
				
	}
	
	
	@GetMapping("/add-coordinator")
	public String openAddCoordinatorForm(Model model,Principal principal)
	{
		model.addAttribute("title","Add Event Coordinator");
		String userName = principal.getName();
		//get user details from user name
		Member member = this.memberRepository.getUserByName(userName);
			
		List<Events> events;
		//get all category
		if(member.getRole().equals("ROLE_ADMIN"))
			events = this.eventRepository.findAll();
		else
			 events = this.eventRepository.findEventsByUser(member.getId());
		
		 List<String> event_options = new ArrayList<String>();
		 
		 for(int i=0;i<events.size();i++)
		 event_options.add(events.get(i).getTitle());
		 
		    
		 model.addAttribute("event_options", event_options);
		 	
		
		return "member/add_coordinator";
	}
	
	
	@PostMapping("/process-coordinator")
	public String processCoordinator(@ModelAttribute Coordinator coordinator,@RequestParam("coord_image") MultipartFile file,Principal principal,HttpSession session)
	{
		try
		{
			String name = principal.getName();
			
			Member member = this.memberRepository.getUserByName(name);
			coordinator.setMember(member);
			
			//processing and uploading file..
			if(file.isEmpty())
			{
				//if the file is empty then try message
				System.out.println("file is empty");
				//default image set
				coordinator.setImage("default.jpeg");
			}
			else
			{
				//file add into folder and update name to contact
				//get file name from form and set into contact class
				coordinator.setImage(file.getOriginalFilename());
				
				//get location where you have to save the file.
				//classpathresources first automatically get path upto static
				File saveFile = new ClassPathResource("static/image/coordinator").getFile();
				
				//get absolute path of above file add separator and add file name
				Path path =  Paths.get(saveFile.getAbsolutePath()+ File.separator+ file.getOriginalFilename());
				
				//copy that file get input stream byte data copy from path 
				Files.copy(file.getInputStream(),path,StandardCopyOption.REPLACE_EXISTING);
					
			    System.out.println("file uploaded");
			}
			
			//file uploading
			
			this.coordinatorRepository.save(coordinator);
			
		   	
			//message success sent
			
			session.setAttribute("message", new Message("Event Coordinator added successfully!! Add More..","success"));
			
		}catch(Exception e)
		{
			System.out.println("ERROR" + e.getMessage());
			//error message sent
			session.setAttribute("message", new Message("Something went wrong! Try Again...","danger"));
		}
		return "redirect:/member/add-coordinator";
	}
	
	@GetMapping("/show-coordinator/{page}")
	public String showCoordinator(@PathVariable("page") Integer page,Model m,Principal principal,HttpSession session)
	{
		try {
			m.addAttribute("title","View All Coordinator");
			String name = principal.getName();
			
			Member member = this.memberRepository.getUserByName(name);
				
			//create an object of pageable with current page no and no of data per page
			Pageable  pageable =  PageRequest.of(page, 5);
			
			Page<Coordinator> coordinator;
			
			if(member.getRole().equals("ROLE_ADMIN"))
			{
				coordinator = this.coordinatorRepository.findAll(pageable);
			}
			else
			{
				coordinator = this.coordinatorRepository.findCoordinatorByUser(member.getId(),pageable);
			}
			
			
			
			//List<Events> events = this.eventRepository.findEventsByUser(user.getId());
			//add all contacts into model
			m.addAttribute("coordinator",coordinator);
			m.addAttribute("currentPage",page);
			m.addAttribute("totalPages",coordinator.getTotalPages());
			
			//return this view
			return "member/view_coordinator";
		}catch(Exception  e)
		{
			System.out.println(e);
			return "redirect:/member/index";
		}
	
	}
	
	@GetMapping("/delete-coordinator/{coord_id}")
	public String deleteCoordinator(@PathVariable("coord_id")Integer coord_id,Principal principal,HttpSession session)
	{
		try
		{
			
			Coordinator coordinator = this.coordinatorRepository.findById(coord_id).get();
			
			//check valid contact or not
			
			//get current login user name
			String userName = principal.getName();

			//from username get user details
			Member member = this.memberRepository.getUserByName(userName);
			if(member.getRole().equals("ROLE_MEMBER") && member.getId()!= coordinator.getMember().getId())
			{
				session.setAttribute("message", new Message("You are not authorized to delete this event record","danger"));
				return "redirect:/member/show-coordinator/0";
				
			}
				this.coordinatorRepository.deleteById(coordinator.getCoord_id());
				
				//set message into session through Message class object
				session.setAttribute("message", new Message("Coordinator record deleted successfully","success"));

		  }catch(Exception e)
		{
			//if any exception occur
			System.out.println(e);
			//set error message on session
			session.setAttribute("message", new Message("something went wrong..","danger"));
			
		}
		//redirect url to show_contact template when this url fired.
		
		return "redirect:/member/show-coordinator/0";
	}
	
	
	//open update form handler
			@PostMapping("/update-coordinator/{cid}")
			public String updateCoordinatorForm(@PathVariable("cid") Integer cid,Model m,Principal principal)
			{
				m.addAttribute("title","Update Coordinator Record");
				Coordinator coordinator = this.coordinatorRepository.findById(cid).get();
				m.addAttribute("coord",coordinator);
				

				//get current login user name
				String userName = principal.getName();

				//from username get user details
				Member member = this.memberRepository.getUserByName(userName);
				
				List<Events> events;
				if(member.getRole().equals("ROLE_ADMIN"))
					events = this.eventRepository.findAll();
				else
					 events = this.eventRepository.findEventsByUser(member.getId());
					
				 List<String> event_options = new ArrayList<String>();
				 
				 for(int i=0;i<events.size();i++)
				 event_options.add(events.get(i).getTitle());
				 
				    
				 m.addAttribute("event_options", event_options);
				 	
				return "member/updatecoordinator";
			}
			
			//update contact action handler
			@PostMapping("/process-coordinator-update")
			public String updateCoordinatorHandler(@ModelAttribute Coordinator coordinator,@RequestParam("coord_image") MultipartFile file ,Model m,HttpSession session,Principal principal)
			{
				try {
					
					//old contact details through id
					Coordinator oldCoord = this.coordinatorRepository.findById(coordinator.getCoord_id()).get();
					
					
					if(!file.isEmpty())
					{
						//if new file is not empty
					
						//update new photo
						//get location where you have to save the file.
						//classpathresources first automatically get path upto static
						
						File saveFile = new ClassPathResource("static/image/coordinator").getFile();
						
						//get absolute path of above file add separator and add file name
						Path path =  Paths.get(saveFile.getAbsolutePath()+ File.separator+ file.getOriginalFilename());
				

						//copy that file get input stream byte data copy from path 
						Files.copy(file.getInputStream(),path,StandardCopyOption.REPLACE_EXISTING);
					
						//set new image into contact
						coordinator.setImage(file.getOriginalFilename());
						
					}
					else
					{
						//set old image 
						coordinator.setImage(oldCoord.getImage());
					}
					
					//get current user
					Member member = this.memberRepository.getUserByName(principal.getName());
					//set active user for this contact 
					coordinator.setMember(member);
					//update contact
					this.coordinatorRepository.save(coordinator);
					
					//set message on session
					session.setAttribute("message", new Message("Coordinator record updated successfully!!","success"));
				}catch(Exception e)
				{
					//set error message on session
					session.setAttribute("message", new Message("Something went wrong..try again!!","danger"));

					e.printStackTrace();
				}
				//reidrect into contact details  page
				return "redirect:/member/show-coordinator/0";
			}

		
			
			
	//show profile of active user
		@GetMapping("/profile")
		public String yourProfile(Model  model)
		{
			model.addAttribute("title","Profile");
			return "member/Profile";
		}
		
		//update profile of active user
		@PostMapping("/update-profile")
		public String updateProfile(Model  model)
		{
			model.addAttribute("title","Update Profile");
			return "member/update_profile";
		}
		
		@PostMapping("/process-update-profile")
		public String processUpdateProfile(@ModelAttribute Member member,@RequestParam("imageUrl") MultipartFile file ,Model m,HttpSession session,Principal principal)
		{
			try {
				
				//old contact details through id
				Member oldUser  = this.memberRepository.findById(member.getId()).get();
				
				if(!file.isEmpty())
				{
					//if new file is not empty
					
					//delete old photo
					//get path of file
					File deleteFile = new ClassPathResource("static/image").getFile();
					//get old image of contact 
					File file1 = new File(deleteFile,oldUser.getImage());
					//remove that file
					file1.delete();
					
					//update new photo
					//get location where you have to save the file.
					//classpathresources first automatically get path upto static
					
					File saveFile = new ClassPathResource("static/image").getFile();
					
					//get absolute path of above file add separator and add file name
					Path path =  Paths.get(saveFile.getAbsolutePath()+ File.separator+ file.getOriginalFilename());
			

					//copy that file get input stream byte data copy from path 
					Files.copy(file.getInputStream(),path,StandardCopyOption.REPLACE_EXISTING);
				
					//set new image into contact
					member.setImage(file.getOriginalFilename());
					
				}
				else
				{
					//set old image 
					member.setImage(oldUser.getImage());
				}
				
				
				if(oldUser.getRole().equals("ROLE_MEMBER"))
				member.setRole("ROLE_MEMBER");
				else
				member.setRole("ROLE_ADMIN");
				
				member.setPassword(oldUser.getPassword());
				List<Events> events = this.eventRepository.findEventsByUser(oldUser.getId());
				member.setEvents(events);
				member.setEnabled(true);
				//update contact
				this.memberRepository.save(member);
				
				//set message on session
				session.setAttribute("message", new Message("Your Profile updated successfully!!","success"));
			}catch(Exception e)
			{
				//set error message on session
				session.setAttribute("message", new Message("Something went wrong..try again!!","danger"));

				e.printStackTrace();
			}
			
			return "member/Profile";
		}

		@GetMapping("/show-users/{page}")
		public String showUsers(@PathVariable("page") Integer page,Model m,HttpSession session)
		{
			try
			{
				m.addAttribute("title","View Registered Users");
				
				//create an object of pageable with current page no and no of data per page
				Pageable  pageable =  PageRequest.of(page, 5);
				
				Page<Member> members = this.memberRepository.findUser(pageable);
				
				//List<Events> events = this.eventRepository.findEventsByUser(user.getId());
				//add all contacts into model
				m.addAttribute("registered",members);
				m.addAttribute("currentPage",page);
				m.addAttribute("totalPages",members.getTotalPages());
				
				//return this view
				return "member/view_registered";
			}catch(Exception e)
			{
				System.out.println(e);
				return "redirect:/member/index";
			}
			
		}
		
		@GetMapping("/accept-user/{uid}")
		public String acceptUser(@PathVariable("uid") Integer uid,Model m,HttpSession session)
		{
			Member member = this.memberRepository.findById(uid).get();
			
			//generate random password
			 String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		        
		        // Create a Random object
		        random = new Random();
		        
		        // Initialize a char array to hold the password characters
		        char[] password = new char[6];
		        
		        // Generate each character of the password
		        for (int i = 0; i < 6; i++) {
		            // Generate a random index within the range of characters
		            int randomIndex = random.nextInt(characters.length());
		            
		            // Get the character at the random index
		            password[i] = characters.charAt(randomIndex);
		        }
		        
		        String newpassword = new String(password);
			String subject = "Random Password From NITRR Campus Event System for login";
			String message = ""
					+"<div style='border:1px solid #e2e2e2; padding:20px'>"
					+"<h1>"
					+"Password  is "
					+"<b>"+newpassword
					+"</n>"
					+"</h1>"
					+"</div>";
			
		    String to = member.getEmail();
		    
			boolean flag = this.emailServices.sendEmail(subject,message,to);
			
			if(flag)
			{
				member.setPassword(this.bCryptPasswordEncoder.encode(newpassword));
				member.setRole("ROLE_MEMBER");
				member.setEnabled(true);
				this.memberRepository.save(member);
				
				session.setAttribute("message", new Message("User verified!! Random password sent to requested email..","success"));
				return "redirect:/member/show-users/0";
			}
			else
			{
				//error
				session.setAttribute("message", new Message("Something went wrong..verification error","danger"));
				return "redirect:/member/show-users/0";
			}
		}
		
		
		@GetMapping("/delete-user/{uid}")
		public String deleteUser(@PathVariable("uid")Integer uid,HttpSession session)
		{
			try
			{
				
				Member member = this.memberRepository.findById(uid).get();
					this.memberRepository.deleteById(member.getId());
					
					//set message into session through Message class object
					session.setAttribute("message", new Message("User request deleted successfully","success"));
				
			  }catch(Exception e)
			{
				//if any exception occur
				System.out.println(e);
				//set error message on session
				session.setAttribute("message", new Message("something went wrong..","danger"));
				
			}
			
			return "redirect:/member/show-users/0";
		}
		
		@GetMapping("/show-feedback/{page}")
		public String showFeedback(@PathVariable("page") Integer page,Model m,HttpSession session)
		{
			try {
				m.addAttribute("title","View Feedback Record");
				
				
				//create an object of pageable with current page no and no of data per page
				Pageable  pageable =  PageRequest.of(page, 5);
				
				Page<UserFeedback> feedback = this.feedbackRepository.findAll(pageable);
				
				//List<Events> events = this.eventRepository.findEventsByUser(user.getId());
				//add all contacts into model
				m.addAttribute("feedback",feedback);
				m.addAttribute("currentPage",page);
				m.addAttribute("totalPages",feedback.getTotalPages());
				
				//return this view
				return "member/view_feedback";
			}catch(Exception  e)
			{
				System.out.println(e);
				return "redirect:/member/index";
			}
		
		}
		
		
		
		@GetMapping("/delete-feedback/{cid}")
		public String deleteFeedback(@PathVariable("cid")Integer cid,HttpSession session)
		{
			try
			{
				
				UserFeedback feedback = this.feedbackRepository.findById(cid).get();
					this.feedbackRepository.deleteById(feedback.getId());
					
					//set message into session through Message class object
					session.setAttribute("message", new Message("Feedback record deleted successfully","success"));
				
			  }catch(Exception e)
			{
				//if any exception occur
				System.out.println(e);
				//set error message on session
				session.setAttribute("message", new Message("something went wrong..try again!!","danger"));
				
			}
			
			return "redirect:/member/show-feedback/0";
		}
		
		@GetMapping("/show-contact/{page}")
		public String showContact(@PathVariable("page") Integer page,Model m,HttpSession session)
		{
			try {
				m.addAttribute("title","View Contacts Record");
				
				
				//create an object of pageable with current page no and no of data per page
				Pageable  pageable =  PageRequest.of(page, 5);
				
				Page<Contact> contact = this.contactRepository.findAll(pageable);
				
				//List<Events> events = this.eventRepository.findEventsByUser(user.getId());
				//add all contacts into model
				m.addAttribute("contact",contact);
				m.addAttribute("currentPage",page);
				m.addAttribute("totalPages",contact.getTotalPages());
				
				//return this view
				return "member/view_contacts";
			}catch(Exception  e)
			{
				System.out.println(e);
				return "redirect:/member/index";
			}
		
		}
		
		
		@GetMapping("/viewcontact/{cid}")
		public String viewContact(@PathVariable("cid") Integer cid,Model m)
		{
			m.addAttribute("title","View Contact Details");
			Contact contact = this.contactRepository.findById(cid).get();
		 
			m.addAttribute("contact",contact);
			return "member/showcontact";
		}
		
		@GetMapping("/delete-contact/{cid}")
		public String deleteContact(@PathVariable("cid")Integer cid,HttpSession session)
		{
			try
			{
				
				Contact contact = this.contactRepository.findById(cid).get();
					this.contactRepository.deleteById(contact.getId());
					
					//set message into session through Message class object
					session.setAttribute("message", new Message("Contact record deleted successfully","success"));
				
			  }catch(Exception e)
			{
				//if any exception occur
				System.out.println(e);
				//set error message on session
				session.setAttribute("message", new Message("something went wrong..try again!!","danger"));
				
			}
			
			return "redirect:/member/show-contact/0";
		}
		
		//open setting handler
		@GetMapping("/settings")
		public String openSetting(Model m)
		{
			m.addAttribute("title","Change Password");
			return "member/setting";
		}
		
		//change password handler
		@PostMapping("/change-password")
		public String changePassword(@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword,Principal principal,HttpSession session)
		{
			//System.out.println("OLD Password " + oldPassword);
			//System.out.println("New Password " + newPassword);
			String userName = principal.getName();
			Member currentUser = this.memberRepository.getUserByName(userName);
			
			if(this.bCryptPasswordEncoder.matches(oldPassword, currentUser.getPassword()))
			{
				//if old password given by  user and old password in database matches
				//change password
				//set new password in coded form
				currentUser.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
				
				//save into database
				this.memberRepository.save(currentUser);
				
				session.setAttribute("message", new Message("Your password successfully changed..","success"));
				return "redirect:/member/settings";
			}
			else
			{
				//error
				session.setAttribute("message", new Message("Please enter correct old password","danger"));
				return "redirect:/member/settings";
			}
			
		}
		
		@GetMapping("/logout")
		public String logOut(HttpSession session)
		{
			 session.removeAttribute("login_member"); // Remove specific session attribute
		     return "redirect:/signin?logout";
		}
		
}
