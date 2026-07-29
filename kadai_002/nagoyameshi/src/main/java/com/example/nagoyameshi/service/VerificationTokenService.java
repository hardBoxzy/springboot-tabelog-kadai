package com.example.nagoyameshi.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.entity.VerificationToken;
import com.example.nagoyameshi.repository.VerificationTokenRepository;

@Service
public class VerificationTokenService {
    private final VerificationTokenRepository verificationTokenRepository;
    
    
    public VerificationTokenService(VerificationTokenRepository verificationTokenRepository) {        
        this.verificationTokenRepository = verificationTokenRepository;
    } 
    
    @Transactional
    public void create(User user, String token) {
        VerificationToken verificationToken = new VerificationToken();
        
        verificationToken.setUser(user);
        verificationToken.setToken(token);        
        
        verificationTokenRepository.save(verificationToken);
    }    
    
    @Transactional
    public void update(User user, String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByUser(user);
        
        verificationToken.setToken(token);        
        
        verificationTokenRepository.save(verificationToken);
    }
    
    // トークンの文字列で検索した結果を返す
    public boolean isExist(User user) {
        
    	VerificationToken verificationToken = verificationTokenRepository.findByUser(user);
    	return verificationToken != null;
    } 
    
    // トークンの文字列で検索した結果を返す
    public VerificationToken getVerificationToken(String token) {
        return verificationTokenRepository.findByToken(token);
    }    
}