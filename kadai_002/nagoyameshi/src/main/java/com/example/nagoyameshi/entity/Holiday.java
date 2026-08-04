package com.example.nagoyameshi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;



@Entity
@Table(name = "holidays")
@Data
public class Holiday {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;
    
    @Column(name = "day_id")
    private Integer dayId;
    
    // ゲッターを追加
    public String getName() {
    	String name = switch (this.dayId) {
    	case 0 -> "日";
    	case 1 -> "月";
        case 2 -> "火";
        case 3 -> "水";
        case 4 -> "木";
        case 5 -> "金";
        case 6 -> "土";
    	default ->"曜日の値が正しく入力されていません";
    	};
    	
    	
        return name;
    }
}