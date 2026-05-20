package scoremanager.main;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import bean.Student;
import bean.Teacher;
import dao.StudentDao;
import tool.Action;

public class StudentUpdateExecuteAction extends Action {

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
        String entYearStr = req.getParameter("ent_year");

        // 学生番号
        String no = req.getParameter("no");

        // 氏名
        String name = req.getParameter("name");

        // クラス番号
        String classNum = req.getParameter("class_num");

        // 在学フラグ（チェックボックス）
        String isAttendStr = req.getParameter("is_attend");

        // 前後の空白を削除
        if (no != null) no = no.trim();
        if (name != null) name = name.trim();
        if (classNum != null) classNum = classNum.trim();

        // ===== バリデーション =====

        // いずれかの項目が未入力の場合はエラーとして更新画面へ戻す
        if (entYearStr == null || entYearStr.isEmpty()
                || no == null || no.isEmpty()
                || name == null || name.isEmpty()
                || classNum == null || classNum.isEmpty()) {

            req.setAttribute("error", "未入力の項目があります");

            // 入力値を保持して画面に戻す
            req.setAttribute("ent_year", entYearStr);
            req.setAttribute("no", no);
            req.setAttribute("name", name);
            req.setAttribute("class_num", classNum);
            req.setAttribute("is_attend", isAttendStr != null);

            req.getRequestDispatcher("/StudentUpdate.action")
            .forward(req, res);

            return;
        }

        // ===== 入学年度変換 =====

        int entYear;

        // 数値変換を試み、失敗した場合はエラーとして更新画面へ戻す
        try {
            entYear = Integer.parseInt(entYearStr);
        } catch (NumberFormatException e) {

            req.setAttribute("error", "入学年度の形式が不正です");

            req.getRequestDispatcher("StudentUpdate.action")
               .forward(req, res);

            return;
        }

        // ===== DAO =====

        StudentDao studentDao = new StudentDao();

        // 更新対象の学生が存在するか確認
        Student oldStudent = studentDao.get(no, teacher.getSchool());

        // 存在しない場合は一覧画面へ戻す
        if (oldStudent == null) {

            req.setAttribute("error", "対象の学生が存在しません");

            req.getRequestDispatcher(
                    "/scoremanager/main/student_list.jsp")
               .forward(req, res);

            return;
        }

        // ===== 更新用 Student オブジェクト作成 =====

        // 在学フラグ（チェックされていれば true）
        boolean isAttend = (isAttendStr != null);

        Student student = new Student();

        student.setNo(no);
        student.setName(name);
        student.setEntYear(entYear);
        student.setClassNum(classNum);
        student.setAttend(isAttend);

        // 学校情報をセット
        student.setSchool(teacher.getSchool());

        // ===== 更新処理 =====

        studentDao.update(student);

        // ===== 完了画面へリダイレクト =====

        res.sendRedirect(
                req.getContextPath()
                + "/scoremanager/main/student_update_done.jsp");
    }
}
