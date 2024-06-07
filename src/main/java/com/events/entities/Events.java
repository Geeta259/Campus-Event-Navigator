package com.events.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="Events")

public class Events {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
  private int event_id;
  private String title;
  private String category;
  @Column(length=1000)
  private String description;
  private String start_date;
  private String end_date;
  private String venue;
  private String sponsored;
  private String status;
  private String event_image;

  
  //multiple events belong to one user
  @ManyToOne
  @JsonIgnore
  //user data not serialized only  contact data will be serialized otherwise circular dependency exist
  private Member member;


public Events() {
	super();
	// TODO Auto-generated constructor stub
}


public int getEvent_id() {
	return event_id;
}


public void setEvent_id(int event_id) {
	this.event_id = event_id;
}


public String getTitle() {
	return title;
}


public void setTitle(String title) {
	this.title = title;
}


public String getCategory() {
	return category;
}


public void setCategory(String category) {
	this.category = category;
}


public String getDescription() {
	return description;
}


public void setDescription(String description) {
	this.description = description;
}


public String getStart_date() {
	return start_date;
}


public void setStart_date(String start_date) {
	this.start_date = start_date;
}


public String getEnd_date() {
	return end_date;
}


public void setEnd_date(String end_date) {
	this.end_date = end_date;
}



public String getVenue() {
	return venue;
}


public void setVenue(String venue) {
	this.venue = venue;
}


public String getSponsored() {
	return sponsored;
}


public void setSponsored(String sponsored) {
	this.sponsored = sponsored;
}


public String getStatus() {
	return status;
}


public void setStatus(String status) {
	this.status = status;
}


public String getImage() {
	return event_image;
}


public void setImage(String event_image) {
	this.event_image = event_image;
}


public Member getMember() {
	return member;
}


public void setMember(Member member) {
	this.member = member;
}
  
	
	
}
