package com.example.nagoyameshi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.nagoyameshi.entity.CompanyInfo;

public interface CompanyInfoRepository extends JpaRepository<CompanyInfo, Integer> {

}