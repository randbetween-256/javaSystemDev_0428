package scoremanager.main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import bean.Teacher;
import dao.ClassNumDao;
import tool.Action;

public class StudentCreateAction extends Action {

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

        // ===== 入学年度一覧作成 =====

        // 今日の日付を取得
        LocalDate today = LocalDate.now();

        // 現在の年を取得
        int year = today.getYear();

        // 入学年度のリストを作成（10年前〜今年まで）
        List<Integer> entYearList = new ArrayList<>();

        for (int i = year - 10; i <= year; i++) {
            entYearList.add(i);
        }

        // ===== クラス一覧取得 =====

        // ClassNumDao を使って学校に紐づくクラス一覧を取得
        ClassNumDao classNumDao = new ClassNumDao();

        List<String> classNumList =
                classNumDao.filter(teacher.getSchool());

        // ===== JSPへ値を渡す =====

        // 入学年度一覧をリクエスト属性に保存
        req.setAttribute("ent_year_set", entYearList);

        // クラス一覧をリクエスト属性に保存
        req.setAttribute("class_num_set", classNumList);

        // student_create.jsp へフォワードして画面を表示
        req.getRequestDispatcher(
                "/scoremanager/main/student_create.jsp")
           .forward(req, res);
    }
}
