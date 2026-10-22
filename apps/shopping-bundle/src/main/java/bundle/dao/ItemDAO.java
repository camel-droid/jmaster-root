package bundle.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bundle.bean.CategoryBean;

/**
 * itemテーブルまたはcategoryテーブルにアクセスするDAO
 */
@SuppressWarnings("unused")
public class ItemDAO {

	/**
	 * クラス定数
	 * TODO: ほかのDAOでも利用できるように定数クラスへの分離または基底DAOクラスの追加を検討
	 */
	// JDBCドライバおよびデータベース接続情報
	private static final String JDBC_DRIVER = "org.postgresql.Driver";
	private static final String DB_URL_FULL = "jdbc:postgresql://localhost:5432/jmaster_shopping_db";
	private static final String DB_URL      = "jdbc:postgresql:jmaster_shopping_db";
	private static final String DB_USER     = "student";
	private static final String DB_PASSWORD = "himitu";
	// SQL文
	private static final String SQL_FIND_ALL_CATEGORY = "SELECT * FROM category ORDER BY code";
	
	/**
	 * コンストラクタ
	 * @throws DAOException JDBCドライバの読込みに失敗した場合
	 */
	public ItemDAO() throws DAOException {
		try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException e) {
			// スタックトレースに表示
			e.printStackTrace();
			// DAO例外をスロー
			throw new DAOException("JDBCドライバの読込みに失敗しました。", e);
		}
	}

	/**
	 * categoryテーブルの全検索
	 * @return カテゴリリスト
	 * @throws DAOException レコードの取得に失敗した場合
	 */
	public List<CategoryBean> findAllCategory() throws DAOException {
		
		try (// 1. データベース接続オブジェクトを取得
			 Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
			 // 2. SQL実行オブジェクトを取得
			 PreparedStatement pstmt = con.prepareStatement(SQL_FIND_ALL_CATEGORY);
			 // 3. SQLの実行と結果セットの取得
			 ResultSet rs = pstmt.executeQuery();) {
			// 4. 結果セットをカテゴリリストに変換
			List<CategoryBean> list = new ArrayList<CategoryBean>();
			while (rs.next()) {
				int code = rs.getInt("code");
				String name = rs.getString("name");
				CategoryBean bean = new CategoryBean(code, name);
				list.add(bean);
			}
			// 5. カテゴリリストを返却
			return list;
		} catch (SQLException e) {
			// スタックトレースに表示
			e.printStackTrace();
			// DAO例外をスロー
			throw new DAOException("レコードの取得に失敗しました。", e);
		}
		
	}

}
