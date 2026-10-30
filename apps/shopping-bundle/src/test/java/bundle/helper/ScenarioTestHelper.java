package bundle.helper;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class ScenarioTestHelper {

	/** テストドライバ */
	protected static WebDriver driver;
	
	/** 
	 * クラス定数
	 */
	// アプリケーションURL
	protected static final String APP_URL = "http://localhost:8080/shopping-bundle";
	// スリープのインターバル時間：ミリ秒単位
	protected static final int SLEEP_INTERVAL = 1000;
	
	/**
	 * シナリオテストの前準備：テストドライバを起動
	 * @throws Exception
	 */
	@BeforeAll
	protected static void setUpBeforeClass() throws Exception {
		// テストドライバを起動
		driver = new ChromeDriver();
	}

	/**
	 * シナリオテストの後始末
	 * @throws Exception 
	 */
	@AfterAll
	protected static void tearDownAfterClass() throws Exception {
		// ブラウザをクローズ
		driver.quit();
	}
	
	/**
	 * 画面IDを取得する
	 * @return 画面ID
	 */
	protected String getScreenId() {
		return this.getIdAtt("body");
	}
	
	/**
	 * 指定されたセレクタのid属性を取得する
	 * @param  cssSelector 取得対象となるCSSセレクタ
	 * @return id属性値
	 */
	protected String getIdAtt(String cssSelector) {
		return driver.findElement(By.cssSelector(cssSelector)).getAttribute("id");
	}

	/**
	 * 指定されたセレクタのWebElementオブジェクトを取得する
	 * @param  cssSelector 取得対象となるCSSセレクタ
	 * @return WebElementオブジェクト
	 */
	protected WebElement getElement(String cssSelector) {
		return driver.findElement(By.cssSelector(cssSelector));
	}

}
