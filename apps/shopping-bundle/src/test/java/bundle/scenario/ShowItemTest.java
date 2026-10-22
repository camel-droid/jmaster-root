package bundle.scenario;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

class ShowItemTest {

	/** テストドライバ */
	private static WebDriver driver;
	
	/** アプリケーションURL */
	private static final String APP_URL = "http://localhost:8080/shopping-bundle";
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		// シナリオテストの前準備：テストドライバを起動
		driver = new ChromeDriver();
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		// シナリオテストの後始末：
		driver.quit();
	}
	
	@BeforeEach
	void setUp() {
		// 個々のテストではあらかじめトップページが表示されている状態から開始
		driver.get(APP_URL);
	}

	@Nested
	class DisplatTopTest {
		
		@Test
		void ようこそリンクをクリックするとトップ画面が表示されること() {
			// setup
			String expected = "SCR009";
			driver.findElement(By.linkText("ようこそ")).click();
			// execute
			String actual = driver.findElement(By.tagName("body")).getAttribute("id");
			// verify
			assertEquals(expected, actual);
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
			// setup
			String expected = "SCR009";
			// execute
			String actual = driver.findElement(By.tagName("body")).getAttribute("id");
			// verify
			assertEquals(expected, actual);
		}
		
	}

}
