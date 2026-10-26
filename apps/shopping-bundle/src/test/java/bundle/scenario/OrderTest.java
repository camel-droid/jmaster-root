package bundle.scenario;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import bundle.bean.CustomerBean;
import bundle.helper.ItemRecordsCreater;
import bundle.helper.ScenarioTestHelper;

class OrderTest extends ItemRecordsCreater {

	/** クラス定数 */
	private static final int IDX_SCENARIO_CART     = 1;
	private static final int IDX_SCENARIO_CUSTOMER = 2;
	
	@Nested
	class OrderedTest extends ScenarioTestHelper {
		
		
		
	}
	
	@Nested
	class CustomerTest extends ScenarioTestHelper {
		
		@BeforeEach
		void setUp() throws Exception {
			// 個々のテストではあらかじめトップページが表示されている状態から開始
			driver.get(APP_URL);
			this.prepareSampleCart(IDX_SCENARIO_CUSTOMER);
		}
		
		@Nested
		class DisplayConfirmTest {
			
			@BeforeEach
			void setUp() throws Exception {
				// 商品をカートに追加して［注文する］ボタンをクリックするシナリオの実行
				clickOrderButton();
			}
			
			@Test
			void 送付先情報を入力して確認ボタンをクリックすると確認画面が表示されること() throws Exception {
				// setup：商品をカートに追加して［注文する］ボタンをクリックするシナリオの実行
				String expected = "SCR004";
				CustomerBean target = new CustomerBean("鈴木一郎", "東京都渋谷区", "03-3333-2222", "ichiro@abc.com");
				Thread.sleep(SLEEP_INTERVAL);
				
				// execute：送付先情報を入力し［確認する］ボタンをクリックするシナリオの実行
				List<WebElement> inputs = driver.findElements(By.tagName("input"));
				for (WebElement input : inputs) {
					switch (input.getAttribute("name")) {
					case "name":
						input.sendKeys(target.getName());
						break;
					case "address":
						input.sendKeys(target.getAddress());
						break;
					case "tel":
						input.sendKeys(target.getPhone());
						break;
					case "email":
						input.sendKeys(target.getEmail());
						break;
					default:
						break;
					}
				}
				// 【確認する】
				clickConfirmButton();
				Thread.sleep(SLEEP_INTERVAL);
				
				// verify：遷移先の画面IDの確認
				// 画面IDの表示取得
				String actual = driver.findElement(By.tagName("body")).getAttribute("id");
				assertEquals(expected, actual);
				// 表示内容の表示確認
				List<WebElement> rows = driver.findElements(By.cssSelector("form table tr"));
				assertEquals(target.getName(), rows.get(0).findElement(By.cssSelector("td:last-child")).getText());
				assertEquals(target.getAddress(), rows.get(1).findElement(By.cssSelector("td:last-child")).getText());
				assertEquals(target.getPhone(), rows.get(2).findElement(By.cssSelector("td:last-child")).getText());
				assertEquals(target.getEmail(), rows.get(3).findElement(By.cssSelector("td:last-child")).getText());
				
			}
		}
		
		@Nested
		class DisplayCustomerTest {
			@Test
			void カート画面で注文するボタンをクリックすると送付先情報入力画面が表示されること() throws Exception {
				// setup
				String expected = "SCR003";
				// execute
				// ［注文する］ボタンをクリック
				clickOrderButton();
				Thread.sleep(SLEEP_INTERVAL);
				String actual = driver.findElement(By.tagName("body")).getAttribute("id");
				// verify
				assertEquals(expected, actual);
			}
		}
		
		/**
		 * ［確認する］ボタンのクリックイベントを実行する
		 */
		private void clickConfirmButton() {
			clickButton();
		}
		
		/**
		 * [注文する]ボタンのクリックイベントを実行する
		 */
		private void clickOrderButton() {
			clickButton();
		}
		
		/**
		 * ボタンをクリックするイベントを実行する
		 */
		private void clickButton() {
			List<WebElement> forms = driver.findElements(By.tagName("form"));
			WebElement button = forms.getLast().findElement(By.tagName("button"));
			button.click();
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
			
			// 対象商品の表示行の［カートに追加］ボタンを取得
			WebElement addButton = forms.get(index).findElement(By.cssSelector("input[type='submit'][value='カートに追加']"));
			addButton.click(); // ［カートに追加］ボタンをクリック
			// レンダリング完了前の操作を防止するために待機
			Thread.sleep(SLEEP_INTERVAL / 2);
		}
		
		/**
		 * 選んだ商品の表示行のインデックスを取得する
		 * @param  name  選択する商品名
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
