package com.events.helper;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpSession;

@Component
public class sessionHelper {
  public void removeMessageFromSession()
  {
	  try
	  {
		  HttpSession session = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest().getSession();
		  session.removeAttribute("message");
		  
	  }catch(Exception e)
	  {
		  e.printStackTrace();
	  }
  }
  
  public void removeProfileUpdateFromSession()
  {
	  try
	  {
		  HttpSession session = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest().getSession();
		  session.removeAttribute("profileupdate");
		  
	  }catch(Exception e)
	  {
		  e.printStackTrace();
	  }
  }
  
  public void removeChangePassFromSession()
  {
	  try
	  {
		  HttpSession session = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest().getSession();
		  session.removeAttribute("changepass");
		  
	  }catch(Exception e)
	  {
		  e.printStackTrace();
	  }
  }
  
  public void removeBookingMessageFromSession()
  {
	  try
	  {
		  HttpSession session = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest().getSession();
		  session.removeAttribute("bookingmessage");
		  
	  }catch(Exception e)
	  {
		  e.printStackTrace();
	  }
  }
}
