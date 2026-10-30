package bundle.servlet;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import bundle.bean.AccountBean;
import bundle.dao.AccountDAO;
import bundle.dao.DAOException;
import bundle.model.Login;

/**
 * Servlet implementation class AuthServlet
 */
@WebServlet("/AuthServlet")
public class AuthServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String nextURL = "/login.jsp";
		RequestDispatcher dispatcher = request.getRequestDispatcher(nextURL);
		dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// 1. actionキーを取得
		String action = request.getParameter("action");
		
		// 2. acstionキーによって処理を分岐
		String nextURL = "";
		if ("login".equals(action)) {
			// 2.1 ログイン処理
			// 2.1-1 リクエストパラメータを取得
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			try {
				// 2.1-2 AccountDAOをインスタンス化
				AccountDAO dao = new AccountDAO();
				// 2.1-3 リクエストパラメータに合致するアカウントを取得
				AccountBean bean = dao.findByEmailAndPassword(email, password);
				// 2.1-4 取得したアカウントを検査：nullの場合はメッセージを表示してログイン画面に遷移
				if (bean == null) {
					request.setAttribute("message", "メールアドレスとパスワードが一致しませんでした。");
					nextURL = "/login.jsp";
					this.gotoPage(request, response, nextURL);
					return;
				}
				// 2.1-5 セッションを取得
				HttpSession session = request.getSession();
				// 2.1-6 取得したアカウントをセッションに登録
				session.setAttribute("login", new Login(bean.getId(), bean.getName()));
				// 2.1-7 遷移先URLを設定
				this.redirect(response, "/ShowItemServlet");
				return;
			} catch (DAOException e) {
				// スタックトレースに表示
				e.printStackTrace();
				// DAO例外をスロー
				// XXX: 【例外処理について】 研修テキストではメッセージなしだが、本サンプルシステムではメッセージを設定
				throw new ServletException("内部エラーが発生しました。", e);
			}
		}
		
		// 画面遷移
		this.gotoPage(request, response, nextURL);
		
	}

	/**
	 * 指定されたURLにredirectする
	 * @param response HttpServletResponse
	 * @param nextURL  遷移先URL
	 * @throws IOException レスポンスの出力上の問題が発生した場合
	 */
	private void redirect(HttpServletResponse response, String nextURL) throws IOException {
		nextURL = getServletContext().getContextPath() + "/ShowItemServlet";
		response.sendRedirect(nextURL);
	}

	/**
	 * 指定されたURLにforwardする
	 * @param request  HttpServletRequest
	 * @param response HttpServletResponse
	 * @param nextURL  遷移先URL
	 * @throws ServletException 転送先のServlet/JSPなどの処理上の問題が発生した場合
	 * @throws IOException      リクエスト・レスポンスの入出力上の問題が発生した場合
	 */
	private void gotoPage(HttpServletRequest request, HttpServletResponse response, String nextURL) throws ServletException, IOException {
		RequestDispatcher dispatcher = request.getRequestDispatcher(nextURL);
		dispatcher.forward(request, response);
	}

}
