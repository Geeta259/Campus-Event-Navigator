package com.events.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.events.entities.UserFeedback;

public interface FeedbackRepository extends JpaRepository<UserFeedback,Integer>{

}
