package com.example.nagoyameshi.entity;

import java.io.Serializable;

import lombok.Data;

// 複合主キーのためのクラス
@Data
public class RestaurantCategoryId implements Serializable {
    private Integer restaurant; // エンティティのフィールド名と一致させる
    private Integer category;
}