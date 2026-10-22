package bundle.bean;

import java.util.Objects;

/**
 * categoryテーブルの1レコードを管理するJavaBean
 */
public class CategoryBean {
	
	/**
	 * フィールド
	 */
	private int code;    // 商品カテゴリコード
	private String name; // 商品カテゴリ名
	
	/**
	 * 引数なしコンストラクタ
	 */
	public CategoryBean() {}
	
	/**
	 * コンストラクタ
	 * @param code 商品カテゴリコード
	 * @param name 商品カテゴリ名
	 */
	public CategoryBean(int code, String name) {
		this.code = code;
		this.name = name;
	}
	
	public void setCode(int code) {
		this.code = code;
	}
	public int getCode() {
		return this.code;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return this.name;
	}

	// 以下のメソッド群をオーバーロードすることによりインスタンス同士の比較を簡略化
	// 研修テキスト『Javaベーシック・第15章 パッケージとクラスライブラリ』の15.3.1 Objectクラスにequalsメソッドについての簡単な解説がある
	
	@Override
	public int hashCode() {
		return Objects.hash(Integer.valueOf(code), name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CategoryBean other = (CategoryBean) obj;
		return code == other.code && Objects.equals(name, other.name);
	}
	

}
