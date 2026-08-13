
package com.example.nagoyameshi.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.nagoyameshi.entity.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {
	public Page<Restaurant> findByNameLike(String keyword, Pageable pageable);
	
    // 1. キーワード検索（名前または住所）
    Page<Restaurant> findByNameLikeOrAddressLike(String nameKeyword, String addressKeyword, Pageable pageable);  
    
    // 2. 価格での絞り込み
    Page<Restaurant> findByPriceLessThanEqual(Integer price, Pageable pageable); 
	    
	    public List<Restaurant> findTop10ByOrderByCreatedAtDesc();
	    public List<Restaurant> findTop5ByOrderByScoreDesc();
	    public List<Restaurant> findTop6ByOrderByPriceAsc();
	    // 👇 カテゴリーIDで店舗一覧を検索する（ページネーション対応）
	 // RestaurantCategories：Restaurant内の restaurantCategories リストを経由して_
	 // Category：そのリスト（中間テーブル）の中にある category の
	 // _Id：id が一致するものを検索する
	    Page<Restaurant> findByRestaurantCategories_Category_Id(Integer categoryId, Pageable pageable);
	    
	    Page<Restaurant> findByRestaurantCategories_Category_IdOrderByCreatedAtDesc(Integer categoryId, Pageable pageable);
	    Page<Restaurant> findByRestaurantCategories_Category_IdOrderByPriceAsc(Integer categoryId, Pageable pageable);
}