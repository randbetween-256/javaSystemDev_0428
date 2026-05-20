package scoremanager.main;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import bean.Subject;
import bean.Teacher;
import dao.SubjectDao;
import tool.Action;

public class SubjectCreateExecuteAction extends Action {

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
        String cd = req.getParameter("cd");   // 科目コード
        String name = req.getParameter("name"); // 科目名

        if (cd != null) cd = cd.trim();
        if (name != null) name = name.trim();

        // ===== DAO 準備 =====
        SubjectDao subjectDao = new SubjectDao();

        // ===== エラー管理用 Map =====
        Map<String, String> errors = new HashMap<>();

        // 科目コード未入力チェック
        if (cd == null || cd.isEmpty()) {
            errors.put("cd", "科目コードを入力してください");
        }

        // 科目名未入力チェック
        if (name == null || name.isEmpty()) {
            errors.put("name", "科目名を入力してください");
        }

        // 科目コードの形式チェック（英数字のみ）
        if (!errors.containsKey("cd") && !cd.matches("^[A-Za-z0-9]+$")) {
            errors.put("cd", "科目コードは英数字で入力してください");
        }

        // 科目コードの文字数チェック（3文字固定）
        if (!errors.containsKey("cd") && cd.length() != 3) {
            errors.put("cd", "科目コードは3文字で入力してください");
        }

        // 科目コードの重複チェック
        if (!errors.containsKey("cd") && subjectDao.get(cd, teacher.getSchool()) != null) {
            errors.put("cd", "科目コードが重複しています");
        }

        // ===== エラーなしの場合 =====
        if (errors.isEmpty()) {
            Subject subject = new Subject();
            subject.setId(cd);
            subject.setName(name);
            subject.setSchool(teacher.getSchool());

            subjectDao.save(subject);

            res.sendRedirect(req.getContextPath() + "/scoremanager/main/subject_create_done.jsp");
            return;
        }

        // ===== エラーがある場合 =====
        req.setAttribute("errors", errors);
        req.setAttribute("cd", cd);
        req.setAttribute("name", name);

        req.getRequestDispatcher("SubjectCreate.action").forward(req, res);
    }
}
