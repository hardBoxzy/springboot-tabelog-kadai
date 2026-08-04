package com.example.nagoyameshi.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagoyameshi.entity.Favorite;
import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.repository.FavoriteRepository;

@Service
public class FavoriteService {
	private final FavoriteRepository favoriteRepository;
	
	public FavoriteService(FavoriteRepository favoriteRepository) {
		this.favoriteRepository = favoriteRepository;
	}
	
	 @Transactional
	   public void create(Restaurant restaurant, User user) {
	       Favorite favorite = new Favorite();        
	       
	       
	       favorite.setUser(user);                
	       favorite.setRestaurant(restaurant);  
	                   
	       favoriteRepository.save(favorite);
	   }  
}
