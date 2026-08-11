package com.example.nagoyameshi.form;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserEditForm {
    @NotNull
    private Integer id;
    
    @NotBlank(message = "氏名を入力してください。")
    private String name;
    
    @NotBlank(message = "フリガナを入力してください。")
    private String furigana;
    
    @NotBlank(message = "郵便番号を入力してください。")
    private String postalCode;
    
    @NotBlank(message = "住所を入力してください。")
    private String address;
    
    @NotBlank(message = "電話番号を入力してください。")
    private String phoneNumber;
    
    @NotNull(message = "年齢を入力してください。")
    @Min(value = 0, message = "1歳以上に設定してください。")
    private Integer age; 
    
    @NotNull(message = "職業を入力してください。")
    private Integer job;
    
    @NotBlank(message = "メールアドレスを入力してください。")
    private String email;
}