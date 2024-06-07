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
@Table(name="Coordinator")

public class Coordinator {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
  private int coord_id;
	private String coordname;
	  private String branch;
	  private String eventname;
	  @Column(length=1000)
	  private String description;
	  String phone;
	  String email;
	  private String coord_image;
	
	  @ManyToOne
	  @JsonIgnore
	 private Member member;
	  
	public Coordinator() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getCoord_id() {
		return coord_id;
	}

	public void setCoord_id(int coord_id) {
		this.coord_id = coord_id;
	}

	public String getCoordname() {
		return coordname;
	}

	public void setCoordname(String coordname) {
		this.coordname = coordname;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getEventname() {
		return eventname;
	}

	public void setEventname(String eventname) {
		this.eventname = eventname;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getImage() {
		return coord_image;
	}


	public void setImage(String coord_image) {
		this.coord_image = coord_image;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}



	  
}
