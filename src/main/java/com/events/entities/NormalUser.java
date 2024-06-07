package com.events.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="normal_user")
public class NormalUser {
	@Id
	 @GeneratedValue(strategy=GenerationType.AUTO)
	 private int id;
	 private String name;	 
	@Column(unique = true)
	 private String email;
	 private String password;
	 private String dob;
	 private String gender;
	 private String phone;
	 private String city;
	 private String address;
	 private String profile_image;
	 
	 
	public NormalUser() {
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
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getProfile_image() {
		return profile_image;
	}
	public void setProfile_image(String profile_image) {
		this.profile_image = profile_image;
	}
	@Override
	public String toString() {
		return "NormalUser [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", dob="
				+ dob + ", gender=" + gender + ", phone=" + phone + ", city=" + city + ", address=" + address
				+ ",  profile_image=" + profile_image + "]";
	}
	 
	 
	
}
