package com.events.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="UserFeedback")
public class UserFeedback {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
  private int id;
  private int userid;
  private String feedback;
  
  
public UserFeedback() {
	super();
	// TODO Auto-generated constructor stub
}
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}

public int getUserid() {
	return userid;
}
public void setUserid(int userid) {
	this.userid = userid;
}
public String getFeedback() {
	return feedback;
}
public void setFeedback(String feedback) {
	this.feedback = feedback;
}
@Override
public String toString() {
	return "EventFeedback [id=" + id + ", userid=" + userid + ", feedback=" + feedback + "]";
}

  
}
