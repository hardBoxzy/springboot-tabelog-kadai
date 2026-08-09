
package com.example.nagoyameshi.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import com.example.nagoyameshi.entity.Category;
import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.entity.RestaurantCategory;
import com.example.nagoyameshi.form.RestaurantEditForm;
import com.example.nagoyameshi.form.RestaurantRegisterForm;
import com.example.nagoyameshi.repository.CategoryRepository;
import com.example.nagoyameshi.repository.RestaurantCategoryRepository;
import com.example.nagoyameshi.repository.RestaurantRepository;
import com.example.nagoyameshi.service.RestaurantService;

import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/admin/restaurants")
public class AdminRestaurantController {
    private final RestaurantRepository restaurantRepository; 
    private final RestaurantService restaurantService;   
    private final RestaurantCategoryRepository restaurantCategoryRepository;
    private final CategoryRepository categoryRepository;
    public AdminRestaurantController(RestaurantRepository restaurantRepository, RestaurantService restaurantService, RestaurantCategoryRepository restaurantCategoryRepository, CategoryRepository categoryRepository) {
        this.restaurantRepository = restaurantRepository; 
        this.restaurantService =  restaurantService;
        this.restaurantCategoryRepository  = restaurantCategoryRepository;
        this.categoryRepository  = categoryRepository;
    }	
    
    @GetMapping
    public String index(Model model,@PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC)  Pageable pageable, @RequestParam(name = "keyword", required = false) String keyword) {
    	Page<Restaurant> restaurantPage;       
        
    	 if (keyword != null && !keyword.isEmpty()) {
    		 restaurantPage = restaurantRepository.findByNameLike("%" + keyword + "%", pageable);                
         } else {
        	 restaurantPage = restaurantRepository.findAll(pageable);
         }  
    	
        model.addAttribute("restaurantPage", restaurantPage);             
        model.addAttribute("keyword", keyword);
        
        return "admin/restaurants/index";
    }  
    
    @GetMapping("/{id}")
    public String show(@PathVariable(name = "id") Integer id, Model model) {
    	Restaurant restaurant = restaurantRepository.getReferenceById(id);
    	// 2. 店舗に紐づくカテゴリーのリストを取得
        List<RestaurantCategory> restaurantCategories = restaurantCategoryRepository.findByRestaurant(restaurant); 
        model.addAttribute("restaurant", restaurant);
        model.addAttribute("restaurantCategories", restaurantCategories);
        return "admin/restaurants/show";
    }  
    
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("restaurantRegisterForm", new RestaurantRegisterForm());
        // 2. チェックボックスに表示する「すべてのカテゴリー」を渡す
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        return "admin/restaurants/register";
    } 
    
    @PostMapping("/create")
    public String create(@ModelAttribute @Validated RestaurantRegisterForm restaurantRegisterForm, BindingResult bindingResult, RedirectAttributesModelMap redirectAttributes,Model model) {        
        if (bindingResult.hasErrors()) {
        	List<Category> categories = categoryRepository.findAll();
            model.addAttribute("categories", categories);
            return "admin/restaurants/register";
        }
        
        restaurantService.create(restaurantRegisterForm);
        redirectAttributes.addFlashAttribute("successMessage", "店舗を登録しました。");    
        
        return "redirect:/admin/restaurants";
    }
    
    @GetMapping("/download")
    public void downloadCsv(@RequestParam(name = "keyword", required = false) String keyword,
                            HttpServletResponse response) throws IOException 
    {
    	Page<Restaurant> restaurantPage;  
    	// 1. ソート条件を組み立てる
        Sort sort = Sort.by("createdAt").descending(); 
        // ★ポイント: ページ制限をかけずに全件取得するため、サイズに Integer.MAX_VALUE を指定する
        Pageable allPageable = PageRequest.of(0, Integer.MAX_VALUE, sort);
        
   	 if (keyword != null && !keyword.isEmpty()) {
   		 restaurantPage = restaurantRepository.findByNameLike("%" + keyword + "%", allPageable);                
        } else {
       	 restaurantPage = restaurantRepository.findAll(allPageable);
        }  
        
        // ★ポイント: .getContent() を使って Page から List<Restaurant> に変換する
        List<Restaurant> restaurants = restaurantPage.getContent();

        // 3. レスポンスヘッダーの設定
        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"restaurants.csv\"");

        // 4. CSVデータの書き込み
        try (PrintWriter writer = response.getWriter()) {
            writer.write('\ufeff'); // Excel文字化け防止用BOM
            writer.println("店舗ID,店舗名,画像名,説明,料金,人数,郵便番号,住所,電話番号,作成時間,更新時間");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


            for (Restaurant r : restaurants) {
                String createdAtStr = "";
                if (r.getCreatedAt() != null) {
                    createdAtStr = r.getCreatedAt().toLocalDateTime().format(formatter);
                }
                String updatedAtStr = "";
                if (r.getUpdatedAt() != null) {
                	updatedAtStr = r.getCreatedAt().toLocalDateTime().format(formatter);
                }
                writer.println(String.format("%d,%s,%s,%s,%d,%d,%s,%s,%s,%s,%s", 
                    r.getId(), 
                    escapeCsv(r.getName()), 
                    escapeCsv(r.getImageName()), 
                    escapeCsv(r.getDescription()),
                    r.getPrice(),
                    r.getCapacity(),
                    escapeCsv(r.getPostalCode()), 
                    escapeCsv(r.getAddress()), 
                    escapeCsv(r.getPhoneNumber()), 
                    createdAtStr,
                    updatedAtStr
                ));
            }
        }
    }
    
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable(name = "id") Integer id, Model model) {
    	Restaurant restaurant = restaurantRepository.getReferenceById(id);
    	String imageName = restaurant.getImageName() ;
        List<Integer> categoryIds = restaurantService.getCategoryIds(restaurant);
        List<Integer> dayIds = restaurantService.getCategoryIds(restaurant);
        
        RestaurantEditForm restaurantEditForm = new RestaurantEditForm(
        		restaurant.getId(), restaurant.getName(),categoryIds,
        		null,restaurant.getDescription(), restaurant.getPrice(), 
        		restaurant.getCapacity(), restaurant.getPostalCode(), restaurant.getAddress(), 
        		restaurant.getPhoneNumber(), dayIds);
                
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);//  画面表示に必要な「すべてのカテゴリー一覧」もModelに渡す
        
        model.addAttribute("imageName", imageName);
        model.addAttribute("restaurantEditForm", restaurantEditForm);
        
        return "admin/restaurants/edit";
    }  
    
    @PostMapping("/{id}/update")
    public String update(@ModelAttribute @Validated RestaurantEditForm restaurantEditForm, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {        
        if (bindingResult.hasErrors()) {
        	List<Category> categories = categoryRepository.findAll();
            model.addAttribute("categories", categories);
            return "admin/restaurants/edit";
        }
        
        restaurantService.update(restaurantEditForm);
        redirectAttributes.addFlashAttribute("successMessage", "店舗情報を編集しました。");
        
        return "redirect:/admin/restaurants";
    }   
    
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {        
        restaurantRepository.deleteById(id);
                
        redirectAttributes.addFlashAttribute("successMessage", "民宿を削除しました。");
        
        return "redirect:/admin/restaurants";
    }
    
    
    
    // カンマや改行が含まれる文字列を安全にCSV用にエスケープする補助メソッド
    private String escapeCsv(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n") || value.contains("\r")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
    
}
