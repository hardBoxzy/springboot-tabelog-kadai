package com.example.nagoyameshi.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagoyameshi.entity.Favorite;
import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.repository.FavoriteRepository;
import com.example.nagoyameshi.repository.RestaurantRepository;
import com.example.nagoyameshi.repository.UserRepository;
import com.example.nagoyameshi.security.UserDetailsImpl;
import com.example.nagoyameshi.service.FavoriteService;

@Controller
public class FavoriteController {
	private final FavoriteRepository favoriteRepository ;
    private final UserRepository userRepository; 
    private final FavoriteService favoriteService;
    private final RestaurantRepository restaurantRepository;   
    public FavoriteController(FavoriteRepository favoriteRepository,UserRepository userRepository,
    		RestaurantRepository restaurantRepository,FavoriteService favoriteService) {
        this.userRepository = userRepository; 
        this.favoriteRepository = favoriteRepository;
        this.favoriteService = favoriteService;
        this.restaurantRepository = restaurantRepository;   
    }    
    
    @GetMapping("/user/favorites")
    public String index(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
    		@PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable,
    		Model model) {     
    	
        User user = userRepository.getReferenceById(userDetailsImpl.getUser().getId());
        Page<Favorite> favoritePage;
        favoritePage = favoriteRepository.findByUserOrderByCreatedAtDesc(user,pageable);
        model.addAttribute("favoritePage", favoritePage);
        return "user/favorites/index";
    }
    
    @PostMapping("/restaurants/{id}/favorites/create")
    public String create(
    		@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
    		@PathVariable(name = "id") Integer restaurantId,
    		@ModelAttribute Restaurant restaurant,
    		RedirectAttributes redirectAttributes) {        
        
        User user = userDetailsImpl.getUser();
        
        favoriteService.create(restaurant, user); 
        
        return "redirect:/restaurants/" + restaurantId;
    }    
    
    @PostMapping("/restaurants/{id}/favorites/delete")
    public String delete(
    		@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
    		@PathVariable(name = "id") Integer restaurantId,
    		RedirectAttributes redirectAttributes) {  
   	
    	  Restaurant restaurant = restaurantRepository.getReferenceById(restaurantId);
    	  User user = userDetailsImpl.getUser();
    	  Favorite favorite = favoriteRepository.findFirstByUserAndRestaurant(user,restaurant).orElse(null);
    	    if (favorite != null) {
    	        favoriteRepository.delete(favorite);
    	    }
        
        return "redirect:/restaurants/" + restaurantId;
    } 
}