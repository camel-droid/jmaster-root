-- jmaster_shopping_dbに接続：このスクリプトの実行はスーパーユーザからstudentユーザに切り替えてログインし直してから実行
\c jmaster_shopping_db

-- テーブルの削除
DROP TABLE IF EXISTS category CASCADE;
DROP TABLE IF EXISTS item CASCADE;
DROP TABLE IF EXISTS customer CASCADE;
DROP TABLE IF EXISTS ordered CASCADE;
DROP TABLE IF EXISTS ordered_detail CASCADE;

-- 各テーブルの生成

-- カテゴリテーブルの作成
CREATE TABLE category (
	code SERIAL NOT NULL PRIMARY KEY,
	name TEXT
);

-- 商品テーブルの作成
CREATE TABLE item (
	code SERIAL NOT NULL PRIMARY KEY,
	category_code INTEGER,
	name TEXT,
	price INTEGER
);

-- 顧客テーブルの作成：研修テキスト『Javaマスター』に記述のフィールド名を「tel」から「phone」に変更フィールドを
CREATE TABLE customer (
	code SERIAL NOT NULL PRIMARY KEY,
	name TEXT,
	address TEXT,
	tel TEXT,
	email TEXT
);

-- 注文テーブルの作成
CREATE TABLE ordered (
	code SERIAL NOT NULL PRIMARY KEY,
	customer_code INTEGER,
	ordered_date DATE,
	total_price INTEGER	
);

-- 注文詳細テーブルの作成：研修テキスト『Javaマスター』に記述のフィールド名を「num」から「quantity」に変更
CREATE TABLE ordered_detail (
	ordered_code SERIAL NOT NULL,
	item_code INTEGER,
	quantity INTEGER
);
