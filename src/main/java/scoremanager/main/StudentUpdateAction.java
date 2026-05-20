package scoremanager.main;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import bean.Student;
import bean.Teacher;
import dao.ClassNumDao;
import dao.StudentDao;
import tool.Action;

public class StudentUpdateAction extends Action {

    @Override
    public void execute(
            HttpServletRequest req,
            HttpServletResponse res) throws Exception {

        // ===== ログインチェック =====

        // 既存のセッションを取得（存在しない場合は null）
        HttpSession session = req.getSession(false);

        // セッションが無ければログイン画面へリダイレクト
        if (session == null) {
            res.sendRedirect(
                    req.getContextPath()
                    + "/scoremanager/login.jsp");
            return;
        }

        // セッションからログイン中の Teacher を取得
        Teacher teacher = (Teacher) session.getAttribute("user");

        // Teacher が無ければ未ログイン扱いとしてログイン画面へリダイレクト
        if (teacher == null) {
            res.sendRedirect(
                    req.getContextPath()
                    + "/scoremanager/login.jsp");
            return;
        }

        // ===== 学生番号取得 =====

        // パラメータから学生番号を取得
        String no = req.getParameter("no");

        // 前後の空白を削除
        if (no != null) {
            no = no.trim();
        }

        // 学生番号が無い場合はエラーとして一覧画面へ戻す
        if (no == null || no.isEmpty()) {

            req.setAttribute("error", "学生番号が指定されていません");

            req.getRequestDispatcher(
                    "/scoremanager/main/student_list.jsp")
               .forward(req, res);

            return;
        }

        // ===== DAO 準備 =====

        StudentDao studentDao = new StudentDao();
        ClassNumDao classNumDao = new ClassNumDao();

        // ===== 学生取得 =====

        // 指定された学生番号の学生情報を取得
        Student student = studentDao.get(no, teacher.getSchool());

        // 学生が存在しない場合はエラーとして一覧画面へ戻す
        if (student == null) {

            req.setAttribute("error", "対象の学生が存在しません");

            req.getRequestDispatcher(
                    "/scoremanager/main/student_list.jsp")
               .forward(req, res);

            return;
        }

        // ===== クラス一覧取得 =====

        // 学校に紐づくクラス一覧を取得
        List<String> classNumList =
                classNumDao.filter(teacher.getSchool());

        // ===== JSPへ値を渡す =====

        // 学生の入学年度
        req.setAttribute("ent_year", student.getEntYear());

        // 学生番号
        req.setAttribute("no", student.getNo());

        // 氏名
        req.setAttribute("name", student.getName());

        // クラス番号
        req.setAttribute("class_num", student.getClassNum());

        // クラス一覧
        req.setAttribute("class_num_set", classNumList);

        // 在学フラグ
        req.setAttribute("is_attend", student.isAttend());

        // student_update.jsp へフォワードして画面を表示
        req.getRequestDispatcher(
                "/scoremanager/main/student_update.jsp")
           .forward(req, res);
    }
}
