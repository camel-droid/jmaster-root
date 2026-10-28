package bundle.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import bundle.bean.CartBean;
import bundle.bean.CustomerBean;
import bundle.bean.ItemBean;

/**
 * customerテーブル、orderedテーブルとordered_detailテーブルにアクセスするDAO
 * TODO: 【DAOの分割】本来はアクセスするデータ単位でDAOを分けるべきなので、要検討！
 */
public class OrderDAO {

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
	private static final String SQL_INSERT_CUSTOMER = "INSERT INTO customer VALUES (?, ?, ?, ?, ?)";
	private static final String SQL_INSERT_ORDER    = "INSERT INTO ordered VALUES (?, ?, ?, ?)";
	private static final String SQL_INSERT_DETAIL    = "INSERT INTO ordered_detail VALUES (?, ?, ?)";
	
	/**
	 * コンストラクタ
	 * @throws DAOException JDBCドライバの読込みに失敗した場合
	 * TODO: 【基底DAOクラスの導入】同じ処理のDAOごとの記述はDRY原則に反するので、基底クラス導入を検討
	 */
	public OrderDAO() throws DAOException {
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

	/**
	 * 注文処理を実行する
	 * @param customer 注文した顧客インスタンス
	 * @param cart     注文する商品が入っているカート
	 * @return         注文番号
	 * @throws DAOException 顧客、注文、注文詳細のレコードの追加に失敗した場合
	 */
	public int saveOrder(CustomerBean customer, CartBean cart) throws DAOException {
		// 1. 顧客の追加
		// 1.1 最新の顧客シーケンスを取得して顧客インスタンスに設定
		int customerCode = this.getSeuence("customer");
		customer.setCode(customerCode);
		// 1.2 顧客インスタンスをcustomerテーブルに挿入
		try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
			 PreparedStatement pstmt = con.prepareStatement(SQL_INSERT_CUSTOMER);) {
			// データバインディング
			pstmt.setInt(1, customerCode);
			pstmt.setString(2, customer.getName());
			pstmt.setString(3, customer.getAddress());
			pstmt.setString(4, customer.getPhone());
			pstmt.setString(5, customer.getEmail());
			// SQLの実行
			pstmt.executeUpdate();
		} catch (SQLException e) {
			// スタックトレースに表示
			e.printStackTrace();
			// DAO例外をスロー
			// XXX: 【例外処理について】 研修テキストではメッセージなしだが、本サンプルシステムではメッセージを設定
			throw new DAOException("レコードの追加に失敗しました。", e);
		}
		
		// 2. 注文の追加
		// 2.1 最新の注文シーケンスを取得
		int orderNumber = this.getSeuence("ordered");
		// 2.3 注文インスタンスをorderedテーブルに挿入
		try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
			 PreparedStatement pstmt = con.prepareStatement(SQL_INSERT_ORDER);) {
			// データバインディング
			pstmt.setInt(1, orderNumber);
			pstmt.setInt(2, customerCode);
			pstmt.setDate(3, new Date(System.currentTimeMillis()));
			pstmt.setInt(4, cart.getTotal());
			// SQLの実行
			pstmt.executeUpdate();
		} catch (SQLException e) {
			// スタックトレースに表示
			e.printStackTrace();
			// DAO例外をスロー
			// XXX: 【例外処理について】 研修テキストではメッセージなしだが、本サンプルシステムではメッセージを設定
			throw new DAOException("レコードの追加に失敗しました。", e);
		}
		
		// 3. 注文詳細の追加
		// 3.1 注文詳細インスタンスをordered_detailテーブルに挿入
		try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
			 PreparedStatement pstmt = con.prepareStatement(SQL_INSERT_DETAIL);) {
			// 3.2 注文詳細インスタンスをordered_detailテーブルに挿入
			for (ItemBean item : cart.getItems()) {
				// パラメータバインディング
				pstmt.setInt(1, orderNumber);
				pstmt.setInt(2, item.getCode());
				pstmt.setInt(3, item.getQuantity());
				// SQLの実行
				pstmt.executeUpdate();
			}
		} catch (SQLException e) {
			// スタックトレースに表示
			e.printStackTrace();
			// DAO例外をスロー
			// XXX: 【例外処理について】 研修テキストではメッセージなしだが、本サンプルシステムではメッセージを設定
			throw new DAOException("レコードの追加に失敗しました。", e);
		}
		
		// 注文番号を返却
		return orderNumber;
	}

	/**
	 * 指定されたシーケンスを取得する
	 * XXX: 研修テキストではメソッド化していないが、本サンプルでは研修テキストを参考にしてメソッド化した
	 * @param  sequenceName取得するシーケンス名
	 * @return シーケンス
	 * @throws DAOException 
	 * @throws SQLException シーケンスの取得に失敗した場合
	 */
	private int getSeuence(String tableName) throws DAOException {
		String SQL_GET_SEQ = "SELECT nextVal('@seqName_code_seq' )";
		String SEQUENCE_PLACEHOLDER = "@seqName";
		String sql = SQL_GET_SEQ.replaceAll(SEQUENCE_PLACEHOLDER, tableName);
		try (// 1. データベース接続オブジェクトを取得
			 Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
			 // 2. SQL実行オブジェクトを取得
			 PreparedStatement pstmt = con.prepareStatement(sql);
			 // 3. SQLの実行と結果セットの取得
			 ResultSet rs = pstmt.executeQuery();) {
			// 3. 結果セットをシーケンスに変換
			int sequence = 0;
			if (rs.next()) {
				sequence = rs.getInt(1);
			}
			// 4. シーケンスを返却
			return sequence;
		} catch (SQLException e) {
			// スタックトレースに表示
			e.printStackTrace();
			// DAO例外をスロー
			// XXX: 【例外処理について】 研修テキストではメッセージなしだが、本サンプルシステムではメッセージを設定
			throw new DAOException("シーケンスの取得に失敗しました。", e);
		}
	}

}
