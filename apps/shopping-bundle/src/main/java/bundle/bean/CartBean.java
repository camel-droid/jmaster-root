package bundle.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * カートを管理するJavaBean
 */
public class CartBean {

	/**
	 * フィールド
	 */
	private List<ItemBean> items; // カート内商品リスト

	/**
	 * 引数なしコンストラクタ
	 */
	public CartBean() {
		this.items = new ArrayList<ItemBean>();
	}

	public List<ItemBean> getItems() {
		return items;
	}

	public int getTotal() {
		int total = 0;
		for (ItemBean item : this.items) {
			total += item.getPrice() * item.getQuantity();
		}
		return total;
	}

	/**
	 * カートに商品を追加する
	 * @param target 追加する商品のインスタンス
	 */
	public void addCart(ItemBean target) {
		// 追加する商品がnullの場合
		if (target == null) return;
		// 追加する商品がnullではない場合
		ItemBean bean = this.getDupeItem(target);
		if (bean == null) {
			this.items.add(target);
		} else {
			int curQuantity = bean.getQuantity();
			bean.setQuantity(curQuantity + target.getQuantity());
		}
	}
	
	/**
	 * カートにすでに入っている商品の中で追加する商品と同じ商品を取得する
	 * @param  starget 追加する商品
	 * @return 追加する商品と同じ商品があればそのカート内商品のインスタンス、それ以外はnull
	 */
	private ItemBean getDupeItem(ItemBean target) {
		for (ItemBean item : this.items) {
			if (item.getCode() == target.getCode()) {
				return item;
			}
		}
		return null;
	}

	/**
	 * カートから指定された商品番号の商品を削除する
	 * @param target 削除対象の商品の商品コード
	 */
	public void deletCart(int target) {
		for (ItemBean item : this.items) {
			if (item.getCode() == target) {
				this.items.remove(item);
				break;
			}
		}
		
	}
	
}
