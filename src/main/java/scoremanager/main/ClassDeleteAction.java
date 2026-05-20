package scoremanager.main;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import bean.ClassNum;
import bean.Teacher;
import dao.ClassNumDao;
import tool.Action;

public class ClassDeleteAction extends Action {

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

        // ===== クラス番号取得 =====
        String classNumStr = req.getParameter("classNum");

        if (classNumStr != null) {
            classNumStr = classNumStr.trim();
        }

        // クラス番号が無い場合 → 一覧へ戻す
        if (classNumStr == null || classNumStr.isEmpty()) {

            req.setAttribute("error", "クラス番号が指定されていません");

            req.getRequestDispatcher(
                    "/scoremanager/main/class_list.jsp")
               .forward(req, res);

            return;
        }

        // ===== DAO =====
        ClassNumDao classNumDao = new ClassNumDao();

        // クラス情報取得
        ClassNum classNum = classNumDao.get(classNumStr, teacher.getSchool());

        // 存在しない場合 → 一覧へ戻す
        if (classNum == null) {

            req.setAttribute("error", "対象のクラスが存在しません");

            req.getRequestDispatcher(
                    "/scoremanager/main/class_list.jsp")
               .forward(req, res);

            return;
        }

        // ===== JSPへ値を渡す =====
        req.setAttribute("classNum", classNumStr);

        // 削除確認画面へフォワード
        req.getRequestDispatcher(
                "/scoremanager/main/class_delete.jsp")
           .forward(req, res);
    }
}
