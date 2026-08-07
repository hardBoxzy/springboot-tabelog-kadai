
package com.example.nagoyameshi.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagoyameshi.entity.Category;
import com.example.nagoyameshi.repository.CategoryRepository;
import com.example.nagoyameshi.service.CategoryService;

@Controller
@RequestMapping("/admin/categories")
public class AdminCategoryController {
    private final CategoryService categoryService;   
    private final CategoryRepository categoryRepository;
    public AdminCategoryController(CategoryService categoryService, CategoryRepository categoryRepository) {
        
        this.categoryService =  categoryService;
        this.categoryRepository  = categoryRepository;
    }	
    
    @GetMapping
    public String index(Model model,@PageableDefault(page = 0, size = 20, sort = "id", direction = Direction.ASC)  Pageable pageable, @RequestParam(name = "keyword", required = false) String keyword) {
    	Page<Category> categoryPage;       
        
    	 if (keyword != null && !keyword.isEmpty()) {
    		 categoryPage = categoryRepository.findByNameLike("%" + keyword + "%", pageable);                
         } else {
        	 categoryPage = categoryRepository.findAll(pageable);
         }  
    	
        model.addAttribute("categoryPage", categoryPage);             
        model.addAttribute("keyword", keyword);
        
        return "admin/categories/index";
    }  
    
    @PostMapping("/{id}/update")
    public String update(@PathVariable("id") Integer id, @RequestParam("name") String name, RedirectAttributes redirectAttributes) {
        // ① 引数の id と name を使って、データベースのカテゴリー情報を更新する
        categoryService.update(id, name); 
        
        redirectAttributes.addFlashAttribute("successMessage", "カテゴリーを更新しました。");
        return "redirect:/admin/categories";
    }
    
    @PostMapping("/create")
    public String create( @RequestParam("newCategoryName") String name, RedirectAttributes redirectAttributes) {
        categoryService.create( name); 
        
        redirectAttributes.addFlashAttribute("successMessage", "カテゴリーを作成しました。");
        return "redirect:/admin/categories";
    }
    
    @PostMapping("/{id}/delete")
    public String update(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        // ① 引数の id と name を使って、データベースのカテゴリー情報を更新する
        categoryService.delete(id); 
        
        redirectAttributes.addFlashAttribute("successMessage", "カテゴリーを削除しました。");
        return "redirect:/admin/categories";
    }
}
