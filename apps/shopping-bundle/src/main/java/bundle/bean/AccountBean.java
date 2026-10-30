package bundle.bean;

import java.util.Objects;

/**
 * acountテーブルの1レコードを管理するJavaBean
 */
public class AccountBean {

	/**
	 * フィールド
	 */
	private Integer id;      // アカウントID
	private String name;     // アカウント名
	private String email;    // メールアドレス
	private String password; // パスワード
	
	/**
	 * 引数なしコンストラクタ
	 */
	public AccountBean() {}

	/**
	 * コンストラクタ
	 * @param email    メールアドレス
	 * @param password パスワード
	 */
	public AccountBean(String email, String password) {
		this.email = email;
		this.password = password;
	}

	/**
	 * コンストラクタ
	 * @param name     アカウント名
	 * @param email    メールアドレス
	 * @param password パスワード
	 */
	public AccountBean(String name, String email, String password) {
		this(email, password);
		this.name = name;
	}
	
	public AccountBean(Integer id, String name, String email, String password) {
		this(name, email, password);
		this.id = id;
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	@Override
	public int hashCode() {
		return Objects.hash(email, name, password, id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AccountBean other = (AccountBean) obj;
		return Objects.equals(email, other.email) 
				&& Objects.equals(name, other.name)
				&& Objects.equals(password, other.password)
				&& Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AccountBean [")
			   .append("id=").append(id).append(", ")
			   .append("name=").append(name).append(", ")
			   .append("email=").append(email).append(", ")
			   .append("password=").append(password)
			   .append("]");
		return builder.toString();
	}

}