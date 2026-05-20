package scoremanager.main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import bean.Student;
import bean.Teacher;
import dao.ClassNumDao;
import dao.StudentDao;
import tool.Action;

public class StudentListAction extends Action {

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

        // ===== パラメータ取得 =====

        // 入学年度
        String entYearStr = req.getParameter("f1");

        // クラス番号
        String classNum = req.getParameter("f2");

        // 在学フラグ（チェックボックス）
        String isAttendStr = req.getParameter("f3");

        // 前後の空白を削除
        if (entYearStr != null) entYearStr = entYearStr.trim();
        if (classNum != null) classNum = classNum.trim();

        // ===== DAO 準備 =====

        StudentDao studentDao = new StudentDao();
        ClassNumDao classNumDao = new ClassNumDao();

        List<Student> students;

        // ===== 初期表示（検索条件なし） =====

        if (entYearStr == null && classNum == null && isAttendStr == null) {

            // 学校に所属する全学生を取得
            students = studentDao.filter(teacher.getSchool());

        } else {

            // ===== 入学年度変換 =====

            int entYear = 0;

            try {
                if (entYearStr != null
                        && !entYearStr.equals("0")
                        && !entYearStr.isEmpty()) {

                    entYear = Integer.parseInt(entYearStr);
                }
            } catch (NumberFormatException e) {
                entYear = 0;
            }

            // 在学フラグ（チェックされていれば true）
            boolean isAttend = (isAttendStr != null);

            // クラス指定があるかどうか
            boolean hasClass =
                    (classNum != null
                    && !classNum.equals("0")
                    && !classNum.isEmpty());

            // ===== 条件に応じて学生一覧を取得 =====

            if (entYear != 0 && hasClass) {

                // 入学年度 + クラス + 在学フラグ
                students = studentDao.filter(
                        teacher.getSchool(),
                        entYear,
                        classNum,
                        isAttend);

            } else if (entYear != 0) {

                // 入学年度 + 在学フラグ
                students = studentDao.filter(
                        teacher.getSchool(),
                        entYear,
                        isAttend);

            } else if (!hasClass) {

                // 在学フラグのみ
                students = studentDao.filter(
                        teacher.getSchool(),
                        isAttend);

            } else {

                // クラスのみ指定された場合はエラー
                Map<String, String> errors = new HashMap<>();
                errors.put("f1", "クラスを指定する場合は入学年度も指定してください");

                req.setAttribute("errors", errors);

                // 在学フラグのみで検索
                students = studentDao.filter(
                        teacher.getSchool(),
                        isAttend);
            }

            // 検索条件を JSP に戻すため保存
            req.setAttribute("f1", entYear);
            req.setAttribute("f2", classNum);
            req.setAttribute("f3", isAttendStr);
        }

        // ===== 入学年度一覧作成 =====

        LocalDate today = LocalDate.now();
        int year = today.getYear();

        List<Integer> entYearList = new ArrayList<>();

        // 10年前〜今年までの入学年度をリストに追加
        for (int i = year - 10; i <= year; i++) {
            entYearList.add(i);
        }

        // ===== クラス一覧取得 =====

        List<String> classNumList =
                classNumDao.filter(teacher.getSchool());

        // ===== JSPへ値を渡す =====

        req.setAttribute("students", students);
        req.setAttribute("ent_year_set", entYearList);
        req.setAttribute("class_num_set", classNumList);

        // student_list.jsp へフォワードして画面を表示
        req.getRequestDispatcher(
                "/scoremanager/main/student_list.jsp")
           .forward(req, res);
    }
}
