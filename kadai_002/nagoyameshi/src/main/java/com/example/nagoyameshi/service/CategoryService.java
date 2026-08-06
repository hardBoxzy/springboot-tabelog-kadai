package com.example.nagoyameshi.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagoyameshi.entity.Category;
import com.example.nagoyameshi.repository.CategoryRepository;

@Service
public class CategoryService { 
   private final CategoryRepository categoryRepository;
   public CategoryService(CategoryRepository categoryRepository) {
       
       this.categoryRepository =  categoryRepository;
   }    
   
   @Transactional
   public void create(String name) {     
	      Category category = new Category();
	      category.setName(name); 
	      categoryRepository.save(category);
   }  
   
   
   
   @Transactional
   public void update(Integer id,String name) {
      Category category = categoryRepository.getReferenceById(id);
      category.setName(name); 
      categoryRepository.save(category);
   }
    
   @Transactional
   public void delete(Integer id) {
      Category category = categoryRepository.getReferenceById(id);
      categoryRepository.delete(category);
   }
   

}