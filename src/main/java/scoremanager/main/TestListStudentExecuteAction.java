package scoremanager.main;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import bean.Student;
import bean.Teacher;
import bean.TestListStudent;
import dao.ClassNumDao;
import dao.StudentDao;
import dao.SubjectDao;
import dao.TestListStudentDao;
import tool.Action;

public class TestListStudentExecuteAction extends Action {

    @Override
    public void execute(
            HttpServletRequest req,
            HttpServletResponse res) throws Exception {

        // ===== ログインチェック =====

        // 既存のセッションを取得（存在しない場合は null）
        HttpSession session = req.getSession(false);

        // セッションが無ければログイン画面へリダイレクト
        if (session == null) {
            res.sendRedirect(req.getContextPath() + "/scoremanager/login.jsp");
            return;
        }

        // セッションからログイン中の Teacher を取得
        Teacher teacher = (Teacher) session.getAttribute("user");

        // Teacher が無ければ未ログイン扱いとしてログイン画面へリダイレクト
        if (teacher == null) {
            res.sendRedirect(req.getContextPath() + "/scoremanager/login.jsp");
            return;
        }

        // ===== パラメータ取得 =====

        // 入学年度・クラス・科目（検索条件）
        String f1 = req.getParameter("f1");
        String f2 = req.getParameter("f2");
        String f3 = req.getParameter("f3");

        // 学生番号
        String studentNo = req.getParameter("f4");

        // 前後の空白を削除
        if (studentNo != null) {
            studentNo = studentNo.trim();
        }

        // ===== DAO 準備 =====

        StudentDao studentDao = new StudentDao();
        ClassNumDao classNumDao = new ClassNumDao();
        SubjectDao subjectDao = new SubjectDao();

        // ===== 共通データを JSP に渡す =====

        // 入学年度一覧
        req.setAttribute("entYearSet",
                studentDao.getEntYearSet(teacher.getSchool()));

        // クラス一覧
        req.setAttribute("cNumlist",
                classNumDao.filter(teacher.getSchool()));

        // 科目一覧
        req.setAttribute("list",
                subjectDao.filter(teacher.getSchool()));

        // ===== 検索条件保持 =====

        req.setAttribute("f1", f1);
        req.setAttribute("f2", f2);
        req.setAttribute("f3", f3);
        req.setAttribute("f4", studentNo);

        // ===== 入力チェック（学生番号必須） =====

        if (studentNo == null || studentNo.isEmpty()) {

            // エラーと空データをセット
            req.setAttribute("error", "学生番号を入力してください");
            req.setAttribute("student", null);
            req.setAttribute("tlslist", null);

            // 画面へ戻す
            req.getRequestDispatcher(
                    "/scoremanager/main/test_list_student.jsp")
               .forward(req, res);
            return;
        }

        // ===== 学生取得 =====

        Student student =
                studentDao.get(studentNo, teacher.getSchool());

        // 学生が存在しない場合
        if (student == null) {

            req.setAttribute("error", "学生番号が存在しません");
            req.setAttribute("student", null);
            req.setAttribute("tlslist", null);

            req.getRequestDispatcher(
                    "/scoremanager/main/test_list_student.jsp")
               .forward(req, res);
            return;
        }

        // ===== 成績一覧取得 =====

        TestListStudentDao tlsDao = new TestListStudentDao();

        List<TestListStudent> tlsList =
                tlsDao.filter(teacher.getSchool(), studentNo);

        // ===== JSPへ値を渡す =====

        // 学生情報
        req.setAttribute("student", student);

        // 成績一覧
        req.setAttribute("tlslist", tlsList);

        // 画面表示
        req.getRequestDispatcher(
                "/scoremanager/main/test_list_student.jsp")
           .forward(req, res);
    }
}
