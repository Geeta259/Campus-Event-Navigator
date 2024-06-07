package com.events.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.events.entities.Category;





public interface CategoryRepository extends JpaRepository<Category,Integer>{
	@Query("select c from Category c")
	public List<Category> findAll();
	
	@Query("select c from Category c where c.member.id =:id")
	public Page<Category> findCategoryByUser(@Param("id")int id,Pageable  pageable);
	
	@Query("select count(c) from Category c where c.member.id =:id")
	public long countAll(@Param("id")int id);
	
	
}
