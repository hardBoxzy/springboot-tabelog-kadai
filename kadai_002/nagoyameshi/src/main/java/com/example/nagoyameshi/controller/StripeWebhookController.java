package com.example.nagoyameshi.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.example.nagoyameshi.entity.StripeCustomer;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.repository.StripeCustomerRepository;
import com.example.nagoyameshi.repository.UserRepository;
import com.example.nagoyameshi.security.UserDetailsImpl;
import com.example.nagoyameshi.service.StripeService;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class StripeWebhookController {
    private final StripeService stripeService;
    private final UserRepository userRepository;
    private final StripeCustomerRepository stripeCustomerRepository;
    @Value("${stripe.api-key}")
    private String stripeApiKey;

    @Value("${stripe.webhook-secret}")
    private String webhookSecret;

    public StripeWebhookController(StripeService stripeService, UserRepository userRepository, StripeCustomerRepository stripeCustomerRepository) {
        this.stripeService = stripeService;
        this.userRepository = userRepository;
        this.stripeCustomerRepository = stripeCustomerRepository;
    }

    @PostMapping("/stripe/webhook")
    public ResponseEntity<String> webhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {
        Stripe.apiKey = stripeApiKey;
        Event event = null;

        try {
            event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
        } catch (SignatureVerificationException e) {
        	
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        
        switch (event.getType()) {
	        // 初回および2回目以降の「支払い成功時」の処理
	        case "checkout.session.completed":
	            stripeService.processSessionCompleted(event);
	            break;
	            
	        // 支払いが最終的に失敗し「サブスクリプションが失効したとき」の処理
	        case "customer.subscription.deleted":
	            stripeService.processWebhookEvent(event); // 先ほど作成したメソッドを呼び出す
	            break;
	            
	        default:
	            System.out.println("未処理のイベント: " + event.getType());
	            break;
	    }

        return new ResponseEntity<>("Success", HttpStatus.OK);
    }
    
    @GetMapping("/user/portal")
    public String redirectToPortal(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, HttpServletRequest request) {
        User user = userRepository.getReferenceById(userDetailsImpl.getUser().getId());
        StripeCustomer stripeCustomer = stripeCustomerRepository.findByUser(user);
        String stripeCustomerId = stripeCustomer.getStripeCustomerId();
        
        String portalUrl = stripeService.createPortalSession(stripeCustomerId, request);
        
        // Stripeが用意した管理・解約画面へジャンプ
        return "redirect:" + portalUrl;
    }
}