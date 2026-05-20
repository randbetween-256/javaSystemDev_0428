package scoremanager.main;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import bean.ClassNum;
import bean.Teacher;
import dao.ClassNumDao;
import tool.Action;

public class ClassUpdateExecuteAction extends Action {

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
        String oldClassNum = req.getParameter("oldClassNum");
        String newClassNum = req.getParameter("newClassNum");

        if (oldClassNum != null) oldClassNum = oldClassNum.trim();
        if (newClassNum != null) newClassNum = newClassNum.trim();

        // ===== エラー管理 =====
        Map<String, String> errors = new HashMap<>();

        // 未入力チェック
        if (oldClassNum == null || oldClassNum.isEmpty()) {
            errors.put("oldClassNum", "クラス番号が取得できませんでした");
        }

        if (newClassNum == null || newClassNum.isEmpty()) {
            errors.put("newClassNum", "新しいクラス番号を入力してください");
        }

        // 英数字チェック（任意）
        if (!errors.containsKey("newClassNum")
                && !newClassNum.matches("^[A-Za-z0-9]+$")) {
            errors.put("newClassNum", "クラス番号は英数字で入力してください");
        }

        // ===== DAO =====
        ClassNumDao classNumDao = new ClassNumDao();

        // 旧クラス番号が存在するか確認
        ClassNum oldClass = classNumDao.get(oldClassNum, teacher.getSchool());
        if (!errors.containsKey("oldClassNum") && oldClass == null) {
            errors.put("oldClassNum", "対象のクラスが存在しません");
        }

        // 新クラス番号が重複していないか確認
        if (!errors.containsKey("newClassNum")
                && classNumDao.get(newClassNum, teacher.getSchool()) != null) {
            errors.put("newClassNum", "このクラス番号は既に使用されています");
        }

        // ===== エラーあり → 入力画面へ戻す =====
        if (!errors.isEmpty()) {

            req.setAttribute("errors", errors);
            req.setAttribute("oldClassNum", oldClassNum);
            req.setAttribute("newClassNum", newClassNum);

            req.getRequestDispatcher("ClassUpdate.action")
               .forward(req, res);

            return;
        }

        // ===== 更新処理 =====
        classNumDao.save(oldClass, newClassNum);

        // ===== 完了画面へリダイレクト =====
        res.sendRedirect(
                req.getContextPath()
                + "/scoremanager/main/class_update_done.jsp");
    }
}
