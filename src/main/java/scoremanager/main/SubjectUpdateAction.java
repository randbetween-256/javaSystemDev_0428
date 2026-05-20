package scoremanager.main;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import bean.Subject;
import bean.Teacher;
import dao.SubjectDao;
import tool.Action;

public class SubjectUpdateAction extends Action {

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

        // ===== 科目コード取得 =====

        // パラメータから科目コードを取得
        String cd = req.getParameter("cd");

        // 前後の空白を削除
        if (cd != null) {
            cd = cd.trim();
        }

        // 科目コードが無い場合はエラーとして一覧画面へ戻す
        if (cd == null || cd.isEmpty()) {

            req.setAttribute("error", "科目コードが指定されていません");

            req.getRequestDispatcher(
                    "/scoremanager/main/subject_list.jsp")
               .forward(req, res);

            return;
        }

        // ===== DAO =====

        SubjectDao subjectDao = new SubjectDao();

        // 指定された科目コードの科目情報を取得
        Subject subject = subjectDao.get(cd, teacher.getSchool());

        // 科目が存在しない場合はエラーとして一覧画面へ戻す
        if (subject == null) {

            req.setAttribute("error", "対象の科目が存在しません");

            req.getRequestDispatcher(
                    "/scoremanager/main/subject_list.jsp")
               .forward(req, res);

            return;
        }

        // ===== JSPへ値を渡す =====

        // 科目コード
        req.setAttribute("cd", subject.getId());

        // 科目名
        req.setAttribute("name", subject.getName());

        // subject_update.jsp へフォワードして更新画面を表示
        req.getRequestDispatcher(
                "/scoremanager/main/subject_update.jsp")
           .forward(req, res);
    }
}
