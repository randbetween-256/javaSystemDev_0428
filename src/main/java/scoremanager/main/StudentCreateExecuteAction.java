package scoremanager.main;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import bean.Student;
import bean.Teacher;
import dao.StudentDao;
import tool.Action;

public class StudentCreateExecuteAction extends Action {

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

        // 入学年度（文字列）
        String entYearStr = req.getParameter("ent_year");

        // 学生番号
        String studentNo = req.getParameter("no");

        // 学生名
        String studentName = req.getParameter("name");

        // クラス番号
        String classNum = req.getParameter("class_num");

        // 在籍チェックボックス（null なら未チェック）
        String isAttendStr = req.getParameter("is_attend");

        // 前後の空白を削除
        if (studentNo != null) studentNo = studentNo.trim();
        if (studentName != null) studentName = studentName.trim();
        if (classNum != null) classNum = classNum.trim();

        // ===== 入学年度変換 =====

        int entYear = 0;

        // 数値変換を試み、失敗したら 0 のままにする
        try {
            entYear = Integer.parseInt(entYearStr);
        } catch (NumberFormatException e) {
            entYear = 0;
        }

        // ===== DAO 準備 =====

        StudentDao studentDao = new StudentDao();

        // ===== エラー管理用 Map =====

        Map<String, String> errors = new HashMap<>();

        // 入学年度チェック
        if (entYear == 0) {
            errors.put("ent_year", "入学年度を選択してください");
        }

        // 学生番号チェック
        if (studentNo == null || studentNo.isEmpty()) {
            errors.put("student_no", "学生番号を入力してください");
        }

        // 学生名チェック
        if (studentName == null || studentName.isEmpty()) {
            errors.put("student_name", "学生名を入力してください");
        }

        // クラス番号チェック
        if (classNum == null || classNum.isEmpty()) {
            errors.put("class_num", "クラスを選択してください");
        }

        // 学生番号の重複チェック（学生番号エラーが無い場合のみ）
        if (!errors.containsKey("student_no")
                && studentDao.get(studentNo, teacher.getSchool()) != null) {

            errors.put("student_no", "学生番号が重複しています");
        }

        // ===== エラーなしの場合 =====

        if (errors.isEmpty()) {

            // Student オブジェクトを作成して値をセット
            Student student = new Student();
            student.setNo(studentNo);
            student.setName(studentName);
            student.setEntYear(entYear);
            student.setClassNum(classNum);

            // チェックボックスは null でなければ true
            student.setAttend(isAttendStr != null);

            // 学校情報をセット
            student.setSchool(teacher.getSchool());

            // DB に登録
            studentDao.save(student);

            // 完了画面へリダイレクト
            res.sendRedirect(
                    req.getContextPath()
                    + "/scoremanager/main/student_create_done.jsp");

            return;
        }

        // ===== エラーがある場合 =====

        // エラーメッセージをリクエスト属性に保存
        req.setAttribute("errors", errors);

        // 入力値を再表示するために保存
        req.setAttribute("ent_year", entYear);
        req.setAttribute("no", studentNo);
        req.setAttribute("name", studentName);
        req.setAttribute("class_num", classNum);
        req.setAttribute("is_attend", isAttendStr != null);

        // 入力画面に戻す（Action を再実行）
        req.getRequestDispatcher("StudentCreate.action")
           .forward(req, res);
    }
}
