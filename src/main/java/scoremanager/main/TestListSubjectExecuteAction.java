package scoremanager.main;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import bean.Subject;
import bean.Teacher;
import bean.TestListSubject;
import dao.ClassNumDao;
import dao.StudentDao;
import dao.SubjectDao;
import dao.TestListSubjectDao;
import tool.Action;

public class TestListSubjectExecuteAction extends Action {

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
        String f1 = req.getParameter("f1"); // 入学年度
        String f2 = req.getParameter("f2"); // クラス
        String f3 = req.getParameter("f3"); // 科目

        if (f2 != null) f2 = f2.trim();
        if (f3 != null) f3 = f3.trim();

        // ===== DAO =====
        ClassNumDao classNumDao = new ClassNumDao();
        SubjectDao subjectDao = new SubjectDao();
        StudentDao studentDao = new StudentDao();

        // ===== 共通データを JSP に渡す =====
        req.setAttribute("entYearSet", studentDao.getEntYearSet(teacher.getSchool()));
        req.setAttribute("cNumlist", classNumDao.filter(teacher.getSchool()));
        req.setAttribute("list", subjectDao.filter(teacher.getSchool()));

        // ===== 検索条件保持 =====
        req.setAttribute("f1", f1);
        req.setAttribute("f2", f2);
        req.setAttribute("f3", f3);

        // ===== 条件不足チェック =====
        if (f1 == null || f1.equals("0")
                || f2 == null || f2.equals("0")
                || f3 == null || f3.equals("0")) {

            // エラーメッセージをセット
            req.setAttribute("error", "入学年度・クラス・科目をすべて選択してください");

            req.setAttribute("tlslist", null);
            req.setAttribute("subject_name", "");

            req.getRequestDispatcher("/scoremanager/main/test_list_subject.jsp")
               .forward(req, res);
            return;
        }

        // ===== 入学年度の数値変換 =====
        int entYear = 0;
        try {
            entYear = Integer.parseInt(f1);
        } catch (NumberFormatException e) {
            req.setAttribute("error", "入学年度が不正です");
            req.getRequestDispatcher("/scoremanager/main/test_list_subject.jsp")
               .forward(req, res);
            return;
        }

        // ===== 科目名取得 =====
        Subject subject = subjectDao.get(f3, teacher.getSchool());
        String subjectName = (subject != null) ? subject.getName() : "";

        // ===== 成績一覧取得 =====
        TestListSubjectDao tlsDao = new TestListSubjectDao();
        List<TestListSubject> tlsList =
                tlsDao.filter(teacher.getSchool(), entYear, f2, f3);

        // ===== JSPへ値を渡す =====
        req.setAttribute("tlslist", tlsList);
        req.setAttribute("subject_name", subjectName);

        req.getRequestDispatcher("/scoremanager/main/test_list_subject.jsp")
           .forward(req, res);
    }
}
