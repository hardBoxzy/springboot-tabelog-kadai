package com.example.nagoyameshi.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CompanyInfoEditForm {
    @NotNull
    private Integer id;    
    
    @NotBlank(message = "会社名を入力してください。")
    private String name; 
    
    @NotBlank(message = "住所を入力してください。")
    private String address;
    
    @NotBlank(message = "代表者を入力してください。")
    private String representative;
    
    @NotBlank(message = "設立日を入力してください。")
    private String estabilishedAt;
    
    @NotBlank(message = "GoogleMapの埋め込みsrcを入力してください。")
    private String mapSrc;
    

}

