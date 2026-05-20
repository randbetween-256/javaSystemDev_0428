package scoremanager.main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import bean.Subject;
import bean.Teacher;
import dao.ClassNumDao;
import dao.SubjectDao;
import tool.Action;

public class TestListAction extends Action {

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

        // ===== 入学年度リスト作成 =====

        // 今日の日付を取得
        LocalDate today = LocalDate.now();

        // 現在の年を取得
        int year = today.getYear();

        // 10年前〜今年までの入学年度をリストに追加
        List<Integer> entYearSet = new ArrayList<>();
        for (int i = year - 10; i <= year; i++) {
            entYearSet.add(i);
        }

        // ===== DAO 準備 =====

        ClassNumDao classNumDao = new ClassNumDao();
        SubjectDao subjectDao = new SubjectDao();

        // ===== クラス一覧取得 =====

        // 学校に紐づくクラス一覧を取得
        List<String> cNumList = classNumDao.filter(teacher.getSchool());

        // ===== 科目一覧取得 =====

        // 学校に紐づく科目一覧を取得
        List<Subject> subjectList = subjectDao.filter(teacher.getSchool());

        // ===== JSPへ値を渡す（JSP の変数名に合わせて設定） =====

        // 入学年度一覧
        req.setAttribute("entYearSet", entYearSet);

        // クラス一覧（JSP の変数名に合わせて cNumlist）
        req.setAttribute("cNumlist", cNumList);

        // 科目一覧（JSP の変数名に合わせて list）
        req.setAttribute("list", subjectList);

        // test_list.jsp へフォワードして画面を表示
        req.getRequestDispatcher("/scoremanager/main/test_list.jsp")
           .forward(req, res);
    }
}
