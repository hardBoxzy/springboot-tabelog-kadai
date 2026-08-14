package com.example.nagoyameshi.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.nagoyameshi.entity.StripeCustomer;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.repository.StripeCustomerRepository;
import com.example.nagoyameshi.repository.UserRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.StripeObject;
import com.stripe.model.Subscription;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.checkout.SessionRetrieveParams;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class StripeService {
	private final UserService userService;
	private final StripeCustomerRepository stripeCustomerRepository;
	private final UserRepository userRepository;
    public StripeService(UserService userService, StripeCustomerRepository stripeCustomerRepository,UserRepository userRepository) {
        this.userService = userService;
        this.stripeCustomerRepository = stripeCustomerRepository;
        this.userRepository = userRepository;
    } 
	@Value("${stripe.api-key}")
    private String stripeApiKey;
	
    // サブスクリプション用セッションを作成し、StripeのセッションIDを返す
    // ※引数は必要に応じて呼び出し元（Controller）に合わせて調整してください
    public String createStripeSession(Integer userId, HttpServletRequest httpServletRequest) {
        Stripe.apiKey =stripeApiKey; // 本番環境では環境変数等から取得を推奨
        String requestUrl = new String(httpServletRequest.getRequestURL());
        
        SessionCreateParams.Builder paramsBuilder =
            SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .addLineItem(
                    SessionCreateParams.LineItem.builder()
                        .setPriceData(
                            SessionCreateParams.LineItem.PriceData.builder()   
                                .setProductData(
                                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName("有料会員（月額プラン）") // サービス名やプラン名
                                        .build())
                                .setUnitAmount(300L) // 月額300円
                                .setCurrency("jpy")     
                                .setRecurring(
                                    SessionCreateParams.LineItem.PriceData.Recurring.builder()
                                        .setInterval(SessionCreateParams.LineItem.PriceData.Recurring.Interval.MONTH) //頻度（MONTH：月ごと、DAY:日ごと
                                        .build())							
                                .build())
                        .setQuantity(1L)
                        .build())
                .setMode(SessionCreateParams.Mode.SUBSCRIPTION) // ★サブスクリプションモードに変更
                .setSuccessUrl(requestUrl.replaceAll("/subscription", "") + "?subscribed=true") // 成功時のリダイレクト先（例: マイページ）
                .setCancelUrl(requestUrl.replaceAll("/subscription", "") + "?canceled=true")             // キャンセル時のリダイレクト先
                .setSubscriptionData(
                    SessionCreateParams.SubscriptionData.builder()
                        .putMetadata("userId", userId.toString()) // ★Webhookで誰の決済か判別するためにIDを格納
                        .build());
        
        // すでにStripe顧客IDを持っていればそれを再利用する
        User user = userRepository.getReferenceById(userId);
        StripeCustomer stripeCustomer = stripeCustomerRepository.findByUser(user);
        String existingCustomerId = null;
        if (stripeCustomer != null) {
            existingCustomerId = stripeCustomer.getStripeCustomerId();
            paramsBuilder.setCustomer(existingCustomerId);
        }
        
        try {
            Session session = Session.create(paramsBuilder.build());
            return session.getId();
        } catch (StripeException e) {
            e.printStackTrace();
            return "";
        }
    } 
    
 // セッションから予約情報を取得し、ReservationServiceクラスを介してデータベースに登録する  
    public void processSessionCompleted(Event event) {
    	// 引数として受け取ったEventオブジェクトからStripeObjectオブジェクトを取得し
        Optional<StripeObject> optionalStripeObject = event.getDataObjectDeserializer().getObject();//Optionalとは、nullを持つ可能性のあるオブジェクトを扱うためのクラスのことです。Java8から導入されました
        // それをSessionオブジェクトに型変換しています。
        optionalStripeObject.ifPresentOrElse(stripeObject -> {
            Session session = (Session)stripeObject;
         // "payment_intent"情報を展開する（詳細情報を含める）ように指定したSessionRetrieveParamsオブジェクトを生成
            SessionRetrieveParams params = SessionRetrieveParams.builder().addExpand("subscription").build();

            try {
                session = Session.retrieve(session.getId(), params, null);
                // ★ サブスクリプションオブジェクトからメタデータを取得
                if (session.getSubscriptionObject() != null) {
                    Map<String, String> subscriptionMetadata = session.getSubscriptionObject().getMetadata();
                    String userIdStr = subscriptionMetadata.get("userId");// マップから "userId" の文字列（例: "15"）を取り出す
                    Integer userId = Integer.valueOf(userIdStr);// 文字列を Integer 型に変換する
                    // メタデータからuserIdを取り出す（userServiceの仕様に合わせてマップごと渡すか、String/Integerに変換して渡してください）
                    userService.changeRoleByUserId(userId, 2);
                    
                    // ★追加：SessionからStripeの顧客ID（cus_xxxx）を取得
                    String stripeCustomerId = session.getCustomer();
                    User user = userRepository.getReferenceById(userId);
                    
                    StripeCustomer existingStripeCustomer = stripeCustomerRepository.findByStripeCustomerId(stripeCustomerId);
                    if (existingStripeCustomer == null) {
	                    StripeCustomer stripeCustomer = new StripeCustomer();
	                    stripeCustomer.setUser(user);
	                    stripeCustomer.setStripeCustomerId(stripeCustomerId);
	                    stripeCustomerRepository.save(stripeCustomer);
                    }
                    System.out.println("有料会員へのアップグレード処理が成功しました。ユーザーID: " + subscriptionMetadata.get("userId"));
                } else {
                    System.out.println("サブスクリプション情報が見つかりませんでした。");
                }
            } catch (StripeException e) {
                e.printStackTrace();
            }
            
            System.out.println("予約一覧ページの登録処理が成功しました。");
            System.out.println("Stripe API Version: " + event.getApiVersion());
            System.out.println("stripe-java Version: " + Stripe.VERSION);
        },
        () -> {
            System.out.println("予約一覧ページの登録処理が失敗しました。");
            System.out.println("Stripe API Version: " + event.getApiVersion());
            System.out.println("stripe-java Version: " + Stripe.VERSION);
        });
    }
    
    public void processWebhookEvent(Event event) {
        Optional<StripeObject> optionalStripeObject = event.getDataObjectDeserializer().getObject();

        optionalStripeObject.ifPresent(stripeObject -> {
            Subscription subscription = (Subscription) stripeObject;
            Map<String, String> subscriptionMetadata = subscription.getMetadata();
            if (subscriptionMetadata != null && subscriptionMetadata.containsKey("userId")) {
                Integer userId = Integer.valueOf(subscriptionMetadata.get("userId"));
                userService.changeRoleByUserId(userId, 1); // 通常会員(1)に戻す
                System.out.println("支払失敗または解約のため、通常会員に降格しました。ユーザーID: " + userId);
            }           
        });
    }
    
    // カスタマーポータルのURLを発行するメソッド
    public String createPortalSession(String stripeCustomerId, HttpServletRequest httpServletRequest) {
        Stripe.apiKey = stripeApiKey;
        
        // 404エラーを防ぐため、リクエストURLから「スキーム + サーバー名 + ポート番号」のベースURLを安全に抽出する
        String scheme = httpServletRequest.getScheme();      // http
        String serverName = httpServletRequest.getServerName(); // localhost
        int serverPort = httpServletRequest.getServerPort();   // 8081
        String baseUrl = scheme + "://" + serverName + ":" + serverPort; // http://localhost:8081
        String returnUrl = baseUrl + "/user"; // ポータルから戻る先を「http://localhost:8081/user」に固定


        try {
        	//checkout.Sessionとは別で、billingportal.Sessionを作成
        	com.stripe.param.billingportal.SessionCreateParams params = com.stripe.param.billingportal.SessionCreateParams.builder()
                .setCustomer(stripeCustomerId) // 対象ユーザーのStripe顧客ID（cus_xxxx）
                .setReturnUrl(returnUrl) // ポータルから戻ってきたときの遷移先（マイページなど）
                .build();

        	com.stripe.model.billingportal.Session portalSession = com.stripe.model.billingportal.Session.create(params);
            return portalSession.getUrl(); // ポータル画面の専用URLを返す
        } catch (StripeException e) {
            e.printStackTrace();
            return "";
        }
    }
    
    
}	
    
