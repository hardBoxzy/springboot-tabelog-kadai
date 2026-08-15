package com.example.nagoyameshi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.nagoyameshi.entity.Restaurant;

import jakarta.persistence.PostLoad;

@Component
public class S3UrlInterceptor {

    private static String bucketName;
    private static String region;

    // Spring起動時に環境変数を static フィールドに読み込む
    public S3UrlInterceptor(
            @Value("${aws.s3.bucket}") String bucketName,
            @Value("${aws.s3.region}") String region) {
        S3UrlInterceptor.bucketName = bucketName;
        S3UrlInterceptor.region = region;
    }

    // 💡 データベースからデータが読み込まれた瞬間に自動実行される処理
    @PostLoad
    public void afterLoad(Object entity) {
        if (entity instanceof Restaurant) {
            Restaurant restaurant = (Restaurant) entity;
            String base = "https://" + bucketName + ".s3." + region + ".amazonaws.com";
            restaurant.setS3UrlBase(base);
        }
    }
}