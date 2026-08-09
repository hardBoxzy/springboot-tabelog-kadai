
package com.example.nagoyameshi.controller;

import java.time.LocalDateTime;
import java.time.YearMonth;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.nagoyameshi.entity.Revenue;
import com.example.nagoyameshi.repository.RevenueRepository;

@Controller
@RequestMapping("/admin/revenues")
public class AdminRevenueController {
    private final RevenueRepository revenueRepository; 
    public AdminRevenueController(RevenueRepository revenueRepository) {
        this.revenueRepository = revenueRepository; 
    }	
    
    @GetMapping
    public String index(Model model,@PageableDefault(page = 0, size = 15, sort = "id", direction = Direction.ASC)  Pageable pageable,
    		@RequestParam(name = "yearMonth", required = false) String yearMonth) {
    	   // デフォルト値の設定（空なら現在の年月）
        if (yearMonth == null || yearMonth.isEmpty()) {
            yearMonth = YearMonth.now().toString(); // "2026-08"
        }
        YearMonth ym = YearMonth.parse(yearMonth);// ★ 文字列 "2026-08" を解析して日時の範囲を作る
        LocalDateTime startOfMonth = ym.atDay(1).atStartOfDay();// 例: 2026-08-01T00:00:00
        LocalDateTime endOfMonth = ym.plusMonths(1).atDay(1).atStartOfDay();// 例: 2026-09-01T00:00:00 (翌月の1日未満という条件にするため)
        
    	Page<Revenue> revenuePage; 
    	if (yearMonth != null && !yearMonth.isEmpty()) {
    		// ★ 修正したリポジトリのメソッドを呼び出す
    	    revenuePage = revenueRepository.findByCreatedAtBetweenOrderByCreatedAtDesc(startOfMonth, endOfMonth, pageable);
        	// 2. ★ その月のすべての売上合計を計算する
        	Long totalRevenue = revenueRepository.sumAmountByCreatedAtBetween(startOfMonth, endOfMonth);
        	if (totalRevenue == null) {
        	    totalRevenue = 0L;
        	}
        	model.addAttribute("totalRevenue", totalRevenue); 
                
        } else {
        	revenuePage = revenueRepository.findAllByOrderByCreatedAtDesc(pageable);
        }
    	


        model.addAttribute("revenuePage", revenuePage);   
        model.addAttribute("yearMonth", yearMonth);
        return "admin/revenues/index";
    }  
    
    
 
}
