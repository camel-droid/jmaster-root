package bundle.helper;

import java.sql.PreparedStatement;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import bundle.bean.ItemBean;

public class ItemRecordsCreater extends DbUnitTestHelper {
	
	/** クラス定数 */
	// SQL文字列定数
	private static final String SQL_INSERT_INTO_ITEM 
		= "INSERT INTO item (code, category_code, name, price) VALUES (?, ?, ?, ?)";
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
	// テスト用サンプルレコードリスト
	private List<ItemBean> testItems;
	
	@BeforeEach
	void setUp() throws Exception {
		OPERATION_INIT("item");  // テーブルの初期化
		this.prepareTestItems(); // テストデータ投入（リストア用データを投入しているだけ）
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
	
	/**
	 * テストデータを生成する：投入するテストデータは本メソッドで生成
	 * @param  items     テストデータ用の商品リスト
	 * @throws Exception テストデータ挿入に失敗した場合
	 */
	private void prepareTestItems() throws Exception {
		this.testItems = restoredItems;
		// 生成した商品リストをテーブルに挿入
		this.insert();
	}
	
}
