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
import bundle.bean.CustomerBean;
import bundle.dao.DAOException;
import bundle.dao.OrderDAO;

/**
 * Servlet implementation class OrderServlet
 */
@WebServlet("/OrderServlet")
public class OrderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// 1. セッションを取得
		HttpSession session = request.getSession(false);
		// 2. セッションの有無を検査：：セッションがない場合はServletExceptionをスロー
		if (session == null) {
			throw new ServletException("セッションが切れています。もう一度トップページから操作してください。");
		}
		// 3. カートを取得
		CartBean cart = (CartBean) session.getAttribute("cart");
		// 4. カートの有無を検査：：カートがない場合はServletExceptionをスロー
		if (cart == null) {
			throw new ServletException("正しく操作をしてください。");
		}
		
		// リクエストパラメータからactionキーを取得
		String action = request.getParameter("action");
		// actionキーによって処理を分岐
		String nextURL = "";
		if (action == null || action.isEmpty() || action.equals("input_customer")) {
			// 遷移先URLを設定
			nextURL = "/customerInfo.jsp";
		} else if (action.equals("confirm")) {
			// 5. リクエストパラメータの取得
			String name = request.getParameter("name");
			String address = request.getParameter("address");
			String phone = request.getParameter("tel");
			String email = request.getParameter("email");
			// 6. 顧客のインスタンス化
			CustomerBean bean = new CustomerBean(name, address, phone, email);
			// 7. 顧客インスタンスをセッションに登録
			session.setAttribute("customer", bean);
			// 遷移先URLを設定
			nextURL = "/confirm.jsp";
		} else if (action.equals("order")) {
			// 注文確定処理
			// 1. セッションから顧客を取得
			CustomerBean customer = (CustomerBean) session.getAttribute("customer");
			// 2. 顧客インスタンスの存在の検査：顧客がない場合はServletExceptionをスロー
			if (customer == null) {
				throw new ServletException("正しく操作をしてください。");
			}
			try {
				// 3. 注文処理の実行：注文番号を取得
				OrderDAO dao = new OrderDAO();
				int orderNumber = dao.saveOrder(customer, cart);
				// 4. セッションの初期化：customerキーとcartキーを削除
				session.removeAttribute("customer");
				session.removeAttribute("cart");
				// 5. 注文番号をリクエストスコープに登録
				request.setAttribute("orderNumber", orderNumber);
				// 6. 遷移先URLを設定
				nextURL = "/order.jsp";
			} catch (DAOException e) {
				// TODO: handle exception
			}
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
