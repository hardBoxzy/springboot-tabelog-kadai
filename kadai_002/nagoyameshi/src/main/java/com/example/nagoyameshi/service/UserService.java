package com.example.nagoyameshi.service;


import java.time.format.DateTimeFormatter;
import java.util.List;

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
        Job job = jobRepository.getReferenceById(userEditForm.getJob());
        
        user.setName(userEditForm.getName());
        user.setFurigana(userEditForm.getFurigana());
        user.setPostalCode(userEditForm.getPostalCode());
        user.setAddress(userEditForm.getAddress());
        user.setPhoneNumber(userEditForm.getPhoneNumber());
        user.setEmail(userEditForm.getEmail()); 
        user.setAge(userEditForm.getAge());
        user.setJob(job);
        
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
    
    public String createCSVStr(List<User> users) {
 	   String result = "ID,名前,フリガナ,郵便番号,住所,電話番号,メール,パスワード,権限,アクティブ,年齢,職業,作成時間,更新時間";
 	   DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
 	   String content= "";
 	   for (User u : users) {
           String createdAtStr = "";
           if (u.getCreatedAt() != null) {
               createdAtStr = u.getCreatedAt().toLocalDateTime().format(formatter);
           }
           String updatedAtStr = "";
           if (u.getUpdatedAt() != null) {
           	updatedAtStr = u.getUpdatedAt().toLocalDateTime().format(formatter);
           }
           content =content+ "\n"+ String.format("%d,%s,%s,%s,%s, %s,%s,%s,%s,%d, %d,%s,%s,%s", 
               u.getId(), 
               escapeCsv(u.getName()), 
               escapeCsv(u.getFurigana()), 
               escapeCsv(u.getPostalCode()),
               escapeCsv(u.getAddress()),
               
               escapeCsv(u.getPhoneNumber()),
               escapeCsv(u.getEmail()), 
               escapeCsv(u.getPassword()), 
               escapeCsv(u.getRole().getName()), 
               u.getEnabled() ? 1 : 0, 
               
               u.getAge(),
               escapeCsv(u.getJob().getName()),
               createdAtStr,
               updatedAtStr
           );
 	   }
 	   result = result + content;
 	   return result;
    }
    
    // カンマや改行が含まれる文字列を安全にCSV用にエスケープする補助メソッド
    private String escapeCsv(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n") || value.contains("\r")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}