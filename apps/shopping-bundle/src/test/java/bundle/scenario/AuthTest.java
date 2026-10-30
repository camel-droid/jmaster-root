package bundle.scenario;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import bundle.bean.AccountBean;
import bundle.bean.CustomerBean;
import bundle.helper.AuthDbTestHelper;
import bundle.helper.ScenarioTestHelper;

class AuthTest extends AuthDbTestHelper {

	@BeforeAll
	static void setUpBeforeClass() {
		// テスト用customerテーブルサンプルレコードリスト
		AuthDbTestHelper.testCustomers
			= List.of(
				new CustomerBean(11, "初代ゴジラ", "東京都八丈島近傍", "03-0000-1111", "godzilla@m-78.jp"),
				new CustomerBean(17, "変身忍者・嵐", "札幌市中央区", "011-000-0000", "aaa@excample.com"),
				new CustomerBean(31, "快傑ライオン丸", "東京都品川区", "03-1234-5678", "bbb@excample.com")
			  );
		// テスト用accountテーブルサンプルレコードリスト
		AuthDbTestHelper.testAccounts
			= List.of(
				new AccountBean("初代ゴジラ", "godzilla@m-78.jp", "pass01"),
				new AccountBean("変身忍者・嵐", "aaa@excample.com", "pass02"),
				new AccountBean("快傑ライオン丸", "bbb@excample.com", "pass03")
			  );
	}
	
	@Nested
	class LoginTest extends ScenarioTestHelper {
		
		@BeforeEach
		void setUp() {
			driver.get(APP_URL);
		}
		
		@Test
		void アプリケーションURLにアクセスするとログイン画面が表示されること() {
			// setup
			String expected = "SCR006";
			// execute
			String actual = this.getScreenId();
			// verify
			assertEquals(expected, actual);
			
		}
	}
}
