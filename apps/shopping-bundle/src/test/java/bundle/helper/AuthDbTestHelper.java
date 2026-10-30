package bundle.helper;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import bundle.bean.AccountBean;
import bundle.bean.CustomerBean;

public class AuthDbTestHelper extends DbUnitTestHelper {
	
	/** クラス定数 */
	// SQL文字列定数
	private static final String SQL_INSERT_INTO_CUSTOMER 
		= "INSERT INTO customer (code, name, address, tel, email) VALUES (?, ?, ?, ?, ?)";
	private static final String SQL_INSERT_INTO_AUTH 
		= "INSERT INTO account (name, email, password) VALUES (?, ?, ?)";
	// テスト用customerテーブルサンプルレコードリスト
	protected static List<CustomerBean> testCustomers;
	// テスト用accountテーブルサンプルレコードリスト
	protected static List<AccountBean> testAccounts;
	
	// 復元用サンプルレコードリスト
	private static final List<CustomerBean> restoredCustomer = new ArrayList<CustomerBean>();
	private static final List<AccountBean> restoredAcount = new ArrayList<AccountBean>();

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		DbUnitTestHelper.setUpBeforeClass();
	}
	
	@BeforeEach
	void setUp() throws Exception {
		OPERATION_INIT("customer");
		OPERATION_INIT("account");
		this.insert();
	}

	@AfterEach
	void tearDown() throws Exception {
		OPERATION_RESTORE("customer");
		OPERATION_RESTORE("account");
	}

	/**
	 * テストに関係するテーブルのテスト事前状態を再現する
	 */
	@Override
	protected void insert() throws Exception {
		this.insertCustomers(testCustomers);
		this.insertAccounts(testAccounts);
	}

	/**
	 * テストに関係するテーブルのレコードを復元する
	 */
	@Override
	protected void restore() throws Exception {
		this.insertCustomers(restoredCustomer);
		this.insertAccounts(restoredAcount);
	}

	/**
	 * テスト用レコードをaccountテーブルに登録する
	 * @param  testAccounts テスト用アカウントインスタンス
	 * @throws Exception    レコードの挿入に失敗した場合
	 */
	private void insertAccounts(List<AccountBean> testAccounts) throws Exception {
		for (AccountBean account : testAccounts) {
			try (PreparedStatement pstmt = testConnection.prepareStatement(SQL_INSERT_INTO_AUTH)) {
				pstmt.setString(1, account.getName());
				pstmt.setString(2, account.getEmail());
				pstmt.setString(3, account.getPassword());
				pstmt.executeUpdate();
			}
		}
	}

	/**
	 * テスト用レコードをcustomerテーブルに登録する
	 * @param  testCustomers テスト用顧客インスタンス
	 * @throws Exception     レコードの挿入に失敗した場合
	 */
	private void insertCustomers(List<CustomerBean> testCustomers) throws Exception {
		for (CustomerBean customer : testCustomers) {
			try (PreparedStatement  pstmt = testConnection.prepareStatement(SQL_INSERT_INTO_CUSTOMER)) {
				pstmt.setInt(1, customer.getCode());
				pstmt.setString(2, customer.getName());
				pstmt.setString(3, customer.getAddress());
				pstmt.setString(4, customer.getPhone());
				pstmt.setString(5, customer.getEmail());
				pstmt.executeUpdate();
			}
		}
	}

}
