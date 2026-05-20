package scoremanager.main;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import bean.Subject;
import bean.Teacher;
import dao.SubjectDao;
import tool.Action;

public class SubjectListAction extends Action {

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

        // ===== DAO =====

        // SubjectDao を使って科目一覧を取得する準備
        SubjectDao subjectDao = new SubjectDao();

        // 学校に紐づく科目一覧を取得
        List<Subject> subjects =
                subjectDao.filter(teacher.getSchool());

        // ===== JSPへ値を渡す =====

        // 科目一覧をリクエスト属性に保存
        req.setAttribute("subjects", subjects);

        // subject_list.jsp へフォワードして画面を表示
        req.getRequestDispatcher(
                "/scoremanager/main/subject_list.jsp")
           .forward(req, res);
    }
}
