package bundle.scenario;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import bundle.bean.ItemBean;
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

}
