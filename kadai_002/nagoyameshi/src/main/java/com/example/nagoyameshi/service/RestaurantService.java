package com.example.nagoyameshi.service;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

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
}