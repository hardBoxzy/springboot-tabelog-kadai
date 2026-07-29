package com.example.nagoyameshi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.entity.RestaurantCategory;
import com.example.nagoyameshi.entity.RestaurantCategoryId;

public interface RestaurantCategoryRepository extends JpaRepository<RestaurantCategory, RestaurantCategoryId> {
    // 店舗に紐づく中間テーブルのデータをすべて取得するメソッド
    List<RestaurantCategory> findByRestaurant(Restaurant restaurant);
}
