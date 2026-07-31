package com.example.nagoyameshi.form;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReservationInputForm {
    @NotBlank(message = "チェックイン日を選択してください。")
    private String fromCheckinDateToCheckoutDate;    
    
    @NotNull(message = "宿泊人数を入力してください。")
    @Min(value = 1, message = "宿泊人数は1人以上に設定してください。")
    private Integer numberOfPeople; 

    // チェックイン日を取得する
    public LocalDateTime getCheckinDate() {
        // スペース区切りのフォーマットを定義
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    	
    	return LocalDateTime.parse(fromCheckinDateToCheckoutDate, formatter);
    }

//    // チェックアウト日を取得する
//    public LocalDate getCheckoutDate() {
//        String[] checkinDateAndCheckoutDate = getFromCheckinDateToCheckoutDate().split(" から ");
//        return LocalDate.parse(checkinDateAndCheckoutDate[1]);
//    }
}