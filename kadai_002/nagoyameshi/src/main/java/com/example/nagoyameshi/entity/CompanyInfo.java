package com.example.nagoyameshi.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "company_info")
@Data
public class CompanyInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
        
    @Column(name = "name")
    private String name;  
    
    @Column(name = "address")
    private String address; 
    
    @Column(name = "representative")
    private String representative; 
    
    @Column(name = "estabilished_at")
    private String estabilishedAt; 
    
    @Column(name = "map_src")
    private String mapSrc;
    
    @Column(name = "updated_at", insertable = false, updatable = false)
    private Timestamp updatedAt;
}