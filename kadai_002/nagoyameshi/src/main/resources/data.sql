-- restaurantsテーブル
INSERT IGNORE INTO restaurants (id, name, image_name, description, price, capacity, postal_code, address, phone_number,score) VALUES 
(1, 'SAMURAIの食堂','restaurant01.png', '最寄り駅から徒歩10分。掘りごたつ席でゆったり過ごせるおでん居酒屋。', 3000, 2, '073-0145', '愛知県名古屋市西五条南X-XX-XX', '012-345-678', 3),
(2, 'なごやの麺処', 'restaurant09.png', '栄駅から徒歩5分。名物の味噌煮込みうどんが自慢の老舗店。', 1500, 4, '460-0008', '愛知県名古屋市中区栄三丁目1-1', '052-111-2222', 0),
(3, 'しゃちほこ鮨', 'restaurant06.png', '伏見駅直結。毎朝市場から仕入れる新鮮なネタが楽しめる本格カウンター寿司。', 8000, 2, '460-0003', '愛知県名古屋市中区錦二丁目2-2', '052-222-3333', 4),
(4, 'ひつまぶし金鯱', 'restaurant01.png', '名古屋駅構内。秘伝のタレと炭火で香ばしく焼き上げた絶品ひつまぶし。', 4500, 4, '450-0002', '愛知県名古屋市中村区名駅四丁目3-3', '052-333-4444', 2),
(5, 'カフェ・ド・シロノワ', 'restaurant02.png', '大須観音近く。朝は名古屋伝統の豪華な小倉トーストモーニングを提供。', 800, 2, '460-0011', '愛知県名古屋市中区大須二丁目4-4', '052-444-5555', 3),
(6, '手羽先キング', 'restaurant01.png', '金山駅から徒歩3分。スパイシーなタレが病みつきになる手羽先唐揚げ専門店。', 2500, 6, '460-0022', '愛知県名古屋市中区金山一丁目5-5', '052-555-6666', 1),
(7, 'トラットリア・マルコ', 'restaurant02.png', '千種駅すぐ。薪窯で焼き上げる本格ナポリピッツァとソムリエ厳選ワイン。', 5000, 2, '464-0850', '愛知県名古屋市千種区今池一丁目6-6', '052-666-7777', 3),
(8, 'ビストロ・ナゴヤ', 'restaurant02.png', '覚王山の閑静な住宅街。地産地消にこだわったカジュアルなフレンチコース。', 7000, 2, '464-0075', '愛知県名古屋市千種区内山三丁目7-7', '052-777-8888', 0),
(9, '四川麻辣坊', 'restaurant04.png', '今池駅から徒歩2分。本場のスパイスを効かせた名物・台湾ラーメンと麻婆豆腐。', 1200, 4, '464-0851', '愛知県名古屋市千種区今池南8-8', '052-888-9999', 4),
(10, '焼肉の横綱', 'restaurant03.png', '国際センター駅近く。最高級 of 飛騨牛をリーズナブルに味わえる個室焼肉店。', 6000, 4, '450-0001', '愛知県名古屋市中村区那古野一丁目9-9', '052-999-0000', 2),
(11, '洋食 みつば', 'restaurant02.png', '東別院駅から徒歩8分。じっくり煮込んだデミグラスソースのハンバーグ。', 1800, 2, '460-0016', '愛知県名古屋市中区橘一丁目10-10', '052-123-4567', 0),
(12, 'ステーキハウス城', 'restaurant12.png', '新栄町駅すぐ。目の前の鉄板でジューシーに焼き上げる贅沢ステーキ。', 12000, 2, '460-0005', '愛知県名古屋市中区東桜二丁目11-11', '052-234-5678', 5),
(13, '博多中洲屋台', 'restaurant09.png', '矢場町駅から徒歩4分。濃厚な豚骨スープと極細ストレート麺が絡む本格派。', 900, 1, '460-0008', '愛知県名古屋市中区栄五丁目12-12', '052-345-6789', 0),
(14, 'グリーンカレーズ', 'restaurant11.png', '鶴舞公園近く。ハーブとココナッツミルクが香る本場タイのグリーンカレー。', 1400, 2, '460-0012', '愛知県名古屋市中区千代田三丁目13-13', '052-456-7890', 0),
(15, '串カツ どて屋', 'restaurant01.png', '上方とは一味違う、名古屋濃厚八丁味噌ベースのどて焼きとサクサク串カツ。', 2000, 4, '453-0015', '愛知県名古屋市中村区椿町14-14', '052-567-8901', 0),
(16, 'バル・エスパーニャ', 'restaurant01.png', '上前津駅から徒歩5分。魚介の旨味が凝縮された特製パエリアが人気のバル。', 4000, 2, '460-0013', '愛知県名古屋市中区上前津一丁目15-15', '052-678-9012', 0),
(17, 'オーガニックキッチン', 'restaurant02.png', '星ヶ丘駅から徒歩6分。契約農家から届く新鮮野菜をふんだんに使ったランチ。', 2200, 2, '464-0026', '愛知県名古屋市千種区星ケ丘元町16-16', '052-789-0123', 0),
(18, 'バー・シャドウ', 'restaurant01.png', '錦の地下に佇む隠れ家。バーテンダーが好みに合わせて作る本格カクテル。', 3500, 1, '460-0003', '愛知県名古屋市中区錦三丁目17-17', '052-890-1234', 0),
(19, '韓流デリ', 'restaurant13.png', '大須商店街内。とろけるチーズがたっぷりの大人気チーズタッカルビ。', 2800, 4, '460-0011', '愛知県名古屋市中区大須三丁目18-18', '052-901-2345', 0),
(20, 'スイーツテラス', 'restaurant02.png', '本山駅近く。季節のフルーツを贅沢に使ったパフェと自家焙煎コーヒー。', 1600, 2, '464-0032', '愛知県名古屋市千種区猫洞通19-19', '052-012-3456', 0),
(21, '天ぷら 万葉', 'restaurant01.png', '御器所駅から徒歩3分。職人が一品ずつ丁寧に揚げるサクサクの江戸前天ぷら。', 5500, 2, '466-0015', '愛知県名古屋市昭和区御器所通20-20', '052-123-0000', 0),
(22, '南蛮屋', '2e6d3fb9-08cd-4d4e-98e6-cc1e0fdc93e6.png', 'チキン南蛮専用店', 1000, 2, '1800000', '愛知県名古屋市中区栄五丁目12-12', '0809699999', 0);

-- rolesテーブル
INSERT IGNORE INTO roles (id, name) VALUES
(1, 'ROLE_GENERAL'),
(2, 'ROLE_PREMIUM'),
(3, 'ROLE_ADMIN');

-- jobsテーブル
INSERT IGNORE INTO jobs (id, name) VALUES
(1, '会社員'),
(2, '公務員'),
(3, '教師'),
(4, '医師'),
(5, '美容師'),
(6, 'エンジニア'),

(7, '警察官'),
(8, 'デザイナー'),
(9, '営業職'),
(10, '自営業'),
(11, 'その他');

-- usersテーブル
INSERT IGNORE INTO users (id, name, furigana, postal_code, address, phone_number, email, password, role_id, enabled, age, job_id) VALUES 
(1, '侍 太郎', 'サムライ タロウ', '101-0022', '東京都千代田区神田練塀町300番地', '090-1234-5678', 'taro.samurai@example.com', '$2a$10$2JNjTwZBwo7fprL2X4sv.OEKqxnVtsVQvuXDkI8xVGix.U3W5B7CO', 2, true, 30,1),
(2, '侍 花子', 'サムライ ハナコ', '101-0022', '東京都千代田区神田練塀町300番地', '090-1234-5678', 'hanako.samurai@example.com', '$2a$10$2JNjTwZBwo7fprL2X4sv.OEKqxnVtsVQvuXDkI8xVGix.U3W5B7CO', 3, true, 20,2),
(3, '侍 義勝', 'サムライ ヨシカツ', '638-0644', '奈良県五條市西吉野町湯川X-XX-XX', '090-1234-5678', 'yoshikatsu.samurai@example.com', '$2a$10$2JNjTwZBwo7fprL2X4sv.OEKqxnVtsVQvuXDkI8xVGix.U3W5B7CO', 1, true, 40,3),
(4, '侍 幸美', 'サムライ サチミ', '342-0006', '埼玉県吉川市南広島X-XX-XX', '090-1234-5678', 'sachimi.samurai@example.com', 'password', 1, true, 50,1),
(5, '侍 雅', 'サムライ ミヤビ', '527-0209', '滋賀県東近江市佐目町X-XX-XX', '090-1234-5678', 'miyabi.samurai@example.com', 'password', 1, true, 60,2),
(6, '侍 正保', 'サムライ マサヤス', '989-1203', '宮城県柴田郡大河原町旭町X-XX-XX', '090-1234-5678', 'masayasu.samurai@example.com', 'password', 1, true, 30,4),
(7, '侍 真由美', 'サムライ マユミ', '951-8015', '新潟県新潟市松岡町X-XX-XX', '090-1234-5678', 'mayumi.samurai@example.com', 'password', 1, true, 30,3),
(8, '侍 安民', 'サムライ ヤスタミ', '241-0033', '神奈川県横浜市旭区今川町X-XX-XX', '090-1234-5678', 'yasutami.samurai@example.com', 'password', 1, true, 20,5),
(9, '侍 章緒', 'サムライ アキオ', '739-2103', '広島県東広島市高屋町宮領X-XX-XX', '090-1234-5678', 'akio.samurai@example.com', 'password', 1, true, 20,3),
(10, '侍 祐子', 'サムライ ユウコ', '601-0761', '京都府南丹市美山町高野X-XX-XX', '090-1234-5678', 'yuko.samurai@example.com', 'password', 1, true, 40,1),
(11, '侍 秋美', 'サムライ アキミ', '606-8235', '京都府京都市左京区田中西春菜町X-XX-XX', '090-1234-5678', 'akimi.samurai@example.com', 'password', 1, true, 40,5),
(12, '侍 信平', 'サムライ シンペイ', '673-1324', '兵庫県加東市新定X-XX-XX', '090-1234-5678', 'shinpei.samurai@example.com', 'password', 1, true, 30,6);



INSERT IGNORE INTO categories (id, name) VALUES 
(1, 'ラーメン'),
(2, '居酒屋'),
(3, 'カフェ'),
(4, '焼肉'),
(5, '寿司'),
(6, 'イタリアン'),
(7, '中華料理'),
(8, 'スイーツ'),
(9, 'カレー'),
(10, '焼き鳥'),
(11, 'その他');

-- restaurant_categories（中間テーブル）の仮データ
INSERT IGNORE INTO restaurant_categories (restaurant_id, category_id) VALUES 
-- 1: SAMURAIの食堂 (おでん居酒屋)
(1, 2), 
(1, 3), 
-- 2: なごやの麺処 (うどん・その他)
(2, 11), 
-- 3: しゃちほこ鮨 (寿司)
(3, 5), 
-- 4: ひつまぶし金鯱 (うなぎ・その他)
(4, 11), 
-- 5: カフェ・ド・シロノワ (カフェ、スイーツ)
(5, 3), 
(5, 8), 
-- 6: 手羽先キング (居酒屋、焼き鳥)
(6, 2), 
(6, 10), 
-- 7: トラットリア・マルコ (イタリアン)
(7, 6), 
-- 8: ビストロ・ナゴヤ (フレンチ・その他)
(8, 11), 
-- 9: 四川麻辣坊 (中華料理、ラーメン)
(9, 7), 
(9, 1), 
-- 10: 焼肉の横綱 (焼肉)
(10, 4), 
-- 11: 洋食 みつば (洋食・その他)
(11, 11), 
-- 12: ステーキハウス城 (ステーキ・その他)
(12, 11), 
-- 13: 博多中洲屋台 (ラーメン)
(13, 1), 
-- 14: グリーンカレーズ (カレー)
(14, 9), 
-- 15: 串カツ どて屋 (居酒屋)
(15, 2), 
-- 16: バル・エスパーニャ (居酒屋・スペイン)
(16, 2), 
-- 17: オーガニックキッチン (カフェ)
(17, 3), 
-- 18: バー・シャドウ (居酒屋・バー)
(18, 2), 
-- 19: 韓流デリ (中華/その他・韓国料理)
(19, 11), 
-- 20: スイーツテラス (スイーツ、カフェ)
(20, 8), 
(20, 3), 
-- 21: 天ぷら 万葉 (和食・その他)
(21, 11);

-- reservationsテーブル
INSERT IGNORE INTO reservations (id, restaurant_id, user_id, checkin_date, number_of_people) VALUES (1, 1, 1, '2023-04-01 00:00:00', 2);
INSERT IGNORE INTO reservations (id, restaurant_id, user_id, checkin_date, number_of_people) VALUES (2, 2, 1, '2023-04-01 00:00:00', 3);
INSERT IGNORE INTO reservations (id, restaurant_id, user_id, checkin_date, number_of_people) VALUES (3, 3, 1, '2023-04-01 00:00:00', 4);
INSERT IGNORE INTO reservations (id, restaurant_id, user_id, checkin_date, number_of_people) VALUES (4, 4, 1, '2023-04-01 00:00:00', 5);
INSERT IGNORE INTO reservations (id, restaurant_id, user_id, checkin_date, number_of_people) VALUES (5, 5, 1, '2023-04-01 00:00:00', 6);
INSERT IGNORE INTO reservations (id, restaurant_id, user_id, checkin_date, number_of_people) VALUES (6, 6, 1, '2023-04-01 00:00:00', 2);
INSERT IGNORE INTO reservations (id, restaurant_id, user_id, checkin_date, number_of_people) VALUES (7, 7, 1, '2023-04-01 00:00:00', 3);
INSERT IGNORE INTO reservations (id, restaurant_id, user_id, checkin_date, number_of_people) VALUES (8, 8, 1, '2023-04-01 00:00:00', 4);
INSERT IGNORE INTO reservations (id, restaurant_id, user_id, checkin_date, number_of_people) VALUES (9, 9, 1, '2023-04-01 00:00:00', 5);
INSERT IGNORE INTO reservations (id, restaurant_id, user_id, checkin_date, number_of_people) VALUES (10, 10, 1, '2023-04-01 00:00:00', 6);
INSERT IGNORE INTO reservations (id, restaurant_id, user_id, checkin_date, number_of_people) VALUES (11, 11, 1, '2023-04-01 00:00:00', 2);


-- restaurant_holidays（中間テーブル）の仮データ
INSERT IGNORE INTO holidays (id, restaurant_id, day_id) VALUES 
-- 1: SAMURAIの食堂 (おでん居酒屋)
(1, 1, 2), 
(2, 1, 3), 
-- 2: なごやの麺処 (うどん・その他)
(3, 2, 4), 
-- 3: しゃちほこ鮨 (寿司)
(4, 3, 5), 
-- 4: ひつまぶし金鯱 (うなぎ・その他)
(5, 4, 4), 
-- 5: カフェ・ド・シロノワ (カフェ、スイーツ)
(6, 5, 3), 
(7, 5, 1), 
-- 6: 手羽先キング (居酒屋、焼き鳥)
(8, 6, 2), 
(9, 6, 3), 
-- 7: トラットリア・マルコ (イタリアン)
(10, 7, 6), 
-- 8: ビストロ・ナゴヤ (フレンチ・その他)
(11, 8, 4), 
-- 9: 四川麻辣坊 (中華料理、ラーメン)
(12, 9, 0), 
(13, 9, 1), 
-- 10: 焼肉の横綱 (焼肉)
(14, 10, 4), 
-- 11: 洋食 みつば (洋食・その他)
(15, 11, 4), 
-- 12: ステーキハウス城 (ステーキ・その他)
(16, 12, 4), 
-- 13: 博多中洲屋台 (ラーメン)
(17, 13, 1), 
-- 14: グリーンカレーズ (カレー)
(18, 14, 2), 
-- 15: 串カツ どて屋 (居酒屋)
(19, 15, 2), 
-- 16: バル・エスパーニャ (居酒屋・スペイン)
(20, 16, 2), 
-- 17: オーガニックキッチン (カフェ)
(21, 17, 3), 
-- 18: バー・シャドウ (居酒屋・バー)
(22, 18, 2), 
-- 19: 韓流デリ (中華/その他・韓国料理)
(23, 19, 4), 
-- 20: スイーツテラス (スイーツ、カフェ)
(24, 20, 1), 
(25, 20, 3), 
-- 21: 天ぷら 万葉 (和食・その他)
(26, 21, 4);

-- favoritesテーブル
INSERT IGNORE INTO favorites (id, restaurant_id, user_id, created_at) VALUES (1, 1, 1, '2023-04-11');
INSERT IGNORE INTO favorites (id, restaurant_id, user_id, created_at) VALUES (2, 2, 1, '2023-04-11');
INSERT IGNORE INTO favorites (id, restaurant_id, user_id, created_at) VALUES (3, 3, 1, '2023-04-11');
INSERT IGNORE INTO favorites (id, restaurant_id, user_id, created_at) VALUES (4, 4, 1, '2023-04-11');
INSERT IGNORE INTO favorites (id, restaurant_id, user_id, created_at) VALUES (5, 5, 1, '2023-04-11');
INSERT IGNORE INTO favorites (id, restaurant_id, user_id, created_at) VALUES (6, 6, 1, '2023-04-11');
INSERT IGNORE INTO favorites (id, restaurant_id, user_id, created_at) VALUES (7, 1, 2, '2023-04-11');
INSERT IGNORE INTO favorites (id, restaurant_id, user_id, created_at) VALUES (8, 2, 2, '2023-04-11');
INSERT IGNORE INTO favorites (id, restaurant_id, user_id, created_at) VALUES (9, 6, 1, '2023-04-11');
INSERT IGNORE INTO favorites (id, restaurant_id, user_id, created_at) VALUES (10, 7, 1, '2023-04-11');
INSERT IGNORE INTO favorites (id, restaurant_id, user_id, created_at) VALUES (11, 8, 1, '2023-04-11');
INSERT IGNORE INTO favorites (id, restaurant_id, user_id, created_at) VALUES (12, 9, 1, '2023-04-11');
INSERT IGNORE INTO favorites (id, restaurant_id, user_id, created_at) VALUES (13, 10, 1, '2023-04-11');
INSERT IGNORE INTO favorites (id, restaurant_id, user_id, created_at) VALUES (14, 11, 1, '2023-04-11');
INSERT IGNORE INTO favorites (id, restaurant_id, user_id, created_at) VALUES (15, 12, 1, '2023-04-11');

-- reviewsテーブル
INSERT IGNORE INTO reviews (id, restaurant_id, user_id, score, content, created_at, updated_at) VALUES (1, 1, 1, 4, '素晴らしい食事でした。また利用したいです。', '2023-04-11', '2023-04-02');
INSERT IGNORE INTO reviews (id, restaurant_id, user_id, score, content, created_at, updated_at) VALUES (2, 1, 1, 5, '店内が綺麗で、快適に過ごせました。', '2023-04-01', '2023-12-02');
INSERT IGNORE INTO reviews (id, restaurant_id, user_id, score, content, created_at, updated_at) VALUES (3, 1, 1, 4, 'アクセスが良く、ランチに最適でした。', '2023-04-01', '2023-04-02');
INSERT IGNORE INTO reviews (id, restaurant_id, user_id, score, content, created_at, updated_at) VALUES (4, 1, 2, 2, 'スタッフが親切で、気持ちよく食事ができました。', '2024-04-01', '2023-04-02');
INSERT IGNORE INTO reviews (id, restaurant_id, user_id, score, content, created_at, updated_at) VALUES (5, 1, 1, 3, '駅から近く、便利でした。', '2023-04-01', '2023-01-02');
INSERT IGNORE INTO reviews (id, restaurant_id, user_id, score, content, created_at, updated_at) VALUES (6, 1, 3, 1, '周辺の観光スポットが充実していて、楽しめました。', '2024-04-01', '2023-04-02');
INSERT IGNORE INTO reviews (id, restaurant_id, user_id, score, content, created_at, updated_at) VALUES (7, 1, 1, 3, 'スタッフが親切で、気持ちよく食事ができました。', '2025-04-01', '2023-04-02');
INSERT IGNORE INTO reviews (id, restaurant_id, user_id, score, content, created_at, updated_at) VALUES (8, 1, 4, 0, '料理が手頃で、コストパフォーマンスが良かったです。', '2026-04-01', '2023-04-02');
INSERT IGNORE INTO reviews (id, restaurant_id, user_id, score, content, created_at, updated_at) VALUES (9, 1, 1, 4, '静かな環境でゆっくり食事ができました。', '2021-04-01', '2023-04-02');
INSERT IGNORE INTO reviews (id, restaurant_id, user_id, score, content, created_at, updated_at) VALUES (10, 1, 1, 2, '店内の設備（個室など）が充実していました。', '2023-04-01', '2023-04-02');
INSERT IGNORE INTO reviews (id, restaurant_id, user_id, score, content, created_at, updated_at) VALUES (11, 11, 1, 0, 'フリーWi-Fiが快適で助かりました。', '2023-04-01', '2023-04-02');
INSERT IGNORE INTO reviews (id, restaurant_id, user_id, score, content, created_at, updated_at) VALUES (12, 12, 1, 5, '清潔感があり、気持ちよく食事ができました。', '2023-04-01', '2023-04-02');


INSERT IGNORE INTO reviews (id, restaurant_id, user_id, score, content, created_at, updated_at) VALUES (13, 1, 1, 4, '2素晴らしい食事でした。また利用したいです。', '2023-04-11', '2023-04-02');
INSERT IGNORE INTO reviews (id, restaurant_id, user_id, score, content, created_at, updated_at) VALUES (14, 3, 1, 5, '2店内が綺麗で、快適に過ごせました。', '2023-04-01', '2023-12-02');
INSERT IGNORE INTO reviews (id, restaurant_id, user_id, score, content, created_at, updated_at) VALUES (15, 3, 1, 4, '2アクセスが良く、ランチに最適でした。', '2023-04-01', '2023-04-02');
INSERT IGNORE INTO reviews (id, restaurant_id, user_id, score, content, created_at, updated_at) VALUES (16, 4, 2, 2, '2スタッフが親切で、気持ちよく食事ができました。', '2024-04-01', '2023-04-02');
INSERT IGNORE INTO reviews (id, restaurant_id, user_id, score, content, created_at, updated_at) VALUES (17, 5, 1, 3, '2駅から近く、便利でした。', '2023-04-01', '2023-01-02');
INSERT IGNORE INTO reviews (id, restaurant_id, user_id, score, content, created_at, updated_at) VALUES (18, 6, 3, 1, '2周辺の観光スポットが充実していて、楽しめました。', '2024-04-01', '2023-04-02');
INSERT IGNORE INTO reviews (id, restaurant_id, user_id, score, content, created_at, updated_at) VALUES (19, 7, 1, 3, '2スタッフが親切で、気持ちよく食事ができました。', '2025-04-01', '2023-04-02');
INSERT IGNORE INTO reviews (id, restaurant_id, user_id, score, content, created_at, updated_at) VALUES (20, 8, 4, 0, '2料理が手頃で、コストパフォーマンスが良かったです。', '2026-04-01', '2023-04-02');
INSERT IGNORE INTO reviews (id, restaurant_id, user_id, score, content, created_at, updated_at) VALUES (21, 9, 1, 4, '2静かな環境でゆっくり食事ができました。', '2021-04-01', '2023-04-02');
INSERT IGNORE INTO reviews (id, restaurant_id, user_id, score, content, created_at, updated_at) VALUES (22, 10, 1, 2, '2店内の設備（個室など）が充実していました。', '2023-04-01', '2023-04-02');
INSERT IGNORE INTO reviews (id, restaurant_id, user_id, score, content, created_at, updated_at) VALUES (23, 11, 1, 0, '2フリーWi-Fiが快適で助かりました。', '2023-04-01', '2023-04-02');
INSERT IGNORE INTO reviews (id, restaurant_id, user_id, score, content, created_at, updated_at) VALUES (24, 12, 1, 5, '2清潔感があり、気持ちよく食事ができました。', '2023-04-01', '2023-04-02');

-- stripe_customers（中間テーブル）の仮データ
-- INSERT IGNORE INTO stripe_customers (id, user_id, stripe_customer_id) VALUES 
-- (1, 1,'cus_V03S0ydDiMhutY'),
-- (2, 4,'cus_V0PZLLOwlUmlBk'); 


INSERT IGNORE INTO revenues (id, user_id, amount, merchandise_id, created_at, updated_at) VALUES 
-- 2026年3月
(1, 6, 300, 1, '2026-03-01', '2026-03-01'),
(2, 1, 300, 1, '2026-03-05', '2026-03-05'),
(3, 7, 300, 1, '2026-03-07', '2026-03-07'),
(4, 2, 300, 1, '2026-03-10', '2026-03-10'),
(5, 8, 300, 1, '2026-03-12', '2026-03-12'),
(6, 12, 300, 1, '2026-03-14', '2026-03-14'),
(7, 3, 300, 1, '2026-03-15', '2026-03-15'),
(8, 9, 300, 1, '2026-03-18', '2026-03-18'),
(9, 4, 300, 1, '2026-03-20', '2026-03-20'),
(10, 10, 300, 1, '2026-03-22', '2026-03-22'),
(11, 5, 300, 1, '2026-03-25', '2026-03-25'),
(12, 11, 300, 1, '2026-03-27', '2026-03-27'),
(13, 13, 300, 1, '2026-03-28', '2026-03-28'),

-- 2026年4月
(14, 6, 300, 1, '2026-04-01', '2026-04-01'),
(15, 1, 300, 1, '2026-04-05', '2026-04-05'),
(16, 7, 300, 1, '2026-04-07', '2026-04-07'),
(17, 2, 300, 1, '2026-04-10', '2026-04-10'),
(18, 8, 300, 1, '2026-04-12', '2026-04-12'),
(19, 12, 300, 1, '2026-04-14', '2026-04-14'),
(20, 3, 300, 1, '2026-04-15', '2026-04-15'),
(21, 9, 300, 1, '2026-04-18', '2026-04-18'),
(22, 4, 300, 1, '2026-04-20', '2026-04-20'),
(23, 10, 300, 1, '2026-04-22', '2026-04-22'),
(24, 5, 300, 1, '2026-04-25', '2026-04-25'),
(25, 11, 300, 1, '2026-04-27', '2026-04-27'),
(26, 13, 300, 1, '2026-04-28', '2026-04-28'),

-- 2026年5月
(27, 6, 300, 1, '2026-05-01', '2026-05-01'),
(28, 1, 300, 1, '2026-05-05', '2026-05-05'),
(29, 7, 300, 1, '2026-05-07', '2026-05-07'),
(30, 2, 300, 1, '2026-05-10', '2026-05-10'),
(31, 8, 300, 1, '2026-05-12', '2026-05-12'),
(32, 12, 300, 1, '2026-05-14', '2026-05-14'),
(33, 3, 300, 1, '2026-05-15', '2026-05-15'),
(34, 9, 300, 1, '2026-05-18', '2026-05-18'),
(35, 4, 300, 1, '2026-05-20', '2026-05-20'),
(36, 10, 300, 1, '2026-05-22', '2026-05-22'),
(37, 5, 300, 1, '2026-05-25', '2026-05-25'),
(38, 11, 300, 1, '2026-05-27', '2026-05-27'),
(39, 13, 300, 1, '2026-05-28', '2026-05-28'),

-- 2026年6月
(40, 6, 300, 1, '2026-06-01', '2026-06-01'),
(41, 1, 300, 1, '2026-06-05', '2026-06-05'),
(42, 7, 300, 1, '2026-06-07', '2026-06-07'),
(43, 2, 300, 1, '2026-06-10', '2026-06-10'),
(44, 8, 300, 1, '2026-06-12', '2026-06-12'),
(45, 12, 300, 1, '2026-06-14', '2026-06-14'),
(46, 3, 300, 1, '2026-06-15', '2026-06-15'),
(47, 9, 300, 1, '2026-06-18', '2026-06-18'),
(48, 4, 300, 1, '2026-06-20', '2026-06-20'),
(49, 10, 300, 1, '2026-06-22', '2026-06-22'),
(50, 5, 300, 1, '2026-06-25', '2026-06-25'),
(51, 11, 300, 1, '2026-06-27', '2026-06-27'),
(52, 13, 300, 1, '2026-06-28', '2026-06-28'),

-- 2026年7月
(53, 6, 300, 1, '2026-07-01', '2026-07-01'),
(54, 1, 300, 1, '2026-07-05', '2026-07-05'),
(55, 7, 300, 1, '2026-07-07', '2026-07-07'),
(56, 2, 300, 1, '2026-07-10', '2026-07-10'),
(57, 8, 300, 1, '2026-07-12', '2026-07-12'),
(58, 12, 300, 1, '2026-07-14', '2026-07-14'),
(59, 3, 300, 1, '2026-07-15', '2026-07-15'),
(60, 9, 300, 1, '2026-07-18', '2026-07-18'),
(61, 4, 300, 1, '2026-07-20', '2026-07-20'),
(62, 10, 300, 1, '2026-07-22', '2026-07-22'),
(63, 5, 300, 1, '2026-07-25', '2026-07-25'),
(64, 11, 300, 1, '2026-07-27', '2026-07-27'),
(65, 13, 300, 1, '2026-07-28', '2026-07-28'),

(66, 6, 300, 1, '2026-08-01', '2026-08-01'),
(67, 1, 300, 1, '2026-08-05', '2026-08-05'),
(68, 7, 300, 1, '2026-08-07', '2026-08-07'),
(69, 2, 300, 1, '2026-08-10', '2026-08-10');


INSERT IGNORE INTO company_info (id, name, address, representative, estabilished_at,map_src, updated_at) VALUES 
(1, '株式会社HEZIYIN', '〒150-0043 東京都渋谷区道玄坂2丁目11-1 Gスクエア渋谷道玄坂4FJR山手線「渋谷駅」より徒歩3分',' 代表取締役 何 太郎','2015年3月19日' ,
'https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3241.7759544892483!2d139.69399665123464!3d35.65789123876098!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x60188caa0042e8d1%3A0xba130bea826a7aa2!2z44CSMTUwLTAwNDMg5p2x5Lqs6YO95riL6LC35Yy66YGT546E5Z2C77yS5LiB55uu77yR77yR4oiS77yR!5e0!3m2!1sja!2sjp!4v1636872991912!5m2!1sja!2sjp',
 '2026-08-11 00:00:00');


