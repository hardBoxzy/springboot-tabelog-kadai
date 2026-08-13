package com.example.nagoyameshi.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.entity.Review;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.form.ReviewEditForm;
import com.example.nagoyameshi.form.ReviewRegisterForm;
import com.example.nagoyameshi.repository.RestaurantRepository;
import com.example.nagoyameshi.repository.ReviewRepository;
import com.example.nagoyameshi.security.UserDetailsImpl;
import com.example.nagoyameshi.service.RestaurantService;
import com.example.nagoyameshi.service.ReviewService;



@Controller
@RequestMapping("/restaurants")
public class ReviewController {
	private final RestaurantRepository restaurantRepository;  
    private final ReviewRepository reviewRepository;
    private final ReviewService reviewService;
    private final RestaurantService restaurantService;
    public ReviewController( RestaurantRepository restaurantRepository, ReviewRepository reviewRepository, 
    		ReviewService reviewService,RestaurantService restaurantService) {
    	this.reviewRepository = reviewRepository;
        this.restaurantRepository = restaurantRepository;  
        this.reviewService = reviewService;
        this.restaurantService = restaurantService;
    }  
	
	
	@GetMapping("/{id}/reviews")
    public String showReview(
    		@PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC)Pageable pageable,
    		@PathVariable(name = "id") Integer restaurantId,
    		Model model) {
    	Restaurant restaurant = restaurantRepository.getReferenceById(restaurantId);
    	model.addAttribute("restaurant", restaurant);
    	
    	 Page<Review> reviewPage = reviewRepository.findAllByRestaurantOrderByCreatedAtDesc(restaurant,pageable);
    	 model.addAttribute("reviewPage", reviewPage);
                 
    	
        return "/restaurants/reviews/index";
    }
    
    @PostMapping("/{id}/reviews/{reviewId}/delete")
    public String deleteReview(
		@PathVariable(name = "id") Integer restaurantId, 
		@PathVariable(name = "reviewId") Integer reviewId, 
		@RequestHeader(value = "Referer", required = false) String referer, 
		RedirectAttributes redirectAttributes) { 
    	
        reviewRepository.deleteById(reviewId);	
        redirectAttributes.addFlashAttribute("successMessage", "レビューを削除しました。");
        
        
        if (referer != null) {
            return "redirect:" + referer;
        }
        
        return "redirect:/restaurants/" + restaurantId;
    }
    
    @GetMapping("/{id}/reviews/register")
    public String registerReview(Model model,@PathVariable(name = "id") Integer restaurantId) {
    	Restaurant restaurant = restaurantRepository.getReferenceById(restaurantId);
    	model.addAttribute("restaurant", restaurant); 
        model.addAttribute("reviewRegisterForm", new ReviewRegisterForm());
        return "restaurants/reviews/register";
    }
    
    @PostMapping("/{id}/reviews/create")
    public String createReview(
    		Model model,
    		@PathVariable(name = "id") Integer restaurantId,
    		@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,  
    		@ModelAttribute @Validated ReviewRegisterForm reviewRegisterForm,
    		BindingResult bindingResult,                 // バリデーションエラー判定用(form引数のすぐに後ろに定義する必要がある)
    		RedirectAttributes redirectAttributes) {
    	
    	if (bindingResult.hasErrors()) {
    		 Restaurant restaurant = restaurantRepository.getReferenceById(restaurantId); 
             model.addAttribute("restaurant", restaurant);
            return "restaurants/reviews/register";
        }
    	
    	User user = userDetailsImpl.getUser();
    	Restaurant restaurant = restaurantRepository.getReferenceById(restaurantId); 
    	reviewService.create(reviewRegisterForm,restaurant,user);
    	restaurantService.updateScore(restaurant);
        redirectAttributes.addFlashAttribute("successMessage", "レビューを投稿しました。");  
        
        return "redirect:/restaurants/" + restaurantId + "/reviews";
    } 
    
    @GetMapping("/{id}/reviews/{reviewId}/edit")
    public String editReview(Model model,
    		@PathVariable(name = "id") Integer restaurantId,
    		@PathVariable(name = "reviewId") Integer reviewId) {
    	
    	Restaurant restaurant = restaurantRepository.getReferenceById(restaurantId);
    	model.addAttribute("restaurant", restaurant); 
    	
    	Review review = reviewRepository.getReferenceById(reviewId);
    	Integer reviewScore = review.getScore();
    	String reviewContent = review.getContent();
        model.addAttribute("reviewEditForm", new ReviewEditForm(reviewId,reviewScore, reviewContent));
        
        return "restaurants/reviews/edit";
    }
    
    @PostMapping("/{id}/reviews/{reviewId}/update")
    public String updateReview(
    		Model model,
    		@PathVariable(name = "id") Integer restaurantId,
    		@PathVariable(name = "reviewId") Integer reviewId,
    		@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,  
    		@ModelAttribute @Validated ReviewEditForm reviewEditForm,
    		BindingResult bindingResult,                 // バリデーションエラー判定用(form引数のすぐに後ろに定義する必要がある)
    		RedirectAttributes redirectAttributes) {
    	Restaurant restaurant = restaurantRepository.getReferenceById(restaurantId);
    	if (bindingResult.hasErrors()) {
    		 
    		model.addAttribute("restaurant", restaurant);
    		
    		//なぜかreviewEditFormのIDの値が消えてしまうので、 URLのreviewIdを使って、フォームにIDを確実に再セットする
    		reviewEditForm.setId(reviewId);
            return "restaurants/reviews/edit";
        }
    	reviewEditForm.setId(reviewId);
    	
    	reviewService.update(reviewEditForm);
    	restaurantService.updateScore(restaurant);
        redirectAttributes.addFlashAttribute("successMessage", "レビューを編集しました。");  
        
        return "redirect:/restaurants/" + restaurantId + "/reviews";
    }
	
}
