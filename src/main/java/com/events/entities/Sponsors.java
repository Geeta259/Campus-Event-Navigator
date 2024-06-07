package com.events.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name="Sponsors")
public class Sponsors {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
  private int sponsor_id;
	  private String sponsor;
	 private String sponsor_image;
	 
	 @ManyToOne
	  @JsonIgnore
	 private Member member;
	 
	 
	public int getSponsor_id() {
		return sponsor_id;
	}
	public void setSponsor_id(int sponsor_id) {
		this.sponsor_id = sponsor_id;
	}
	public String getSponsor() {
		return sponsor;
	}
	public void setSponsor(String sponsor) {
		this.sponsor = sponsor;
	}
	public String getImage() {
		return sponsor_image;
	}
	public void setImage(String sponsor_image) {
		this.sponsor_image = sponsor_image;
	}
	 
	public Member getMember() {
		return member;
	}


	public void setMember(Member member) {
		this.member = member;
	}
}
