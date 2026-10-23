package bundle.bean;

import java.io.Serializable;
import java.util.Objects;

/**
 * itemテーブルの1レコードを管理するJavaBean
 */
public class ItemBean implements Serializable {

	/**
	 * フィールド
	 */
	private int code;         // 商品コード
	private int categoryCode; // 商品カテゴリコード
	private String name;      // 商品名
	private int price;        // 価格
	private int quantity;     // 数量
	
	/**
	 * 引数なしコンストラクタ
	 */
	public ItemBean() {}

	/**
	 * コンストラクタ
	 * @param code         商品コード
	 * @param categoryCode 商品カテゴリコード
	 * @param name         商品名
	 * @param price        価格
	 */
	public ItemBean(int code, int categoryCode, String name, int price) {
		this.code = code;
		this.categoryCode = categoryCode;
		this.name = name;
		this.price = price;
	}

	/**
	 * コンストラクタ
	 * @param code         商品コード
	 * @param categoryCode 商品カテゴリコード
	 * @param name         商品名
	 * @param price        価格
	 * @param quantity     数量
	 */
	public ItemBean(int code, int categoryCode, String name, int price, int quantity) {
		this(code, categoryCode, name, price);
		this.quantity = quantity;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public int getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(int categoryCode) {
		this.categoryCode = categoryCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	// 以下のメソッド群をオーバーロードすることによりインスタンス同士の比較を簡略化
	// 研修テキスト『Javaベーシック・第15章 パッケージとクラスライブラリ』の15.3.1 Objectクラスにequalsメソッドについての簡単な解説がある
	
	@Override
	public int hashCode() {
		return Objects.hash(Integer.valueOf(categoryCode), Integer.valueOf(code), name, Integer.valueOf(price),
				Integer.valueOf(quantity));
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ItemBean other = (ItemBean) obj;
		return categoryCode == other.categoryCode && code == other.code && Objects.equals(name, other.name)
				&& price == other.price && quantity == other.quantity;
	}
	
}
