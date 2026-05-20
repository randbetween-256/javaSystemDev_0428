package scoremanager.main;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import bean.ClassNum;
import bean.Teacher;
import dao.ClassNumDao;
import tool.Action;

public class ClassCreateExecuteAction extends Action {

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

        // ===== パラメータ取得 =====
        String classNumStr = req.getParameter("classNum");

        if (classNumStr != null) classNumStr = classNumStr.trim();

        // ===== DAO =====
        ClassNumDao classNumDao = new ClassNumDao();

        // ===== エラー管理 =====
        Map<String, String> errors = new HashMap<>();

        // 未入力チェック
        if (classNumStr == null || classNumStr.isEmpty()) {
            errors.put("classNum", "クラス番号を入力してください");
        }

        // 英数字チェック（任意：必要なら）
        if (!errors.containsKey("classNum")
                && !classNumStr.matches("^[A-Za-z0-9]+$")) {
            errors.put("classNum", "クラス番号は英数字で入力してください");
        }

        // 重複チェック
        if (!errors.containsKey("classNum")
                && classNumDao.get(classNumStr, teacher.getSchool()) != null) {
            errors.put("classNum", "このクラス番号は既に登録されています");
        }

        // ===== エラーなし → 登録処理 =====
        if (errors.isEmpty()) {

            ClassNum cn = new ClassNum();
            cn.setClassNum(classNumStr);
            cn.setSchool(teacher.getSchool());

            classNumDao.save(cn);

            // 完了画面へリダイレクト
            res.sendRedirect(
                req.getContextPath() + "/scoremanager/main/class_create_done.jsp"
            );
            return;
        }

        // ===== エラーあり → 入力画面へ戻す =====
        req.setAttribute("errors", errors);
        req.setAttribute("classNum", classNumStr);

        req.getRequestDispatcher("ClassCreate.action")
           .forward(req, res);
    }
}
