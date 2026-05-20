package scoremanager;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import bean.Teacher;
import dao.TeacherDao;
import tool.Action;

public class LoginExecuteAction extends Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

        String id = req.getParameter("id");
        String password = req.getParameter("password");

        if (id != null) id = id.trim();
        if (password != null) password = password.trim();

        // 認証処理
        TeacherDao dao = new TeacherDao();
        Teacher teacher = dao.login(id, password);

        // 認証失敗時
        if (teacher == null) {
            req.setAttribute("error", "ログインに失敗しました。ID またはパスワードが違います");
            req.setAttribute("id", id);
            req.getRequestDispatcher("/scoremanager/login.jsp").forward(req, res);
            return;
        }

        // セッション再生成
        HttpSession oldSession = req.getSession(false);
        if (oldSession != null) oldSession.invalidate();

        HttpSession session = req.getSession(true);
        session.setAttribute("user", teacher);

        // メニューへ
        res.sendRedirect(req.getContextPath() + "/scoremanager/main/menu.jsp");
    }
}
