package bundle.servlet;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import bundle.bean.CartBean;
import bundle.bean.ItemBean;
import bundle.dao.DAOException;
import bundle.dao.ItemDAO;

/**
 * Servlet implementation class CartServlet
 */
@WebServlet("/CartServlet")
public class CartServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// リクエストパラメータからactionキーを取得
		String action = request.getParameter("action");
		// actionキーによって処理を分岐
		String nextURL = "";
		if (action == null || action.isEmpty() || action.equals("show")) {
			// 1. カート画面表示
			nextURL = "/cart.jsp";
		} else if (action.equals("add")) {
			// 2. ［カートに追加］ボタンがクリックされた場合
			// 2.1 セッションスコープを取得
			HttpSession session = request.getSession();
			// 2.2 セッションスコープからcartキーを取得
			CartBean cart = (CartBean) session.getAttribute("cart");
			// 2.3 cartキーの有無を検査：cartキーがない場合は新たにカートをセッションに登録
			if (cart == null) {
				cart = new CartBean();
				session.setAttribute("cart", cart);
			}
			try {
				// 2.4 リクエストパラメータを取得
				int code = Integer.parseInt(request.getParameter("item_code"));
				int quantity = Integer.parseInt(request.getParameter("quantity"));
				// 2.5 ItemDAOをインスタンス化
				ItemDAO dao = new ItemDAO();
				// 2.6 商品の商品コード検索
				ItemBean bean = dao.findByCode(code);
				// 2.7 商品に数量を設定
				bean.setQuantity(quantity);
				// 2.8 商品をカートに追加
				cart.addCart(bean);
				// 2.9 遷移先URLを設定
				nextURL = "/cart.jsp";
			} catch (NumberFormatException | DAOException e) {
				// スタックトレースに表示
				e.printStackTrace();
				// DAO例外をスロー
				throw new ServletException("内部エラーが発生しました。", e);
			}
		} else if (action.equals("delete")) {
			// 3. カートの商品を削除
			// 3.1 セッションを取得
			HttpSession session = request.getSession(false);
			// 3.2 セッションの有無を検査：セッションがない場合はServletExceptionをスロー
			if (session == null) {
				throw new ServletException("セッションが切れています。もう一度トップページから操作してください。");
			}
			// 3.3 セッションからカートを取得
			CartBean cart = (CartBean) session.getAttribute("cart");
			// 3.4 カートの存在の有無を検査：カートがない場合はServletExceptionをスロー
			if (cart == null) {
				throw new ServletException("正しく操作をしてください。");
			}
			// 3.5 リクエストパラメータを取得
			int code = Integer.parseInt(request.getParameter("item_code"));
			// 3.6 カートから商品を削除
			cart.deletCart(code);
			// 3.7 遷移先画面の設定
			nextURL = "/cart.jsp";
		} else {
			throw new ServletException("正しく操作をしてください。");
		}
		// 画面遷移
		RequestDispatcher dispatcher = request.getRequestDispatcher(nextURL);
		dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
