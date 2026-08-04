package com.example.nagoyameshi.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 複合主キーのためのクラス
@Data
@NoArgsConstructor  // ★追加：引数なしのコンストラクタを自動生成
@AllArgsConstructor // ★追加：すべてのフィールドを受け取るpublicコンストラクタを自動生成
public class RestaurantCategoryId implements Serializable {
    private Integer restaurant; // エンティティのフィールド名と一致させる
    private Integer category;
    
   
}