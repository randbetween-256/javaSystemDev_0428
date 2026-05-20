package scoremanager.main;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import bean.Teacher;
import tool.Action;

public class ClassCreateAction extends Action {

    @Override
    public void execute(
            HttpServletRequest req,
            HttpServletResponse res) throws Exception {

        // ===== ログインチェック =====

        HttpSession session = req.getSession(false);

        if (session == null) {
            res.sendRedirect(req.getContextPath() + "/scoremanager/login.jsp");
            return;
        }

        Teacher teacher = (Teacher) session.getAttribute("user");

        if (teacher == null) {
            res.sendRedirect(req.getContextPath() + "/scoremanager/login.jsp");
            return;
        }

        // ===== クラス登録画面へフォワード =====

        req.getRequestDispatcher(
                "/scoremanager/main/class_create.jsp")
           .forward(req, res);
    }
}
