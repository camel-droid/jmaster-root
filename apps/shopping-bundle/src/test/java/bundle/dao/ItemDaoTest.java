package bundle.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import bundle.bean.ItemBean;
import bundle.helper.DbUnitTestHelper;

class ItemDaoTest extends DbUnitTestHelper {

	/** テスト対象クラス：system under test */
	private ItemDAO sut;
	
	/** クラス定数 */
	// SQL文字列定数
	private static final String SQL_INSERT_INTO_ITEM 
		= "INSERT INTO item (code, category_code, name, price) VALUES (?, ?, ?, ?)";
	// テスト用サンプルレコードリスト
	private List<ItemBean> testItems;
	// 復元用サンプルレコードリスト
	private static final List<ItemBean> restoredItems 
		= List.of(
			new ItemBean(1, 1, "Javaの基本", 2500),
			new ItemBean(2, 1, "MLB Fun", 980),
			new ItemBean(3, 1, "料理 BOOK!", 1200),
			new ItemBean(4, 2, "懐かしのアニメシリーズ", 2000),
			new ItemBean(5, 2, "The Racer", 1000),
			new ItemBean(6, 2, "Space Wars 3", 1800),
			new ItemBean(7, 3, "パズルゲーム", 780),
			new ItemBean(8, 3, "Invader Fighter", 3400),
			new ItemBean(9, 3, "Play the BasketBall",2200)
		);
	
	@BeforeEach
	void setUp() throws Exception {
		sut = new ItemDAO();
		OPERATION_INIT("item");
	}
	
	@AfterEach
	void tearDown() throws Exception {
		OPERATION_RESTORE("item");
	}
	
	@Override
	protected void insert() throws Exception {
		this.insertItems(testItems);
	}
	
	@Override
	protected void restore() throws Exception {
		this.insertItems(restoredItems);
	}
	
	/**
	 * 商品リストをitemテーブルに登録する
	 * @param  items     登録対象の商品リスト
	 * @throws Exception レコードの登録に失敗した場合
	 */
	private void insertItems(List<ItemBean> items) throws Exception {
		for (ItemBean item : items) {
			try (PreparedStatement pstmt = testConnection.prepareStatement(SQL_INSERT_INTO_ITEM);) {
				pstmt.setInt(1, item.getCode());
				pstmt.setInt(2, item.getCategoryCode());
				pstmt.setString(3, item.getName());
				pstmt.setInt(4, item.getPrice());
				pstmt.executeUpdate();
			}
		}
	}
	
	@Test
	void テーブルに登録されていない商品を取得できない() throws Exception {
		// setup
		this.prepareTestItems();
		int target = 9;
		// execute
		ItemBean actual = sut.findByCode(target);
		// verify
		assertNull(actual);
	}
	
	@Test
	void テーブルに登録されている商品を取得できる() throws Exception {
		// setup
		this.prepareTestItems();
		int target = 4;
		ItemBean expected = new ItemBean(4, 2, "懐かしのアニメシリーズ", 2000);
		// execute
		ItemBean actual = sut.findByCode(target);
		// verify
		assertEquals(expected, actual);
	}

	/**
	 * テストデータを生成する
	 * @param  items     テストデータ用の商品リスト
	 * @throws Exception テストデータ挿入に失敗した場合
	 */
	private void prepareTestItems() throws Exception {
		this.testItems = new ArrayList<>();
		for (ItemBean bean : restoredItems) {
			if (bean.getCode() % 3 == 1) {
				// 商品コードの3に対する剰余が1である商品を取得
				testItems.add(bean);
			}
		}
		// 生成した商品リストをテーブルに挿入
		this.insert();
	}

}
