package bundle.scenario;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;

import bundle.bean.AccountBean;
import bundle.bean.CustomerBean;
import bundle.dao.AccountDAO;
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
				new AccountBean(11, "初代ゴジラ", "godzilla@m-78.jp", "pass01"),
				new AccountBean(17, "変身忍者・嵐", "aaa@excample.com", "pass02"),
				new AccountBean(31, "快傑ライオン丸", "bbb@excample.com", "pass03")
			  );
	}
	
	@Nested
	class DbAccessTest {
		
		/** テスト対象クラス：system under test */
		private AccountDAO sut;
		
		@BeforeEach
		void setUp() throws Exception {
			sut = new AccountDAO();
		}
		
		@ParameterizedTest
		@MethodSource("authDbProvider")
		void ユーザ認証できること(AccountBean target, AccountBean expected ) throws Exception {
			// setup & execute
			AccountBean actual = sut.findByEmailAndPassword(target.getEmail(), target.getPassword());
			// verify
			assertEquals(expected, actual);
			
		}
		
		/**
		 * ユーザ認証テスト用のテストパラメータを提供する
		 * @return テストパラメータ
		 *         テストパラメータは以下の項目を返す
		 *         	・認証に用いるメールアドレスを持つアカウントインスタンス
		 *         	・取得したアカウントインスタンスの期待値（AccountBean）
		 */
		static Stream<Arguments> authDbProvider() {
			return 
				Stream.of(
					// 登録されているメールアドレスと登録されていないパスワードの組み合わせではnullとなること
					Arguments.of(
						new AccountBean("godzilla@m-78.jp", "pass00"),
						null
					),
					
					// 登録されていないメールアドレスと登録されているパスワードの組み合わせではnullとなること
					Arguments.of(
						new AccountBean("info@examlpe.com", "pass02"),
						null
					),
					
					// ともに登録されていないメールアドレスとパスワードの組み合わせではnullとなること
					Arguments.of(
						new AccountBean("info@examlpe.com", "pass00"),
						null
					),
					
					// 登録されたアカウントを取得できる
					Arguments.of(
						new AccountBean("godzilla@m-78.jp", "pass01"),
						new AccountBean(11, "初代ゴジラ", "godzilla@m-78.jp", "pass01")
					)
				);
		}
	}
	
	@Nested
	class ScenarioTest extends ScenarioTestHelper {
		
		@BeforeEach
		void setUp() {
			driver.get(APP_URL);
		}
		
		@Test
		void 正しくないメールアドレスとパスワードを入力してログインに失敗すること() {
			// setup
			Map<String, String> expected = new HashMap<String, String>();
			expected.put("id", "SCR006");
			expected.put("message", "メールアドレスとパスワードが一致しませんでした。");
			AccountBean target = new AccountBean("godzilla@m-78.jp", "password");
			driver.findElement(By.cssSelector("input[name='email']")).sendKeys(target.getEmail());
			driver.findElement(By.cssSelector("input[name='password']")).sendKeys(target.getPassword());
			driver.findElement(By.cssSelector("button[name='login']")).click();
			// execute
			Map<String, String> actual = new HashMap<String, String>();
			actual.put("id", this.getScreenId());
			actual.put("message", this.getElement("p[class='message error'").getText());
			// verify
			Set<String> keys = expected.keySet();
			for (String key : keys) {
				assertEquals(expected.get(key), actual.get(key));
			}
			
		}
		
		@Test
		void 正しいメールアドレスとパスワードを入力してログインできること() {
			// setup
			String expected = "SCR009";
			AccountBean target = new AccountBean("godzilla@m-78.jp", "pass01");
			driver.findElement(By.cssSelector("input[name='email']")).sendKeys(target.getEmail());
			driver.findElement(By.cssSelector("input[name='password']")).sendKeys(target.getPassword());
			driver.findElement(By.cssSelector("button[name='login']")).click();
			// execute
			String actual = this.getScreenId();
			// verify
			assertEquals(expected, actual);
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
