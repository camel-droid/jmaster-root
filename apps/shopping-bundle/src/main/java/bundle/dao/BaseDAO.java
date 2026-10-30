package bundle.dao;

/**
 * DAOが継承する基底クラス
 */
public class BaseDAO {
	/**
	 * クラス定数
	 * TODO: ほかのDAOでも利用できるように定数クラスへの分離または基底DAOクラスの追加を検討
	 */
	// JDBCドライバおよびデータベース接続情報
	protected static final String JDBC_DRIVER = "org.postgresql.Driver";
	protected static final String DB_URL_FULL = "jdbc:postgresql://localhost:5432/jmaster_shopping_db";
	protected static final String DB_URL      = "jdbc:postgresql:jmaster_shopping_db";
	protected static final String DB_USER     = "student";
	protected static final String DB_PASSWORD = "himitu";
	// SQL文
	protected static final String SQL_INSERT_CUSTOMER = "INSERT INTO customer VALUES (?, ?, ?, ?, ?)";
	protected static final String SQL_INSERT_ORDER    = "INSERT INTO ordered VALUES (?, ?, ?, ?)";
	protected static final String SQL_INSERT_DETAIL    = "INSERT INTO ordered_detail VALUES (?, ?, ?)";
	
	/**
	 * コンストラクタ
	 * @throws DAOException JDBCドライバの読込みに失敗した場合
	 * TODO: 【基底DAOクラスの導入】同じ処理のDAOごとの記述はDRY原則に反するので、基底クラス導入を検討
	 */
	protected BaseDAO() throws DAOException {
		try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException e) {
			// スタックトレースに表示
			e.printStackTrace();
			// DAO例外をスロー
			// XXX: 【例外処理について】 研修テキストではメッセージなしだが、本サンプルシステムではメッセージを設定
			throw new DAOException("JDBCドライバの読込みに失敗しました。", e);
		}
	}

}
