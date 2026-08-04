package com.example.nagoyameshi.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
    
    public StripeService(UserService userService) {
        this.userService = userService;
    } 
	@Value("${stripe.api-key}")
    private String stripeApiKey;
	
    // サブスクリプション用セッションを作成し、StripeのセッションIDを返す
    // ※引数は必要に応じて呼び出し元（Controller）に合わせて調整してください
    public String createStripeSession(Integer userId, HttpServletRequest httpServletRequest) {
        Stripe.apiKey =stripeApiKey; // 本番環境では環境変数等から取得を推奨
        String requestUrl = new String(httpServletRequest.getRequestURL());

        SessionCreateParams params =
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
                                        .setInterval(SessionCreateParams.LineItem.PriceData.Recurring.Interval.DAY) // ★日次
                                        .build())							
                                .build())
                        .setQuantity(1L)
                        .build())
                .setMode(SessionCreateParams.Mode.SUBSCRIPTION) // ★サブスクリプションモードに変更
                .setSuccessUrl(requestUrl + "?subscribed") // 成功時のリダイレクト先（例: マイページ）
                .setCancelUrl(requestUrl + "?canceled")             // キャンセル時のリダイレクト先
                .setSubscriptionData(
                    SessionCreateParams.SubscriptionData.builder()
                        .putMetadata("userId", userId.toString()) // ★Webhookで誰の決済か判別するためにIDを格納
                        .build())
                .build();
        try {
            Session session = Session.create(params);
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
            try {
                switch (event.getType()) {
                    
                    // パターンA：決済が成功したとき（初回および2回目以降の自動更新もこれでカバー可能）
                    case "checkout.session.completed":
                        Session session = (Session) stripeObject;
                        
                        // サブスクリプション詳細を展開するパラメータ
                        SessionRetrieveParams params = SessionRetrieveParams.builder().addExpand("subscription").build();
                        session = Session.retrieve(session.getId(), params, null);
                        
                        if (session.getSubscriptionObject() != null) {
                            Map<String, String> subscriptionMetadata = session.getSubscriptionObject().getMetadata();
                            String userIdStr = subscriptionMetadata.get("userId");
                            
                            if (userIdStr != null) {
                                Integer userId = Integer.valueOf(userIdStr);
                                userService.changeRoleByUserId(userId, 2); // 有料会員(2)へ
                                System.out.println("有料会員へのアップグレード/継続処理が成功しました。ユーザーID: " + userId);
                            }
                        } else {
                            System.out.println("サブスクリプション情報が見つかりませんでした。");
                        }
                        break;

                    // パターンB：支払いが最終的に失敗し、サブスクリプションが失効したとき
                    case "customer.subscription.deleted":
                        Subscription subscription = (Subscription) stripeObject;
                        Map<String, String> subscriptionMetadata = subscription.getMetadata();
                        
                        if (subscriptionMetadata != null && subscriptionMetadata.containsKey("userId")) {
                            Integer userId = Integer.valueOf(subscriptionMetadata.get("userId"));
                            userService.changeRoleByUserId(userId, 1); // 通常会員(1)に戻す
                            System.out.println("支払失敗または解約のため、通常会員に降格しました。ユーザーID: " + userId);
                        }
                        break;
                        
                    default:
                        System.out.println("未処理のイベント: " + event.getType());
                        break;
                }
            } catch (StripeException e) {
                e.printStackTrace();
            }
        });
    }
    
}	
    // セッションを作成し、Stripeに必要な情報を返す
//    public String createStripeSession(String houseName, ReservationRegisterForm reservationRegisterForm, HttpServletRequest httpServletRequest) {
//        Stripe.apiKey = "Stripeのシークレットキー";
//        String requestUrl = new String(httpServletRequest.getRequestURL());
//        SessionCreateParams params =
//            SessionCreateParams.builder()
//                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
//                .addLineItem(
//                    SessionCreateParams.LineItem.builder()
//                        .setPriceData(
//                            SessionCreateParams.LineItem.PriceData.builder()   
//                                .setProductData(
//                                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
//                                        .setName(houseName)
//                                        .build())
//                                .setUnitAmount((long)300)
//                                .setCurrency("jpy")                                
//                                .build())
//                        .setQuantity(1L)
//                        .build())
//                .setMode(SessionCreateParams.Mode.PAYMENT)
//                .setSuccessUrl(requestUrl.replaceAll("/houses/[0-9]+/reservations/confirm", "") + "/reservations?reserved")
//                .setCancelUrl(requestUrl.replace("/reservations/confirm", ""))
//                .setPaymentIntentData(
//                    SessionCreateParams.PaymentIntentData.builder()
//                        .putMetadata("houseId", reservationRegisterForm.getHouseId().toString())
//                        .putMetadata("userId", reservationRegisterForm.getUserId().toString())
//                        .putMetadata("checkinDate", reservationRegisterForm.getCheckinDate())
//                        .putMetadata("checkoutDate", reservationRegisterForm.getCheckoutDate())
//                        .putMetadata("numberOfPeople", reservationRegisterForm.getNumberOfPeople().toString())
//                        .putMetadata("amount", reservationRegisterForm.getAmount().toString())
//                        .build())
//                .build();
//        try {
//            Session session = Session.create(params);
//            return session.getId();
//        } catch (StripeException e) {
//            e.printStackTrace();
//            return "";
//        }
//    } 
