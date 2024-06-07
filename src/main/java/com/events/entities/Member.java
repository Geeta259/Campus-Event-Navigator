package com.events.entities;


import java.util.ArrayList;
import java.util.List;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name="Member")
public class Member {
 @Id
 @GeneratedValue(strategy=GenerationType.AUTO)
 private int id;
 
 @NotBlank(message = "Name field is required !!")
 @Size(min = 2,max=20,message="min 2 and max 20 characters are allowed !!")
 private String name;
 
@Column(unique = true)
 private String email;
 private String password;
 private String role;
 private boolean enabled;
 private String imageUrl;
@Column(length = 500)
 private String about;
 private String phone;
 
 //one user have many contacts
 @OneToMany(cascade = CascadeType.ALL,mappedBy="member",orphanRemoval=true)
 private List<Events> events = new ArrayList<>();

 public Member() {
	super();
	// TODO Auto-generated constructor stub
}
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getEmail() {
	return email;
}
public void setEmail(String email) {
	this.email = email;
}
public String getPassword() {
	return password;
}
public void setPassword(String password) {
	this.password = password;
}

public boolean isEnabled() {
	return enabled;
}
public void setEnabled(boolean enabled) {
	this.enabled = enabled;
}
public String getRole() {
	return role;
}
public void setRole(String role) {
	this.role = role;
}
public String getImage() {
	return imageUrl;
}
public void setImage(String imageUrl) {
	this.imageUrl = imageUrl;
}
public String getAbout() {
	return about;
}
public void setAbout(String about) {
	this.about = about;
}
public String getPhone() {
	return phone;
}
public void setPhone(String phone) {
	this.phone = phone;
}

public List<Events> getEvents() {
	return events;
}
public void setEvents(List<Events> events) {
	this.events = events;
}
@Override
public String toString() {
	return "Member [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", role=" + role
			+ ", enabled=" + enabled + ", imageUrl=" + imageUrl + ", about=" + about + ", phone=" + phone
			+ ", events=" + events + "]";
}

}
