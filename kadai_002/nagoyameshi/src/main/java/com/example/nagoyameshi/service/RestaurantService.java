package com.example.nagoyameshi.service;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.nagoyameshi.entity.Category;
import com.example.nagoyameshi.entity.Holiday;
import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.entity.RestaurantCategory;
import com.example.nagoyameshi.form.RestaurantEditForm;
import com.example.nagoyameshi.form.RestaurantRegisterForm;
import com.example.nagoyameshi.repository.CategoryRepository;
import com.example.nagoyameshi.repository.HolidayRepository;
import com.example.nagoyameshi.repository.RestaurantCategoryRepository;
import com.example.nagoyameshi.repository.RestaurantRepository;

import jakarta.persistence.EntityManager;

@Service
public class RestaurantService {
	// 0. EntityManager を使えるようにクラスの先頭（フィールド）でDI（インジェクション）してください
	@Autowired
	private EntityManager entityManager;
   private final RestaurantRepository restaurantRepository;    
   private final CategoryRepository categoryRepository;
   private final RestaurantCategoryRepository restaurantCategoryRepository;
   private final HolidayRepository holidayRepository;
   public RestaurantService(RestaurantRepository restaurantRepository, 
		   CategoryRepository categoryRepository, RestaurantCategoryRepository restaurantCategoryRepository,
		   HolidayRepository holidayRepository) {
       this.restaurantRepository = restaurantRepository; 
       this.categoryRepository =  categoryRepository;
       this.restaurantCategoryRepository =  restaurantCategoryRepository;
       this.holidayRepository =  holidayRepository;
   }    
   
   @Transactional
   public void create(RestaurantRegisterForm restaurantRegisterForm) {
	   Restaurant restaurant = new Restaurant();        
       MultipartFile imageFile = restaurantRegisterForm.getImageFile();
       
       if (!imageFile.isEmpty()) {
           String imageName = imageFile.getOriginalFilename(); 
           String hashedImageName = generateNewFileName(imageName);
           Path filePath = Paths.get("src/main/resources/static/storage/" + hashedImageName);
           copyImageFile(imageFile, filePath);
           restaurant.setImageName(hashedImageName);
       }
       
       
       restaurant.setName(restaurantRegisterForm.getName());                
       restaurant.setDescription(restaurantRegisterForm.getDescription());
       restaurant.setPrice(restaurantRegisterForm.getPrice());
       restaurant.setCapacity(restaurantRegisterForm.getCapacity());
       restaurant.setPostalCode(restaurantRegisterForm.getPostalCode());
       restaurant.setAddress(restaurantRegisterForm.getAddress());
       restaurant.setPhoneNumber(restaurantRegisterForm.getPhoneNumber());
                   
       restaurantRepository.save(restaurant);
       
       // 2. 画面でチェックされたカテゴリーIDがあれば、中間テーブルに保存
       if (restaurantRegisterForm.getCategoryIds() != null) {
           for (Integer categoryId : restaurantRegisterForm.getCategoryIds()) {
               Category category = categoryRepository.findById(categoryId).orElseThrow();
               
               RestaurantCategory restaurantCategory = new RestaurantCategory();
               restaurantCategory.setRestaurant(restaurant); // 保存直後の店舗オブジェクトをセット
               restaurantCategory.setCategory(category);
               
               restaurantCategoryRepository.save(restaurantCategory);
           }
       }
   }  
   
   
   
   @Transactional
   public void update(RestaurantEditForm restaurantEditForm) {
      Restaurant restaurant = restaurantRepository.getReferenceById(restaurantEditForm.getId());
       MultipartFile imageFile = restaurantEditForm.getImageFile();
       
       if (!imageFile.isEmpty()) {
           String imageName = imageFile.getOriginalFilename(); 
           String hashedImageName = generateNewFileName(imageName);
           Path filePath = Paths.get("src/main/resources/static/storage/" + hashedImageName);
           copyImageFile(imageFile, filePath);
           restaurant.setImageName(hashedImageName);
       }
       
       restaurant.setName(restaurantEditForm.getName());                
       restaurant.setDescription(restaurantEditForm.getDescription());
       restaurant.setPrice(restaurantEditForm.getPrice());
       restaurant.setCapacity(restaurantEditForm.getCapacity());
       restaurant.setPostalCode(restaurantEditForm.getPostalCode());
       restaurant.setAddress(restaurantEditForm.getAddress());
       restaurant.setPhoneNumber(restaurantEditForm.getPhoneNumber());
       restaurantRepository.save(restaurant);
       
    // 👇【重要】1. まず、この店舗に紐づいている既存の中間テーブルデータをすべて削除する
       restaurantCategoryRepository.deleteByRestaurant(restaurant);
    // 👇【重要】これを追加：削除のSQLを今すぐデータベースに強制実行（反映）させる
       restaurantCategoryRepository.flush();
       
    // ★店舗編集ページでいじっていないカテゴリーが消えないようにするためのエラー対策
       //【重要】JPAのメモリキャッシュを完全にクリアし、古いデータの記憶を消し去る 
       entityManager.clear();
       // ※注意：JPAをクリアしたため、引数の restaurant オブジェクトもJPAの管理外になります。
       // 安全のため、DBから最新の状態の restaurant を1回再取得します。
       Restaurant freshRestaurant = restaurantRepository.findById(restaurant.getId()).orElseThrow();
      
       
	// 2. 画面でチェックされたカテゴリーIDがあれば、中間テーブルに保存
       if (restaurantEditForm.getCategoryIds() != null) {
           for (Integer categoryId : restaurantEditForm.getCategoryIds()) {
               Category category = categoryRepository.findById(categoryId).orElseThrow();
                              
               RestaurantCategory restaurantCategory = new RestaurantCategory();
               
               restaurantCategory.setRestaurant(freshRestaurant); // // 再取得した店舗オブジェクトをセット
               restaurantCategory.setCategory(category);
               
               restaurantCategoryRepository.save(restaurantCategory);
               
//               //debug
//               RestaurantCategoryId id = new RestaurantCategoryId(restaurant.getId(), categoryId);
//               RestaurantCategory rc = restaurantCategoryRepository.getReferenceById(id);
//               System.out.println(rc.getCategory().getName());
              
           }
       }
       
       
       
       holidayRepository.deleteByRestaurant(restaurant);
//       holidayRepository.flush();
       if (restaurantEditForm.getHolidays() != null) {
           for (Integer dayId : restaurantEditForm.getHolidays()) {              
               Holiday holiday = new Holiday();
               holiday.setRestaurant(restaurant); 
               holiday.setDayId(dayId);              
               holidayRepository.save(holiday);
           }
       }       
   }
    
   
   
   // UUIDを使って生成したファイル名を返す
   public String generateNewFileName(String fileName) {
       String[] fileNames = fileName.split("\\.");                
       for (int i = 0; i < fileNames.length - 1; i++) {
           fileNames[i] = UUID.randomUUID().toString();            
       }
       String hashedFileName = String.join(".", fileNames);
       return hashedFileName;
   }     
   
   // 画像ファイルを指定したファイルにコピーする
   public void copyImageFile(MultipartFile imageFile, Path filePath) {           
       try {
           Files.copy(imageFile.getInputStream(), filePath);
       } catch (IOException e) {
           e.printStackTrace();
       }          
   } 
   
   // RestaurantCategory型のListをInteger型に変換
   public List<Integer> getCategoryIds(Restaurant restaurant) {
		List<RestaurantCategory> restaurantCategories = restaurantCategoryRepository.findByRestaurant(restaurant);
		// 1. 中間テーブルのリストから、カテゴリーID（Integer）のリストを抽出する
	   List<Integer> categoryIds = restaurantCategories.stream()//forの代わりに使用、restaurantCategories＝{  "restaurant": 10, "category": { "id": 3, "name": "ラーメン" } }, {  "restaurant": 10, "category": { "id": 5, "name": "中華" } }
	           .map(restaurantCategory -> restaurantCategory.getCategory().getId())//mapは流れてきたデータを、別の形に変換（マッピング）します。getCategory()＝{ "id": 3, "name": "ラーメン" }、getId()＝"id": 3
	           .toList();// 新しい List にまとめる
	   return categoryIds;
   }
   
   // Holiday型のListをInteger型に変換
   public List<Integer> getDayIds(Restaurant restaurant) {
	   List<Holiday> holidays = restaurant.getHolidays();
	   List<Integer> dayIds = holidays.stream()
	           .map(holiday -> holiday.getDayId())
	           .toList();
	   return dayIds;
   }
   
   public String createCSVStr(List<Restaurant> restaurants) {
	   String result = "店舗ID,店舗名,画像名,説明,料金,人数,郵便番号,住所,電話番号,定休日,カテゴリー,作成時間,更新時間";
	   DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	   String content= "";
	   for (Restaurant r : restaurants) {
      	 String holidaysStr = "";
           if (r.getHolidays() != null) {
               holidaysStr = r.getHolidays().stream()
                   .map(holiday -> String.valueOf(holiday.getDayId())) // 各Holidayオブジェクトから曜日名（文字列）を抽出
                   .collect(Collectors.joining(" & ")); // それらを「 & 」で繋ぐ
           }
           String restaurantCategoriesStr = "";
           if (r.getRestaurantCategories() != null) {
          	 restaurantCategoriesStr = r.getRestaurantCategories().stream()
                   .map(restaurantCategory -> restaurantCategory.getCategory().getName())
                   .collect(Collectors.joining(" & ")); // それらを「 & 」で繋ぐ
           }
          String createdAtStr = "";
          if (r.getCreatedAt() != null) {
              createdAtStr = r.getCreatedAt().toLocalDateTime().format(formatter);
          }
          String updatedAtStr = "";
          if (r.getUpdatedAt() != null) {
          	updatedAtStr = r.getCreatedAt().toLocalDateTime().format(formatter);
          }
          content =content+ "\n"+ String.format("%d,%s,%s,%s,%d,%d,%s,%s,%s,%s,%s,%s,%s", 
              r.getId(), 
              escapeCsv(r.getName()), 
              escapeCsv(r.getImageName()), 
              escapeCsv(r.getDescription()),
              r.getPrice(),
              r.getCapacity(),
              escapeCsv(r.getPostalCode()), 
              escapeCsv(r.getAddress()), 
              escapeCsv(r.getPhoneNumber()), 
              escapeCsv(holidaysStr),
              escapeCsv(restaurantCategoriesStr),
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
   
   public List<Restaurant> createRestaurantsByCSV(CSVParser csvParser) {
	// 出力時と同じフォーマット（秒まで。スラッシュとハイフンの両方に対応できるようにします）
       // ユーザーがExcelで編集して「2026/08/11 15:30:00」のようになっていても読み込めるようにフォーマットを作成
       DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[yyyy-MM-dd HH:mm:ss][yyyy/MM/dd HH:mm:ss][yyyy-MM-dd HH:mm][yyyy/MM/dd HH:mm]");
 
	   List<Restaurant> restaurantList = new ArrayList<>();
       Iterable<CSVRecord> csvRecords = csvParser.getRecords();

       for (CSVRecord record : csvRecords) {
           Restaurant restaurant = new Restaurant();
           
           // ⚠️ 新規登録なので ID (カラム1) はセットせず、DBの自動採番に任せる
           // カラム名（ヘッダー文字列）でデータを取得します
           restaurant.setName(record.get("店舗名"));
           restaurant.setImageName(record.get("画像名"));
           restaurant.setDescription(record.get("説明"));
           restaurant.setPrice(Integer.parseInt(record.get("料金")));
           restaurant.setCapacity(Integer.parseInt(record.get("人数")));
           restaurant.setPostalCode(record.get("郵便番号"));
           restaurant.setAddress(record.get("住所"));
           restaurant.setPhoneNumber(record.get("電話番号"));

        // 2. 日時のパース（例 2026-07-25 20:21:02 ）
           if (!record.get("作成時間").isEmpty()) {
               LocalDateTime parsed = LocalDateTime.parse(record.get("作成時間"), formatter);
               restaurant.setCreatedAt(Timestamp.valueOf(parsed));
           }
           if (!record.get("更新時間").isEmpty()) {
               LocalDateTime parsed = LocalDateTime.parse(record.get("更新時間"), formatter);
               restaurant.setUpdatedAt(Timestamp.valueOf(parsed));
           }
           restaurantRepository.save(restaurant); 
           // ---  紐づく中間テーブル（カテゴリー等）の処理
           // ※まずは店舗の基本データが登録できることを最優先にします
        // 3. 定休日（Holiday）の登録処理
           String holidaysField = record.get("定休日");
           if (holidaysField != null && !holidaysField.isEmpty()) {
               // "2 & 3" を ["2", "3"] に分解してループ
               String[] dayIds = holidaysField.split("\\s*&\\s*");
               for (String dayIdStr : dayIds) {
                   Holiday holiday = new Holiday();
                   holiday.setRestaurant(restaurant); // 保存した店舗を紐づけ
                   holiday.setDayId(Integer.parseInt(dayIdStr));
                   holidayRepository.save(holiday); // holidayRepositoryが必要です
               }
           }

           // 4. カテゴリー（RestaurantCategory）の登録処理
           String categoriesField = record.get("カテゴリー");
           if (categoriesField != null && !categoriesField.isEmpty()) {
               // "居酒屋 & カフェ" を ["居酒屋", "カフェ"] に分解
               String[] categoryNames = categoriesField.split("\\s*&\\s*");
               for (String name : categoryNames) {
                   // カテゴリー名からデータベースにあるCategoryを取得
                   Category category = categoryRepository.findByName(name); // メソッドがある前提
                   if (category != null) {
                       RestaurantCategory rc = new RestaurantCategory();
                       rc.setRestaurant(restaurant);
                       rc.setCategory(category);
                       restaurantCategoryRepository.save(rc); // 中間テーブルのリポジトリが必要です
                   }
               }
           }
           
           restaurantList.add(restaurant);
       }
	   return restaurantList;
   }
}