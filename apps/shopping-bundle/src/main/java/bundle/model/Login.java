package bundle.model;

/**
 * ログインアカウント情報を保持するモデル
 */
public class Login {

	/**
	 * フィールド
	 */
	private Integer id;
	private String name;  // アカウント名
	
	/**
	 * コンストラクタ
	 * @param id    アカウントID
	 * @param name  アカウント名
	 */
	public Login(Integer id, String name) {
		this.id = id;
		this.name = name;
	}
	
}
