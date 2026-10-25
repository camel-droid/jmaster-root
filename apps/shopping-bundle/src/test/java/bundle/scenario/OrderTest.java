package bundle.scenario;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import bundle.helper.ItemRecordsCreater;
import bundle.helper.ScenarioTestHelper;

class OrderTest extends ItemRecordsCreater {

	@Nested
	class CustomerTest extends ScenarioTestHelper {
		
		/** クラス定数 */
		private static final int IDX_SCENARIO_CART     = 1;
		private static final int IDX_SCENARIO_CUSTOMER = 2;
		
		@BeforeEach
		void setUp() throws Exception {
			// 個々のテストではあらかじめトップページが表示されている状態から開始
			driver.get(APP_URL);
			this.prepareSampleCart(IDX_SCENARIO_CUSTOMER);
		}
		
		@Nested
		class DisplayCustomerTest {
			@Test
			void カート画面で注文するボタンをクリックすると送付先情報入力画面が表示されること() {
				// setup
				String expected = "SCR003";
				// execute
				// ［注文する］ボタンをクリック
				List<WebElement> forms = driver.findElements(By.tagName("form"));
				WebElement orderButton = forms.getLast().findElement(By.tagName("button"));
				orderButton.click();
				String actual = driver.findElement(By.tagName("body")).getAttribute("id");
				// verify
				assertEquals(expected, actual);
			}
		}
		
		/**
		 * シナリオごとのカートを準備する
		 * @param  index     シナリオを特定するためのインデックス
		 * @throws Exception 表示確認スリープに失敗した倍
		 */
		private void prepareSampleCart(int index) throws Exception {
			
			switch (index) {
			case IDX_SCENARIO_CART:
				this.putInCart("本", "Javaの基本", 3);
				this.putInCart("本", "MLB Fun", 4);
				this.putInCart("DVD", "懐かしのアニメシリーズ", 3);
				break;
			case IDX_SCENARIO_CUSTOMER:
				this.putInCart("本", "Javaの基本", 1);
				this.putInCart("DVD", "The Racer", 2);
				break;
			}
			
		}
		
		/**
		 * カートに商品を追加する
		 * @param linkText   メニュリンクの表示文字列
		 * @param name       商品名
		 * @param quantity   数量
		 * @throws Exception 表示確認スリープに失敗した倍
		 */
		private void putInCart(String linkText, String name, int quantity) throws Exception {
			// 1. カテゴリリンクをクリック
			driver.findElement(By.linkText(linkText)).click();
			
			// 2. 商品をカートに追加
			// 2.1 追加する商品の表示行を特定
			List<WebElement> forms = driver.findElements(By.tagName("form"));
			int index = getSelectedIndex(name, forms);
			// 2.2 １行目に表示されているセレクトボックスを取得
			Select select = new Select(forms.get(index).findElement(By.cssSelector("select[name='quantity']")));
			select.selectByValue(Integer.toString(quantity)); // 数量を選択
			
			// １行目に表示されている商品の［カートに追加］ボタンを取得
			WebElement addButton = forms.get(index).findElement(By.cssSelector("input[type='submit'][value='カートに追加']"));
			addButton.click(); // ［カートに追加］ボタンをクリック
			// 表示確認のためのスリープ
			Thread.sleep(SLEEP_INTERVAL / 2);
		}
		
		/**
		 * 選んだ商品の表示行のインデックスを取得する
		 * @param  name  選択する種品名
		 * @param  forms 商品表示領域（form要素）のリスト
		 * @return 商品表示行のインデックス
		 */
		private int getSelectedIndex(String name, List<WebElement> forms) {
			int index = 0;
			for (WebElement form : forms) {
				String title = form.findElements(By.tagName("b")).get(1).getText();
				if (title.equals(name))  {
					break;
				}
				index++;
			}
			return index;
		}
		
	}

}
