package bundle.scenario;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import bundle.bean.ItemBean;
import bundle.helper.DbUnitTestHelper;
import bundle.helper.ScenarioTestHelper;

class ShowItemTest extends ScenarioTestHelper {

	@BeforeEach
	void setUp() {
		// 個々のテストではあらかじめトップページが表示されている状態から開始
		driver.get(APP_URL);
	}
	
	@Nested
	class DisplayItemListTest {
		
		/** クラス定数 */
		static final String DISPLAYED_SCREEN_ID = "SCR001";
		
		/**
		 * このテストクラスは追加仕様を実施するためのテストである（追加仕様の内容はlist.jspを参照）。
		 * このテストクラスのエラーを解消することをもって追加仕様の実装を完了とする。
		 * このテストクラスを実施する際には@Disabledアノテーションの行をコメントアウトする。
		 * なお、エラーの解消とはJUnitビュに表示されるレッドバーがグリーンバーになるようにJSPを修正することとする。
		 */
		@Disabled
		@Nested
		class ForCategoryWithoutItemTest extends DbUnitTestHelper {
			
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
			void 商品が登録されていないカテゴリをクリックすると商品一覧にメッセージが表示されること() throws Exception {
				// setup
				String expected = "指定されたカテゴリの商品は見つかりませんでした。";
				this.prepareTestItems(); // テストデータの登録
				driver.findElement(By.linkText("DVD")).click(); // 「DVD」カテゴリのリンクをクリック
				// execute
				String actual = driver.findElement(By.cssSelector("p.message")).getText();
				// verify
				// 画面IDの確認
				assertEquals("SCR001", driver.findElement(By.tagName("body")).getAttribute("id"));
				// メッセージの表示確認
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
					if (bean.getCode() * bean.getCategoryCode() % 2 == 1) {
						testItems.add(bean);
					}
				}
				// 生成した商品リストをテーブルに挿入
				this.insert();
			}
		}
		
		@Nested
		class ForCategoryWithItemTest {
			
			@ParameterizedTest
			@MethodSource("displayItemListByCategoryLinkCkickProvider")
			void カテゴリリンクをクリックすると当該カテゴリの商品が表示される(String targetLintText, List<ItemBean> expectedList) {
				// setup
				driver.findElement(By.linkText(targetLintText)).click(); // メニュのカテゴリリンクをクリック
				
				// execute
				// 画面IDの実行値を取得
				String actualScreenId = driver.findElement(By.tagName("body")).getAttribute("id");
				// 表示商品領域を取得
				List<WebElement> actualList = driver.findElements(By.cssSelector("form"));
				
				// verify
				// 画面IDの確認
				assertEquals(DISPLAYED_SCREEN_ID, actualScreenId);
				// 表示件数の確認
				assertEquals(expectedList.size(), actualList.size());
				// 各商品の確認
				for (int i = 0; i < expectedList.size(); ++i) {
					// 表示内容の確認
					WebElement actual = actualList.get(i);
					ItemBean expected = expectedList.get(i);
					assertEquals(Integer.toString(expected.getCode()),
								 actual.findElement(By.cssSelector("b:nth-of-type(1)")).getText());
					assertEquals(expected.getName(),
								 actual.findElement(By.cssSelector("b:nth-of-type(2)")).getText());
					assertEquals(expected.getPrice() + "円",
								 actual.findElement(By.cssSelector("b:nth-of-type(3)")).getText());
					// ［カートに追加］ボタンの表示確認
					WebElement addButton
						= actual.findElement(By.cssSelector("input[type='submit'][value='カートに追加']"));
					assertTrue(addButton.isDisplayed());				
				}
			}
			
			/**
			 * カテゴリリンクをクリックするテスト用のテストパラメータを提供する
			 * @return テストパラメータ
			 *         テストパラメータは以下の項目を返す
			 *         	・クリックするカテゴリリンクの表示文字列
			 *         	・表示商品の期待値（List<ItemBean>）
			 */
			static Stream<Arguments> displayItemListByCategoryLinkCkickProvider() {
				return Stream.of(
							// 「ゲーム」カテゴリのリンクをクリックした場合
							Arguments.of(
								"ゲーム",
								List.of(
									new ItemBean(7, 3, "パズルゲーム", 780),
									new ItemBean(8, 3, "Invader Fighter", 3400),
									new ItemBean(9, 3, "Play the BasketBall",2200)
								 )
							),
						
							// 「DVD」カテゴリのリンクをクリックした場合
							Arguments.of(
								"DVD",
								List.of(
									new ItemBean(4, 2, "懐かしのアニメシリーズ", 2000),
									new ItemBean(5, 2, "The Racer", 1000),
									new ItemBean(6, 2, "Space Wars 3", 1800)
								 )
							),
						
							// 「本」カテゴリのリンクをクリックした場合
							Arguments.of(
								"本",
								List.of(
									new ItemBean(1, 1, "Javaの基本", 2500),
									new ItemBean(2, 1, "MLB Fun", 980),
									new ItemBean(3, 1, "料理 BOOK!", 1200)
								 )
							)
					   );
			}
		}
		
	}
	
	@Nested
	class DisplatTopTest {
		
		/** クラス定数 */
		static final String DISPLAYED_SCREEN_ID = "SCR009";
		
		@Test
		void ようこそリンクをクリックするとトップ画面が表示されること() {
			// setup
			driver.findElement(By.linkText("ようこそ")).click(); // メニュの「ようこそ」リンクをクリック
			// execute
			String actual = driver.findElement(By.tagName("body")).getAttribute("id");
			// verify
			assertEquals(DISPLAYED_SCREEN_ID, actual);
		}
		
		@Test
		void トップ画面にリンクメニュが表示されること() {
			// setup
			List<String> expectedList = List.of("ようこそ", "本", "DVD", "ゲーム", "カートを見る");
			// execute
			List<WebElement> actualList = driver.findElements(By.tagName("a"));
			// verify
			// リンクの表示数確認
			assertEquals(expectedList.size(), actualList.size());
			// リンクの表示文字列の確認：href属性のクエリ文字列の検証は商品一覧への遷移シナリオで検証
			for (int i = 0; i < expectedList.size(); ++i) {
				String expected = expectedList.get(i);
				String actual = actualList.get(i).getText();
				assertEquals(expected, actual);
			}
		}
		
		@Test
		void アプリケーションURLでアクセスするとトップ画面が表示されること() {
			// execute
			String actual = driver.findElement(By.tagName("body")).getAttribute("id");
			// verify
			assertEquals(DISPLAYED_SCREEN_ID, actual);
		}
		
	}

	/**
	 * 画面IDを取得する
	 * @return 画面ID
	 */
	public String getScreenId() {
		return this.getIdAtt("body");
	}
	
	/**
	 * 指定されたセレクタのid属性を取得する
	 * @param  target 取得対象となるCSSセレクタ
	 * @return id属性値
	 */
	public String getIdAtt(String target) {
		return driver.findElement(By.cssSelector(target)).getAttribute("id");
	}

}
