package scoremanager.main;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import bean.Teacher;
import dao.ClassNumDao;
import tool.Action;

public class ClassListAction extends Action {

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

        // ===== DAO =====
        ClassNumDao classNumDao = new ClassNumDao();

        // 学校に紐づくクラス一覧を取得
        List<String> classList = classNumDao.filter(teacher.getSchool());

        // ===== JSPへ値を渡す =====
        req.setAttribute("classList", classList);

        // class_list.jsp へフォワード
        req.getRequestDispatcher(
                "/scoremanager/main/class_list.jsp")
           .forward(req, res);
    }
}
