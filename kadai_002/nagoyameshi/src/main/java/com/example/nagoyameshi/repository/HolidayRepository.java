package com.example.nagoyameshi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.nagoyameshi.entity.Holiday;
import com.example.nagoyameshi.entity.Restaurant;

public interface HolidayRepository extends JpaRepository<Holiday, Integer> {
	@Modifying
    @Query("DELETE FROM Holiday holiday WHERE holiday.restaurant = :restaurant")
    void deleteByRestaurant(Restaurant restaurant);
}