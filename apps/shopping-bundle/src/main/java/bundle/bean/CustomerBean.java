package bundle.bean;

import java.util.Objects;

/**
 * customerテーブルの1レコードを管理するJavaBean
 */
public class CustomerBean {

	/**
	 * フィールド
	 */
	private int code;       // 顧客コード
	private String name;    // 顧客名
	private String address; // 住所
	private String phone;   // 電話番号
	private String email;   // メールアドレス
	
	/**
	 * 引数なしコンストラクタ
	 */
	public CustomerBean() {}

	/**
	 * コンストラクタ
	 * @param name    顧客名
	 * @param address 住所
	 * @param phone   電話番号
	 * @param email   メールアドレス
	 */
	public CustomerBean(String name, String address, String phone, String email) {
		this.name = name;
		this.address = address;
		this.phone = phone;
		this.email = email;
	}

	/**
	 * コンストラクタ
	 * @param code    顧客コード
	 * @param name    顧客名
	 * @param address 住所
	 * @param phone   電話番号
	 * @param email   メールアドレス
	 */
	public CustomerBean(int code, String name, String address, String phone, String email) {
		this(name, address, phone, email);
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public int hashCode() {
		return Objects.hash(address, Integer.valueOf(code), email, name, phone);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CustomerBean other = (CustomerBean) obj;
		return Objects.equals(address, other.address) && code == other.code && Objects.equals(email, other.email)
				&& Objects.equals(name, other.name) && Objects.equals(phone, other.phone);
	}
	
}
