package com.example.nagoyameshi.service;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagoyameshi.entity.Job;
import com.example.nagoyameshi.entity.Role;
import com.example.nagoyameshi.entity.TemperaryPassword;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.form.SignupForm;
import com.example.nagoyameshi.form.UserEditForm;
import com.example.nagoyameshi.form.UserPasswordEditForm;
import com.example.nagoyameshi.repository.JobRepository;
import com.example.nagoyameshi.repository.RoleRepository;
import com.example.nagoyameshi.repository.TemperaryPasswordRepository;
import com.example.nagoyameshi.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JobRepository jobRepository;
    private final PasswordEncoder passwordEncoder;
    private final TemperaryPasswordRepository temperaryPasswordRepository;
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder,JobRepository jobRepository,TemperaryPasswordRepository temperaryPasswordRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;        
        this.passwordEncoder = passwordEncoder;
        this.jobRepository = jobRepository;
        this.temperaryPasswordRepository  = temperaryPasswordRepository;
    }    
    
    @Transactional
    public User create(SignupForm signupForm) {
        User user = new User();
        Role role = roleRepository.findByName("ROLE_GENERAL");
        Job job = jobRepository.getReferenceById(signupForm.getJob());
        
        user.setName(signupForm.getName());
        user.setFurigana(signupForm.getFurigana());
        user.setPostalCode(signupForm.getPostalCode());
        user.setAddress(signupForm.getAddress());
        user.setPhoneNumber(signupForm.getPhoneNumber());
        user.setEmail(signupForm.getEmail());
        user.setPassword(passwordEncoder.encode(signupForm.getPassword()));
        user.setRole(role);
        user.setAge(signupForm.getAge());
        user.setJob(job);
        user.setEnabled(false);        
        
        return userRepository.save(user);
    }   
    
    @Transactional
    public void update(UserEditForm userEditForm) {
        User user = userRepository.getReferenceById(userEditForm.getId());
        
        user.setName(userEditForm.getName());
        user.setFurigana(userEditForm.getFurigana());
        user.setPostalCode(userEditForm.getPostalCode());
        user.setAddress(userEditForm.getAddress());
        user.setPhoneNumber(userEditForm.getPhoneNumber());
        user.setEmail(userEditForm.getEmail());      
        
        userRepository.save(user);
    } 
    
    @Transactional
    public TemperaryPassword createTemperaryPassword(UserPasswordEditForm userPasswordEditForm) {
    	User user = userRepository.findByEmail(userPasswordEditForm.getEmail());
    	TemperaryPassword temperaryPassword = temperaryPasswordRepository.findByUser(user);
    	String password = passwordEncoder.encode(userPasswordEditForm.getPassword());
    	if(temperaryPassword != null){
    		temperaryPassword.setPassword(password);
    	}else {
    		temperaryPassword = new TemperaryPassword();
    		temperaryPassword.setUser(user);
    		temperaryPassword.setPassword(password);
    	}
        return temperaryPasswordRepository.save(temperaryPassword);
    } 
    
    @Transactional
    public User userPasswordEdit( User user) {
        TemperaryPassword temperaryPassword = temperaryPasswordRepository.findByUser(user);
        user.setPassword(temperaryPassword.getPassword());
        return userRepository.save(user);
    } 
    
    // メールアドレスが登録済みかどうかをチェックする
    public boolean isEmailRegistered(String email) {
        User user = userRepository.findByEmail(email);  
        return user != null;
    }
    
    public User findByEmail(String email) {
        User user = userRepository.findByEmail(email);  
        return user ;
    }
    
    
    
    // メールアドレスで探し出したアカウントがアクティブかどうかをチェックする
    public boolean isEnabled(String email) {
        User user = userRepository.findByEmail(email);
        if(user != null) {
        	return user.getEnabled();
        }
        return false;
    }
    
    // パスワードとパスワード（確認用）の入力値が一致するかどうかをチェックする
    public boolean isSamePassword(String password, String passwordConfirmation) {
        return password.equals(passwordConfirmation);
    }  
    
    // ユーザーを有効にする
    @Transactional
    public void enableUser(User user) {
        user.setEnabled(true); 
        userRepository.save(user);
    }  
    
 // ユーザーを有効にする
    @Transactional
    public void changeRoleByUserId(Integer userId,Integer roleId) {
    	User user = userRepository.getReferenceById(userId);
    	user.setRole(roleRepository.getReferenceById(roleId));
        userRepository.save(user);
    } 
    
    // メールアドレスが変更されたかどうかをチェックする
    public boolean isEmailChanged(UserEditForm userEditForm) {
        User currentUser = userRepository.getReferenceById(userEditForm.getId());
        return !userEditForm.getEmail().equals(currentUser.getEmail());      
    }  
}