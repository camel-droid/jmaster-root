package bundle.bean;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class CartBeanTest {

	/** テスト対象クラス：system under test */
	private CartBean sut;
	
	@BeforeEach
	void setUp() throws Exception {
		sut = new CartBean();
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Nested
	class DisplayCartTest {
		@Test
		void すでに商品が入っているカートに商品を追加することができる() {
			// setup
			ItemBean target = new ItemBean(2, 1, "MLB Fun", 980, 1);

			List<ItemBean> before
				= List.of(
					new ItemBean(1, 1, "Javaの基本", 2500, 2),
					new ItemBean(2, 1, "MLB Fun", 980, 3),
					new ItemBean(4, 2, "懐かしのアニメシリーズ", 2000, 3)
				  );
			List<ItemBean> expectedList
				= List.of(
					new ItemBean(1, 1, "Javaの基本", 2500, 2),
					new ItemBean(2, 1, "MLB Fun", 980, 4),
					new ItemBean(4, 2, "懐かしのアニメシリーズ", 2000, 3)
				  );
			
			for (ItemBean bean : before) {
				sut.addCart(bean);
			}
			
			// execute
			sut.addCart(target);
			List<ItemBean> actualList = sut.getItems();
			// verify
			assertEquals(expectedList.size(), actualList.size());
			for (int i = 0; i < expectedList.size(); ++i) {
				ItemBean expected = expectedList.get(i);
				ItemBean actual = actualList.get(i);
				assertEquals(expected, actual);
			}
		}
		@Test
		void 空のカートに商品を追加できること() {
			// setup
			ItemBean target = new ItemBean(1, 1, "Javaの基本", 2500);
			List<ItemBean> expectedList = List.of(target);
			// execute
			sut.addCart(target);
			List<ItemBean> actualList = sut.getItems();
			// verify
			// 商品の個数確認
			assertEquals(expectedList.size(), actualList.size());
			// 商品の内容確認
			for (int i = 0; i < expectedList.size(); ++i) {
				ItemBean actual = actualList.get(i);
				ItemBean expected = expectedList.get(i);
				assertEquals(expected, actual);
			}
		}
	}
}
