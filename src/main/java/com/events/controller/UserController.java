package com.events.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.events.dao.EventBookRepository;
import com.events.dao.EventRepository;
import com.events.dao.FeedbackRepository;
import com.events.dao.NormalUserRepository;
import com.events.entities.BookingEvent;
import com.events.entities.Events;
import com.events.entities.NormalUser;
import com.events.entities.UserFeedback;
import com.events.helper.Message;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class UserController {
	
	@Autowired
	private NormalUserRepository normaluserRepository;
	
	@Autowired
	private EventRepository eventRepository;
	
	@Autowired
	private EventBookRepository eventBookRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	
	@Autowired
	private FeedbackRepository feedbackrepository;

	
	@RequestMapping("/user_signup")
	public String signup(Model model)
	{
		return "user_register";
	}
	@PostMapping("/process-registration")
	public String registerUser(@Valid @ModelAttribute("user") NormalUser user,
			BindingResult result,Model model,@RequestParam("profile_image") MultipartFile file,HttpSession session)
	{
		try
		{		
			
			//processing and uploading file..
			if(file.isEmpty())
			{
				//if the file is empty then try message
				System.out.println("file is empty");
				//default image set
				user.setProfile_image("banner.jpg");
			}
			else
			{
				//file add into folder and update name to contact
				//get file name from form and set into contact class
				user.setProfile_image(file.getOriginalFilename());
				
				//get location where you have to save the file.
				//classpathresources first automatically get path upto static
				File saveFile = new ClassPathResource("static/image/normal_user").getFile();
				
				//get absolute path of above file add separator and add file name
				Path path =  Paths.get(saveFile.getAbsolutePath()+ File.separator+ file.getOriginalFilename());
				
				//copy that file get input stream byte data copy from path 
				Files.copy(file.getInputStream(),path,StandardCopyOption.REPLACE_EXISTING);
					
			    System.out.println("file uploaded");
			}
			
			user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
				
			this.normaluserRepository.save(user);
			
			model.addAttribute("normal_user",new NormalUser());
			session.setAttribute("message", new Message("Successfully Registered","alert-success"));
			return "user_register";
			
		}catch(Exception e)
		{
			e.printStackTrace();
			model.addAttribute("user",user);
			session.setAttribute("message", new Message("Something went wrong or E-mail already exist!!","alert-danger"));
			System.out.println(e.getMessage());
			return "user_register";
		}
		
	}
	
	
	@RequestMapping("/user_signin")
	public String signin(Model model)
	{
		return "user_signin";
	}
	
	@PostMapping("/donormallogin")
	public String processLogin(Model model,@RequestParam("username")String username,@RequestParam("password") String password,HttpSession session)
	{
		//password = this.bCryptPasswordEncoder.encode(password);
		//verify  email first
		//System.out.println(user);
		
		NormalUser user = this.normaluserRepository.getUserByEmail(username);
        if (user != null && bCryptPasswordEncoder.matches(password, user.getPassword())) 
        {
			session.setAttribute("login_user", user);
			
			session.removeAttribute("login_member");
			 model.addAttribute("loginuser", user);
			 
			    //get all booked event
	            List<BookingEvent> bkevt = this.normaluserRepository.findEventsByUserId(user.getId());
	            List<Events> events = new ArrayList<>();
	            
	            for(BookingEvent be : bkevt)
	            {
	            	Optional<Events> eventOpt = this.eventRepository.findEventByEventId(be.getEventid());
	                 eventOpt.ifPresent(events::add);
	            	
	            	
	                // System.out.println(eventOpt);
	            }
	            
	           // System.out.println(list);
	            model.addAttribute("bkevt",bkevt);
	            model.addAttribute("events",events);
	            
			return "redirect:/user/index";
		}
		else
		{
			return "redirect:/user_signin?error";
		}
	}
	
	@RequestMapping("/user/index")
	public String dashboard(Model model,HttpSession session)
	{
		  NormalUser user = (NormalUser)session.getAttribute("login_user");
	        if (user != null) {
	            model.addAttribute("loginuser", user);
	            
	            //get all booked event
	            List<BookingEvent> bkevt = this.normaluserRepository.findEventsByUserId(user.getId());
	            List<Events> events = new ArrayList<>();
	            
	            for(BookingEvent be : bkevt)
	            {
	            	 Optional<Events> eventOpt = eventRepository.findById(be.getEventid());
	                 eventOpt.ifPresent(events::add);
	            }
	            model.addAttribute("bkevt",bkevt);
	            model.addAttribute("events",events);
	            return "user/user_dash";
	        } else {
	            return "redirect:/user_signin";
	        }
	}
	
	@GetMapping("/event_detail/{eid}")
	public String showEvent(Model model ,@PathVariable("eid")Integer eid,HttpSession session)
	{
		try
		{
			Events  event = this.eventRepository.findById(eid).get();
			model.addAttribute("event",event);
			NormalUser user = (NormalUser)session.getAttribute("login_user");
			
			if(user==null)
			{
				session.removeAttribute("booked");
			}
			else
			{
				BookingEvent bkevt = this.eventBookRepository.findEventById(eid, user.getId());
		        if(bkevt!=null)
		        session.setAttribute("booked","yes");
		        else
		        session.removeAttribute("booked");
			}
			
			return "event_details";
		}catch(Exception  e)
		{
			System.out.println(e);
			return "redirect:/";
		}	
	}
	
	
	
	@RequestMapping("/bookevent/{eid}")
	public String bookEvent(Model model,@PathVariable("eid")Integer eid,@ModelAttribute BookingEvent  bookingEvent,HttpSession session)
	{
		  NormalUser user = (NormalUser)session.getAttribute("login_user");
	        if (user != null) {
	           //book  this event
	        	bookingEvent.setEventid(eid);
	        	bookingEvent.setUid(user.getId());
	        	bookingEvent.setStatus("Pending");
	        	
	        	BookingEvent bkevt = this.eventBookRepository.findEventById(eid, user.getId());
	        	if(bkevt==null)
	        	{
	        		
	        		this.eventBookRepository.save(bookingEvent);
	        		session.setAttribute("message", new Message("Your seat booked for this event wait for confirmation...","alert-success"));
	        		
	        	}
	        	Events  event = this.eventRepository.findById(eid).get();
	        		model.addAttribute("event",event);
	    		 session.setAttribute("booked","yes");
	    		 return "redirect:/event_detail/{eid}";
	        } else {
	            return "redirect:/user_signin";
	        }
	}
	
	
	@RequestMapping("/cancelbooking/{eid}")
	public String cancelBooking(Model model,@PathVariable("eid")Integer eid,@ModelAttribute BookingEvent  bookingEvent,HttpSession session)
	{
		  NormalUser user = (NormalUser)session.getAttribute("login_user");
	        if (user != null) {
	           //book  this event
	        	this.eventBookRepository.deleteByEventIdAndUserId(eid, user.getId());
	        	session.setAttribute("bookingmessage", new Message("Your booking cancelled for this event successfully!!","alert-success"));	        
				return  "redirect:/user/index";
	        } else {
	            return "redirect:/user_signin";
	        }
	}
	
	
	//change password handler
			@PostMapping("/user-change-password")
			public String changeUserPassword(@RequestParam("oldPassword") String oldPassword,@RequestParam("newPassword") String newPassword,Model model,HttpSession session)
			{
				//System.out.println("OLD Password " + oldPassword);
				//System.out.println("New Password " + newPassword);
				 NormalUser user = (NormalUser)session.getAttribute("login_user");
				 if (user != null) {
				//	String orgPassword = this.bCryptPasswordEncoder.encode(user.getPassword());
					
					 if(this.bCryptPasswordEncoder.matches(oldPassword,user.getPassword()))
						{
							//if old password given by  user and old password in database matches
							//change password
							//set new password in coded form
							user.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
							
							//save into database
							this.normaluserRepository.save(user);
							
							session.setAttribute("changepass", new Message("Your password is successfully changed..","alert-success"));
						}
					 else
					 {
						//error
							session.setAttribute("changepass", new Message("Please enter correct old password","alert-danger"));
					 }
					 return  "redirect:/user/index";
				 } 
				else
				{
					 return "redirect:/user_signin";
				}
				
			}
			
			
	@RequestMapping("user_feedback")
	public  String userFeedback(@ModelAttribute UserFeedback feedback,Model model,HttpSession  session)
	{
		 NormalUser user = (NormalUser)session.getAttribute("login_user");
		 if(user==null)
		 {
			 return "redirect:/user_signin"; 
		 }
		 feedback.setUserid(user.getId());
		 this.feedbackrepository.save(feedback);
		session.setAttribute("message", new Message("Thankyou for your  valuable feedback!!","alert-success"));		
		return  "redirect:/user/index";
				
	}
	
	
	@PostMapping("/user-update-profile")
	public String userUpdateProfile(@Valid @ModelAttribute NormalUser user,
			BindingResult result,Model model,@RequestParam("profile_image") MultipartFile file,HttpSession session)
	{
		 NormalUser oldUser = (NormalUser)session.getAttribute("login_user");
			
		try {
			
			 if(oldUser==null)
			 {
				 return "redirect:/user_signin"; 
			 }
			
			 if(!file.isEmpty())
			{
				//get path of file
					File deleteFile = new ClassPathResource("static/image/normal_user").getFile();
					//get old image of contact 
					File file1 = new File(deleteFile,oldUser.getProfile_image());
					//remove that file
					file1.delete();
					
					//update new photo
					//get location where you have to save the file.
					//classpathresources first automatically get path upto static
					
					File saveFile = new ClassPathResource("static/image/normal_user").getFile();
					
					//get absolute path of above file add separator and add file name
					Path path =  Paths.get(saveFile.getAbsolutePath()+ File.separator+ file.getOriginalFilename());
			

					//copy that file get input stream byte data copy from path 
					Files.copy(file.getInputStream(),path,StandardCopyOption.REPLACE_EXISTING);
				
					//set new image into contact
					oldUser.setProfile_image(file.getOriginalFilename());
			}
			
			 oldUser.setName(user.getName());
			 oldUser.setEmail(user.getEmail());
			 oldUser.setAddress(user.getAddress());
			 oldUser.setCity(user.getCity());
			 oldUser.setDob(user.getDob());
			 oldUser.setGender(user.getGender());
			 oldUser.setPhone(user.getPhone());
			 		 
			 this.normaluserRepository.save(oldUser);	
				
			session.setAttribute("profileupdate", new Message("Your profile updated successfully!!","alert-success"));
			 
			 
		}catch(Exception e){
			session.setAttribute("profileupdate", new Message("Something went wrong..","alert-danger"));

			e.printStackTrace();
		}
		 
		 return  "redirect:/user/index";
		 		
	}
		
	@RequestMapping("/user_logout")
	public String signout(Model model,HttpSession  session)
	{
		session.removeAttribute("login_user"); // Remove specific session attribute
        return "redirect:/user_signin?logout";
	}
	
}
