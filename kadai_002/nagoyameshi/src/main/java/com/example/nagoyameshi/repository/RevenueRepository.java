package com.example.nagoyameshi.repository;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.nagoyameshi.entity.Revenue;
import com.example.nagoyameshi.entity.User;

public interface RevenueRepository extends JpaRepository<Revenue, Integer> {
	 public Page<Revenue> findAllByOrderByCreatedAtDesc(Pageable pageable);
	 public Page<Revenue> findByUser(User user, Pageable pageable);
//	 public Page<Revenue> findByCreatedAtLikeOrderByCreatedAtDesc(String yearMonth, Pageable pageable);
	// ★ LIKE から Between (範囲検索) に変更します
	    public Page<Revenue> findByCreatedAtBetweenOrderByCreatedAtDesc(LocalDateTime start, LocalDateTime end, Pageable pageable);
	 // ★ 特定の期間内の amount の合計値を計算するクエリを追加
	    @Query("SELECT SUM(r.amount) FROM Revenue r WHERE r.createdAt >= :start AND r.createdAt < :end")
	    public Long sumAmountByCreatedAtBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
	}