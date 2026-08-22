package com.example.nagoyameshi.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagoyameshi.entity.Category;
import com.example.nagoyameshi.entity.CompanyInfo;
import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.form.CompanyInfoEditForm;
import com.example.nagoyameshi.repository.CategoryRepository;
import com.example.nagoyameshi.repository.CompanyInfoRepository;
import com.example.nagoyameshi.repository.RestaurantRepository;

@Controller
public class HomeController {
	 private final RestaurantRepository restaurantRepository;        
	 private final CategoryRepository categoryRepository; 
	 private final CompanyInfoRepository companyInfoRepository; 
	 
	    public HomeController(RestaurantRepository restaurantRepository,CategoryRepository categoryRepository, CompanyInfoRepository companyInfoRepository) {
	        this.restaurantRepository = restaurantRepository;     
	        this.categoryRepository  = categoryRepository;
	        this.companyInfoRepository  = companyInfoRepository;
	    }  
    @GetMapping("/")
    public String index(Model model) {
    	List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        List<Restaurant> newRestaurants = restaurantRepository.findTop10ByOrderByCreatedAtDesc();
        model.addAttribute("newRestaurants", newRestaurants);
        List<Restaurant> highScoreRestaurants = restaurantRepository.findTop5ByOrderByScoreDesc();
        model.addAttribute("highScoreRestaurants", highScoreRestaurants);
        List<Restaurant> lowPriceRestaurants = restaurantRepository.findTop6ByOrderByPriceAsc();
        model.addAttribute("lowPriceRestaurants", lowPriceRestaurants);
        return "index";
    }   
    @GetMapping("/company")
    public String company(Model model) { 
    	String mapSrc = "";
    	CompanyInfo companyInfo = companyInfoRepository.getReferenceById(1);
    	model.addAttribute("companyInfo", companyInfo);
    	model.addAttribute("mapSrc", mapSrc);
        return "company/index";
    }  
    
    @GetMapping("/company/edit")
    public String editCompany(Model model) { 
    	CompanyInfo companyInfo = companyInfoRepository.getReferenceById(1);
    	model.addAttribute("companyInfo", companyInfo);
    	
        CompanyInfoEditForm companyInfoEditForm = new CompanyInfoEditForm(
        		companyInfo.getId(), companyInfo.getName(),companyInfo.getAddress(), 
        		companyInfo.getRepresentative(), companyInfo.getEstabilishedAt(),companyInfo.getMapSrc());
        model.addAttribute("companyInfoEditForm", companyInfoEditForm);
        
        return "company/edit";
    } 
    
    @PostMapping("/company/update")
    public String updateCompany(@ModelAttribute @Validated CompanyInfoEditForm companyInfoEditForm, BindingResult bindingResult, 
    		RedirectAttributes redirectAttributes, Model model) {        
        if (bindingResult.hasErrors()) {
            return "company/edit";
        }
        CompanyInfo companyInfo = companyInfoRepository.getReferenceById(1);
        companyInfo.setName(companyInfoEditForm.getName());
        companyInfo.setAddress(companyInfoEditForm.getAddress());
        companyInfo.setRepresentative(companyInfoEditForm.getRepresentative());
        companyInfo.setEstabilishedAt(companyInfoEditForm.getEstabilishedAt());
        companyInfo.setMapSrc(companyInfoEditForm.getMapSrc());
        companyInfoRepository.save(companyInfo);
        redirectAttributes.addFlashAttribute("successMessage", "店舗情報を編集しました。");
        
        return "redirect:/company";
    }  
    
    
    /**
     * これはHomeのコントローラーです。
     */
}