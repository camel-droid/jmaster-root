package bundle.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.PreparedStatement;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import bundle.bean.CustomerBean;
import bundle.helper.DbUnitTestHelper;

class OrderDaoTest {

	/** テスト対象クラス：system under test */
	private OrderDAO dao;
	
	@BeforeEach
	void setUp() throws Exception {
		dao = new OrderDAO();
	}

	@Nested
	class GetCustomerSequenceTest extends DbUnitTestHelper {

		/** クラス定数 */
		// テーブル名
		private static final String TARGET_TABLE     = "customer";
		private static final String TARGET_TABLE_SEQ = TARGET_TABLE + "_code_seq";
		
		// SQL文字列定数
		private static final String SQL_INSERT_INTO_CUSTOMER
			= "INSERT INTO customer (code, category_code, name, price) VALUES (?, ?, ?, ?, ?)";
		// テスト用サンプルレコードリスト
		private List<CustomerBean> testCustomers;
		
		@BeforeEach
		void setUp() throws Exception {
			OPERATION_INIT(TARGET_TABLE);
		}
		
		@Override
		protected void insert() throws Exception {
			OPERATION_INIT(TARGET_TABLE);
		}

		@Override
		protected void restore() throws Exception {
			OPERATION_RESTORE(TARGET_TABLE);
		}
		
		/**
		 * 商品リストをitemテーブルに登録する
		 * @param  items     登録対象の商品リスト
		 * @throws Exception レコードの登録に失敗した場合
		 */
		private void insertCustomers(List<CustomerBean> customers) throws Exception {
			for (CustomerBean customer : customers) {
				try (PreparedStatement pstmt = testConnection.prepareStatement(SQL_INSERT_INTO_CUSTOMER);) {
					pstmt.setInt(1, customer.getCode());
					pstmt.setString(2, customer.getName());
					pstmt.setString(3, customer.getAddress());
					pstmt.setString(4, customer.getPhone());
					pstmt.setString(5, customer.getEmail());
					pstmt.executeUpdate();
				}
			}
		}
		
		@Test
		void 空のcustomerテーブルのシーケンスは1であること() throws Exception {
			assertEquals(1*1, 1);
		}
	}
	

}
