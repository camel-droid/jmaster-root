package bundle.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import bundle.bean.AccountBean;

/**
 * accountテーブルにアクセスするDAO
 */
public class AccountDAO extends BaseDAO {

	/**
	 * クラス定数
	 */
	private static final String SQL_FIND_BY_EMAIL_AND_PASSWORD
		= "SELECT * FROM account WHERE email = ? AND password = ?";

	/**
	 * コンストラクタ
	 * @throws DAOException JDBCドライバの読込みに失敗した場合
	 */
	public AccountDAO() throws DAOException {
		super();
	}

	/**
	 * メールアドレスとパスワードの組み合わせが合致するアカウントを取得する
	 * @param  email メールアドレス
	 * @param  password パスワード
	 * @return メールアドレスとパスワードが一致する場合はそのアカウントインスタンス、それ以外はnull
	 * @throws DAOException レコードの取得に失敗した場合
	 */
	public AccountBean findByEmailAndPassword(String email, String password) throws DAOException {
		
		try (// 1. データベース接続オブジェクトの取得
			 Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
			 // 2. SQL実行オブジェクトの取得
			 PreparedStatement pstmt = con.prepareStatement(SQL_FIND_BY_EMAIL_AND_PASSWORD);) {
			// 3. パラメータバインディング
			pstmt.setString(1, email);
			pstmt.setString(2, password);
			
			try (// 4. SQLの実行と結果セットの取得
				 ResultSet rs = pstmt.executeQuery();) {
				// 5. 結果セットからアカウントインスタンスに変換
				AccountBean bean = null;
				if (rs.next()) {
					bean = new AccountBean(
							rs.getInt("id"),
							rs.getString("name"),
							rs.getString("email"), 
							rs.getString("password")
						   );
				}
				// 6. アカウントインスタンスを返却
				return bean;
			}
		} catch (SQLException e) {
			// スタックトレースに表示
			e.printStackTrace();
			// DAO例外をスロー
			// XXX: 【例外処理について】 研修テキストではメッセージなしだが、本サンプルシステムではメッセージを設定
			throw new DAOException("レコードの取得に失敗しました。", e);
		}
	}

}
