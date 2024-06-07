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
@Table(name="Category")
public class Category {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
  private int cat_id;
	
	  private String cat_name;
	  @Column(length=1000)
	  private String description;
	 private String category_image;
	 
	 @ManyToOne
	  @JsonIgnore
	 private Member member;
	 
	public int getCat_id() {
		return cat_id;
	}
	public void setCat_id(int cat_id) {
		this.cat_id = cat_id;
	}
	
	public String getCategory() {
		return cat_name;
	}
	public void setCategory(String cat_name) {
		this.cat_name = cat_name;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getImage() {
		return category_image;
	}
	public void setImage(String category_image) {
		this.category_image = category_image;
	}

public Member getMember() {
	return member;
}


public void setMember(Member member) {
	this.member = member;
}
  
}
