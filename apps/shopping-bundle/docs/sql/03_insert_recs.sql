-- jmaster_shopping_dbに接続：このスクリプトの実行はスーパーユーザからstudentユーザに切り替えてログインし直してから実行
\c jmaster_shopping_db

-- 各テーブルのレコードを削除：
TRUNCATE TABLE category RESTART IDENTITY CASCADE;
TRUNCATE TABLE item RESTART IDENTITY CASCADE;
TRUNCATE TABLE customer RESTART IDENTITY CASCADE;
TRUNCATE TABLE ordered RESTART IDENTITY CASCADE;
TRUNCATE TABLE ordered_detail CASCADE;

-- テーブルのサンプルレコードを登録
INSERT INTO category (name) VALUES
	  ('本')
	, ('DVD')
	, ('ゲーム');

INSERT INTO item (category_code, name, price) VALUES
	  (1, 'Javaの基本', 2500)
	, (1, 'MLB Fun', 980)
	, (1, '料理 BOOK!', 1200)
	, (2, '懐かしのアニメシリーズ', 2000)
	, (2, 'The Racer', 1000)
	, (2, 'Space Wars 3', 1800)
	, (3, 'パズルゲーム', 780)
	, (3, 'Invader Fighter', 3400)
	, (3, 'Play the BasketBall', 2200);
