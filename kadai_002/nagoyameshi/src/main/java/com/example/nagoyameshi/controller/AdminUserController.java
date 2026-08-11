package com.example.nagoyameshi.controller;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.repository.UserRepository;
import com.example.nagoyameshi.service.UserService;

import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {
    private final UserRepository userRepository;        
    private final UserService userService; 
    public AdminUserController(UserRepository userRepository,UserService userService) {
        this.userRepository = userRepository;   
        this.userService = userService; 
    }    
    
    @GetMapping
    public String index(@RequestParam(name = "keyword", required = false) String keyword, @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable, Model model) {
        Page<User> userPage;
        
        if (keyword != null && !keyword.isEmpty()) {
            userPage = userRepository.findByNameLikeOrFuriganaLike("%" + keyword + "%", "%" + keyword + "%", pageable);                   
        } else {
            userPage = userRepository.findAll(pageable);
        }        
        
        model.addAttribute("userPage", userPage);        
        model.addAttribute("keyword", keyword);                
        
        return "admin/users/index";
    }
    
    @GetMapping("/{id}")
    public String show(@PathVariable(name = "id") Integer id, Model model) {
        User user = userRepository.getReferenceById(id);
        
        model.addAttribute("user", user);
        
        return "admin/users/show";
    } 
    
    @GetMapping("/download")
    public void downloadCsv(@RequestParam(name = "keyword", required = false) String keyword,
                            HttpServletResponse response) throws IOException 
    {
    	Page<User> userPage;  
    	// 1. ソート条件を組み立てる
        Sort sort = Sort.by("createdAt").descending(); 
        // ★ポイント: ページ制限をかけずに全件取得するため、サイズに Integer.MAX_VALUE を指定する
        Pageable allPageable = PageRequest.of(0, Integer.MAX_VALUE, sort);
        
   	 if (keyword != null && !keyword.isEmpty()) {
   		 userPage = userRepository.findByNameLikeOrFuriganaLike("%" + keyword + "%", "%" + keyword + "%", allPageable);                
        } else {
       	 userPage = userRepository.findAll(allPageable);
        }
        // ★ポイント: .getContent() を使って Page から List<Restaurant> に変換する
        List<User> users = userPage.getContent();
        // 3. レスポンスヘッダーの設定
        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"users.csv\"");
        // 4. CSVデータの書き込み
        try (PrintWriter writer = response.getWriter()) {
            writer.write('\ufeff'); // Excel文字化け防止用BOM
            writer.println(userService.createCSVStr(users));
            
        }
    }
    
}
