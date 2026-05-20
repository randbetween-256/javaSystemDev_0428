package scoremanager.main;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import bean.Teacher;
import tool.Action;

public class MenuAction extends Action {

    @Override
    public void execute(
            HttpServletRequest req,
            HttpServletResponse res) throws Exception {

        // 既存のセッションを取得（存在しない場合は null が返る）
        HttpSession session = req.getSession(false);

        // セッションが存在しない場合は未ログインとしてログイン画面へリダイレクト
        if (session == null) {

            res.sendRedirect(
                    req.getContextPath()
                    + "/scoremanager/login.jsp");

            return;
        }

        // セッションからログイン中の Teacher オブジェクトを取得
        Teacher teacher = (Teacher) session.getAttribute("user");

        // Teacher が保存されていない場合も未ログイン扱いとしてログイン画面へリダイレクト
        if (teacher == null) {

            res.sendRedirect(
                    req.getContextPath()
                    + "/scoremanager/login.jsp");

            return;
        }

        // ログイン済みの場合はメニュー画面へフォワード
        req.getRequestDispatcher(
                "/scoremanager/main/menu.jsp")
           .forward(req, res);
    }
}
