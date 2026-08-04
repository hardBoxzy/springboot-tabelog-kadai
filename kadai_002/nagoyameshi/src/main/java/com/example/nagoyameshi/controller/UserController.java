package com.example.nagoyameshi.controller;


import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.form.UserEditForm;
import com.example.nagoyameshi.repository.UserRepository;
import com.example.nagoyameshi.security.UserDetailsImpl;
import com.example.nagoyameshi.service.StripeService;
import com.example.nagoyameshi.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/user")
public class UserController {
    private final UserRepository userRepository;    
    private final UserService userService; 
    private final StripeService stripeService;
    public UserController(UserRepository userRepository, UserService userService, StripeService stripeService) {
            this.userRepository = userRepository;     
            this.userService = userService;    
            this.stripeService = stripeService;  
    }    
    
    @GetMapping
    public String index(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, Model model) {         
        User user = userRepository.getReferenceById(userDetailsImpl.getUser().getId());  
        
        model.addAttribute("user", user);
        
        return "user/index";
    }
    
    @GetMapping("/edit")
    public String edit(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, Model model) {        
        User user = userRepository.getReferenceById(userDetailsImpl.getUser().getId());  
        UserEditForm userEditForm = new UserEditForm(user.getId(), user.getName(), user.getFurigana(), user.getPostalCode(), user.getAddress(), user.getPhoneNumber(), user.getEmail());
        
        model.addAttribute("userEditForm", userEditForm);
        
        return "user/edit";
    } 
    
    @PostMapping("/update")
    public String update(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,@ModelAttribute @Validated UserEditForm userEditForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        // メールアドレスが変更されており、かつ登録済みであれば、BindingResultオブジェクトにエラー内容を追加する
        if (userService.isEmailChanged(userEditForm) && userService.isEmailRegistered(userEditForm.getEmail())) {
            FieldError fieldError = new FieldError(bindingResult.getObjectName(), "email", "すでに登録済みのメールアドレスです。");
            bindingResult.addError(fieldError);                       
        }
        
        if (bindingResult.hasErrors()) {
            return "user/edit";
        }
        
        userService.update(userEditForm);
        redirectAttributes.addFlashAttribute("successMessage", "会員情報を編集しました。");
        
     // --- ユーザー情報の更新処理の直後に追加します ---

     // 1. 最新のユーザー情報（更新後のUserエンティティ）を使って、新しいUserDetailsを作成する
     // ※教材の仕様に合わせて、新しくUserDetailsImplをnewするか、既存のログイン主体のオブジェクトを書き換えます。
        User user = userRepository.getReferenceById(userDetailsImpl.getUser().getId());
        Collection<GrantedAuthority> authorities = new ArrayList<>();         
        authorities.add(new SimpleGrantedAuthority(user.getRole().getName()));
        UserDetailsImpl newUserDetails = new UserDetailsImpl(user, authorities);

     // 2. 新しい認証オブジェクト（Authentication）を作成する
     Authentication newAuth = new UsernamePasswordAuthenticationToken(
         newUserDetails, 
         newUserDetails.getPassword(), 
         newUserDetails.getAuthorities()
     );

     // 3. Spring Securityのコンテキストに新しい認証情報をセットして、セッションを上書きする
     SecurityContextHolder.getContext().setAuthentication(newAuth);
     
        return "redirect:/user";
    } 
    
    @GetMapping("/subscription")
    public String subscription(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,HttpServletRequest httpServletRequest, Model model) {        
         
      //StripeServiceクラスに定義したcreateStripeSession()メソッドを実行してセッションIDを取得し、それをビューに渡す処理
        String sessionId = stripeService.createStripeSession(userDetailsImpl.getUser().getId(), httpServletRequest);
        model.addAttribute("sessionId", sessionId);
        return "user/subscription/confirm";
    }
}
