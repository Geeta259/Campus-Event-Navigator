package com.events.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="BookEvents")

public class BookingEvent {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
  private int bookId;
  private int uid;
  private int eventid;
  private String status;
  
  public BookingEvent() {
	super();
	// TODO Auto-generated constructor stub
}
public int getBookId() {
	return bookId;
}
public void setBookId(int bookId) {
	this.bookId = bookId;
}
public int getUid() {
	return uid;
}
public void setUid(int uid) {
	this.uid = uid;
}
public int getEventid() {
	return eventid;
}
public void setEventid(int eventid) {
	this.eventid = eventid;
}

public String getStatus() {
	return status;
}
public void setStatus(String status) {
	this.status = status;
}
@Override
public String toString() {
	return "BookingEvent [bookId=" + bookId + ", uid=" + uid + ", eventid=" + eventid + ", status=" + status + "]";
}


}
