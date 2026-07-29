
package com.example.nagoyameshi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.nagoyameshi.entity.Restaurant;


public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {
	public Page<Restaurant> findByNameLike(String keyword, Pageable pageable);
	
	 public Page<Restaurant> findByNameLikeOrAddressLike(String nameKeyword, String addressKeyword, Pageable pageable);    
	    public Page<Restaurant> findByAddressLike(String area, Pageable pageable);
	    public Page<Restaurant> findByPriceLessThanEqual(Integer price, Pageable pageable); 
	    
	    // 👇 カテゴリーIDで店舗一覧を検索する（ページネーション対応）
	 // RestaurantCategories：Restaurant内の restaurantCategories リストを経由して_
	 // Category：そのリスト（中間テーブル）の中にある category の
	 // _Id：id が一致するものを検索する
	    Page<Restaurant> findByRestaurantCategories_Category_Id(Integer categoryId, Pageable pageable);

}