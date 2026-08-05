package com.example.nagoyameshi.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.nagoyameshi.entity.StripeCustomer;
import com.example.nagoyameshi.entity.User;

public interface StripeCustomerRepository extends JpaRepository<StripeCustomer, Integer> {
	public StripeCustomer findByUser(User user);
	public StripeCustomer findByStripeCustomerId(String stripeCustomerId);
}