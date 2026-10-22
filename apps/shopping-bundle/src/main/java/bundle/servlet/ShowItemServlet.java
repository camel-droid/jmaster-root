package bundle.servlet;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import bundle.bean.CategoryBean;
import bundle.dao.DAOException;
import bundle.dao.ItemDAO;

/**
 * Servlet implementation class ShowItemServlet
 */
@WebServlet("/ShowItemServlet")
public class ShowItemServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	public void init() throws ServletException {
		// 1. アプリケーションスコープを取得
		ServletContext application = getServletContext();
		// 2. アプリケーションスコープからcategoriesキーを取得
		@SuppressWarnings("unchecked")
		List<CategoryBean> list = (List<CategoryBean>) application.getAttribute("categories");
		// 3. categoriesキーの存在検査：存在する場合は処理を中断
		if (list != null) return;
		try {
			// 4. ItemDAOのインスタンス化
			ItemDAO dao = new ItemDAO();
			// 5. categoryテーブルの全件検索
			list = dao.findAllCategory();
			// 6. カテゴリリストをアプリケーションスコープに登録
			application.setAttribute("categories", list);
		} catch (DAOException e) {
			// スタックトレースに表示
			e.printStackTrace();
			// DAO例外をスロー
			// XXX: 【例外処理について】 研修テキストではメッセージなしだが、本サンプルシステムではメッセージを設定
			throw new ServletException("内部エラーが発生しました。", e);
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("top");
		String nextURL = "/top.jsp";
		RequestDispatcher dispatcher = request.getRequestDispatcher(nextURL);
		dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
