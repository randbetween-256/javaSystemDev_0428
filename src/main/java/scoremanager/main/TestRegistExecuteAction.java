package scoremanager.main;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import bean.Teacher;
import dao.TestDao;
import tool.Action;

public class TestRegistExecuteAction extends Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

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

        // ===== パラメータ取得 =====

        // 科目コード
        String subjectCode = req.getParameter("subject");

        // 回数
        String countStr = req.getParameter("count");

        // 前後の空白を削除
        if (subjectCode != null) subjectCode = subjectCode.trim();
        if (countStr != null) countStr = countStr.trim();

        // ===== 必須チェック =====

        // 科目コードまたは回数が未入力の場合
        if (subjectCode == null || subjectCode.isEmpty()
                || countStr == null || countStr.isEmpty()) {

            req.setAttribute("error", "科目または回数の情報が不足しています。");

            req.getRequestDispatcher("TestRegist.action")
               .forward(req, res);
            return;
        }

        // ===== 回数の数値変換 =====

        int count;

        try {
            count = Integer.parseInt(countStr);
        } catch (NumberFormatException e) {

            req.setAttribute("error", "回数の形式が不正です。");

            req.getRequestDispatcher("TestRegist.action")
               .forward(req, res);
            return;
        }

        // ===== 学生番号一覧取得 =====

        // regist という name のチェックボックスに入っている学生番号
        String[] studentNos = req.getParameterValues("regist");

        // 学生が 1 人も選択されていない場合
        if (studentNos == null || studentNos.length == 0) {

            req.setAttribute("error", "対象となる学生が存在しません。");

            req.getRequestDispatcher("TestRegist.action")
               .forward(req, res);
            return;
        }

        // ===== DAO 準備 =====

        TestDao dao = new TestDao();

        // ===== エラー管理（行番号 → エラーメッセージ） =====

        Map<Integer, String> errors = new HashMap<>();

        int row = 1; // 画面上の行番号として扱う

        // ===== 学生ごとに点数を処理 =====

        for (String studentNo : studentNos) {

            // 点数入力欄の name は point_学生番号
            String pointStr = req.getParameter("point_" + studentNo);

            // 前後の空白を削除
            if (pointStr != null) pointStr = pointStr.trim();

            // 点数未入力の場合はスキップ（登録しない）
            if (pointStr == null || pointStr.isEmpty()) {
                row++;
                continue;
            }

            // ===== 点数の数値変換 =====

            int point;

            try {
                point = Integer.parseInt(pointStr);
            } catch (NumberFormatException e) {

                errors.put(row, "点数は数値で入力してください");
                row++;
                continue;
            }

            // ===== 点数の範囲チェック =====

            if (point < 0 || point > 100) {

                errors.put(row, "点数は 0〜100 の範囲で入力してください");
                row++;
                continue;
            }

            // ===== クラス番号取得 =====

            // class_num_学生番号 という name で送られてくる
            String classNum = req.getParameter("class_num_" + studentNo);

            if (classNum != null) classNum = classNum.trim();

            // クラス番号が取得できない場合
            if (classNum == null || classNum.isEmpty()) {

                errors.put(row, "クラス情報が取得できませんでした");
                row++;
                continue;
            }

            // ===== 成績登録（INSERT or UPDATE） =====

            dao.saveScore(
                    studentNo,
                    subjectCode,
                    count,
                    point,
                    teacher.getSchool(),
                    classNum
            );

            row++;
        }

        // ===== エラーがある場合 =====

        if (!errors.isEmpty()) {

            req.setAttribute("errors", errors);

            req.getRequestDispatcher("TestRegist.action")
               .forward(req, res);
            return;
        }

        // ===== 完了画面へ =====

        res.sendRedirect(
                req.getContextPath()
                + "/scoremanager/main/test_regist_done.jsp");
    }
}
