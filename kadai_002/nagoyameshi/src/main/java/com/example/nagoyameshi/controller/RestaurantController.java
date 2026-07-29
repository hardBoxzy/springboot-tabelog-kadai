package com.example.nagoyameshi.controller;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.nagoyameshi.entity.Category;
import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.repository.CategoryRepository;
import com.example.nagoyameshi.repository.RestaurantRepository;

@Controller
@RequestMapping("/restaurants")
public class RestaurantController {
    private final RestaurantRepository restaurantRepository;        
    private final CategoryRepository categoryRepository;
    public RestaurantController(RestaurantRepository restaurantRepository,CategoryRepository categoryRepository) {
        this.restaurantRepository = restaurantRepository;
        this.categoryRepository  = categoryRepository;            
    }     
  
    @GetMapping
    public String index(@RequestParam(name = "keyword", required = false) String keyword,
                        @RequestParam(name = "categoryId", required = false) Integer categoryId,
                        @RequestParam(name = "price", required = false) Integer price,                        
                        @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable,
                        Model model) 
    {
        Page<Restaurant> restaurantPage;
                
        if (keyword != null && !keyword.isEmpty()) {
            restaurantPage = restaurantRepository.findByNameLikeOrAddressLike("%" + keyword + "%", "%" + keyword + "%", pageable);
        } else if (categoryId != null ) {
            restaurantPage = restaurantRepository.findByRestaurantCategories_Category_Id(categoryId, pageable);
        } else if (price != null) {
            restaurantPage = restaurantRepository.findByPriceLessThanEqual(price, pageable);
        } else {
            restaurantPage = restaurantRepository.findAll(pageable);
        }                
        
        model.addAttribute("restaurantPage", restaurantPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("price", price);
        
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        
        return "restaurants/index";
    }
}