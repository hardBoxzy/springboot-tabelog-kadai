package com.example.nagoyameshi.entity;

import java.sql.Timestamp;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "restaurants")
@Data
@NoArgsConstructor
@jakarta.persistence.EntityListeners(com.example.nagoyameshi.config.S3UrlInterceptor.class)
public class Restaurant {
    
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "image_name")
    private String imageName;
    
    
    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Integer price;

    @Column(name = "capacity")
    private Integer capacity;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "address")
    private String address;

    @Column(name = "phone_number")
    private String phoneNumber;
    
    @Column(name = "score")
    private Integer score= 0;
    
    @Column(name = "created_at", insertable = false, updatable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private Timestamp updatedAt;
    //fetch = FetchType.EAGER（即時読み込み）にすると、店舗（Restaurant）のデータを1件取得するだけで、毎回自動的にカテゴリーや定休日のデータも同時にデータベースから裏で取得（JOIN）されます。
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<RestaurantCategory> restaurantCategories;
    
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Holiday> holidays;
    //cascade = CascadeType.REMOVE親の削除を子にも連動  orphanRemoval = true親との繋がりを失った子（＝孤児：orphan）を、自動的にデータベース
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Reservation> reservations;
    
    // ゲッターを追加
    public List<RestaurantCategory> getRestaurantCategories() {
        return this.restaurantCategories;
    }
    
    // ゲッターを追加
    public List<Holiday> getHolidays() {
        return this.holidays;
    }
    
    @Transient
    private String s3UrlBase;

    // 💡 ゲッターでこれらを結合して返却する
    public String getImagePath() {
        if (this.imageName == null || this.imageName.isEmpty()) {
            return null;
        }
        // もし画像名がすでにフルURL（過去のデータなど）ならそのまま返す安全策
        if (this.imageName.startsWith("http")) {
            return this.imageName;
        }
        return this.s3UrlBase + "/" + this.imageName;
    }
}