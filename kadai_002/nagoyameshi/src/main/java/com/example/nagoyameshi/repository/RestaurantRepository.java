
package com.example.nagoyameshi.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.nagoyameshi.entity.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {
	public Page<Restaurant> findByNameLike(String keyword, Pageable pageable);

	    public Page<Restaurant> findByNameLikeOrAddressLikeOrderByCreatedAtDesc(String nameKeyword, String addressKeyword, Pageable pageable);  
	    public Page<Restaurant> findByNameLikeOrAddressLikeOrderByPriceAsc(String nameKeyword, String addressKeyword, Pageable pageable);  
	    public Page<Restaurant> findByAddressLikeOrderByCreatedAtDesc(String area, Pageable pageable);
	    public Page<Restaurant> findByAddressLikeOrderByPriceAsc(String area, Pageable pageable);
	    public Page<Restaurant> findByPriceLessThanEqualOrderByCreatedAtDesc(Integer price, Pageable pageable);
	    public Page<Restaurant> findByPriceLessThanEqualOrderByPriceAsc(Integer price, Pageable pageable); 
	    public Page<Restaurant> findAllByOrderByCreatedAtDesc(Pageable pageable);
	    public Page<Restaurant> findAllByOrderByPriceAsc(Pageable pageable);   
	    
	    public List<Restaurant> findTop10ByOrderByCreatedAtDesc();
	    
	    // 👇 カテゴリーIDで店舗一覧を検索する（ページネーション対応）
	 // RestaurantCategories：Restaurant内の restaurantCategories リストを経由して_
	 // Category：そのリスト（中間テーブル）の中にある category の
	 // _Id：id が一致するものを検索する
	    Page<Restaurant> findByRestaurantCategories_Category_Id(Integer categoryId, Pageable pageable);
	    
	    Page<Restaurant> findByRestaurantCategories_Category_IdOrderByCreatedAtDesc(Integer categoryId, Pageable pageable);
	    Page<Restaurant> findByRestaurantCategories_Category_IdOrderByPriceAsc(Integer categoryId, Pageable pageable);
}