package com.example.nagoyameshi.controller;


import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.nagoyameshi.entity.Category;
import com.example.nagoyameshi.entity.Favorite;
import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.entity.Review;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.form.ReservationInputForm;
import com.example.nagoyameshi.repository.CategoryRepository;
import com.example.nagoyameshi.repository.FavoriteRepository;
import com.example.nagoyameshi.repository.RestaurantRepository;
import com.example.nagoyameshi.repository.ReviewRepository;
import com.example.nagoyameshi.repository.UserRepository;
import com.example.nagoyameshi.security.UserDetailsImpl;

@Controller
@RequestMapping("/restaurants")
public class RestaurantController {
	
	private final UserRepository userRepository;
	private final FavoriteRepository favoriteRepository;
    private final RestaurantRepository restaurantRepository;        
    private final CategoryRepository categoryRepository;
    private final ReviewRepository reviewRepository;
    public RestaurantController(RestaurantRepository restaurantRepository,CategoryRepository categoryRepository, 
    		UserRepository userRepository, FavoriteRepository favoriteRepository,ReviewRepository reviewRepository) {
        this.restaurantRepository = restaurantRepository;
        this.categoryRepository  = categoryRepository;
        this.userRepository  = userRepository;
        this.favoriteRepository  = favoriteRepository;
        this.reviewRepository  = reviewRepository;
    }     
  
    @GetMapping
    public String index(@RequestParam(name = "keyword", required = false) String keyword,
                        @RequestParam(name = "categoryId", required = false) Integer categoryId,
                        @RequestParam(name = "price", required = false) Integer price,    
                        @RequestParam(name = "order", required = false) String order,
                        @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable,
                        Model model) 
    {
        Page<Restaurant> restaurantPage;
                
        if (keyword != null && !keyword.isEmpty()) {
            if (order != null && order.equals("priceAsc")) {
            	restaurantPage = restaurantRepository.findByNameLikeOrAddressLikeOrderByPriceAsc("%" + keyword + "%", "%" + keyword + "%", pageable);
            } else {
            	restaurantPage = restaurantRepository.findByNameLikeOrAddressLikeOrderByCreatedAtDesc("%" + keyword + "%", "%" + keyword + "%", pageable);
            }   
        } else if (categoryId != null ) {
            restaurantPage = restaurantRepository.findByRestaurantCategories_Category_Id(categoryId, pageable);
            if (order != null && order.equals("priceAsc")) {
            	restaurantPage = restaurantRepository.findByRestaurantCategories_Category_IdOrderByPriceAsc(categoryId, pageable);
            } else {
            	restaurantPage = restaurantRepository.findByRestaurantCategories_Category_IdOrderByCreatedAtDesc(categoryId, pageable);
            }    
        } else if (price != null) {
        	if (order != null && order.equals("priceAsc")) {
                restaurantPage = restaurantRepository.findByPriceLessThanEqualOrderByPriceAsc(price, pageable);
            } else {
                restaurantPage = restaurantRepository.findByPriceLessThanEqualOrderByCreatedAtDesc(price, pageable);
            } 
        } else {
        	 if (order != null && order.equals("priceAsc")) {
                 restaurantPage = restaurantRepository.findAllByOrderByPriceAsc(pageable);
             } else {
                 restaurantPage = restaurantRepository.findAllByOrderByCreatedAtDesc(pageable);   
             }     
        }                
        
        model.addAttribute("restaurantPage", restaurantPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("price", price);
        model.addAttribute("order", order);
        
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        
        return "restaurants/index";
    }
    		
    @GetMapping("/{id}")
    public String show(@PathVariable(name = "id") Integer id,
    		@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,Model model) {
    	Restaurant restaurant = restaurantRepository.getReferenceById(id);
    	List<Review> newReviews = reviewRepository.findTop6ByRestaurantOrderByCreatedAtDesc(restaurant);
        model.addAttribute("newReviews", newReviews);
        model.addAttribute("restaurant", restaurant);         
        model.addAttribute("reservationInputForm", new ReservationInputForm());
        
        if (userDetailsImpl != null) {
            User user = userRepository.getReferenceById(userDetailsImpl.getUser().getId());
            Optional<Favorite> favorite = favoriteRepository.findFirstByUserAndRestaurant(user, restaurant);
            // 箱の中にデータがあれば true、なければ false を渡す
            model.addAttribute("isFavorite", favorite.isPresent());
        } else {
            // 未ログインなら確実にお気に入りではない（false）
            model.addAttribute("isFavorite", false);
        }
        
        return "restaurants/show";
    }   
}