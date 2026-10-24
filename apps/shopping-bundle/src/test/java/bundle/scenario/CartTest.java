package bundle.scenario;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import bundle.bean.ItemBean;
import bundle.helper.ScenarioTestHelper;

/**
 * カート機能のテストクラス
 * このテストではセションスコープが関係するが、テストの付加を減らすため
 * サーバを再起動してセッションをクリアしてからテストを実施すること
 */
class CartTest extends ScenarioTestHelper {

	@BeforeEach
	void setUp() throws Exception {
		// 個々のテストではあらかじめトップページが表示されている状態から開始
		driver.get(APP_URL);
		driver.findElement(By.linkText("本")).click(); // 「本」カテゴリのリンクをクリック
	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	class DisplayCart {
		
		// このテストクラスでは、改めてテストデータの挿入はせず、デフォルトのレコードでテストを実施
		// 具体的な商品群については bundle.scenario.ItemDaoTestの復元用サンプルレコードリスト参照
		@Test
		@Order(3)
		void すでにカートに入っている商品と同じ商品コードの商品を追加すると数量が追加される() {
			// setup
			List<ItemBean> expectedList 
				= List.of(new ItemBean(1, 1, "Javaの基本", 2500, 2), 
						  new ItemBean(2, 1, "MLB Fun", 980, 4));
			// formタグのリストを取得
			List<WebElement> forms = driver.findElements(By.cssSelector("form"));
			
			// １行目に表示されているセレクトボックスを取得
			Select select = new Select(forms.get(1).findElement(By.cssSelector("select[name='quantity']")));
			select.selectByValue("1"); // 数量を選択
			
			// １行目に表示されている商品の［カートに追加］ボタンを取得
			WebElement button = forms.get(0).findElement(By.cssSelector("input[type='submit'][value='カートに追加']"));
			button.click(); // ［カートに追加］ボタンをクリック
			
			// execute
			// 商品表示行を取得：見出し行（先頭行）と総計表示行（末尾行）以外を取得
			List<WebElement> actualList = driver.findElements(By.cssSelector("table tr:not(:first-child):not(:last-child)"));
			
			// verify
			// 商品の表示数確認
			assertEquals(expectedList.size(), actualList.size());
			// 商品の情報表示確認
			for (int i = 0; i < expectedList.size(); ++i) {
				ItemBean expected = expectedList.get(i);
				List<WebElement> actual = actualList.get(i).findElements(By.tagName("td"));
				assertEquals(Integer.toString(expected.getCode()), actual.get(0).getText());
				assertEquals(expected.getName(), actual.get(1).getText());
				assertEquals(expected.getPrice() + "円", actual.get(2).getText());
				assertEquals(Integer.toString(expected.getQuantity()), actual.get(3).getText());
				assertEquals((expected.getPrice() * expected.getQuantity()) + "円", 
							  actual.get(4).getText());
			}
			
		}
		
		@Test
		@Order(2)
		void カートに入っていない商品を追加できる() {
			// setup
			List<ItemBean> expectedList 
				= List.of(new ItemBean(1, 1, "Javaの基本", 2500, 1), 
						  new ItemBean(2, 1, "MLB Fun", 980, 4));
			// 総計額の期待値
			int total = 0;
			for (ItemBean item : expectedList) {
				total += item.getPrice() * item.getQuantity();
			}
			String expectedTotal = "総計：" + total + "円";
			
			// formタグのリストを取得
			List<WebElement> forms = driver.findElements(By.cssSelector("form"));
			
			// ２行目に表示されているセレクトボックスを取得
			Select select = new Select(forms.get(1).findElement(By.cssSelector("select[name='quantity']")));
			select.selectByValue(Integer.toString(expectedList.get(1).getQuantity())); // 数量を選択
			
			// ２行目に表示されている商品の［カートに追加］ボタンを取得
			WebElement button = forms.get(1).findElement(By.cssSelector("input[type='submit'][value='カートに追加']"));
			button.click(); // ［カートに追加］ボタンをクリック
			
			// execute
			// 商品表示行を取得：見出し行（先頭行）と総計表示行（末尾行）以外を取得
			List<WebElement> actualList = driver.findElements(By.cssSelector("table tr:not(:first-child):not(:last-child)"));
			
			// verify
			// 商品の表示数確認
			assertEquals(expectedList.size(), actualList.size());
			// 商品の情報表示確認
			for (int i = 0; i < expectedList.size(); ++i) {
				ItemBean expected = expectedList.get(i);
				List<WebElement> actual = actualList.get(i).findElements(By.tagName("td"));
				assertEquals(Integer.toString(expected.getCode()), actual.get(0).getText());
				assertEquals(expected.getName(), actual.get(1).getText());
				assertEquals(expected.getPrice() + "円", actual.get(2).getText());
				assertEquals(Integer.toString(expected.getQuantity()), actual.get(3).getText());
				assertEquals((expected.getPrice() * expected.getQuantity()) + "円", 
							  actual.get(4).getText());
			}
			// 商品の総計額確認
			assertEquals(expectedTotal, driver.findElement(By.cssSelector("td[colspan='6']")).getText());
		}
		
		@Test
		@Order(1)
		void カートに追加ボタンをクリックするとカート画面が表示される() throws Exception {
			// setup
			String expected = "SCR002";
			// formタグのリストを取得
			List<WebElement> forms = driver.findElements(By.tagName("form"));
			// 1行目に表示されている［カートに追加］ボタンを取得
			WebElement button = forms.get(0).findElement(By.cssSelector("input[type='submit'][value='カートに追加']"));
			button.click(); // ［カートに追加］ボタンをクリック
			// execute
			String actual = driver.findElement(By.tagName("body")).getAttribute("id");
			// verify
			// 画面IDの表示確認
			assertEquals(expected, actual);
		}
	}
	
}
