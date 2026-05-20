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

public class SubjectUpdateExecuteAction extends Action {

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

        // 科目コード
        String cd = req.getParameter("cd");

        // 科目名
        String name = req.getParameter("name");

        // 前後の空白を削除
        if (cd != null) cd = cd.trim();
        if (name != null) name = name.trim();

        // ===== エラー管理 =====

        Map<String, String> errors = new HashMap<>();

        // 科目コード未入力チェック
        if (cd == null || cd.isEmpty()) {
            errors.put("cd", "科目コードを入力してください");
        }

        // 科目名未入力チェック
        if (name == null || name.isEmpty()) {
            errors.put("name", "科目名を入力してください");
        }

        // ===== DAO =====

        SubjectDao subjectDao = new SubjectDao();

        // 科目コードが入力されている場合のみ存在確認
        if (!errors.containsKey("cd")) {

            Subject oldSubject =
                    subjectDao.get(cd, teacher.getSchool());

            // 科目が存在しない場合はエラー
            if (oldSubject == null) {
                errors.put("cd", "対象の科目が存在しません");
            }
        }

        // ===== エラーあり =====

        if (!errors.isEmpty()) {

            // エラー内容をリクエスト属性に保存
            req.setAttribute("errors", errors);

            // 入力値を保持
            req.setAttribute("cd", cd);
            req.setAttribute("name", name);

            // 更新画面へ戻す
            req.getRequestDispatcher("SubjectUpdate.action")
               .forward(req, res);

            return;
        }

        // ===== 更新用 Subject オブジェクト作成 =====

        Subject subject = new Subject();

        subject.setId(cd);
        subject.setName(name);
        subject.setSchool(teacher.getSchool());

        // ===== 更新処理 =====

        subjectDao.update(subject);

        // ===== 完了画面へリダイレクト =====

        res.sendRedirect(
                req.getContextPath()
                + "/scoremanager/main/subject_update_done.jsp");
    }
}
