package bundle.helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

public abstract class DbUnitTestHelper {

	/**
	 * クラス変数
	 */
	// データベース接続情報文字列定数
	protected static final String JDBC_DRIVER = "org.postgresql.Driver";
	protected static final String DB_URL      = "jdbc:postgresql://localhost:5432/jmaster_shopping_db";
	protected static final String DB_USER     = "student";
	protected static final String DB_PASSWORD = "himitu";
	// 初期化用TRUNCATE
	protected static final String SQL_TRUNCATE = "TRUNCATE TABLE @table_name RESTART IDENTITY CASCADE"; 
	
	/** テスト補助変数 */
	protected static Connection testConnection;
	
	@BeforeAll
	protected static void setUpBeforeClass() throws Exception {
		// JDBCドライバの読込み
		Class.forName(JDBC_DRIVER);
		// テストデータ投入用データベース接続オブジェクトの取得
		testConnection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		// テストデータ投入用データベース接続オブジェクトの破棄
		testConnection.close();
	}

	/**
	 * テーブルを初期化する：自動付番のフィールドも初期化
	 * @param  tableName 初期化対象のテーブル名
	 * @throws Exception テーブルの初期化に失敗した場合
	 */
	public static void OPERATION_INIT(String tableName) throws Exception {
		String sql = SQL_TRUNCATE.replace("@table_name", tableName);
		try (PreparedStatement pstmt = testConnection.prepareStatement(sql);) {
			pstmt.executeUpdate();
		}
	}
	
	/**
	 * テストデータを挿入する：挿入するテストデータは継承クラスで定義する
	 * @throws Exception テストデータ挿入に失敗した場合
	 */
	protected abstract void insert() throws Exception;
	
	/**
	 * サンプルデータを復元する：復元するサンプルデータは継承クラスで定義する
	 * @throws Exception サンプルデータ挿入に失敗した場合
	 */
	protected abstract void restore() throws Exception;
	
	/**
	 *テーブルのレコードを復元する：復元するテストデータは継承クラスで定義する
	 * @param  tableName 初期化対象のテーブル名
	 * @throws Exception テーブルの復元に失敗した場合
	 */
	public final void OPERATION_RESTORE(String tableName) throws Exception {
		OPERATION_INIT(tableName);
		restore();
	}

}
